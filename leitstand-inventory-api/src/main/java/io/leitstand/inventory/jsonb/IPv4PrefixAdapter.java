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
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.IPv4Prefix;

public class IPv4PrefixAdapter implements JsonbAdapter<IPv4Prefix,String> {

	@Override
	public IPv4Prefix adaptFromJson(String v) throws Exception {
		return IPv4Prefix.valueOf(v);
	}

	@Override
	public String adaptToJson(IPv4Prefix v) throws Exception {
		return IPv4Prefix.toString(v);
	}

}
