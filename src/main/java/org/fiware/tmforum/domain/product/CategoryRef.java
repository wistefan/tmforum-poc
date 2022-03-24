package org.fiware.tmforum.domain.product;

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

import java.net.URI;
import java.net.URL;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@MappingEnabled
public class CategoryRef extends RefEntity {

	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "version")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "version")}))
	private String version;


	public CategoryRef(String id) {
		super(id);
	}

	@Override
	public List<String> getReferencedTypes() {
		return List.of(Category.TYPE_CATEGORY);
	}

}
