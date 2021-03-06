package org.fiware.tmforum.domain.party;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fiware.tmforum.domain.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContactMedium extends Entity {

	private String mediumType;
	private boolean preferred;
	private MediumCharacteristic mediumCharacteristic;
	private TimePeriod validFor;

}
