package org.fiware.tmforum.repository;

import io.micronaut.cache.annotation.CachePut;
import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.http.HttpResponse;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.fiware.canismajor.api.EntityApiClient;
import org.fiware.canismajor.api.NgsiLdApiClient;
import org.fiware.ngsi.api.EntitiesApi;
import org.fiware.ngsi.api.EntitiesApiClient;
import org.fiware.ngsi.model.EntityVO;
import org.fiware.tmforum.configuration.GeneralProperties;
import org.fiware.tmforum.domain.CanisMajorMapper;

import java.net.URI;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Baserepository implementation for using the NGSI-LD API as a storage backend.
 */
@RequiredArgsConstructor
public abstract class NgsiLdBaseRepository {

	private static final String ENTITIES_CACHE_NAME = "entities";

	protected final GeneralProperties generalProperties;
	protected final EntitiesApi entitiesApi;
	protected final NgsiLdApiClient canisMajorApi;
	protected final CanisMajorMapper canisMajorMapper;

	protected String getLinkHeader() {
		return String.format("<%s>; rel=\"http://www.w3.org/ns/json-ld#context\"; type=\"application/ld+json", generalProperties.getContextUrl());
	}

	@CachePut(value = ENTITIES_CACHE_NAME, keyGenerator = EntityIdKeyGenerator.class)
	public Completable createEntity(EntityVO entityVO, String ngSILDTenant) {
		if (generalProperties.isCmForwarding()) {
			// we ignore the result for now
			canisMajorApi.createNgsiLDEntity(null, null, null, null, new org.fiware.canismajor.model.EntityVO());
		}
		return entitiesApi.createEntity(entityVO, ngSILDTenant);
	}

	@Cacheable(ENTITIES_CACHE_NAME)
	public Maybe<EntityVO> retrieveEntityById(URI entityId) {
		return asyncRetrieveEntityById(entityId, generalProperties.getTenant(), null, null, null, getLinkHeader());
	}

	private Maybe<EntityVO> asyncRetrieveEntityById(URI entityId, String ngSILDTenant, String attrs, String type, String options, String link) {
		return entitiesApi.retrieveEntityById(entityId, ngSILDTenant, attrs, type, options, link);
	}

}

