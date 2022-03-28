package org.fiware.tmforum.rest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.reactivex.Single;
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
import org.fiware.tmforum.domain.product.offering.ProductOfferingPrice;
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
	public Single<HttpResponse<CatalogVO>> createCatalog(CatalogCreateVO catalogCreateVO) {
		CatalogVO catalogVO = tmForumMapper.map(catalogCreateVO);
		Catalog catalog = tmForumMapper.map(catalogVO);


		Single<Catalog> catalogSingle = Single.just(catalog);
		if (catalog.getCategory() != null && !catalog.getCategory().isEmpty()) {
			Single<Catalog> popRel = validationService.getCheckingSingle(catalog.getCategory(), catalog);
			catalogSingle = Single.zip(catalogSingle, popRel, (p1, p2) -> p1);
		}
		if (catalog.getRelatedParty() != null && !catalog.getRelatedParty().isEmpty()) {
			Single<Catalog> constRel = validationService.getCheckingSingle(catalog.getRelatedParty(), catalog);
			catalogSingle = Single.zip(catalogSingle, constRel, (p1, p2) -> p1);
		}

		return catalogSingle
				.flatMap(cat -> productCatalogRepository
						.createCatalog(cat).toSingleDefault(cat))
				.cast(Catalog.class)
				.map(tmForumMapper::map)
				.map(HttpResponse::created);
	}

	@Override
	public Single<HttpResponse<Object>> deleteCatalog(String id) {
		return productCatalogRepository.deleteEntry(id).toSingleDefault(HttpResponse.noContent());
	}

	@Override
	public Single<HttpResponse<List<CatalogVO>>> listCatalog(@Nullable String fields, @Nullable Integer offset, @Nullable Integer limit) {
		return productCatalogRepository
				.findCatalogs()
				.map(List::stream)
				.map(catalogStream -> catalogStream.map(tmForumMapper::map).toList())
				.map(HttpResponse::ok);
	}

	@Override
	public Single<HttpResponse<CatalogVO>> patchCatalog(String id, CatalogUpdateVO catalog) {
		return null;
	}

	@Override
	public Single<HttpResponse<CatalogVO>> retrieveCatalog(String id, @Nullable String fields) {
		return productCatalogRepository
				.getCatalog(id)
				.map(tmForumMapper::map)
				.toSingle()
				.map(HttpResponse::ok);
	}
}
