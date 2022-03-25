package org.fiware.tmforum.domain;

import org.fiware.canismajor.model.EntityVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jsr330")
public interface CanisMajorMapper {

	EntityVO map(org.fiware.ngsi.model.EntityVO entityVO);

}
