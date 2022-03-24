package org.fiware.tmforum.repository;

import io.micronaut.http.HttpResponse;
import org.fiware.ngsi.api.EntitiesApiClient;
import org.fiware.ngsi.model.EntityListVO;
import org.fiware.ngsi.model.EntityVO;
import org.fiware.tmforum.configuration.GeneralProperties;
import org.fiware.tmforum.domain.EntityMapper;
import org.fiware.tmforum.domain.TMForumMapper;
import org.fiware.tmforum.domain.product.BundleProductSpecification;
import org.fiware.tmforum.domain.product.Catalog;
import org.fiware.tmforum.domain.product.Category;
import org.fiware.tmforum.domain.product.ProductSpecification;
import org.fiware.tmforum.domain.product.ProductSpecificationCharacteristic;
import org.fiware.tmforum.domain.product.offering.BundleProductOffering;
import org.fiware.tmforum.domain.product.offering.PricingLogicAlgorithm;
import org.fiware.tmforum.domain.product.offering.ProductOffering;
import org.fiware.tmforum.domain.product.offering.ProductOfferingPrice;
import org.fiware.tmforum.domain.product.offering.ProductSpecificationCharacteristicValueUse;
import org.fiware.tmforum.domain.product.offering.TaxItem;
import org.fiware.tmforum.exception.NonExistentReferenceException;
import org.fiware.tmforum.rest.ValidationService;

