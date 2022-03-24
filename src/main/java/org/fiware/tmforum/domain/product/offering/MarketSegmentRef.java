package org.fiware.tmforum.domain.product.offering;

import lombok.EqualsAndHashCode;
import org.fiware.tmforum.domain.ngsi.MappingEnabled;
import org.fiware.tmforum.domain.product.RefEntity;

import java.net.URI;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@MappingEnabled
public class MarketSegmentRef extends RefEntity {

	public MarketSegmentRef(String id) {
		super(id);
	}

	@Override
	public List<String> getReferencedTypes() {
		return List.of("market-segment");
	}
}
