package org.fiware.tmforum.domain.party.individual;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fiware.tmforum.domain.Entity;
import org.fiware.tmforum.domain.party.TimePeriod;

@Data
@EqualsAndHashCode(callSuper = true)
public class LanguageAbility extends Entity {

	private boolean isFavouriteLanguage;
	private String languageCode;
	private String languageName;
	private String listeningProficiency;
	private String readingProficiency;
	private String speakingProficiency;
	private String writingProficiency;
	private TimePeriod validFor;
}
