package org.fiware.tmforum.domain.party;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fiware.tmforum.domain.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
public class PartyCreditProfile extends Entity {

	private String creditAgencyName;
	private String creditAgencyType;
	private String ratingReference;
	private Integer ratingScore;
	private TimePeriod validFor;

}
