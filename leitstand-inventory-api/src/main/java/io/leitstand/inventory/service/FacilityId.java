package io.leitstand.inventory.service;

import static io.leitstand.commons.model.Patterns.UUID_PATTERN;

import java.util.UUID;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Patterns;
import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.FacilityIdAdapter;

/**
 * Unique facility identifier in UUIDv4 format.
 */
@JsonbTypeAdapter(FacilityIdAdapter.class)
public class FacilityId extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a random facility ID
	 * @return a random facility ID
	 */
	public static FacilityId randomFacilityId() {
		return valueOf(UUID.randomUUID().toString());
	}
	
	/**
	 * Creates a facility ID from the specified string.
	 * <p>
	 * Alias method for {@link #valueOf(String)} to improve readability.
	 * @param id the facility ID
	 * @return the facility ID
	 */
	public static FacilityId facilityId(String id) {
		return valueOf(id);
	}
	
	/**
	 * Creates a facility ID from the specified string.
	 * @param id the facility ID
	 * @return the facility ID
	 */
	public static FacilityId valueOf(String id) {
		return fromString(id,FacilityId::new);
	}
	
	@Pattern(regexp=UUID_PATTERN, message="{facility_id.invalid}")
	private String value;
	
	/**
	 * Creates a new facility ID.
	 * @param id the facility ID
	 */
	public FacilityId(String id) {
		this.value = id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}

}
