package org.fiware.tmforum.rest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fiware.party.api.OrganizationApi;
import org.fiware.party.model.OrganizationCreateVO;
import org.fiware.party.model.OrganizationUpdateVO;
import org.fiware.party.model.OrganizationVO;
import org.fiware.tmforum.domain.TMForumMapper;
import org.fiware.tmforum.domain.party.TaxExemptionCertificate;
import org.fiware.tmforum.domain.party.organization.Organization;
import org.fiware.tmforum.domain.party.organization.OrganizationRelationship;
import org.fiware.tmforum.repository.PartyRepository;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OrganizationApiController implements OrganizationApi {

	private final TMForumMapper tmForumMapper;
	private final PartyRepository partyRepository;
	private final ValidationService validationService;

	@Override
	public HttpResponse<OrganizationVO> createOrganization(OrganizationCreateVO organizationCreateVO) {

		OrganizationVO organizationVO = tmForumMapper.map(organizationCreateVO);
		Organization organization = tmForumMapper.map(organizationVO);
		Optional.ofNullable(organization.getOrganizationChildRelationship()).map(list -> list.stream().map(OrganizationRelationship.class::cast)
				.toList()).ifPresent(validationService::checkReferenceExists);

		Optional.ofNullable(organization.getOrganizationParentRelationship())
				.map(OrganizationRelationship.class::cast)
				.map(List::of)
				.ifPresent(validationService::checkReferenceExists);
		Optional.ofNullable(organization.getRelatedParty()).ifPresent(validationService::checkReferenceExists);

		List<TaxExemptionCertificate> taxExemptionCertificates = Optional.ofNullable(organization.getTaxExemptionCertificate()).orElseGet(List::of);
		List<TaxExemptionCertificate> updatedCerts = taxExemptionCertificates.stream().map(partyRepository::getOrCreate).toList();
		organization.setTaxExemptionCertificate(updatedCerts);

		partyRepository.createOrganization(organization);

		return HttpResponse.created(tmForumMapper.map(organization));
	}


	@Override
	public HttpResponse<Object> deleteOrganization(String id) {
		try {
			partyRepository.deleteParty(id);
			return HttpResponse.noContent();
		} catch (HttpClientResponseException e) {
			return HttpResponse.status(e.getStatus());
		}
	}

	@Override
	public HttpResponse<List<OrganizationVO>> listOrganization(@Nullable String fields, @Nullable Integer offset, @Nullable Integer limit) {
		return HttpResponse.ok(partyRepository.findOrganizations().stream().map(tmForumMapper::map).collect(Collectors.toList()));
	}

	@Override
	public HttpResponse<OrganizationVO> patchOrganization(String id, OrganizationUpdateVO organization) {
		// implement proper patch
		return null;
	}

	@Override
	public HttpResponse<OrganizationVO> retrieveOrganization(String id, @Nullable String fields) {
		return partyRepository
				.getOrganization(id)
				.map(tmForumMapper::map)
				.map(HttpResponse::ok)
				.orElseGet(HttpResponse::notFound);
	}
}
