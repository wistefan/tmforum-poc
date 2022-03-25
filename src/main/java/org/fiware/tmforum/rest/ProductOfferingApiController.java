package org.fiware.tmforum.rest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fiware.product.api.ProductOfferingApi;
import org.fiware.product.model.ProductOfferingCreateVO;
import org.fiware.product.model.ProductOfferingUpdateVO;
import org.fiware.product.model.ProductOfferingVO;
import org.fiware.tmforum.domain.TMForumMapper;
import org.fiware.tmforum.domain.product.offering.BundleProductOffering;
import org.fiware.tmforum.domain.product.offering.ProductOffering;
import org.fiware.tmforum.repository.ProductCatalogRepository;

import javax.annotation.Nullable;
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
	public HttpResponse<ProductOfferingVO> createProductOffering(ProductOfferingCreateVO productOfferingCreateVO) {

		ProductOfferingVO productOfferingVO = tmForumMapper.map(productOfferingCreateVO);
		ProductOffering productOffering = tmForumMapper.map(productOfferingVO);

		Optional.ofNullable(productOffering.getAgreement()).ifPresent(validationService::checkReferenceExists);
		Optional.ofNullable(productOffering.getCategory()).ifPresent(validationService::checkReferenceExists);
		Optional.ofNullable(productOffering.getChannel()).ifPresent(validationService::checkReferenceExists);
		Optional.ofNullable(productOffering.getResourceCandidate()).map(List::of).ifPresent(validationService::checkReferenceExists);
		Optional.ofNullable(productOffering.getServiceCandidate()).map(List::of).ifPresent(validationService::checkReferenceExists);
		Optional.ofNullable(productOffering.getServiceLevelAgreement()).map(List::of).ifPresent(validationService::checkReferenceExists);
		Optional.ofNullable(productOffering.getProductOfferingRelationship()).ifPresent(validationService::checkReferenceExists);
		Optional.ofNullable(productOffering.getProductSpecification()).map(List::of).ifPresent(validationService::checkReferenceExists);

		List<BundleProductOffering> bundleProductOfferings =
				Optional.ofNullable(productOffering.getBundledProductOffering()).orElseGet(List::of);
		List<BundleProductOffering> updatedOfferings = bundleProductOfferings.stream().map(productCatalogRepository::getOrCreateBPO).toList();
		productOffering.setBundledProductOffering(updatedOfferings);

		productCatalogRepository.createProductOffering(productOffering);
		return HttpResponse.created(productOfferingVO);
	}

	@Override
	public HttpResponse<Object> deleteProductOffering(String id) {
		try {
			productCatalogRepository.deleteEntry(id);
			return HttpResponse.noContent();
		} catch (HttpClientResponseException e) {
			return HttpResponse.status(e.getStatus());
		}
	}

	@Override
	public HttpResponse<List<ProductOfferingVO>> listProductOffering(@Nullable String fields, @Nullable Integer offset, @Nullable Integer limit) {
		return HttpResponse.ok(productCatalogRepository.findProductOfferings().stream().map(tmForumMapper::map).collect(Collectors.toList()));
	}

	@Override
	public HttpResponse<ProductOfferingVO> patchProductOffering(String id, ProductOfferingUpdateVO productOffering) {
		return null;
	}

	@Override
	public HttpResponse<ProductOfferingVO> retrieveProductOffering(String id, @Nullable String fields) {
		return productCatalogRepository
				.getProductOffering(id)
				.map(tmForumMapper::map)
				.map(HttpResponse::ok)
				.orElseGet(HttpResponse::notFound);
	}
}