import javax.inject.Singleton;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class ProductCatalogRepository extends NgsiLdBaseRepository {

	private final EntityMapper entityMapper;
	private final ValidationService validationService;

	public ProductCatalogRepository(GeneralProperties generalProperties, EntitiesApiClient entitiesApi, EntityMapper entityMapper, ValidationService validationService) {
		super(generalProperties, entitiesApi);
		this.entityMapper = entityMapper;
		this.validationService = validationService;
	}

	public void createCatalog(Catalog catalog) {
		entitiesApi.createEntity(entityMapper.toEntityVO(catalog), generalProperties.getTenant());
	}

	public void deleteEntry(String id) {
		entitiesApi.removeEntityById(URI.create(id), generalProperties.getTenant(), null);
	}

	public List<Catalog> findCatalogs() {
		HttpResponse<EntityListVO> entityListVOHttpResponse = entitiesApi.queryEntities(generalProperties.getTenant(),
				null,
				null,
				Catalog.TYPE_CATALOGUE,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				getLinkHeader());

		return entityListVOHttpResponse.getBody()
				.map(ArrayList::new)
				.orElseGet(ArrayList::new)
				.stream()
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, Catalog.class))
				.collect(Collectors.toList());

	}

	public Optional<Catalog> getCatalog(String id) {
		HttpResponse<EntityVO> entityVOHttpResponse = entitiesApi.retrieveEntityById(URI.create(id), generalProperties.getTenant(), null, null, null, getLinkHeader());
		return entityVOHttpResponse.getBody().map(entityVO -> entityMapper.fromEntityVO(entityVO, Catalog.class));
	}

	// category
	public void createCategory(Category category) {
		entitiesApi.createEntity(entityMapper.toEntityVO(category), generalProperties.getTenant());
	}

	public List<Category> findCategories() {
		HttpResponse<EntityListVO> entityListVOHttpResponse = entitiesApi.queryEntities(generalProperties.getTenant(),
				null,
				null,
				Category.TYPE_CATEGORY,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				getLinkHeader());

		return entityListVOHttpResponse.getBody()
				.map(ArrayList::new)
				.orElseGet(ArrayList::new)
				.stream()
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, Category.class))
				.collect(Collectors.toList());

	}

	public Optional<Category> getCategory(String id) {
		HttpResponse<EntityVO> entityVOHttpResponse = entitiesApi.retrieveEntityById(URI.create(id), generalProperties.getTenant(), null, null, null, getLinkHeader());
		return entityVOHttpResponse.getBody().map(entityVO -> entityMapper.fromEntityVO(entityVO, Category.class));
	}

	public void createProductOffering(ProductOffering productOffering) {
		entitiesApi.createEntity(entityMapper.toEntityVO(productOffering), generalProperties.getTenant());
	}

	public List<ProductOffering> findProductOfferings() {
		HttpResponse<EntityListVO> entityListVOHttpResponse = entitiesApi.queryEntities(generalProperties.getTenant(),
				null,
				null,
				ProductOffering.TYPE_PRODUCT_OFFERING,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				getLinkHeader());

		return entityListVOHttpResponse.getBody()
				.map(ArrayList::new)
				.orElseGet(ArrayList::new)
				.stream()
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, ProductOffering.class))
				.collect(Collectors.toList());

	}

	public Optional<ProductOffering> getProductOffering(String id) {
		HttpResponse<EntityVO> entityVOHttpResponse = entitiesApi.retrieveEntityById(URI.create(id), generalProperties.getTenant(), null, null, null, getLinkHeader());
		return entityVOHttpResponse.getBody().map(entityVO -> entityMapper.fromEntityVO(entityVO, ProductOffering.class));
	}

	public void createProductOfferingPrice(ProductOfferingPrice productOfferingPrice) {
		entitiesApi.createEntity(entityMapper.toEntityVO(productOfferingPrice), generalProperties.getTenant());
	}

	public List<ProductOfferingPrice> findProductOfferingPrices() {
		HttpResponse<EntityListVO> entityListVOHttpResponse = entitiesApi.queryEntities(generalProperties.getTenant(),
				null,
				null,
				ProductOfferingPrice.TYPE_PRODUCT_OFFERING_PRICE,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				getLinkHeader());

		return entityListVOHttpResponse.getBody()
				.map(ArrayList::new)
				.orElseGet(ArrayList::new)
				.stream()
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, ProductOfferingPrice.class))
				.collect(Collectors.toList());

	}

	public Optional<ProductOfferingPrice> getProductOfferingPrice(String id) {
		HttpResponse<EntityVO> entityVOHttpResponse = entitiesApi.retrieveEntityById(URI.create(id), generalProperties.getTenant(), null, null, null, getLinkHeader());
		return entityVOHttpResponse.getBody().map(entityVO -> entityMapper.fromEntityVO(entityVO, ProductOfferingPrice.class));
	}

	public void createProductSpecification(ProductSpecification productSpecification) {
		entitiesApi.createEntity(entityMapper.toEntityVO(productSpecification), generalProperties.getTenant());
	}

	public List<ProductSpecification> findProductSpecifications() {
		HttpResponse<EntityListVO> entityListVOHttpResponse = entitiesApi.queryEntities(generalProperties.getTenant(),
				null,
				null,
				ProductSpecification.TYPE_PRODUCT_SPECIFICATION,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				getLinkHeader());

		return entityListVOHttpResponse.getBody()
				.map(ArrayList::new)
				.orElseGet(ArrayList::new)
				.stream()
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, ProductSpecification.class))
				.collect(Collectors.toList());

	}

	public Optional<ProductSpecification> getProductSpecification(String id) {
		HttpResponse<EntityVO> entityVOHttpResponse = entitiesApi.retrieveEntityById(URI.create(id), generalProperties.getTenant(), null, null, null, getLinkHeader());
		return entityVOHttpResponse.getBody().map(entityVO -> entityMapper.fromEntityVO(entityVO, ProductSpecification.class));
	}

	public void createProductSpecificationCharacteristic(ProductSpecificationCharacteristic productSpecificationCharacteristic) {
		entitiesApi.createEntity(entityMapper.toEntityVO(productSpecificationCharacteristic), generalProperties.getTenant());
	}

	public List<ProductSpecificationCharacteristic> findProductSpecificationCharacteristic() {
		HttpResponse<EntityListVO> entityListVOHttpResponse = entitiesApi.queryEntities(generalProperties.getTenant(),
				null,
				null,
				ProductSpecificationCharacteristic.TYPE_PRODUCT_SPECIFICATION_CHARACTERISTIC,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				getLinkHeader());

		return entityListVOHttpResponse.getBody()
				.map(ArrayList::new)
				.orElseGet(ArrayList::new)
				.stream()
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, ProductSpecificationCharacteristic.class))
				.collect(Collectors.toList());

	}

	public Optional<ProductSpecificationCharacteristic> getProductSpecificationCharacteristic(String id) {
		HttpResponse<EntityVO> entityVOHttpResponse = entitiesApi.retrieveEntityById(URI.create(id), generalProperties.getTenant(), null, null, null, getLinkHeader());
		return entityVOHttpResponse.getBody().map(entityVO -> entityMapper.fromEntityVO(entityVO, ProductSpecificationCharacteristic.class));
	}

	public void createBundleProductSpecification(BundleProductSpecification bundleProductSpecification) {
		entitiesApi.createEntity(entityMapper.toEntityVO(bundleProductSpecification), generalProperties.getTenant());
	}

	public List<BundleProductSpecification> findBundleProductSpecification() {
		HttpResponse<EntityListVO> entityListVOHttpResponse = entitiesApi.queryEntities(generalProperties.getTenant(),
				null,
				null,
				BundleProductSpecification.TYPE_BUNDLE_PRODUCT_SPEC,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				getLinkHeader());

		return entityListVOHttpResponse.getBody()
				.map(ArrayList::new)
				.orElseGet(ArrayList::new)
				.stream()
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, BundleProductSpecification.class))
				.collect(Collectors.toList());

	}

	public Optional<BundleProductSpecification> getBundleProductSpecification(String id) {
		HttpResponse<EntityVO> entityVOHttpResponse = entitiesApi.retrieveEntityById(URI.create(id), generalProperties.getTenant(), null, null, null, getLinkHeader());
		return entityVOHttpResponse.getBody().map(entityVO -> entityMapper.fromEntityVO(entityVO, BundleProductSpecification.class));
	}

	public void createPricingLogicAlgorithm(PricingLogicAlgorithm pricingLogicAlgorithm) {
		entitiesApi.createEntity(entityMapper.toEntityVO(pricingLogicAlgorithm), generalProperties.getTenant());
	}

	public List<PricingLogicAlgorithm> findPricingLogicAlgorithms() {
		HttpResponse<EntityListVO> entityListVOHttpResponse = entitiesApi.queryEntities(generalProperties.getTenant(),
				null,
				null,
				BundleProductSpecification.TYPE_BUNDLE_PRODUCT_SPEC,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				getLinkHeader());

		return entityListVOHttpResponse.getBody()
				.map(ArrayList::new)
				.orElseGet(ArrayList::new)
				.stream()
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, PricingLogicAlgorithm.class))
				.collect(Collectors.toList());

	}

	public Optional<PricingLogicAlgorithm> getPricingLogicAlgorithm(String id) {
		HttpResponse<EntityVO> entityVOHttpResponse = entitiesApi.retrieveEntityById(URI.create(id), generalProperties.getTenant(), null, null, null, getLinkHeader());
		return entityVOHttpResponse.getBody().map(entityVO -> entityMapper.fromEntityVO(entityVO, PricingLogicAlgorithm.class));
	}

	public void createProductSpecificationCharacteristicValueUse(ProductSpecificationCharacteristicValueUse productSpecificationCharacteristicValueUse) {
		entitiesApi.createEntity(entityMapper.toEntityVO(productSpecificationCharacteristicValueUse), generalProperties.getTenant());
	}

	public List<ProductSpecificationCharacteristicValueUse> findProductSpecificationCharacteristicValueUses() {
		HttpResponse<EntityListVO> entityListVOHttpResponse = entitiesApi.queryEntities(generalProperties.getTenant(),
				null,
				null,
				ProductSpecificationCharacteristicValueUse.TYPE_PRODUCT_SPEC_CHARACTERISTIC_VALUE_USE,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				getLinkHeader());

		return entityListVOHttpResponse.getBody()
				.map(ArrayList::new)
				.orElseGet(ArrayList::new)
				.stream()
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, ProductSpecificationCharacteristicValueUse.class))
				.collect(Collectors.toList());

	}

	public Optional<ProductSpecificationCharacteristicValueUse> getProductSpecificationCharacteristicValueUse(String id) {
		HttpResponse<EntityVO> entityVOHttpResponse = entitiesApi.retrieveEntityById(URI.create(id), generalProperties.getTenant(), null, null, null, getLinkHeader());
		return entityVOHttpResponse.getBody().map(entityVO -> entityMapper.fromEntityVO(entityVO, ProductSpecificationCharacteristicValueUse.class));
	}

	public void createBundleProductOffering(BundleProductOffering bundleProductOffering) {
		entitiesApi.createEntity(entityMapper.toEntityVO(bundleProductOffering), generalProperties.getTenant());
	}

	public List<BundleProductOffering> findBundleProductOfferings() {
		HttpResponse<EntityListVO> entityListVOHttpResponse = entitiesApi.queryEntities(generalProperties.getTenant(),
				null,
				null,
				BundleProductOffering.TYPE_BUNDLE_PRODUCT_OFFERING,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				getLinkHeader());

		return entityListVOHttpResponse.getBody()
				.map(ArrayList::new)
				.orElseGet(ArrayList::new)
				.stream()
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, BundleProductOffering.class))
				.collect(Collectors.toList());
	}

	public Optional<TaxItem> getTaxItem(String id) {
		HttpResponse<EntityVO> entityVOHttpResponse = entitiesApi.retrieveEntityById(URI.create(id), generalProperties.getTenant(), null, null, null, getLinkHeader());
		return entityVOHttpResponse.getBody().map(entityVO -> entityMapper.fromEntityVO(entityVO, TaxItem.class));
	}

	public void createTaxItem(TaxItem taxItem) {
		entitiesApi.createEntity(entityMapper.toEntityVO(taxItem), generalProperties.getTenant());
	}


	public Optional<BundleProductOffering> getBundleProductOffering(String id) {
		HttpResponse<EntityVO> entityVOHttpResponse = entitiesApi.retrieveEntityById(URI.create(id), generalProperties.getTenant(), null, null, null, getLinkHeader());
		return entityVOHttpResponse.getBody().map(entityVO -> entityMapper.fromEntityVO(entityVO, BundleProductOffering.class));
	}

	public PricingLogicAlgorithm getOrCreatePLA(PricingLogicAlgorithm pricingLogicAlgorithm) {
		Optional<URI> optionalID = Optional.ofNullable(pricingLogicAlgorithm.getId());
		if (optionalID.isPresent()) {
			return getPricingLogicAlgorithm(optionalID.get().toString())
					.orElseThrow(() ->
							new NonExistentReferenceException(String.format("The referenced pricing logic algorithm with id %s does not exists.",
									optionalID.get()),
									optionalID.get().toString()));
		} else {
			URI specID = URI.create(String.format(TMForumMapper.ID_TEMPLATE, PricingLogicAlgorithm.TYPE_PRICING_LOGIC_ALGORITHM, UUID.randomUUID()));
			pricingLogicAlgorithm.setId(specID);
			createPricingLogicAlgorithm(pricingLogicAlgorithm);
			return pricingLogicAlgorithm;
		}
	}

	public BundleProductOffering getOrCreateBPO(BundleProductOffering bundleProductOffering) {
		Optional<URI> optionalID = Optional.ofNullable(bundleProductOffering.getId());
		if (optionalID.isPresent()) {
			return getBundleProductOffering(optionalID.get().toString())
					.orElseThrow(() ->
							new NonExistentReferenceException(String.format("The referenced bundle product offering with id %s does not exists.",
									optionalID.get()),
									optionalID.get().toString()));
		} else {
			URI specID = URI.create(String.format(TMForumMapper.ID_TEMPLATE, BundleProductOffering.TYPE_BUNDLE_PRODUCT_OFFERING, UUID.randomUUID()));
			bundleProductOffering.setId(specID);
			createBundleProductOffering(bundleProductOffering);
			return bundleProductOffering;
		}
	}

	public ProductSpecificationCharacteristicValueUse getOrCreatePSCVU(ProductSpecificationCharacteristicValueUse productSpecificationCharacteristicValueUse) {
		Optional<URI> optionalID = Optional.ofNullable(productSpecificationCharacteristicValueUse.getId());
		if (optionalID.isPresent()) {
			return getProductSpecificationCharacteristicValueUse(optionalID.get().toString())
					.orElseThrow(() ->
							new NonExistentReferenceException(String.format("The referenced pricing logic algorithm with id %s does not exists.",
									optionalID.get()),
									optionalID.get().toString()));
		} else {
			URI specID = URI.create(String.format(TMForumMapper.ID_TEMPLATE, ProductSpecificationCharacteristicValueUse.TYPE_PRODUCT_SPEC_CHARACTERISTIC_VALUE_USE, UUID.randomUUID()));
			Optional.ofNullable(productSpecificationCharacteristicValueUse.getProductSpecification()).map(List::of).ifPresent(validationService::checkReferenceExists);
			productSpecificationCharacteristicValueUse.setId(specID);
			createProductSpecificationCharacteristicValueUse(productSpecificationCharacteristicValueUse);
			return productSpecificationCharacteristicValueUse;
		}
	}

	public BundleProductSpecification getOrCreateBPS(BundleProductSpecification bundleProductSpecification) {
		Optional<URI> optionalSpecID = Optional.ofNullable(bundleProductSpecification.getId());
		if (optionalSpecID.isPresent()) {
			return getBundleProductSpecification(optionalSpecID.get().toString())
					.orElseThrow(() ->
							new NonExistentReferenceException(String.format("The referenced product specifcation with id %s does not exists.",
									optionalSpecID.get()),
									optionalSpecID.get().toString()));
		} else {
			URI specID = URI.create(String.format(TMForumMapper.ID_TEMPLATE, BundleProductSpecification.TYPE_BUNDLE_PRODUCT_SPEC, UUID.randomUUID()));
			bundleProductSpecification.setId(specID);
			createBundleProductSpecification(bundleProductSpecification);
			return bundleProductSpecification;
		}
	}

	public ProductSpecificationCharacteristic getOrCreatePSC(ProductSpecificationCharacteristic productSpecificationCharacteristic) {
		Optional<URI> optionalSpecID = Optional.ofNullable(productSpecificationCharacteristic.getId());
		if (optionalSpecID.isPresent()) {
			return getProductSpecificationCharacteristic(optionalSpecID.get().toString())
					.orElseThrow(() ->
							new NonExistentReferenceException(String.format("The referenced product specifcation with id %s does not exists.",
									optionalSpecID.get()),
									optionalSpecID.get().toString()));
		} else {
			URI specID = URI.create(String.format(TMForumMapper.ID_TEMPLATE, ProductSpecificationCharacteristic.TYPE_PRODUCT_SPECIFICATION_CHARACTERISTIC, UUID.randomUUID()));
			productSpecificationCharacteristic.setId(specID);
			Optional.ofNullable(productSpecificationCharacteristic.getProductSpecCharRelationship()).ifPresent(validationService::checkReferenceExists);
			createProductSpecificationCharacteristic(productSpecificationCharacteristic);
			return productSpecificationCharacteristic;
		}
	}

	public TaxItem getOrCreate(TaxItem taxItem) {
		Optional<URI> optionalId = Optional.ofNullable(taxItem.getId());
		if (optionalId.isPresent()) {
			return getTaxItem(optionalId.get().toString())
					.orElseThrow(() ->
							new NonExistentReferenceException(String.format("The referenced tax item with id %s does not exists.",
									optionalId.get()),
									optionalId.get().toString()));
		} else {
			URI specID = URI.create(String.format(TMForumMapper.ID_TEMPLATE, ProductSpecificationCharacteristic.TYPE_PRODUCT_SPECIFICATION_CHARACTERISTIC, UUID.randomUUID()));
			taxItem.setId(specID);
			createTaxItem(taxItem);
			return taxItem;
		}
	}
}

