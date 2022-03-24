package org.fiware.tmforum.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.fiware.tmforum.domain.ngsi.AttributeGetter;
import org.fiware.tmforum.domain.ngsi.AttributeSetter;
import org.fiware.tmforum.domain.ngsi.AttributeType;
import org.fiware.tmforum.domain.ngsi.EntityType;

import java.net.URI;

/**
 * Base class for all entities to be created
 */
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class Entity {

	/**
	 * When sub-classing, this defines the super-class
	 */
	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "@baseType")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "@baseType")}))
	String atBaseType;

	/**
	 * A URI to a JSON-Schema file that defines additional attributes and relationships
	 */
	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "@schemaLocation")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "@schemaLocation")}))
	URI atSchemaLocation;

	/**
	 * When sub-classing, this defines the sub-class entity name
	 */
	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "@type")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "@type")}))
	String atType;

}
