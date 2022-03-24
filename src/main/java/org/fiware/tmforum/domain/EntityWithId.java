package org.fiware.tmforum.domain;

import lombok.Getter;
import lombok.Setter;
import org.fiware.tmforum.domain.ngsi.AttributeGetter;
import org.fiware.tmforum.domain.ngsi.AttributeSetter;
import org.fiware.tmforum.domain.ngsi.AttributeType;
import org.fiware.tmforum.domain.ngsi.DatasetId;
import org.fiware.tmforum.domain.ngsi.EntityId;
import org.fiware.tmforum.domain.ngsi.EntityType;
import org.fiware.tmforum.domain.ngsi.Ignore;
import org.fiware.tmforum.domain.ngsi.RelationshipObject;

import javax.annotation.Nullable;
import java.net.URI;

public abstract class EntityWithId {

	/**
	 * Type of the entity
	 */
	@Getter(onMethod = @__({@EntityType}))
	final String type;

	/**
	 * Id of the entity. This is the id part of "urn:ngsi-ld:TYPE:ID"
	 */
	@Ignore
	@Getter(onMethod = @__({@EntityId, @RelationshipObject, @DatasetId}))
	@Setter
	URI id;

	public EntityWithId(String type, String id) {
		this.type = type;
		if (id != null) {
			this.id = URI.create(id);
		}
	}

	/**
	 * When sub-classing, this defines the super-class
	 */
	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "@baseType")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "@baseType")}))
	@Nullable
	String atBaseType;

	/**
	 * A URI to a JSON-Schema file that defines additional attributes and relationships
	 */
	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "@schemaLocation")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "@schemaLocation")}))
	@Nullable
	URI atSchemaLocation;

	/**
	 * When sub-classing, this defines the sub-class entity name.
	 * We cannot use @type, since it clashes with the ngsi-ld type field(e.g. reserved name)
	 */
	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "tmForumType")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "tmForumType")}))
	@Nullable
	String atType;


}
