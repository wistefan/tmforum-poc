package org.fiware.tmforum.rest;

import io.micronaut.http.annotation.Controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fiware.tmforum.domain.product.RefEntity;
import org.fiware.tmforum.exception.NonExistentReferenceException;
import org.fiware.tmforum.repository.ReferencesRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ValidationService {

	private final ReferencesRepository referencesRepository;

	public <T extends RefEntity> void checkReferenceExists(List<T> references) {
		Optional<T> nonExistentReference = references.stream()
				.filter(refEntity -> !referencesRepository.referenceExists(refEntity.getId().toString(), refEntity.getReferencedTypes()))
				.findFirst();
		if (nonExistentReference.isPresent()) {
			throw new NonExistentReferenceException(String.format("Referenced %s with id %s does not exist.", nonExistentReference.get().getReferencedTypes(), nonExistentReference.get().getId()), nonExistentReference.get().getId().toString());
		}
	}

}
