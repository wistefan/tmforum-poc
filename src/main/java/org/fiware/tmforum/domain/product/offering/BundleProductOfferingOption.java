package org.fiware.tmforum.domain.product.offering;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fiware.tmforum.domain.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
public class BundleProductOfferingOption extends Entity {

	private int numberRelOfferDefault;
	private int numberRelOfferLowerLimit;
	private int numberRelOfferUpperLimit;
}
