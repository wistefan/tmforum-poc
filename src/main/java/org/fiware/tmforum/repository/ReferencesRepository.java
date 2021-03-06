package org.fiware.tmforum.repository;

import io.micronaut.http.HttpResponse;
import io.reactivex.Maybe;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import org.fiware.ngsi.api.EntitiesApiClient;
import org.fiware.ngsi.model.EntityVO;
import org.fiware.tmforum.configuration.GeneralProperties;

import javax.inject.Singleton;
import java.net.URI;
import java.util.List;

@Slf4j
@Singleton
public class ReferencesRepository extends NgsiLdBaseRepository {

	public ReferencesRepository(GeneralProperties generalProperties, EntitiesApiClient entitiesApi) {
		// this repo is only for validating referential integrity and does not change anything, therefor canismajor is not required.
		super(generalProperties, entitiesApi, null, null);
	}

	public Maybe<EntityVO> referenceExists(String id, List<String> acceptedTypes) {
		return retrieveEntityById(URI.create(id)).filter(e -> acceptedTypes.contains(e.getType()));
	}
}
