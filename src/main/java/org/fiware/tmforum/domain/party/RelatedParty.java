package org.fiware.tmforum.domain.party;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.fiware.tmforum.domain.Entity;
import org.fiware.tmforum.domain.ngsi.AttributeGetter;
import org.fiware.tmforum.domain.ngsi.AttributeSetter;
import org.fiware.tmforum.domain.ngsi.AttributeType;
import org.fiware.tmforum.domain.ngsi.Ignore;
import org.fiware.tmforum.domain.ngsi.MappingEnabled;
import org.fiware.tmforum.domain.ngsi.RelationshipObject;
import org.fiware.tmforum.domain.party.individual.Individual;
import org.fiware.tmforum.domain.party.organization.Organization;
import org.fiware.tmforum.domain.product.RefEntity;

import java.net.URI;
import java.net.URL;
import java.util.List;

@MappingEnabled
@EqualsAndHashCode(callSuper = true)
public class RelatedParty extends RefEntity {

	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "name")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "name", targetClass = String.class)}))
	private String name;

	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "role")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "role", targetClass = String.class)}))
	private String role;

	public RelatedParty(String id) {
		super(id);
	}

	@Override
	@Ignore
	public List<String> getReferencedTypes() {
		return List.of(Organization.TYPE_ORGANIZATION, Individual.TYPE_INDIVIDUAL);
	}
}
