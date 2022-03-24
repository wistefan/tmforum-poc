package org.fiware.tmforum.domain.party.organization;

import lombok.Getter;
import lombok.Setter;
import org.fiware.tmforum.domain.Entity;
import org.fiware.tmforum.domain.ngsi.AttributeGetter;
import org.fiware.tmforum.domain.ngsi.AttributeSetter;
import org.fiware.tmforum.domain.ngsi.AttributeType;
import org.fiware.tmforum.domain.ngsi.RelationshipObject;
import org.fiware.tmforum.domain.product.RefEntity;

import java.net.URI;
import java.util.List;

public abstract class OrganizationRelationship extends RefEntity {


	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "relationshipType")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "relationshipType", targetClass = String.class)}))
	private String relationshipType;

	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "name")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "name", targetClass = String.class)}))
	private String name;

	public OrganizationRelationship(String id) {
		super(id);
	}

	@Override
	public List<String> getReferencedTypes() {
		return List.of(Organization.TYPE_ORGANIZATION);
	}
}


