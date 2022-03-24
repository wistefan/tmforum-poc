package org.fiware.tmforum.rest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fiware.party.api.IndividualApi;
import org.fiware.party.model.IndividualCreateVO;
import org.fiware.party.model.IndividualUpdateVO;
import org.fiware.party.model.IndividualVO;
import org.fiware.tmforum.domain.TMForumMapper;
import org.fiware.tmforum.domain.party.RelatedParty;
import org.fiware.tmforum.domain.party.TaxExemptionCertificate;
import org.fiware.tmforum.domain.party.individual.Individual;
import org.fiware.tmforum.domain.product.ProductSpecificationCharacteristic;
import org.fiware.tmforum.exception.NonExistentReferenceException;
import org.fiware.tmforum.repository.PartyRepository;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndividualApiController implements IndividualApi {

	private final TMForumMapper tmForumMapper;
	private final PartyRepository partyRepository;
	private final ValidationService validationService;

	@Override
	public HttpResponse<IndividualVO> createIndividual(@Valid IndividualCreateVO individualCreateVO) {
		IndividualVO individualVO = tmForumMapper.map(individualCreateVO);
		Individual individual = tmForumMapper.map(individualVO);
		Optional.ofNullable(individual.getRelatedParty()).ifPresent(validationService::checkReferenceExists);

		List<TaxExemptionCertificate> taxExemptionCertificates = Optional.ofNullable(individual.getTaxExemptionCertificate()).orElseGet(List::of);
		List<TaxExemptionCertificate> updatedCerts = taxExemptionCertificates.stream().map(partyRepository::getOrCreate).toList();
		individual.setTaxExemptionCertificate(updatedCerts);
		partyRepository.createIndividual(individual);
		return HttpResponse.created(tmForumMapper.map(individual));
	}


	@Override
	public HttpResponse<Object> deleteIndividual(String id) {
		try {
			partyRepository.deleteParty(id);
			return HttpResponse.noContent();
		} catch (HttpClientResponseException e) {
			return HttpResponse.status(e.getStatus());
		}
	}

	@Override
	public HttpResponse<List<IndividualVO>> listIndividual(@Nullable String fields, @Nullable Integer offset, @Nullable Integer limit) {
		return HttpResponse.ok(partyRepository.findIndividuals().stream().map(tmForumMapper::map).collect(Collectors.toList()));

	}

	@Override
	public HttpResponse<IndividualVO> patchIndividual(String id, IndividualUpdateVO individual) {
		// implement proper patch
		return null;
	}

	@Override
	public HttpResponse<IndividualVO> retrieveIndividual(String id, @Nullable String fields) {
		return partyRepository
				.getIndividual(id)
				.map(tmForumMapper::map)
				.map(HttpResponse::ok)
				.orElseGet(HttpResponse::notFound);
	}
}

