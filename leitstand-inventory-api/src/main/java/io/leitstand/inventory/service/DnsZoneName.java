/*
 * Copyright 2020 RtBrick Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.Patterns.DNS_PATTERN;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.DnsZoneNameAdapter;

@JsonbTypeAdapter(DnsZoneNameAdapter.class)
public class DnsZoneName extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	public static final DnsZoneName dnsZoneName(String name) {
		return valueOf(name);
	}
	
	public static final DnsZoneName valueOf(String name) {
		return Scalar.fromString(name, DnsZoneName::new);
	}
	
	@NotNull(message="{dns_zone_name.required}")
	@Pattern(regexp=DNS_PATTERN, message="{dns_zone_name.invalid}")
	private String value;
	
	public DnsZoneName(String name) {
		this.value = name;
	}
	
	@Override
	public String getValue() {
		return value;
	}

	
	
}
