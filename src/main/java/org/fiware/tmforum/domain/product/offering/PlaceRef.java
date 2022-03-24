package org.fiware.tmforum.domain.product.offering;

import lombok.EqualsAndHashCode;
import org.fiware.tmforum.domain.ngsi.MappingEnabled;
import org.fiware.tmforum.domain.product.RefEntity;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@MappingEnabled
public class PlaceRef extends RefEntity {

	public PlaceRef(String id) {
		super(id);
	}

	@Override
	public List<String> getReferencedTypes() {
		return List.of("place");
	}

}
