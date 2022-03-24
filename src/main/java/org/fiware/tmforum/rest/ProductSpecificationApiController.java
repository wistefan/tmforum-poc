package org.fiware.tmforum.rest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
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
import org.fiware.tmforum.exception.NonExistentReferenceException;
import org.fiware.tmforum.repository.ProductCatalogRepository;

import javax.annotation.Nullable;
import javax.swing.text.html.Option;
import java.net.URI;
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
	public HttpResponse<ProductSpecificationVO> createProductSpecification(ProductSpecificationCreateVO productSpecificationCreateVO) {
		ProductSpecificationVO productSpecificationVO = tmForumMapper.map(productSpecificationCreateVO);
		ProductSpecification productSpecification = tmForumMapper.map(productSpecificationVO);

		Optional.ofNullable(productSpecification.getProductSpecificationRelationship()).ifPresent(validationService::checkReferenceExists);
		Optional.ofNullable(productSpecification.getRelatedParty()).ifPresent(validationService::checkReferenceExists);
		Optional.ofNullable(productSpecification.getServiceSpecification()).ifPresent(validationService::checkReferenceExists);
		Optional.ofNullable(productSpecification.getResourceSpecification()).ifPresent(validationService::checkReferenceExists);

		List<ProductSpecificationCharacteristic> productSpecificationCharacteristics =
				Optional.ofNullable(productSpecification.getProductSpecCharacteristic()).orElseGet(List::of);
		List<ProductSpecificationCharacteristic> updatedSpecs = productSpecificationCharacteristics.stream().map(productCatalogRepository::getOrCreatePSC).toList();
		productSpecification.setProductSpecCharacteristic(updatedSpecs);

		List<BundleProductSpecification> bundleProductSpecifications =
				Optional.ofNullable(productSpecification.getBundledProductSpecification()).orElseGet(List::of);
		List<BundleProductSpecification> updatedBundleSpecs = bundleProductSpecifications.stream().map(productCatalogRepository::getOrCreateBPS).toList();
		productSpecification.setBundledProductSpecification(updatedBundleSpecs);

		productCatalogRepository.createProductSpecification(productSpecification);
		return HttpResponse.created(tmForumMapper.map(productSpecification));
	}



	@Override
	public HttpResponse<Object> deleteProductSpecification(String id) {
		try {
			productCatalogRepository.deleteEntry(id);
			return HttpResponse.noContent();
		} catch (HttpClientResponseException e) {
			return HttpResponse.status(e.getStatus());
		}
	}

	@Override
	public HttpResponse<List<ProductSpecificationVO>> listProductSpecification(@Nullable String fields, @Nullable Integer offset, @Nullable Integer limit) {
		return HttpResponse.ok(productCatalogRepository.findProductSpecifications().stream().map(tmForumMapper::map).collect(Collectors.toList()));
	}

	@Override
	public HttpResponse<ProductSpecificationVO> patchProductSpecification(String id, ProductSpecificationUpdateVO productSpecification) {
		return null;
	}

	@Override
	public HttpResponse<ProductSpecificationVO> retrieveProductSpecification(String id, @Nullable String fields) {
		return productCatalogRepository
				.getProductSpecification(id)
				.map(tmForumMapper::map)
				.map(HttpResponse::ok)
				.orElseGet(HttpResponse::notFound);
	}
}
