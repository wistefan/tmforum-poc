package org.fiware.tmforum.domain.product;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.fiware.tmforum.domain.Entity;
import org.fiware.tmforum.domain.EntityWithId;
import org.fiware.tmforum.domain.ngsi.AttributeGetter;
import org.fiware.tmforum.domain.ngsi.AttributeSetter;
import org.fiware.tmforum.domain.ngsi.AttributeType;
import org.fiware.tmforum.domain.ngsi.MappingEnabled;

import java.net.URL;

@MappingEnabled(entityType = BundleProductSpecification.TYPE_BUNDLE_PRODUCT_SPEC)
@EqualsAndHashCode(callSuper = true)
public class BundleProductSpecification  extends EntityWithId {

	public static final String TYPE_BUNDLE_PRODUCT_SPEC = "bundle-product-specification";

	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "href")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "href")}))
	private URL href;
	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "name")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "name")}))
	private String name;
	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "lifecycleStatus")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "lifecycleStatus")}))
	private String lifecycleStatus;

	public BundleProductSpecification(String id) {
		super(TYPE_BUNDLE_PRODUCT_SPEC, id);
	}
}
