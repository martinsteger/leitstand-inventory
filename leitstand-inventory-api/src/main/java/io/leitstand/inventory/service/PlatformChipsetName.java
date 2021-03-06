package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.PlatformChipsetNameAdapter;

@JsonbTypeAdapter(PlatformChipsetNameAdapter.class)
public class PlatformChipsetName extends Scalar<String>{

	private static final long serialVersionUID = 1L;
	
	public static PlatformChipsetName platformChipsetName(String name) {
		return valueOf(name);
	}
	
	public static PlatformChipsetName valueOf(String name) {
		return fromString(name,PlatformChipsetName::new);
	}
	
	@NotNull(message="{chipset_name.required}")
	private String value;

	public PlatformChipsetName(String name) {
		this.value = name;
	}
	
	@Override
	public String getValue() {
		return value;
	}
}
