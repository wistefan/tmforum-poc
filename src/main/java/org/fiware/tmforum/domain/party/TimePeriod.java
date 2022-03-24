package org.fiware.tmforum.domain.party;

import lombok.Data;
import org.fiware.tmforum.domain.Entity;

import java.time.Instant;

@Data
public class TimePeriod {

	private Instant endDateTime;
	private Instant startDateTime;
}
