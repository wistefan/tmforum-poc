package org.fiware.tmforum.rest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fiware.product.api.CategoryApi;
import org.fiware.product.model.CatalogVO;
import org.fiware.product.model.CategoryCreateVO;
import org.fiware.product.model.CategoryUpdateVO;
import org.fiware.product.model.CategoryVO;
import org.fiware.tmforum.domain.TMForumMapper;
import org.fiware.tmforum.domain.product.Catalog;
import org.fiware.tmforum.domain.product.Category;
import org.fiware.tmforum.domain.product.CategoryRef;
import org.fiware.tmforum.domain.product.offering.ProductOfferingPriceRelationship;
import org.fiware.tmforum.domain.product.offering.ProductOfferingRef;
import org.fiware.tmforum.exception.NonExistentReferenceException;
import org.fiware.tmforum.repository.ProductCatalogRepository;

import javax.annotation.Nullable;
import javax.print.attribute.standard.Sides;
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
	public Single<HttpResponse<CategoryVO>> createCategory(CategoryCreateVO categoryCreateVO) {
		CategoryVO categoryVO = tmForumMapper.map(categoryCreateVO);
		Category category = tmForumMapper.map(categoryVO);

		Single<Category> categorySingle = Single.just(category);

		if (category.getSubCategory() != null && !category.getSubCategory().isEmpty()) {
			Single<Category> checkingSingle = validationService.getCheckingSingle(category.getSubCategory(), category);
			categorySingle = Single.zip(categorySingle, checkingSingle, (p1, p2) -> p1);
		}

		if (category.getParentId() != null) {
			Single<Category> checkingSingle = validationService.getCheckingSingle(List.of(category.getParentId()), category);
			categorySingle = Single.zip(categorySingle, checkingSingle, (p1, p2) -> p1);
		}

		if (category.getProductOffering() != null && !category.getProductOffering().isEmpty()) {
			Single<Category> checkingSingle = validationService.getCheckingSingle(category.getProductOffering(), category);
			categorySingle = Single.zip(categorySingle, checkingSingle, (p1, p2) -> p1);
		}

		return categorySingle.flatMap(
						cat ->
								productCatalogRepository
										.createCategory(tmForumMapper.map(categoryVO))
										.toSingleDefault(category))
				.cast(Category.class)
				.map(tmForumMapper::map)
				.map(HttpResponse::created);
	}


	@Override
	public Single<HttpResponse<Object>> deleteCategory(String id) {
		return productCatalogRepository.deleteEntry(id).toSingleDefault(HttpResponse.noContent());
	}

	@Override
	public Single<HttpResponse<List<CategoryVO>>> listCategory(@Nullable String fields, @Nullable Integer offset, @Nullable Integer limit) {
		return productCatalogRepository
				.findCategories()
				.map(List::stream)
				.map(categoryStream -> categoryStream.map(tmForumMapper::map).toList())
				.map(HttpResponse::ok);
	}

	@Override
	public Single<HttpResponse<CategoryVO>> patchCategory(String id, CategoryUpdateVO category) {
		return null;
	}

	@Override
	public Single<HttpResponse<CategoryVO>> retrieveCategory(String id, @Nullable String fields) {
		return productCatalogRepository
				.getCategory(id)
				.map(tmForumMapper::map)
				.toSingle()
				.map(HttpResponse::ok);
	}
}
