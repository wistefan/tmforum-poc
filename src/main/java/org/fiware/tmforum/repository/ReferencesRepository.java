package org.fiware.tmforum.repository;

import io.micronaut.http.HttpResponse;
import org.fiware.ngsi.api.EntitiesApiClient;
import org.fiware.ngsi.model.EntityVO;
import org.fiware.tmforum.configuration.GeneralProperties;

import javax.inject.Singleton;
import java.net.URI;
import java.util.List;

@Singleton
public class ReferencesRepository extends NgsiLdBaseRepository {

	public ReferencesRepository(GeneralProperties generalProperties, EntitiesApiClient entitiesApi) {
		// this repo is only for validating referential integrity and does not change anything, therefor canismajor is not required.
		super(generalProperties, entitiesApi, null, null);
	}

	public boolean referenceExists(String id, List<String> acceptedTypes) {
		HttpResponse<EntityVO> entityVOHttpResponse = entitiesApi.retrieveEntityById(URI.create(id), generalProperties.getTenant(), null, null, null, getLinkHeader());
		return entityVOHttpResponse.getBody().map(EntityVO::getType).filter(acceptedTypes::contains).isPresent();
	}
}
