package org.fiware.tmforum.domain.party;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fiware.tmforum.domain.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
public class Characteristic extends Entity {

	private String name;
	private String valueType;
	private Object value;

}
