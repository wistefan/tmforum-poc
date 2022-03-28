package org.fiware.tmforum.repository;

import io.micronaut.cache.annotation.CachePut;
import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.http.HttpResponse;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.checkerframework.checker.units.qual.C;
import org.fiware.canismajor.api.NgsiLdApiClient;
import org.fiware.ngsi.api.EntitiesApiClient;
import org.fiware.ngsi.model.EntityListVO;
import org.fiware.ngsi.model.EntityVO;
import org.fiware.tmforum.configuration.GeneralProperties;
import org.fiware.tmforum.domain.CanisMajorMapper;
import org.fiware.tmforum.domain.EntityMapper;
import org.fiware.tmforum.domain.TMForumMapper;
import org.fiware.tmforum.domain.party.TaxDefinition;
import org.fiware.tmforum.domain.party.TaxExemptionCertificate;
import org.fiware.tmforum.domain.party.individual.Individual;
import org.fiware.tmforum.domain.party.organization.Organization;
import org.fiware.tmforum.exception.NonExistentReferenceException;

import javax.inject.Singleton;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class PartyRepository extends NgsiLdBaseRepository {

	private final EntityMapper entityMapper;

	public PartyRepository(GeneralProperties generalProperties, EntitiesApiClient entitiesApi, EntityMapper entityMapper, NgsiLdApiClient canisMajorClient, CanisMajorMapper canisMajorMapper) {
		super(generalProperties, entitiesApi, canisMajorClient, canisMajorMapper);
		this.entityMapper = entityMapper;
	}

	public Completable createOrganization(Organization organization) {
		return createEntity(entityMapper.toEntityVO(organization), generalProperties.getTenant());
	}

	public Completable deleteParty(String id) {
		return entitiesApi.removeEntityById(URI.create(id), generalProperties.getTenant(), null);
	}

	public Single<List<Organization>> findOrganizations() {
		return entitiesApi.queryEntities(generalProperties.getTenant(),
						null,
						null,
						Organization.TYPE_ORGANIZATION,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						getLinkHeader())
				.map(List::stream)
				.map(entityVOStream -> entityVOStream.map(entityVO -> entityMapper.fromEntityVO(entityVO, Organization.class)).toList());

	}

	public Maybe<Organization> getOrganization(String id) {
		return retrieveEntityById(URI.create(id))
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, Organization.class));
	}

	public Completable createIndividual(Individual individual) {
		return createEntity(entityMapper.toEntityVO(individual), generalProperties.getTenant());
	}

	public Maybe<Individual> getIndividual(String id) {
		return retrieveEntityById(URI.create(id))
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, Individual.class));
	}


	public Single<List<Individual>> findIndividuals() {
		return entitiesApi.queryEntities(generalProperties.getTenant(),
						null,
						null,
						Individual.TYPE_INDIVIDUAL,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						getLinkHeader())
				.map(List::stream)
				.map(entityVOStream -> entityVOStream.map(entityVO -> entityMapper.fromEntityVO(entityVO, Individual.class)).toList());

	}

	public Completable createTaxExemptionCertificate(TaxExemptionCertificate taxExemptionCertificate) {
		return createEntity(entityMapper.toEntityVO(taxExemptionCertificate), generalProperties.getTenant());
	}

	public Maybe<TaxExemptionCertificate> getTaxExemptionCertificate(String id) {
		return retrieveEntityById(URI.create(id))
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, TaxExemptionCertificate.class));
	}


	public Single<List<TaxExemptionCertificate>> findTaxExemptionCertificates() {
		return entitiesApi.queryEntities(generalProperties.getTenant(),
						null,
						null,
						TaxExemptionCertificate.TYPE_TAX_EXEMPTION_CERTIFICATE,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						getLinkHeader())
				.map(List::stream)
				.map(entityVOStream -> entityVOStream.map(entityVO -> entityMapper.fromEntityVO(entityVO, TaxExemptionCertificate.class)).toList());

	}

	public Completable createTaxDefinition(TaxDefinition taxDefinition) {
		return createEntity(entityMapper.toEntityVO(taxDefinition), generalProperties.getTenant());
	}

	public Maybe<TaxDefinition> getTaxDefinition(String id) {
		return retrieveEntityById(URI.create(id))
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, TaxDefinition.class));
	}


	public Single<List<TaxDefinition>> findTaxDefinitions() {
		return entitiesApi.queryEntities(generalProperties.getTenant(),
						null,
						null,
						TaxDefinition.TYPE_TAX_DEFINITION,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						getLinkHeader())
				.map(List::stream)
				.map(entityVOStream -> entityVOStream.map(entityVO -> entityMapper.fromEntityVO(entityVO, TaxDefinition.class)).toList());

	}

	public Single<TaxDefinition> getOrCreate(TaxDefinition taxDefinition) {
		Optional<URI> optionalID = Optional.ofNullable(taxDefinition.getId());
		if (optionalID.isPresent()) {
			return getTaxDefinition(optionalID.get().toString())
					.toSingle();

		} else {
			URI specID = URI.create(String.format(TMForumMapper.ID_TEMPLATE, TaxDefinition.TYPE_TAX_DEFINITION, UUID.randomUUID()));
			taxDefinition.setId(specID);
			return createTaxDefinition(taxDefinition).toSingleDefault(taxDefinition);
		}
	}

	public Single<TaxExemptionCertificate> getOrCreate(TaxExemptionCertificate taxExemptionCertificate) {
		Optional<URI> optionalCertID = Optional.ofNullable(taxExemptionCertificate.getId());
		if (optionalCertID.isPresent()) {
			return getTaxExemptionCertificate(optionalCertID.get().toString())
					.toSingle();
		} else {
			URI specID = URI.create(String.format(TMForumMapper.ID_TEMPLATE, TaxExemptionCertificate.TYPE_TAX_EXEMPTION_CERTIFICATE, UUID.randomUUID()));
			taxExemptionCertificate.setId(specID);

			List<TaxDefinition> taxDefinitions = Optional.ofNullable(taxExemptionCertificate.getTaxDefinition()).orElseGet(List::of);
			Single<List<TaxDefinition>> taxDefSingle = Single.zip(taxDefinitions.stream().map(this::getOrCreate).toList(), t -> Arrays.stream(t).map(TaxDefinition.class::cast).toList());

			return taxDefSingle
					.map(updatedTaxDefinitions -> {
						taxExemptionCertificate.setTaxDefinition(updatedTaxDefinitions);
						return taxExemptionCertificate;
					})
					.flatMap(cert -> createTaxExemptionCertificate(cert).toSingleDefault(cert));
		}
	}
}
