package org.fiware.tmforum.domain.party.individual;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fiware.tmforum.domain.Entity;
import org.fiware.tmforum.domain.party.TimePeriod;

@Data
@EqualsAndHashCode(callSuper = true)
public class OtherIndividualName extends Entity {

	private String aristocraticTitle;
	private String familyName;
	private String familyNamePrefix;
	private String formattedName;
	private String fullName;
	private String generation;
	private String givenName;
	private String legalName;
	private String middleName;
	private String preferredGivenName;
	private String title;
	private TimePeriod validFor;

}
