package org.fiware.tmforum.domain.product;

import lombok.Getter;
import lombok.Setter;
import org.fiware.tmforum.domain.Entity;
import org.fiware.tmforum.domain.ngsi.AttributeGetter;
import org.fiware.tmforum.domain.ngsi.AttributeSetter;
import org.fiware.tmforum.domain.ngsi.AttributeType;
import org.fiware.tmforum.domain.ngsi.DatasetId;
import org.fiware.tmforum.domain.ngsi.RelationshipObject;

import java.net.URI;
import java.util.List;

public abstract class RefEntity extends Entity {

	@Getter(onMethod = @__({@RelationshipObject, @DatasetId}))
	final URI id;

	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "href")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "href")}))
	private URI href;

	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "name")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "name")}))
	private String name;

	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "@referredType")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "@referredType")}))
	private String atReferredType;

	public RefEntity(String id) {
		this.id = URI.create(id);
	}

	public RefEntity(URI id) {
		this.id = id;
	}

	public abstract List<String> getReferencedTypes();
}
