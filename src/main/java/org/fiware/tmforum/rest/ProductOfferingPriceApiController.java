package org.fiware.tmforum.rest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fiware.product.api.ProductOfferingPriceApi;
import org.fiware.product.model.ProductOfferingPriceCreateVO;
import org.fiware.product.model.ProductOfferingPriceUpdateVO;
import org.fiware.product.model.ProductOfferingPriceVO;
import org.fiware.tmforum.domain.TMForumMapper;
import org.fiware.tmforum.domain.product.BundleProductSpecification;
import org.fiware.tmforum.domain.product.offering.ConstraintRef;
import org.fiware.tmforum.domain.product.offering.PlaceRef;
import org.fiware.tmforum.domain.product.offering.PricingLogicAlgorithm;
import org.fiware.tmforum.domain.product.offering.ProductOfferingPrice;
import org.fiware.tmforum.domain.product.offering.TaxItem;
import org.fiware.tmforum.repository.ProductCatalogRepository;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductOfferingPriceApiController implements ProductOfferingPriceApi {

	private final TMForumMapper tmForumMapper;
	private final ProductCatalogRepository productCatalogRepository;
	private final ValidationService validationService;

	@Override
	public HttpResponse<ProductOfferingPriceVO> createProductOfferingPrice(ProductOfferingPriceCreateVO productOfferingPriceCreateVO) {
		ProductOfferingPriceVO productOfferingPriceVO = tmForumMapper.map(productOfferingPriceCreateVO);
		ProductOfferingPrice productOfferingPrice = tmForumMapper.map(productOfferingPriceVO);
		Optional.ofNullable(productOfferingPrice.getPopRelationship()).ifPresent(validationService::checkReferenceExists);
		Optional.ofNullable(productOfferingPrice.getConstraint()).ifPresent(validationService::checkReferenceExists);
		Optional.ofNullable(productOfferingPrice.getPlace()).ifPresent(validationService::checkReferenceExists);

		List<PricingLogicAlgorithm> pricingLogicAlgorithms =
				Optional.ofNullable(productOfferingPrice.getPricingLogicAlgorithm()).orElseGet(List::of);
		List<PricingLogicAlgorithm> updatedAlgos = pricingLogicAlgorithms.stream().map(productCatalogRepository::getOrCreatePLA).toList();
		productOfferingPrice.setPricingLogicAlgorithm(updatedAlgos);
		List<TaxItem> taxItems =
				Optional.ofNullable(productOfferingPrice.getTax()).orElseGet(List::of);
		List<TaxItem> updatedTaxItems = taxItems.stream().map(productCatalogRepository::getOrCreate).toList();
		productOfferingPrice.setTax(updatedTaxItems);

		productCatalogRepository.createProductOfferingPrice(productOfferingPrice);
		return HttpResponse.created(productOfferingPriceVO);
	}

	@Override
	public HttpResponse<Object> deleteProductOfferingPrice(String id) {
		try {
			productCatalogRepository.deleteEntry(id);
			return HttpResponse.noContent();
		} catch (HttpClientResponseException e) {
			return HttpResponse.status(e.getStatus());
		}
	}

	@Override
	public HttpResponse<List<ProductOfferingPriceVO>> listProductOfferingPrice(@Nullable String fields, @Nullable Integer offset, @Nullable Integer limit) {
		return HttpResponse.ok(productCatalogRepository.findProductOfferingPrices().stream().map(tmForumMapper::map).collect(Collectors.toList()));

	}

	@Override
	public HttpResponse<ProductOfferingPriceVO> patchProductOfferingPrice(String id, ProductOfferingPriceUpdateVO productOfferingPrice) {
		return null;
	}

	@Override
	public HttpResponse<ProductOfferingPriceVO> retrieveProductOfferingPrice(String id, @Nullable String fields) {
		return productCatalogRepository
				.getProductOfferingPrice(id)
				.map(tmForumMapper::map)
				.map(HttpResponse::ok)
				.orElseGet(HttpResponse::notFound);
	}
}
