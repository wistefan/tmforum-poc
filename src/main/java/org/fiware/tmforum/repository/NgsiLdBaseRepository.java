package org.fiware.tmforum.repository;

import lombok.RequiredArgsConstructor;
import org.fiware.ngsi.api.EntitiesApiClient;
import org.fiware.tmforum.configuration.GeneralProperties;

/**
 * Baserepository implementation for using the NGSI-LD API as a storage backend.
 */
@RequiredArgsConstructor
public abstract class NgsiLdBaseRepository {

	protected final GeneralProperties generalProperties;
	protected final EntitiesApiClient entitiesApi;

	protected String getLinkHeader() {
		return String.format("<%s>; rel=\"http://www.w3.org/ns/json-ld#context\"; type=\"application/ld+json", generalProperties.getContextUrl());
	}
}
