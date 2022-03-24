package org.fiware.tmforum.rest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fiware.product.api.CatalogApi;
import org.fiware.product.model.CatalogCreateVO;
import org.fiware.product.model.CatalogUpdateVO;
import org.fiware.product.model.CatalogVO;
import org.fiware.tmforum.domain.TMForumMapper;
import org.fiware.tmforum.domain.party.RelatedParty;
import org.fiware.tmforum.domain.product.Catalog;
import org.fiware.tmforum.domain.product.CategoryRef;
import org.fiware.tmforum.domain.product.offering.ProductOfferingRef;
import org.fiware.tmforum.exception.NonExistentReferenceException;
import org.fiware.tmforum.repository.PartyRepository;
import org.fiware.tmforum.repository.ProductCatalogRepository;

import javax.annotation.Nullable;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CatalogApiController implements CatalogApi {

	private final TMForumMapper tmForumMapper;
	private final ProductCatalogRepository productCatalogRepository;
	private final PartyRepository partyRepository;
	private final ValidationService validationService;

	@Override
	public HttpResponse<CatalogVO> createCatalog(CatalogCreateVO catalogCreateVO) {
		CatalogVO catalogVO = tmForumMapper.map(catalogCreateVO);
		Catalog catalog = tmForumMapper.map(catalogVO);

		Optional.ofNullable(catalog.getCategory()).ifPresent(validationService::checkReferenceExists);
		Optional.ofNullable(catalog.getRelatedParty()).ifPresent(validationService::checkReferenceExists);

		productCatalogRepository.createCatalog(catalog);
		return HttpResponse.ok(catalogVO);
	}

	@Override
	public HttpResponse<Object> deleteCatalog(String id) {
		try {
			productCatalogRepository.deleteEntry(id);
			return HttpResponse.noContent();
		} catch (HttpClientResponseException e) {
			return HttpResponse.status(e.getStatus());
		}
	}

	@Override
	public HttpResponse<List<CatalogVO>> listCatalog(@Nullable String fields, @Nullable Integer offset, @Nullable Integer limit) {
		return HttpResponse.ok(productCatalogRepository.findCatalogs().stream().map(tmForumMapper::map).collect(Collectors.toList()));
	}

	@Override
	public HttpResponse<CatalogVO> patchCatalog(String id, CatalogUpdateVO catalog) {
		return null;
	}

	@Override
	public HttpResponse<CatalogVO> retrieveCatalog(String id, @Nullable String fields) {
		return productCatalogRepository
				.getCatalog(id)
				.map(tmForumMapper::map)
				.map(HttpResponse::ok)
				.orElseGet(HttpResponse::notFound);
	}
}
