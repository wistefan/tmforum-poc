package org.fiware.tmforum.domain.party;

import lombok.Data;
import org.fiware.tmforum.domain.Entity;

@Data
public class Quantity {

	private Float amount;
	private String units;
}
