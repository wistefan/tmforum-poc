package org.fiware.tmforum.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fiware.ngsi.model.EntityVO;
import org.fiware.ngsi.model.GeoPropertyVO;
import org.fiware.ngsi.model.PropertyVO;
import org.fiware.ngsi.model.RelationshipVO;
import org.fiware.tmforum.domain.ngsi.AttributeGetter;
import org.fiware.tmforum.domain.ngsi.AttributeSetter;
import org.fiware.tmforum.domain.ngsi.DatasetId;
import org.fiware.tmforum.domain.ngsi.EntityId;
import org.fiware.tmforum.domain.ngsi.EntityType;
import org.fiware.tmforum.domain.ngsi.MappingEnabled;
import org.fiware.tmforum.domain.ngsi.RelationshipObject;

import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class EntityMapper {

	private final ObjectMapper objectMapper;

	private static final String DEFAULT_CONTEXT = "https://smartdatamodels.org/context.jsonld";

	public <T> T fromEntityVO(EntityVO entityVO, Class<T> targetClass) {

		MappingEnabled mappingEnabled = isMappingEnabled(targetClass).orElseThrow(() -> new MappingException(String.format("Mapping is not enabled for class %s", targetClass)));

		if (!entityVO.getType().equals(mappingEnabled.entityType())) {
			throw new MappingException(String.format("Entity and Class type do not match - %s vs %s.", entityVO.getType(), mappingEnabled.entityType()));
		}

		try {
			Constructor<T> objectConstructor = targetClass.getDeclaredConstructor(String.class);
			T constructedObject = objectConstructor.newInstance(entityVO.getId().toString());

			entityVO.getAdditionalProperties().entrySet().forEach(
					entry -> {
						Optional<Method> optionalSetter = getCorrespondingSetterMethod(constructedObject, entry.getKey());
						if (optionalSetter.isEmpty()) {
							log.warn("Ignoring property {} for entity {} since there is no mapping configured.", entry.getKey(), entityVO.getId());
							return;
						}
						Method setterMethod = optionalSetter.get();
						AttributeSetter setterAnnotation = getAttributeSetterAnnotation(setterMethod).get();
						Class<?> parameterType = getParameterType(setterMethod.getParameterTypes());

						try {
							switch (setterAnnotation.value()) {
								case PROPERTY, GEO_PROPERTY -> {
									if (entry.getValue() instanceof Map) {
										Map<String, Object> propertyMap = (Map) entry.getValue();
										optionalSetter.get().invoke(constructedObject, objectMapper.convertValue(propertyMap.get("value"), parameterType));
										break;
									} else {
										throw new MappingException("");
									}
								}
								case RELATIONSHIP -> {
									Class<?> targetClassMapping = setterAnnotation.targetClass();
									optionalSetter.get().invoke(constructedObject, getObjectFromRelationship(entry, targetClassMapping));
									break;
								}
								case GEO_PROPERTY_LIST -> {
									break;
								}
								case RELATIONSHIP_LIST -> {
									Class<?> targetClassMapping = setterAnnotation.targetClass();
									optionalSetter.get().invoke(constructedObject, relationshipListToTargetClass(entry, targetClassMapping));
									break;
								}
							}
						} catch (IllegalAccessException | InvocationTargetException e) {
							log.error("Was not able to set property {} for entity {}", entry.getKey(), entityVO.getId(), e);
						}
					}
			);
			return constructedObject;
		} catch (NoSuchMethodException e) {
			throw new MappingException(String.format("The class %s does not declare the required String id constructor.", targetClass));
		} catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
			throw new MappingException(String.format("Was not able to create instance of %s.", targetClass), e);
		}
	}

	private <T> List<T> relationshipListToTargetClass(Map.Entry<String, Object> entry, Class<T> targetClass) {
		if (entry.getValue() instanceof Map) {
			return List.of(getObjectFromRelationship(entry, targetClass));
		} else if (entry.getValue() instanceof List) {
			List relationshipMap = (List) entry.getValue();
			return relationshipMap.stream().filter(relationshipEntry -> relationshipEntry instanceof Map.Entry).map(relationshipEntry -> getObjectFromRelationship((Map.Entry<String, Object>) relationshipEntry, targetClass)).toList();
		}
		throw new MappingException(String.format("Did not receive a valid entry: %s", entry));
	}

	private <T> T getObjectFromRelationship(Map.Entry<String, Object> entry, Class<T> targetClass) {
		try {
			Map<String, Object> relationshipMap = (Map) entry.getValue();
			Constructor<T> declaredConstructor = targetClass.getDeclaredConstructor(String.class);
			Object relationshipObject = relationshipMap.get("object");
			if (!(relationshipObject instanceof String)) {
				throw new MappingException("Target of the relationship cannot be cast to string.");
			}
			T constructedObject = declaredConstructor.newInstance((String) relationshipObject);
			getAttributeSettersMethods(constructedObject).forEach(method -> {
				AttributeSetter attributeSetter = getAttributeSetterAnnotation(method).get();
				Object attributeObject = relationshipMap.get(attributeSetter.targetName());
				if (attributeObject == null) {
					return;
				}
				if (!(attributeObject instanceof Map)) {
					throw new MappingException("The additional property needs to be a map.");
				}
				try {
					method.invoke(constructedObject, objectMapper.convertValue(((Map) attributeObject).get("value"), getTargetClass(attributeSetter, method)));
				} catch (IllegalAccessException | InvocationTargetException e) {
					throw new MappingException(String.format("Was not able to set value with method %s for the constructed object for class %", method, targetClass));
				}
			});
			return constructedObject;
		} catch (NoSuchMethodException e) {
			throw new MappingException(String.format("Class %s does not declare a String id constructor that is required for relationship mappings.", targetClass));
		} catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
			throw new MappingException(String.format("Was not able to instantiate %s", targetClass), e);
		}
	}

	private Class<?> getTargetClass(AttributeSetter setterAnnotation, Method m) {
		if (setterAnnotation.targetClass() != Object.class) {
			return setterAnnotation.targetClass();
		}
		return m.getReturnType();
	}

	private Class<?> getParameterType(Class<?>[] arrayOfClasses) {
		if (arrayOfClasses.length != 1) {
			throw new MappingException("Setter method should only have one parameter declared.");
		}
		return arrayOfClasses[0];
	}

	private <T> Optional<Method> getCorrespondingSetterMethod(T entity, String propertyName) {
		return getAttributeSettersMethods(entity).stream().filter(m ->
						getAttributeSetterAnnotation(m)
								.map(attributeSetter -> attributeSetter.targetName().equals(propertyName)).orElse(false))
				.findFirst();
	}

	private <T> List<Method> getAttributeSettersMethods(T entity) {
		return Arrays.stream(entity.getClass().getMethods()).filter(m -> getAttributeSetterAnnotation(m).isPresent()).collect(Collectors.toList());
	}

	private Optional<AttributeSetter> getAttributeSetterAnnotation(Method m) {
		return Arrays.stream(m.getAnnotations()).filter(a -> a instanceof AttributeSetter).findFirst().map(a -> (AttributeSetter) a);
	}

	private <T> Optional<MappingEnabled> isMappingEnabled(Class<T> tClass) {
		return Arrays.stream(tClass.getAnnotations())
				.filter(annotation -> annotation instanceof MappingEnabled)
				.map(annotation -> (MappingEnabled) annotation)
				.findFirst();
	}


	public <T> EntityVO toEntityVO(T entity) {
		isMappingEnabled(entity.getClass())
				.orElseThrow(() -> new UnsupportedOperationException(String.format("Generic mapping to NGSI-LD entities is not supported for object %s", entity)));

		List<Method> entityIdMethod = new ArrayList<>();
		List<Method> entityTypeMethod = new ArrayList<>();
		List<Method> propertyMethods = new ArrayList<>();
		List<Method> relationshipMethods = new ArrayList<>();
		List<Method> relationshipListMethods = new ArrayList<>();
		List<Method> geoPropertyMethods = new ArrayList<>();
		List<Method> geoPropertyListMethods = new ArrayList<>();

		Arrays.stream(entity.getClass().getMethods()).forEach(method -> {
			if (isEntityIdMethod(method)) {
				entityIdMethod.add(method);
			} else if (isEntityTypeMethod(method)) {
				entityTypeMethod.add(method);
			} else {
				getAttributeGetter(method.getAnnotations()).ifPresent(annotation -> {
					switch (annotation.value()) {
						case PROPERTY -> propertyMethods.add(method);
						case GEO_PROPERTY -> geoPropertyMethods.add(method);
						case RELATIONSHIP -> relationshipMethods.add(method);
						case GEO_PROPERTY_LIST -> geoPropertyListMethods.add(method);
						case RELATIONSHIP_LIST -> relationshipListMethods.add(method);
						default -> throw new UnsupportedOperationException(String.format("Mapping target %s is not supported.", annotation.value()));
					}
				});
			}
		});

		if (entityIdMethod.size() != 1) {
			throw new IllegalArgumentException(String.format("The provided object declares %s id methods, exactly one is expected.", entityIdMethod.size()));
		}
		if (entityTypeMethod.size() != 1) {
			throw new IllegalArgumentException(String.format("The provided object declares %s type methods, exactly one is expected.", entityTypeMethod.size()));
		}

		return buildEntity(entity, entityIdMethod.get(0), entityTypeMethod.get(0), propertyMethods, geoPropertyMethods, relationshipMethods, relationshipListMethods);
	}

	private <T> Optional<Method> getRelationshipObjectMethod(T entity) {
		return Arrays.stream(entity.getClass().getMethods()).filter(this::isRelationShipObject).findFirst();
	}

	private <T> Optional<Method> getDatasetIdMethod(T entity) {
		return Arrays.stream(entity.getClass().getMethods()).filter(this::isDatasetId).findFirst();
	}

	private boolean isRelationShipObject(Method m) {
		return Arrays.stream(m.getAnnotations()).anyMatch(a -> a instanceof RelationshipObject);
	}

	private boolean isDatasetId(Method m) {
		return Arrays.stream(m.getAnnotations()).anyMatch(a -> a instanceof DatasetId);
	}

	private <T> EntityVO buildEntity(T entity, Method entityIdMethod, Method entityTypeMethod, List<Method> propertyMethods, List<Method> geoPropertyMethods, List<Method> relationshipMethods, List<Method> relationshipListMethods) {

		EntityVO entityVO = new EntityVO();
		// TODO: Check if we need that configurable
		entityVO.setAtContext(DEFAULT_CONTEXT);

		// TODO: include extraction via annotation
		entityVO.setOperationSpace(null);
		entityVO.setObservationSpace(null);
		entityVO.setLocation(null);


		try {
			Object entityIdObject = entityIdMethod.invoke(entity);
			if (!(entityIdObject instanceof URI)) {
				throw new IllegalArgumentException("The entityId method does not return a valid URI.");
			}
			entityVO.id((URI) entityIdObject);

			Object entityTypeObject = entityTypeMethod.invoke(entity);
			if (!(entityTypeObject instanceof String)) {
				throw new IllegalArgumentException("The entityType method does not return a valid String.");
			}
			entityVO.setType((String) entityTypeObject);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new IllegalArgumentException(String.format("Was not able to invoke method on %s.", entity), e);
		}

		Map<String, Object> additionalProperties = new LinkedHashMap<>();
		additionalProperties.putAll(buildProperties(entity, propertyMethods));
		additionalProperties.putAll(buildGeoProperties(entity, geoPropertyMethods));
		additionalProperties.putAll(buildRelationships(entity, relationshipMethods));
		additionalProperties.putAll(buildRelationshipList(entity, relationshipListMethods));
		//TODO: add handling for geoproperty lists
		entityVO.setAdditionalProperties(additionalProperties);

		return entityVO;
	}

	private <T> Map<String, Object> buildRelationships(T entity, List<Method> relationshipMethods) {
		return relationshipMethods.stream()
				.map(method -> methodToRelationshipEntry(entity, method))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private <T> Map<String, Object> buildRelationshipList(T entity, List<Method> relationshipListMethods) {
		return relationshipListMethods.stream()
				.map(relationshipMethod -> methodToRelationshipListEntry(entity, relationshipMethod))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private <T> Map<String, Object> buildPropertyList(T entity, List<Method> propertyListMethods) {
		return propertyListMethods.stream()
				.map(propertyListMethod -> methodToPropertyListEntry(entity, propertyListMethod))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private <T> Map<String, Object> buildGeoProperties(T entity, List<Method> geoPropertyMethods) {
		return geoPropertyMethods.stream()
				.map(geoPropertyMethod -> methodToGeoPropertyEntry(entity, geoPropertyMethod))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private <T> Map<String, Object> buildProperties(T entity, List<Method> propertyMethods) {
		return propertyMethods.stream()
				.map(propertyMethod -> methodToPropertyEntry(entity, propertyMethod))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private boolean isEntityTypeMethod(Method method) {
		return Arrays.stream(method.getAnnotations()).anyMatch(annotation -> annotation instanceof EntityType);
	}

	private boolean isEntityIdMethod(Method method) {
		return Arrays.stream(method.getAnnotations()).anyMatch(annotation -> annotation instanceof EntityId);
	}

	private <T> List<Method> getAttributeGettersMethods(T entity) {
		return Arrays.stream(entity.getClass().getMethods()).filter(m -> getAttributeGetterAnnotation(m).isPresent()).collect(Collectors.toList());
	}

	private Optional<AttributeGetter> getAttributeGetterAnnotation(Method m) {
		return Arrays.stream(m.getAnnotations()).filter(a -> a instanceof AttributeGetter).findFirst().map(a -> (AttributeGetter) a);
	}

	private Optional<AttributeGetter> getAttributeGetter(Annotation[] annotations) {
		return Arrays.stream(annotations).filter(annotation -> annotation instanceof AttributeGetter).map(annotation -> (AttributeGetter) annotation).findFirst();
	}

	private <T> Optional<Map.Entry<String, Object>> methodToPropertyEntry(T entity, Method method) {
		try {
			Object o = method.invoke(entity);
			if (o == null) {
				return Optional.empty();
			}
			AttributeGetter attributeMapping = getAttributeGetter(method.getAnnotations()).orElseThrow(() -> new IllegalArgumentException(String.format("No mapping defined for method %s", method)));
			PropertyVO propertyVO = new PropertyVO();
			propertyVO.setType(PropertyVO.Type.PROPERTY);
			propertyVO.setValue(o);
			return Optional.of(new AbstractMap.SimpleEntry<>(attributeMapping.targetName(), propertyVO));
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new IllegalArgumentException(String.format("Was not able invoke method %s on %s", method, entity));
		}
	}

	private <T> Optional<Map.Entry<String, Object>> methodToGeoPropertyEntry(T entity, Method method) {
		try {
			Object o = method.invoke(entity);
			if (o == null) {
				return Optional.empty();
			}
			AttributeGetter attributeMapping = getAttributeGetter(method.getAnnotations()).orElseThrow(() -> new IllegalArgumentException(String.format("No mapping defined for method %s", method)));
			GeoPropertyVO geoPropertyVO = new GeoPropertyVO();
			geoPropertyVO.setType(GeoPropertyVO.Type.GEOPROPERTY);
			geoPropertyVO.setValue(o);
			return Optional.of(new AbstractMap.SimpleEntry<>(attributeMapping.targetName(), geoPropertyVO));
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new IllegalArgumentException(String.format("Was not able invoke method %s on %s", method, entity));
		}
	}

	private <T> Optional<Map.Entry<String, Object>> methodToRelationshipEntry(T entity, Method method) {
		try {
			Object relationShipObject = method.invoke(entity);
			if (relationShipObject == null) {
				return Optional.empty();
			}
			RelationshipVO relationshipVO = getRelationshipVO(method, relationShipObject);
			AttributeGetter attributeMapping = getAttributeGetter(method.getAnnotations()).orElseThrow(() -> new IllegalArgumentException(String.format("No mapping defined for method %s", method)));
			return Optional.of(new AbstractMap.SimpleEntry<>(attributeMapping.targetName(), relationshipVO));
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new MappingException(String.format("Was not able invoke method %s on %s", method, entity));
		}
	}

	private RelationshipVO getRelationshipVO(Method method, Object relationShipObject) {
		try {

			Method objectMethod = getRelationshipObjectMethod(relationShipObject).orElseThrow(() -> new MappingException(String.format("The relationship %s-%s does not provide an object method.", relationShipObject, method)));
			Object objectObject = objectMethod.invoke(relationShipObject);
			if (!(objectObject instanceof URI)) {
				throw new MappingException(String.format("The object %s of the relationship is not a URI.", relationShipObject));
			}

			Method datasetIdMethod = getDatasetIdMethod(relationShipObject).orElseThrow(() -> new MappingException(String.format("The relationship %s-%s does not provide a datasetId method.", relationShipObject, method)));
			Object datasetIdObject = datasetIdMethod.invoke(relationShipObject);
			if (!(datasetIdObject instanceof URI)) {
				throw new MappingException(String.format("The datasetId %s of the relationship is not a URI.", relationShipObject));
			}
			RelationshipVO relationshipVO = new RelationshipVO();
			relationshipVO.setType(RelationshipVO.Type.RELATIONSHIP);
			relationshipVO.setObject((URI) objectObject);
			relationshipVO.setDatasetId((URI) datasetIdObject);

			// get additional properties. We do not support more depth/complexity for now
			Map<String, Object> additionalProperties = getAttributeGettersMethods(relationShipObject).stream()
					.map(getterMethod -> getAdditionalPropertyEntryFromMethod(relationShipObject, getterMethod))
					.filter(Optional::isPresent)
					.map(Optional::get)
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			relationshipVO.setAdditionalProperties(additionalProperties);
			return relationshipVO;
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new MappingException(String.format("Was not able invoke method %s on %s", method, relationShipObject));
		}
	}

	private <T> Optional<Map.Entry<String, Object>> methodToRelationshipListEntry(T entity, Method method) {
		try {
			Object o = method.invoke(entity);
			if (o == null) {
				return Optional.empty();
			}
			if (!(o instanceof List)) {
				throw new IllegalArgumentException(String.format("Relationship list method %s::%s did not return a List.", entity, method));
			}
			List<Object> entityObjects = (List) o;

			AttributeGetter attributeGetter = getAttributeGetter(method.getAnnotations()).orElseThrow(() -> new IllegalArgumentException(String.format("No mapping defined for method %s", method)));
			List<RelationshipVO> relationshipVOList = entityObjects.stream()
					.map(entityObject -> getRelationshipVO(method, entityObject))
					.toList();
			return Optional.of(new AbstractMap.SimpleEntry<>(attributeGetter.targetName(), relationshipVOList));
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new IllegalArgumentException(String.format("Was not able invoke method %s on %s", method, entity));
		}
	}

	private Optional<Map.Entry<String, Object>> getAdditionalPropertyEntryFromMethod(Object relationShipObject, Method getterMethod) {
		Optional<AttributeGetter> optionalAttributeGetter = getAttributeGetter(getterMethod.getAnnotations());
		if (optionalAttributeGetter.isEmpty()) {
			return Optional.empty();
		}
		switch (optionalAttributeGetter.get().value()) {
			case PROPERTY -> {
				return methodToPropertyEntry(relationShipObject, getterMethod);
			}
			default -> {
				return Optional.empty();
			}
		}
	}

	private <T> Optional<Map.Entry<String, Object>> methodToPropertyListEntry(T entity, Method method) {
		try {
			Object o = method.invoke(entity);
			if (o == null) {
				return Optional.empty();
			}
			if (!(o instanceof List)) {
				throw new IllegalArgumentException(String.format("Property list method %s::%s did not return a List.", entity, method));
			}
			AttributeGetter attributeMapping = getAttributeGetter(method.getAnnotations()).orElseThrow(() -> new IllegalArgumentException(String.format("No mapping defined for method %s", method)));
			List<Object> entityObjects = (List) o;

			List<PropertyVO> propertyVOList = entityObjects.stream()
					.map(propertyObject -> {
						PropertyVO propertyVO = new PropertyVO();
						propertyVO.setType(PropertyVO.Type.PROPERTY);
						propertyVO.setValue(propertyObject);
						return propertyVO;
					})
					.collect(Collectors.toList());
			return Optional.of(new AbstractMap.SimpleEntry<>(attributeMapping.targetName(), propertyVOList));
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new IllegalArgumentException(String.format("Was not able invoke method %s on %s", method, entity));
		}
	}

}
