package org.fiware.tmforum.domain.product.offering;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.fiware.tmforum.domain.EntityWithId;
import org.fiware.tmforum.domain.ngsi.AttributeGetter;
import org.fiware.tmforum.domain.ngsi.AttributeSetter;
import org.fiware.tmforum.domain.ngsi.AttributeType;
import org.fiware.tmforum.domain.ngsi.MappingEnabled;

import java.net.URL;

@MappingEnabled(entityType = BundledProductOfferingPriceRelationship.TYPE_BUNDLE_POP_RELATIONSHIP)
@EqualsAndHashCode(callSuper = true)
public class BundledProductOfferingPriceRelationship extends EntityWithId {

	public static final String TYPE_BUNDLE_POP_RELATIONSHIP = "bundle-pop-relationship";

	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "href")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "href")}))
	private URL href;

	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "name")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "name")}))
	private String name;

	public BundledProductOfferingPriceRelationship(String id) {
		super(TYPE_BUNDLE_POP_RELATIONSHIP, id);
	}
}
