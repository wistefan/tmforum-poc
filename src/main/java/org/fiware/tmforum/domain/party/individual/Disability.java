package org.fiware.tmforum.domain.party.individual;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fiware.tmforum.domain.Entity;
import org.fiware.tmforum.domain.party.TimePeriod;

@Data
@EqualsAndHashCode(callSuper = true)
public class Disability extends Entity {

	private String disabilityCode;
	private String disabilityName;
	private TimePeriod validFor;
}
