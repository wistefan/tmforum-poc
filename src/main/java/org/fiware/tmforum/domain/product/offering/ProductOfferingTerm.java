package org.fiware.tmforum.domain.product.offering;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fiware.tmforum.domain.Entity;
import org.fiware.tmforum.domain.party.TimePeriod;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductOfferingTerm extends Entity {

	private String description;
	private String name;
	private Duration duration;
	private TimePeriod validFor;

}
