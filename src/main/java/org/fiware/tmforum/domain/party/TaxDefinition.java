package org.fiware.tmforum.domain.party;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.fiware.tmforum.domain.EntityWithId;
import org.fiware.tmforum.domain.ngsi.AttributeGetter;
import org.fiware.tmforum.domain.ngsi.AttributeSetter;
import org.fiware.tmforum.domain.ngsi.AttributeType;
import org.fiware.tmforum.domain.ngsi.MappingEnabled;

@MappingEnabled(entityType = TaxDefinition.TYPE_TAX_DEFINITION)
@EqualsAndHashCode(callSuper = true)
public class TaxDefinition extends EntityWithId {

	public static final String TYPE_TAX_DEFINITION = "tax-definition";

	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "name")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "name")}))
	private String name;

	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "taxType")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "taxType")}))
	private String taxType;

	@Getter(onMethod = @__({@AttributeGetter(value = AttributeType.PROPERTY, targetName = "atReferredType")}))
	@Setter(onMethod = @__({@AttributeSetter(value = AttributeType.PROPERTY, targetName = "atReferredType")}))
	private String atReferredType;

	public TaxDefinition(String id) {
		super(TYPE_TAX_DEFINITION, id);
	}
}
