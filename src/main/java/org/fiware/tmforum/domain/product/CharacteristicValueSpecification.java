package org.fiware.tmforum.domain.product;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fiware.tmforum.domain.Entity;
import org.fiware.tmforum.domain.party.TimePeriod;

@Data
@EqualsAndHashCode(callSuper = true)
public class CharacteristicValueSpecification extends Entity {

	private boolean isDefault;
	private String rangeInterval;
	private String regex;
	private String unitOfMeasure;
	private int valueFrom;
	private int valueTo;
	private TimePeriod validFor;
	private String valueType;
	private Object value;
}
