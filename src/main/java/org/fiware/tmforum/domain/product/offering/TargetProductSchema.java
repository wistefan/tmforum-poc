package org.fiware.tmforum.domain.product.offering;

import lombok.Data;

import java.net.URI;

@Data
public class TargetProductSchema {

	private URI atSchemaLocation;
	private String atType;
}
