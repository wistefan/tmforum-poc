package org.fiware.tmforum.domain.party.individual;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fiware.tmforum.domain.Entity;
import org.fiware.tmforum.domain.party.TimePeriod;

@Data
@EqualsAndHashCode(callSuper = true)
public class Skill extends Entity {

	private String comment;
	private String evaluatedLevel;
	private String skillCode;
	private String skillName;
	private TimePeriod validFor;
}
