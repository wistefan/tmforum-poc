package org.fiware.tmforum.domain.party.organization;

import lombok.EqualsAndHashCode;
import org.fiware.tmforum.domain.ngsi.MappingEnabled;

import java.net.URI;

@EqualsAndHashCode(callSuper = true)
@MappingEnabled
public class OrganizationChildRelationship extends OrganizationRelationship {


	public OrganizationChildRelationship(String id) {
		super(id);
	}

}
