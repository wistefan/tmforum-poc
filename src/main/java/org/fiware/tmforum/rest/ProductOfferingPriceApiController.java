package org.fiware.tmforum.rest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fiware.product.api.ProductOfferingPriceApi;
import org.fiware.product.model.ProductOfferingPriceCreateVO;
import org.fiware.product.model.ProductOfferingPriceUpdateVO;
import org.fiware.product.model.ProductOfferingPriceVO;
import org.fiware.tmforum.domain.TMForumMapper;
import org.fiware.tmforum.domain.party.TaxExemptionCertificate;
import org.fiware.tmforum.domain.party.organization.Organization;
import org.fiware.tmforum.domain.product.BundleProductSpecification;
import org.fiware.tmforum.domain.product.RefEntity;
import org.fiware.tmforum.domain.product.offering.ConstraintRef;
import org.fiware.tmforum.domain.product.offering.PlaceRef;
import org.fiware.tmforum.domain.product.offering.PricingLogicAlgorithm;
import org.fiware.tmforum.domain.product.offering.ProductOfferingPrice;
import org.fiware.tmforum.domain.product.offering.TaxItem;
import org.fiware.tmforum.exception.NonExistentReferenceException;
import org.fiware.tmforum.repository.ProductCatalogRepository;

import javax.annotation.Nullable;
import java.util.Arrays;
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
	public Single<HttpResponse<ProductOfferingPriceVO>> createProductOfferingPrice(ProductOfferingPriceCreateVO productOfferingPriceCreateVO) {

		ProductOfferingPriceVO productOfferingPriceVO = tmForumMapper.map(productOfferingPriceCreateVO);
		ProductOfferingPrice productOfferingPrice = tmForumMapper.map(productOfferingPriceVO);

		Single<ProductOfferingPrice> popSingle = Single.just(productOfferingPrice);
		if (productOfferingPrice.getPopRelationship() != null && !productOfferingPrice.getPopRelationship().isEmpty()) {
			Single<ProductOfferingPrice> popRel = validationService.getCheckingSingle(productOfferingPrice.getPopRelationship(), productOfferingPrice);
			popSingle = Single.zip(popSingle, popRel, (p1, p2) -> p1);
		}
		if (productOfferingPrice.getConstraint() != null && !productOfferingPrice.getConstraint().isEmpty()) {
			Single<ProductOfferingPrice> constRel = validationService.getCheckingSingle(productOfferingPrice.getConstraint(), productOfferingPrice);
			popSingle = Single.zip(popSingle, constRel, (p1, p2) -> p1);
		}
		if (productOfferingPrice.getPlace() != null && !productOfferingPrice.getPlace().isEmpty()) {
			Single<ProductOfferingPrice> placeRel = validationService.getCheckingSingle(productOfferingPrice.getPlace(), productOfferingPrice);
			popSingle = Single.zip(popSingle, placeRel, (p1, p2) -> p1);
		}

		List<PricingLogicAlgorithm> pricingLogicAlgorithms =
				Optional.ofNullable(productOfferingPrice.getPricingLogicAlgorithm()).orElseGet(List::of);

		List<TaxItem> taxItems =
				Optional.ofNullable(productOfferingPrice.getTax()).orElseGet(List::of);

		if (!pricingLogicAlgorithms.isEmpty()) {
			Single<List<PricingLogicAlgorithm>> pricingLogicAlgorithmsSingles =
					Single.zip(pricingLogicAlgorithms.stream().map(productCatalogRepository::getOrCreate).toList(), t -> Arrays.stream(t).map(PricingLogicAlgorithm.class::cast).toList());

			Single<ProductOfferingPrice> plaSingle = pricingLogicAlgorithmsSingles
					.map(updatedAlgos -> {
						productOfferingPrice.setPricingLogicAlgorithm(updatedAlgos);
						return productOfferingPrice;
					});
			popSingle = Single.zip(popSingle, plaSingle, (pop1, pop2) -> pop2);
		}

		if (!taxItems.isEmpty()) {
			Single<List<TaxItem>> taxItemsSingles =
					Single.zip(taxItems.stream().map(productCatalogRepository::getOrCreate).toList(), t -> Arrays.stream(t).map(TaxItem.class::cast).toList());
			Single<ProductOfferingPrice> taxItemsSingle = taxItemsSingles
					.map(updatedTaxItems -> {
						productOfferingPrice.setTax(updatedTaxItems);
						return productOfferingPrice;
					});
			popSingle = Single.zip(popSingle, taxItemsSingle, (pop1, pop2) -> pop2);
		}
		return popSingle
				.flatMap(pop -> productCatalogRepository.createProductOfferingPrice(pop).toSingleDefault(productOfferingPrice))
				.cast(ProductOfferingPrice.class)
				.map(tmForumMapper::map)
				.map(HttpResponse::created);
	}

	@Override
	public Single<HttpResponse<Object>> deleteProductOfferingPrice(String id) {
		return productCatalogRepository.deleteEntry(id).toSingleDefault(HttpResponse.noContent());

	}

	@Override
	public Single<HttpResponse<List<ProductOfferingPriceVO>>> listProductOfferingPrice(@Nullable String fields, @Nullable Integer offset, @Nullable Integer limit) {
		return productCatalogRepository.findProductOfferingPrices()
				.map(List::stream)
				.map(productOfferingPriceStream -> productOfferingPriceStream
						.map(tmForumMapper::map)
						.toList())
				.map(HttpResponse::ok);
	}

	@Override
	public Single<HttpResponse<ProductOfferingPriceVO>> patchProductOfferingPrice(String id, ProductOfferingPriceUpdateVO productOfferingPrice) {
		return null;
	}

	@Override
	public Single<HttpResponse<ProductOfferingPriceVO>> retrieveProductOfferingPrice(String id, @Nullable String fields) {
		return productCatalogRepository
				.getProductOfferingPrice(id)
				.map(tmForumMapper::map)
				.toSingle()
				.map(HttpResponse::ok);
	}
}
