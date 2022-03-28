package org.fiware.tmforum.rest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fiware.product.api.ProductOfferingApi;
import org.fiware.product.model.ProductOfferingCreateVO;
import org.fiware.product.model.ProductOfferingUpdateVO;
import org.fiware.product.model.ProductOfferingVO;
import org.fiware.tmforum.domain.TMForumMapper;
import org.fiware.tmforum.domain.party.TaxExemptionCertificate;
import org.fiware.tmforum.domain.party.organization.Organization;
import org.fiware.tmforum.domain.product.offering.BundleProductOffering;
import org.fiware.tmforum.domain.product.offering.ProductOffering;
import org.fiware.tmforum.repository.ProductCatalogRepository;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductOfferingApiController implements ProductOfferingApi {

	private final TMForumMapper tmForumMapper;
	private final ProductCatalogRepository productCatalogRepository;
	private final ValidationService validationService;

	@Override
	public Single<HttpResponse<ProductOfferingVO>> createProductOffering(ProductOfferingCreateVO productOfferingCreateVO) {
		ProductOfferingVO productOfferingVO = tmForumMapper.map(productOfferingCreateVO);
		ProductOffering productOffering = tmForumMapper.map(productOfferingVO);

		Single<ProductOffering> productOfferingSingle = Single.just(productOffering);
		if (productOffering.getAgreement() != null && !productOffering.getAgreement().isEmpty()) {
			Single<ProductOffering> checkingSingle = validationService.getCheckingSingle(productOffering.getAgreement(), productOffering);
			productOfferingSingle = Single.zip(productOfferingSingle, checkingSingle, (p1, p2) -> p1);
		}
		if (productOffering.getCategory() != null && !productOffering.getCategory().isEmpty()) {
			Single<ProductOffering> checkingSingle = validationService.getCheckingSingle(productOffering.getCategory(), productOffering);
			productOfferingSingle = Single.zip(productOfferingSingle, checkingSingle, (p1, p2) -> p1);
		}
		if (productOffering.getChannel() != null && !productOffering.getChannel().isEmpty()) {
			Single<ProductOffering> checkingSingle = validationService.getCheckingSingle(productOffering.getChannel(), productOffering);
			productOfferingSingle = Single.zip(productOfferingSingle, checkingSingle, (p1, p2) -> p1);
		}
		if (productOffering.getResourceCandidate() != null) {
			Single<ProductOffering> checkingSingle = validationService.getCheckingSingle(List.of(productOffering.getResourceCandidate()), productOffering);
			productOfferingSingle = Single.zip(productOfferingSingle, checkingSingle, (p1, p2) -> p1);
		}
		if (productOffering.getServiceCandidate() != null) {
			Single<ProductOffering> checkingSingle = validationService.getCheckingSingle(List.of(productOffering.getServiceCandidate()), productOffering);
			productOfferingSingle = Single.zip(productOfferingSingle, checkingSingle, (p1, p2) -> p1);
		}
		if (productOffering.getServiceLevelAgreement() != null) {
			Single<ProductOffering> checkingSingle = validationService.getCheckingSingle(List.of(productOffering.getServiceLevelAgreement()), productOffering);
			productOfferingSingle = Single.zip(productOfferingSingle, checkingSingle, (p1, p2) -> p1);
		}
		if (productOffering.getProductOfferingRelationship() != null && !productOffering.getProductOfferingRelationship().isEmpty()) {
			Single<ProductOffering> checkingSingle = validationService.getCheckingSingle(productOffering.getProductOfferingRelationship(), productOffering);
			productOfferingSingle = Single.zip(productOfferingSingle, checkingSingle, (p1, p2) -> p1);
		}
		if (productOffering.getProductSpecification() != null) {
			Single<ProductOffering> checkingSingle = validationService.getCheckingSingle(List.of(productOffering.getProductSpecification()), productOffering);
			productOfferingSingle = Single.zip(productOfferingSingle, checkingSingle, (p1, p2) -> p1);
		}

		List<BundleProductOffering> bundleProductOfferings =
				Optional.ofNullable(productOffering.getBundledProductOffering()).orElseGet(List::of);

		if (!bundleProductOfferings.isEmpty()) {
			Single<List<BundleProductOffering>> bundleProductOfferingsSingles =
					Single.zip(bundleProductOfferings.stream().map(productCatalogRepository::getOrCreate).toList(), t -> Arrays.stream(t).map(BundleProductOffering.class::cast).toList());
			Single<ProductOffering> updatingSingle = bundleProductOfferingsSingles
					.map(updatedBundles -> {
						productOffering.setBundledProductOffering(updatedBundles);
						return productOffering;
					});
			productOfferingSingle = Single.zip(productOfferingSingle, updatingSingle, (p1, p2) -> p1);

		}

		return productOfferingSingle
				.flatMap(pOToCreate -> productCatalogRepository
						.createProductOffering(pOToCreate)
						.toSingleDefault(pOToCreate))
				.cast(ProductOffering.class)
				.map(tmForumMapper::map)
				.map(HttpResponse::created);
	}

	@Override
	public Single<HttpResponse<Object>> deleteProductOffering(String id) {
		return productCatalogRepository.deleteEntry(id).toSingleDefault(HttpResponse.noContent());
	}

	@Override
	public Single<HttpResponse<List<ProductOfferingVO>>> listProductOffering(@Nullable String fields, @Nullable Integer offset, @Nullable Integer limit) {
		return productCatalogRepository.findProductOfferings()
				.map(List::stream)
				.map(productOfferingStream -> productOfferingStream.map(tmForumMapper::map).toList())
				.map(HttpResponse::ok);
	}

	@Override
	public Single<HttpResponse<ProductOfferingVO>> patchProductOffering(String id, ProductOfferingUpdateVO productOffering) {
		return null;
	}

	@Override
	public Single<HttpResponse<ProductOfferingVO>> retrieveProductOffering(String id, @Nullable String fields) {
		return productCatalogRepository
				.getProductOffering(id)
				.map(tmForumMapper::map)
				.toSingle()
				.map(HttpResponse::ok);
	}
}
