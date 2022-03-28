package org.fiware.tmforum.repository;

import io.micronaut.http.HttpResponse;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.checkerframework.checker.units.qual.C;
import org.fiware.canismajor.api.NgsiLdApiClient;
import org.fiware.ngsi.api.EntitiesApiClient;
import org.fiware.ngsi.model.EntityListVO;
import org.fiware.tmforum.configuration.GeneralProperties;
import org.fiware.tmforum.domain.CanisMajorMapper;
import org.fiware.tmforum.domain.EntityMapper;
import org.fiware.tmforum.domain.TMForumMapper;
import org.fiware.tmforum.domain.party.individual.Individual;
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

	public ProductCatalogRepository(GeneralProperties generalProperties, EntitiesApiClient entitiesApi, EntityMapper entityMapper, ValidationService validationService, NgsiLdApiClient canisMajorClient, CanisMajorMapper canisMajorMapper) {
		super(generalProperties, entitiesApi, canisMajorClient, canisMajorMapper);
		this.entityMapper = entityMapper;
		this.validationService = validationService;
	}

	public Completable createCatalog(Catalog catalog) {
		return createEntity(entityMapper.toEntityVO(catalog), generalProperties.getTenant());
	}

	public Completable deleteEntry(String id) {
		return entitiesApi.removeEntityById(URI.create(id), generalProperties.getTenant(), null);
	}

	public Single<List<Catalog>> findCatalogs() {
		return entitiesApi.queryEntities(generalProperties.getTenant(),
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
						getLinkHeader())
				.map(List::stream)
				.map(entityVOStream -> entityVOStream.map(entityVO -> entityMapper.fromEntityVO(entityVO, Catalog.class)).toList());


	}

	public Maybe<Catalog> getCatalog(String id) {
		return retrieveEntityById(URI.create(id))
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, Catalog.class));
	}

	// category
	public Completable createCategory(Category category) {
		return createEntity(entityMapper.toEntityVO(category), generalProperties.getTenant());
	}

	public Single<List<Category>> findCategories() {
		return entitiesApi.queryEntities(generalProperties.getTenant(),
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
						getLinkHeader())
				.map(List::stream)
				.map(entityVOStream -> entityVOStream.map(entityVO -> entityMapper.fromEntityVO(entityVO, Category.class)).toList());

	}

	public Maybe<Category> getCategory(String id) {
		return retrieveEntityById(URI.create(id))
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, Category.class));
	}

	public Completable createProductOffering(ProductOffering productOffering) {
		return createEntity(entityMapper.toEntityVO(productOffering), generalProperties.getTenant());
	}

	public Single<List<ProductOffering>> findProductOfferings() {
		return entitiesApi.queryEntities(generalProperties.getTenant(),
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
						getLinkHeader())
				.map(List::stream)
				.map(entityVOStream -> entityVOStream.map(entityVO -> entityMapper.fromEntityVO(entityVO, ProductOffering.class)).toList());
	}

	public Maybe<ProductOffering> getProductOffering(String id) {
		return retrieveEntityById(URI.create(id))
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, ProductOffering.class));

	}

	public Completable createProductOfferingPrice(ProductOfferingPrice productOfferingPrice) {
		return createEntity(entityMapper.toEntityVO(productOfferingPrice), generalProperties.getTenant());
	}

	public Single<List<ProductOfferingPrice>> findProductOfferingPrices() {
		return entitiesApi.queryEntities(generalProperties.getTenant(),
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
						getLinkHeader())
				.map(List::stream)
				.map(entityVOStream -> entityVOStream.map(entityVO -> entityMapper.fromEntityVO(entityVO, ProductOfferingPrice.class)).toList());
	}

	public Maybe<ProductOfferingPrice> getProductOfferingPrice(String id) {
		return retrieveEntityById(URI.create(id))
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, ProductOfferingPrice.class));
	}

	public Completable createProductSpecification(ProductSpecification productSpecification) {
		return createEntity(entityMapper.toEntityVO(productSpecification), generalProperties.getTenant());
	}

	public Single<List<ProductSpecification>> findProductSpecifications() {
		return entitiesApi.queryEntities(generalProperties.getTenant(),
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
						getLinkHeader())
				.map(List::stream)
				.map(entityVOStream -> entityVOStream.map(entityVO -> entityMapper.fromEntityVO(entityVO, ProductSpecification.class)).toList());
	}

	public Maybe<ProductSpecification> getProductSpecification(String id) {
		return retrieveEntityById(URI.create(id))
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, ProductSpecification.class));
	}

	public Completable createProductSpecificationCharacteristic(ProductSpecificationCharacteristic productSpecificationCharacteristic) {
		return createEntity(entityMapper.toEntityVO(productSpecificationCharacteristic), generalProperties.getTenant());
	}

	public Single<List<ProductSpecificationCharacteristic>> findProductSpecificationCharacteristic() {
		return entitiesApi.queryEntities(generalProperties.getTenant(),
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
						getLinkHeader())
				.map(List::stream)
				.map(entityVOStream -> entityVOStream.map(entityVO -> entityMapper.fromEntityVO(entityVO, ProductSpecificationCharacteristic.class)).toList());
	}

	public Maybe<ProductSpecificationCharacteristic> getProductSpecificationCharacteristic(String id) {
		return retrieveEntityById(URI.create(id))
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, ProductSpecificationCharacteristic.class));
	}

	public Completable createBundleProductSpecification(BundleProductSpecification bundleProductSpecification) {
		return createEntity(entityMapper.toEntityVO(bundleProductSpecification), generalProperties.getTenant());
	}

	public Single<List<BundleProductSpecification>> findBundleProductSpecification() {
		return entitiesApi.queryEntities(generalProperties.getTenant(),
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
						getLinkHeader())
				.map(List::stream)
				.map(entityVOStream -> entityVOStream.map(entityVO -> entityMapper.fromEntityVO(entityVO, BundleProductSpecification.class)).toList());
	}

	public Maybe<BundleProductSpecification> getBundleProductSpecification(String id) {
		return retrieveEntityById(URI.create(id))
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, BundleProductSpecification.class));
	}

	public Completable createPricingLogicAlgorithm(PricingLogicAlgorithm pricingLogicAlgorithm) {
		return createEntity(entityMapper.toEntityVO(pricingLogicAlgorithm), generalProperties.getTenant());
	}

	public Single<List<PricingLogicAlgorithm>> findPricingLogicAlgorithms() {
		return entitiesApi.queryEntities(generalProperties.getTenant(),
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
						getLinkHeader())
				.map(List::stream)
				.map(entityVOStream -> entityVOStream.map(entityVO -> entityMapper.fromEntityVO(entityVO, PricingLogicAlgorithm.class)).toList());
	}

	public Maybe<PricingLogicAlgorithm> getPricingLogicAlgorithm(String id) {
		return retrieveEntityById(URI.create(id))
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, PricingLogicAlgorithm.class));
	}

	public Completable createProductSpecificationCharacteristicValueUse(ProductSpecificationCharacteristicValueUse productSpecificationCharacteristicValueUse) {
		return createEntity(entityMapper.toEntityVO(productSpecificationCharacteristicValueUse), generalProperties.getTenant());
	}

	public Single<List<ProductSpecificationCharacteristicValueUse>> findProductSpecificationCharacteristicValueUses() {
		return entitiesApi.queryEntities(generalProperties.getTenant(),
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
						getLinkHeader())
				.map(List::stream)
				.map(entityVOStream -> entityVOStream.map(entityVO -> entityMapper.fromEntityVO(entityVO, ProductSpecificationCharacteristicValueUse.class)).toList());
	}

	public Maybe<ProductSpecificationCharacteristicValueUse> getProductSpecificationCharacteristicValueUse(String id) {
		return retrieveEntityById(URI.create(id))
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, ProductSpecificationCharacteristicValueUse.class));
	}

	public Completable createBundleProductOffering(BundleProductOffering bundleProductOffering) {
		return createEntity(entityMapper.toEntityVO(bundleProductOffering), generalProperties.getTenant());
	}

	public Single<List<BundleProductOffering>> findBundleProductOfferings() {
		return entitiesApi.queryEntities(generalProperties.getTenant(),
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
						getLinkHeader())
				.map(List::stream)
				.map(entityVOStream -> entityVOStream.map(entityVO -> entityMapper.fromEntityVO(entityVO, BundleProductOffering.class)).toList());
	}

	public Maybe<TaxItem> getTaxItem(String id) {
		return retrieveEntityById(URI.create(id))
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, TaxItem.class));
	}

	public Completable createTaxItem(TaxItem taxItem) {
		return createEntity(entityMapper.toEntityVO(taxItem), generalProperties.getTenant());
	}

	public Maybe<BundleProductOffering> getBundleProductOffering(String id) {
		return retrieveEntityById(URI.create(id))
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, BundleProductOffering.class));
	}

	public Single<PricingLogicAlgorithm> getOrCreate(PricingLogicAlgorithm pricingLogicAlgorithm) {
		Optional<URI> optionalID = Optional.ofNullable(pricingLogicAlgorithm.getId());
		if (optionalID.isPresent()) {
			return getPricingLogicAlgorithm(optionalID.get().toString())
					.toSingle();
		} else {
			URI specID = URI.create(String.format(TMForumMapper.ID_TEMPLATE, PricingLogicAlgorithm.TYPE_PRICING_LOGIC_ALGORITHM, UUID.randomUUID()));
			pricingLogicAlgorithm.setId(specID);
			return createPricingLogicAlgorithm(pricingLogicAlgorithm).toSingleDefault(pricingLogicAlgorithm);
		}
	}

	public Single<BundleProductOffering> getOrCreate(BundleProductOffering bundleProductOffering) {
		Optional<URI> optionalID = Optional.ofNullable(bundleProductOffering.getId());
		if (optionalID.isPresent()) {
			return getBundleProductOffering(optionalID.get().toString())
					.toSingle();
		} else {
			URI specID = URI.create(String.format(TMForumMapper.ID_TEMPLATE, BundleProductOffering.TYPE_BUNDLE_PRODUCT_OFFERING, UUID.randomUUID()));
			bundleProductOffering.setId(specID);
			return createBundleProductOffering(bundleProductOffering).toSingleDefault(bundleProductOffering);
		}
	}

	public Single<ProductSpecificationCharacteristicValueUse> getOrCreate(ProductSpecificationCharacteristicValueUse productSpecificationCharacteristicValueUse) {
		Optional<URI> optionalID = Optional.ofNullable(productSpecificationCharacteristicValueUse.getId());
		if (optionalID.isPresent()) {
			return getProductSpecificationCharacteristicValueUse(optionalID.get().toString())
					.toSingle();
		} else {
			URI specID = URI.create(String.format(TMForumMapper.ID_TEMPLATE, ProductSpecificationCharacteristicValueUse.TYPE_PRODUCT_SPEC_CHARACTERISTIC_VALUE_USE, UUID.randomUUID()));
			Optional.ofNullable(productSpecificationCharacteristicValueUse.getProductSpecification()).map(List::of).ifPresent(validationService::checkReferenceExists);
			productSpecificationCharacteristicValueUse.setId(specID);
			return createProductSpecificationCharacteristicValueUse(productSpecificationCharacteristicValueUse).toSingleDefault(productSpecificationCharacteristicValueUse);
		}
	}

	public Single<BundleProductSpecification> getOrCreate(BundleProductSpecification bundleProductSpecification) {
		Optional<URI> optionalSpecID = Optional.ofNullable(bundleProductSpecification.getId());
		if (optionalSpecID.isPresent()) {
			return getBundleProductSpecification(optionalSpecID.get().toString())
					.toSingle();
		} else {
			URI specID = URI.create(String.format(TMForumMapper.ID_TEMPLATE, BundleProductSpecification.TYPE_BUNDLE_PRODUCT_SPEC, UUID.randomUUID()));
			bundleProductSpecification.setId(specID);
			return createBundleProductSpecification(bundleProductSpecification).toSingleDefault(bundleProductSpecification);
		}
	}

	public Single<ProductSpecificationCharacteristic> getOrCreate(ProductSpecificationCharacteristic productSpecificationCharacteristic) {
		Optional<URI> optionalSpecID = Optional.ofNullable(productSpecificationCharacteristic.getId());
		if (optionalSpecID.isPresent()) {
			return getProductSpecificationCharacteristic(optionalSpecID.get().toString())
					.toSingle();
		} else {
			URI specID = URI.create(String.format(TMForumMapper.ID_TEMPLATE, ProductSpecificationCharacteristic.TYPE_PRODUCT_SPECIFICATION_CHARACTERISTIC, UUID.randomUUID()));
			productSpecificationCharacteristic.setId(specID);
			Optional.ofNullable(productSpecificationCharacteristic.getProductSpecCharRelationship()).ifPresent(validationService::checkReferenceExists);
			return createProductSpecificationCharacteristic(productSpecificationCharacteristic).toSingleDefault(productSpecificationCharacteristic);
		}
	}

	public Single<TaxItem> getOrCreate(TaxItem taxItem) {
		Optional<URI> optionalId = Optional.ofNullable(taxItem.getId());
		if (optionalId.isPresent()) {
			return getTaxItem(optionalId.get().toString())
					.toSingle();
		} else {
			URI specID = URI.create(String.format(TMForumMapper.ID_TEMPLATE, ProductSpecificationCharacteristic.TYPE_PRODUCT_SPECIFICATION_CHARACTERISTIC, UUID.randomUUID()));
			taxItem.setId(specID);
			return createTaxItem(taxItem).toSingleDefault(taxItem);
		}
	}
}

