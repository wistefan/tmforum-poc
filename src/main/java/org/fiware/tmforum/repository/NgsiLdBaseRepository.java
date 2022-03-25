package org.fiware.tmforum.repository;

import io.micronaut.cache.annotation.CachePut;
import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.fiware.canismajor.api.EntityApiClient;
import org.fiware.canismajor.api.NgsiLdApiClient;
import org.fiware.ngsi.api.EntitiesApiClient;
import org.fiware.ngsi.model.EntityVO;
import org.fiware.tmforum.configuration.GeneralProperties;
import org.fiware.tmforum.domain.CanisMajorMapper;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Baserepository implementation for using the NGSI-LD API as a storage backend.
 */
@RequiredArgsConstructor
public abstract class NgsiLdBaseRepository {

	private static final String ENTITIES_CACHE_NAME = "entities";

	protected final GeneralProperties generalProperties;
	protected final EntitiesApiClient entitiesApi;
	protected final NgsiLdApiClient canisMajorApi;
	protected final CanisMajorMapper canisMajorMapper;
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();

	protected String getLinkHeader() {
		return String.format("<%s>; rel=\"http://www.w3.org/ns/json-ld#context\"; type=\"application/ld+json", generalProperties.getContextUrl());
	}

	@CachePut(value = ENTITIES_CACHE_NAME, keyGenerator = EntityIdKeyGenerator.class)
	public HttpResponse<Object> createEntity(EntityVO entityVO, String ngSILDTenant) {
		HttpResponse<Object> brokerResponse = entitiesApi.createEntity(entityVO, ngSILDTenant);
		if (generalProperties.isCmForwarding()) {
			executorService.submit(() -> {
				canisMajorApi.createNgsiLDEntity(null, null, null, null, canisMajorMapper.map(entityVO));
			});
		}
		return brokerResponse;
	}

	@Cacheable(ENTITIES_CACHE_NAME)
	public HttpResponse<EntityVO> retrieveEntityById(URI entityId, String ngSILDTenant, String attrs, String type, String options, String link) {
		return entitiesApi.retrieveEntityById(entityId, ngSILDTenant, attrs, type, options, link);
	}

}

