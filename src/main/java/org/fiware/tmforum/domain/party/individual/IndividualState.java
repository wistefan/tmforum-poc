package org.fiware.tmforum.domain.party.individual;

public enum IndividualState {

	INITIALIZED("initialized"),
	VALIDATED("validated"),
	DECEADED("deceaded");

	private final String value;

	IndividualState(String value) {
		this.value = value;
	}
}
