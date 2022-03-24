package org.fiware.tmforum.domain.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.fiware.tmforum.domain.ngsi.AttributeGetter;
import org.fiware.tmforum.domain.ngsi.AttributeSetter;
import org.fiware.tmforum.domain.ngsi.AttributeType;
import org.fiware.tmforum.domain.ngsi.MappingEnabled;
import org.fiware.tmforum.domain.party.TimePeriod;

import java.net.URI;
import java.util.List;

@MappingEnabled
@EqualsAndHashCode(callSuper = true)
public class ProductSpecificationRelationship extends RefEntity {

	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "relationshipType")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "relationshipType")}))
	private String relationshipType;

	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "validFor")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "validFor")}))
	private TimePeriod validFor;

	public ProductSpecificationRelationship(String id) {
		super(id);
	}

	@Override
	public List<String> getReferencedTypes() {
		return List.of(ProductSpecification.TYPE_PRODUCT_SPECIFICATION);
	}

}
