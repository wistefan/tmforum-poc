package org.fiware.tmforum.domain.party;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fiware.tmforum.domain.Entity;

import java.net.URL;

//TODO: Make sense of this.
@EqualsAndHashCode(callSuper = true)
@Data
public class AttachmentRefOrValue extends Entity {

	private String id;
	private URL href;
	private String attachmentType;
	private String content;
	private String description;
	private String mimeType;
	private URL url;
	private Quantity size;
	private TimePeriod validFor;
	private String name;
	private String atReferredType;


}
