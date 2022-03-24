package org.fiware.tmforum.domain.party.organization;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fiware.tmforum.domain.Entity;
import org.fiware.tmforum.domain.party.AttachmentRefOrValue;
import org.fiware.tmforum.domain.party.TimePeriod;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrganizationIdentification extends Entity {

	private String identificationId;
	private String identificationType;
	private String issuingAuthority;
	private Instant issuingDate;
	private AttachmentRefOrValue attachment;
	private TimePeriod validFor;

}
