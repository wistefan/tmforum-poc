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

@MappingEnabled(entityType = BundleProductOffering.TYPE_BUNDLE_PRODUCT_OFFERING)
@EqualsAndHashCode(callSuper = true)
public class BundleProductOffering extends EntityWithId {

	public static final String TYPE_BUNDLE_PRODUCT_OFFERING = "bundle-product-offering";

	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "href")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "href")}))
	private URL href;

	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "lifecycleStatus")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "lifecycleStatus")}))
	private String lifecycleStatus;

	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "name")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "name")}))
	private String name;

	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "bundledProductOfferingOption")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "bundledProductOfferingOption")}))
	private BundleProductOfferingOption bundledProductOfferingOption;

	public BundleProductOffering(String id) {
		super(TYPE_BUNDLE_PRODUCT_OFFERING, id);
	}
}
