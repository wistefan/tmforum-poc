package org.fiware.tmforum.rest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fiware.product.api.ProductSpecificationApi;
import org.fiware.product.model.ProductSpecificationCreateVO;
import org.fiware.product.model.ProductSpecificationUpdateVO;
import org.fiware.product.model.ProductSpecificationVO;
import org.fiware.tmforum.domain.EntityWithId;
import org.fiware.tmforum.domain.TMForumMapper;
import org.fiware.tmforum.domain.product.BundleProductSpecification;
import org.fiware.tmforum.domain.product.ProductSpecification;
import org.fiware.tmforum.domain.product.ProductSpecificationCharacteristic;
import org.fiware.tmforum.domain.product.ResourceSpecificationRef;
import org.fiware.tmforum.domain.product.ServiceSpecificationRef;
import org.fiware.tmforum.domain.product.offering.ConstraintRef;
import org.fiware.tmforum.domain.product.offering.PlaceRef;
import org.fiware.tmforum.domain.product.offering.PricingLogicAlgorithm;
import org.fiware.tmforum.domain.product.offering.ProductOffering;
import org.fiware.tmforum.domain.product.offering.ProductOfferingPrice;
import org.fiware.tmforum.domain.product.offering.TaxItem;
import org.fiware.tmforum.exception.NonExistentReferenceException;
import org.fiware.tmforum.repository.ProductCatalogRepository;

import javax.annotation.Nullable;
import javax.swing.text.html.Option;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductSpecificationApiController implements ProductSpecificationApi {

	private final TMForumMapper tmForumMapper;
	private final ProductCatalogRepository productCatalogRepository;
	private final ValidationService validationService;

	@Override
	public Single<HttpResponse<ProductSpecificationVO>> createProductSpecification(ProductSpecificationCreateVO productSpecificationCreateVO) {
		ProductSpecificationVO productSpecificationVO = tmForumMapper.map(productSpecificationCreateVO);
		ProductSpecification productSpecification = tmForumMapper.map(productSpecificationVO);

		Single<ProductSpecification> productSpecificationSingle = Single.just(productSpecification);
		if (productSpecification.getProductSpecificationRelationship() != null && !productSpecification.getProductSpecificationRelationship().isEmpty()) {
			Single<ProductSpecification> checkingSingle = validationService.getCheckingSingle(productSpecification.getProductSpecificationRelationship(), productSpecification);
			productSpecificationSingle = Single.zip(productSpecificationSingle, checkingSingle, (p1, p2) -> p1);
		}
		if (productSpecification.getRelatedParty() != null && !productSpecification.getRelatedParty().isEmpty()) {
			Single<ProductSpecification> checkingSingle = validationService.getCheckingSingle(productSpecification.getRelatedParty(), productSpecification);
			productSpecificationSingle = Single.zip(productSpecificationSingle, checkingSingle, (p1, p2) -> p1);
		}
		if (productSpecification.getServiceSpecification() != null && !productSpecification.getServiceSpecification().isEmpty()) {
			Single<ProductSpecification> checkingSingle = validationService.getCheckingSingle(productSpecification.getServiceSpecification(), productSpecification);
			productSpecificationSingle = Single.zip(productSpecificationSingle, checkingSingle, (p1, p2) -> p1);
		}
		if (productSpecification.getResourceSpecification() != null && !productSpecification.getResourceSpecification().isEmpty()) {
			Single<ProductSpecification> checkingSingle = validationService.getCheckingSingle(productSpecification.getResourceSpecification(), productSpecification);
			productSpecificationSingle = Single.zip(productSpecificationSingle, checkingSingle, (p1, p2) -> p1);
		}

		List<ProductSpecificationCharacteristic> productSpecificationCharacteristics =
				Optional.ofNullable(productSpecification.getProductSpecCharacteristic()).orElseGet(List::of);
		List<BundleProductSpecification> bundleProductSpecifications =
				Optional.ofNullable(productSpecification.getBundledProductSpecification()).orElseGet(List::of);

		if (!productSpecificationCharacteristics.isEmpty()) {
			Single<List<ProductSpecificationCharacteristic>> prodSpecCharaSingles =
					Single.zip(productSpecificationCharacteristics.stream().map(productCatalogRepository::getOrCreate).toList(), t -> Arrays.stream(t).map(ProductSpecificationCharacteristic.class::cast).toList());

			Single<ProductSpecification> plaSingle = prodSpecCharaSingles
					.map(updatedCharas -> {
						productSpecification.setProductSpecCharacteristic(updatedCharas);
						return productSpecification;
					});
			productSpecificationSingle = Single.zip(productSpecificationSingle, plaSingle, (pop1, pop2) -> pop2);
		}

		if (!bundleProductSpecifications.isEmpty()) {
			Single<List<BundleProductSpecification>> bundleProductSpecificationsSingles =
					Single.zip(bundleProductSpecifications.stream().map(productCatalogRepository::getOrCreate).toList(), t -> Arrays.stream(t).map(BundleProductSpecification.class::cast).toList());
			Single<ProductSpecification> bpsSingle = bundleProductSpecificationsSingles
					.map(updatedBundleSpecs -> {
						productSpecification.setBundledProductSpecification(updatedBundleSpecs);
						return productSpecification;
					});
			productSpecificationSingle = Single.zip(productSpecificationSingle, bpsSingle, (pop1, pop2) -> pop2);
		}
		return productSpecificationSingle
				.flatMap(ps -> productCatalogRepository
						.createProductSpecification(ps)
						.toSingleDefault(productSpecification))
				.cast(ProductSpecification.class)
				.map(tmForumMapper::map)
				.map(HttpResponse::created);

	}


	@Override
	public Single<HttpResponse<Object>> deleteProductSpecification(String id) {
		return productCatalogRepository.deleteEntry(id).toSingleDefault(HttpResponse.noContent());
	}

	@Override
	public Single<HttpResponse<List<ProductSpecificationVO>>> listProductSpecification(@Nullable String fields, @Nullable Integer offset, @Nullable Integer limit) {
		return productCatalogRepository.findProductSpecifications()
				.map(List::stream)
				.map(productSpecificationStream -> productSpecificationStream
						.map(tmForumMapper::map)
						.toList())
				.map(HttpResponse::ok);

	}

	@Override
	public Single<HttpResponse<ProductSpecificationVO>> patchProductSpecification(String id, ProductSpecificationUpdateVO productSpecification) {
		return null;
	}

	@Override
	public Single<HttpResponse<ProductSpecificationVO>> retrieveProductSpecification(String id, @Nullable String fields) {
		return productCatalogRepository
				.getProductSpecification(id)
				.map(tmForumMapper::map)
				.toSingle()
				.map(HttpResponse::ok);
	}
}
