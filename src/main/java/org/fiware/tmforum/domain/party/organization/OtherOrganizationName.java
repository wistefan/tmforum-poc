package org.fiware.tmforum.domain.party.organization;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fiware.tmforum.domain.Entity;
import org.fiware.tmforum.domain.party.TimePeriod;

@Data
@EqualsAndHashCode(callSuper = true)
public class OtherOrganizationName extends Entity {

	private String name;
	private String nameType;
	private String tradingName;
	private TimePeriod validFor;
}
