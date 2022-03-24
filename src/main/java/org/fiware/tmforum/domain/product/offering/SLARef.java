package org.fiware.tmforum.domain.product.offering;

import lombok.EqualsAndHashCode;
import org.fiware.tmforum.domain.ngsi.MappingEnabled;
import org.fiware.tmforum.domain.product.RefEntity;

import java.net.URI;
import java.util.List;

@MappingEnabled
@EqualsAndHashCode(callSuper = true)
public class SLARef extends RefEntity {

	public SLARef(String id) {
		super(id);
	}

	@Override
	public List<String> getReferencedTypes() {
		return List.of("sla");
	}
}
