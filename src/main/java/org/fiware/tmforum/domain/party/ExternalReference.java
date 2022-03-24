package org.fiware.tmforum.domain.party;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fiware.tmforum.domain.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExternalReference extends Entity {

	private String externalReferenceType;
	private String name;
}
