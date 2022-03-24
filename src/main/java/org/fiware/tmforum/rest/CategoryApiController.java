package org.fiware.tmforum.rest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fiware.product.api.CategoryApi;
import org.fiware.product.model.CatalogVO;
import org.fiware.product.model.CategoryCreateVO;
import org.fiware.product.model.CategoryUpdateVO;
import org.fiware.product.model.CategoryVO;
import org.fiware.tmforum.domain.TMForumMapper;
import org.fiware.tmforum.domain.product.Category;
import org.fiware.tmforum.domain.product.CategoryRef;
import org.fiware.tmforum.domain.product.offering.ProductOfferingPriceRelationship;
import org.fiware.tmforum.domain.product.offering.ProductOfferingRef;
import org.fiware.tmforum.exception.NonExistentReferenceException;
import org.fiware.tmforum.repository.ProductCatalogRepository;

import javax.annotation.Nullable;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CategoryApiController implements CategoryApi {

	private final TMForumMapper tmForumMapper;
	private final ProductCatalogRepository productCatalogRepository;
	private final ValidationService validationService;

	@Override
	public HttpResponse<CategoryVO> createCategory(CategoryCreateVO categoryCreateVO) {
		CategoryVO categoryVO = tmForumMapper.map(categoryCreateVO);
		Category category = tmForumMapper.map(categoryVO);
		Optional.ofNullable(category.getSubCategory()).ifPresent(validationService::checkReferenceExists);
		Optional.ofNullable(category.getParentId()).map(List::of).ifPresent(validationService::checkReferenceExists);
		Optional.ofNullable(category.getProductOffering()).ifPresent(validationService::checkReferenceExists);

		productCatalogRepository.createCategory(tmForumMapper.map(categoryVO));
		return HttpResponse.created(categoryVO);
	}


	@Override
	public HttpResponse<Object> deleteCategory(String id) {
		try {
			productCatalogRepository.deleteEntry(id);
			return HttpResponse.noContent();
		} catch (HttpClientResponseException e) {
			return HttpResponse.status(e.getStatus());
		}
	}

	@Override
	public HttpResponse<List<CategoryVO>> listCategory(@Nullable String fields, @Nullable Integer offset, @Nullable Integer limit) {
		return HttpResponse.ok(productCatalogRepository.findCategories().stream().map(tmForumMapper::map).collect(Collectors.toList()));
	}

	@Override
	public HttpResponse<CategoryVO> patchCategory(String id, CategoryUpdateVO category) {
		return null;
	}

	@Override
	public HttpResponse<CategoryVO> retrieveCategory(String id, @Nullable String fields) {
		return productCatalogRepository
				.getCategory(id)
				.map(tmForumMapper::map)
				.map(HttpResponse::ok)
				.orElseGet(HttpResponse::notFound);
	}
}
