package org.fiware.tmforum.rest;

import io.micronaut.http.annotation.Controller;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fiware.tmforum.domain.product.RefEntity;
import org.fiware.tmforum.exception.NonExistentReferenceException;
import org.fiware.tmforum.repository.ReferencesRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ValidationService {

	private final ReferencesRepository referencesRepository;

	public <T extends RefEntity> Single<Boolean> checkReferenceExists(List<T> references) {

		Single<Boolean> zippedSingle = Single.zip(
				references.stream()
						.map(ref -> referencesRepository.referenceExists(ref.getId().toString(), ref.getReferencedTypes())
								// we want true in case its there
								.isEmpty().map(res -> !res))
						.toList(),
				t -> Arrays.stream(t).map(Boolean.class::cast).anyMatch(b -> b));

		return zippedSingle;
	}

	public <T extends RefEntity, R> Single<R> getCheckingSingle(List<T> refs, R defaultValue) {
		return checkReferenceExists(refs)
				.map(res -> {
					if (!res) {
						throw new NonExistentReferenceException(String.format("Invalid reference from %s was referenced", defaultValue));
					}
					return defaultValue;
				});
	}

}
