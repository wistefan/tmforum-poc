package org.fiware.tmforum.repository;

import io.micronaut.http.HttpResponse;
import org.fiware.ngsi.api.EntitiesApiClient;
import org.fiware.ngsi.model.EntityListVO;
import org.fiware.ngsi.model.EntityVO;
import org.fiware.tmforum.configuration.GeneralProperties;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class PartyRepository extends NgsiLdBaseRepository {

	private static final List<String> PARTY_TYPES = List.of(Organization.TYPE_ORGANIZATION, Individual.TYPE_INDIVIDUAL);

	private final EntityMapper entityMapper;

	public PartyRepository(GeneralProperties generalProperties, EntitiesApiClient entitiesApi, EntityMapper entityMapper) {
		super(generalProperties, entitiesApi);
		this.entityMapper = entityMapper;
	}

	public void createOrganization(Organization organization) {
		entitiesApi.createEntity(entityMapper.toEntityVO(organization), generalProperties.getTenant());
	}


	public boolean partyExists(String id) {
		HttpResponse<EntityVO> entityVOHttpResponse = entitiesApi.retrieveEntityById(URI.create(id), generalProperties.getTenant(), null, null, null, getLinkHeader());
		return entityVOHttpResponse.getBody().map(EntityVO::getType).filter(PARTY_TYPES::contains).isPresent();
	}

	public void deleteParty(String id) {
		entitiesApi.removeEntityById(URI.create(id), generalProperties.getTenant(), null);
	}

	public List<Organization> findOrganizations() {
		HttpResponse<EntityListVO> entityListVOHttpResponse = entitiesApi.queryEntities(generalProperties.getTenant(),
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
				getLinkHeader());

		return entityListVOHttpResponse.getBody()
				.map(ArrayList::new)
				.orElseGet(ArrayList::new)
				.stream()
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, Organization.class))
				.collect(Collectors.toList());

	}

	public Optional<Organization> getOrganization(String id) {
		HttpResponse<EntityVO> entityVOHttpResponse = entitiesApi.retrieveEntityById(URI.create(id), generalProperties.getTenant(), null, null, null, getLinkHeader());
		return entityVOHttpResponse.getBody().map(entityVO -> entityMapper.fromEntityVO(entityVO, Organization.class));
	}

	public void createIndividual(Individual individual) {
		entitiesApi.createEntity(entityMapper.toEntityVO(individual), generalProperties.getTenant());
	}

	public Optional<Individual> getIndividual(String id) {
		HttpResponse<EntityVO> entityVOHttpResponse = entitiesApi.retrieveEntityById(URI.create(id), generalProperties.getTenant(), null, null, null, getLinkHeader());
		return entityVOHttpResponse.getBody().map(entityVO -> entityMapper.fromEntityVO(entityVO, Individual.class));
	}


	public List<Individual> findIndividuals() {
		HttpResponse<EntityListVO> entityListVOHttpResponse = entitiesApi.queryEntities(generalProperties.getTenant(),
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
				getLinkHeader());

		return entityListVOHttpResponse.getBody()
				.map(ArrayList::new)
				.orElseGet(ArrayList::new)
				.stream()
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, Individual.class))
				.collect(Collectors.toList());

	}

	public void createTaxExemptionCertificate(TaxExemptionCertificate taxExemptionCertificate) {
		entitiesApi.createEntity(entityMapper.toEntityVO(taxExemptionCertificate), generalProperties.getTenant());
	}

	public Optional<TaxExemptionCertificate> getTaxExemptionCertificate(String id) {
		HttpResponse<EntityVO> entityVOHttpResponse = entitiesApi.retrieveEntityById(URI.create(id), generalProperties.getTenant(), null, null, null, getLinkHeader());
		return entityVOHttpResponse.getBody().map(entityVO -> entityMapper.fromEntityVO(entityVO, TaxExemptionCertificate.class));
	}


	public List<TaxExemptionCertificate> findTaxExemptionCertificates() {
		HttpResponse<EntityListVO> entityListVOHttpResponse = entitiesApi.queryEntities(generalProperties.getTenant(),
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
				getLinkHeader());

		return entityListVOHttpResponse.getBody()
				.map(ArrayList::new)
				.orElseGet(ArrayList::new)
				.stream()
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, TaxExemptionCertificate.class))
				.collect(Collectors.toList());

	}

	public void createTaxDefinition(TaxDefinition taxDefinition) {
		entitiesApi.createEntity(entityMapper.toEntityVO(taxDefinition), generalProperties.getTenant());
	}

	public Optional<TaxDefinition> getTaxDefinition(String id) {
		HttpResponse<EntityVO> entityVOHttpResponse = entitiesApi.retrieveEntityById(URI.create(id), generalProperties.getTenant(), null, null, null, getLinkHeader());
		return entityVOHttpResponse.getBody().map(entityVO -> entityMapper.fromEntityVO(entityVO, TaxDefinition.class));
	}


	public List<TaxDefinition> findTaxDefinitions() {
		HttpResponse<EntityListVO> entityListVOHttpResponse = entitiesApi.queryEntities(generalProperties.getTenant(),
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
				getLinkHeader());

		return entityListVOHttpResponse.getBody()
				.map(ArrayList::new)
				.orElseGet(ArrayList::new)
				.stream()
				.map(entityVO -> entityMapper.fromEntityVO(entityVO, TaxDefinition.class))
				.collect(Collectors.toList());

	}

	public TaxDefinition getOrCreate(TaxDefinition taxDefinition) {
		Optional<URI> optionalID = Optional.ofNullable(taxDefinition.getId());
		if (optionalID.isPresent()) {
			return getTaxDefinition(optionalID.get().toString())
					.orElseThrow(() ->
							new NonExistentReferenceException(String.format("The referenced tax definition with id %s does not exists.",
									optionalID.get()),
									optionalID.get().toString()));
		} else {
			URI specID = URI.create(String.format(TMForumMapper.ID_TEMPLATE, TaxDefinition.TYPE_TAX_DEFINITION, UUID.randomUUID()));
			taxDefinition.setId(specID);
			createTaxDefinition(taxDefinition);
			return taxDefinition;
		}
	}

	public TaxExemptionCertificate getOrCreate(TaxExemptionCertificate taxExemptionCertificate) {
		Optional<URI> optionalCertID = Optional.ofNullable(taxExemptionCertificate.getId());
		if (optionalCertID.isPresent()) {
			return getTaxExemptionCertificate(optionalCertID.get().toString())
					.orElseThrow(() ->
							new NonExistentReferenceException(String.format("The referenced tax exemption cert with id %s does not exists.",
									optionalCertID.get()),
									optionalCertID.get().toString()));
		} else {
			URI specID = URI.create(String.format(TMForumMapper.ID_TEMPLATE, TaxExemptionCertificate.TYPE_TAX_EXEMPTION_CERTIFICATE, UUID.randomUUID()));
			taxExemptionCertificate.setId(specID);

			List<TaxDefinition> taxDefinitions = Optional.ofNullable(taxExemptionCertificate.getTaxDefinition()).orElseGet(List::of);
			List<TaxDefinition> updatedTaxDefinitions = taxDefinitions.stream().map(this::getOrCreate).toList();
			taxExemptionCertificate.setTaxDefinition(updatedTaxDefinitions);

			createTaxExemptionCertificate(taxExemptionCertificate);
			return taxExemptionCertificate;
		}
	}

}
