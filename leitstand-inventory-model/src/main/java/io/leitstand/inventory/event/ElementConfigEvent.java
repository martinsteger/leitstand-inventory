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
package io.leitstand.inventory.event;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import java.util.Date;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.jsonb.IsoDateAdapter;
import io.leitstand.inventory.service.ConfigurationState;
import io.leitstand.inventory.service.ElementConfigId;
import io.leitstand.inventory.service.ElementConfigName;
import io.leitstand.security.auth.UserName;

public abstract class ElementConfigEvent extends ElementEvent {

	static class ElementConfigEventBuilder<E extends ElementConfigEvent, B extends ElementConfigEventBuilder<E,B>> extends ElementEventBuilder<E, B>{
		
		ElementConfigEventBuilder(E event){
			super(event);
		}

		public B withConfigId(ElementConfigId configId) {
			assertNotInvalidated(getClass(), object);
			((ElementConfigEvent)object).configId = configId;
			return (B) this;
		}
		
		public B withConfigName(ElementConfigName configName) {
			assertNotInvalidated(getClass(), object);
			((ElementConfigEvent)object).configName = configName;
			return (B) this;
		}

		public B withConfigState(ConfigurationState configState) {
			assertNotInvalidated(getClass(), object);
			((ElementConfigEvent)object).configState = configState;
			return (B) this;
		}
		
		public B withContentType(String contentType) {
			assertNotInvalidated(getClass(), object);
			((ElementConfigEvent)object).contentType = contentType;
			return (B) this;
		}
		
		public B withCreator(UserName creator) {
			assertNotInvalidated(getClass(),object);
			((ElementConfigEvent)object).creator = creator;
			return (B) this;
		}
		
		public B withConfigDate(Date dateConfig) {
			assertNotInvalidated(getClass(), object);
			((ElementConfigEvent)object).dateConfig = new Date(dateConfig.getTime());
			return (B) this;
		}
		
	}
	
	private ElementConfigId configId;
	
	@JsonbProperty("datastore")
	private ElementConfigName configName;
	
	private ConfigurationState configState;
	private String contentType;
	@JsonbTypeAdapter(IsoDateAdapter.class)
	private Date dateConfig;
	private UserName creator;
	
	
	public ElementConfigId getConfigId() {
		return configId;
	}
	
	public ElementConfigName getConfigName() {
		return configName;
	}
	
	public ConfigurationState getConfigState() {
		return configState;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public Date getDateConfig() {
		if(dateConfig == null) {
			return null;
		}
		return new Date(dateConfig.getTime());
	}
	
	public UserName getCreator() {
		return creator;
	}
	
}
