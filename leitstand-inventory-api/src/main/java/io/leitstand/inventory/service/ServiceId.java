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

import static io.leitstand.commons.model.Patterns.UUID_PATTERN;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.ServiceIdAdapter;

@JsonbTypeAdapter(ServiceIdAdapter.class)
public class ServiceId extends Scalar<String>{

	private static final long serialVersionUID = 1L;
	
	public static ServiceId serviceId(String name) {
		return valueOf(name);
	}
	
	public static ServiceId valueOf(String name) {
		return fromString(name,ServiceId::new);
	}

	@NotNull(message="{service_id.required}")
	@Pattern(regexp=UUID_PATTERN ,message="{service_id.invalid}" )
	private String value;
	
	public ServiceId(String name){
		this.value = name;
	}
	
	@Override
	public String getValue() {
		return value;
	}




}
