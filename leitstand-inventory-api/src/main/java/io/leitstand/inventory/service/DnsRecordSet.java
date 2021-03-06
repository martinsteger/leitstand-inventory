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

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static io.leitstand.inventory.service.DnsRecordSetId.randomDnsRecordSetId;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import javax.json.bind.annotation.JsonbProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.model.ValueObject;

public class DnsRecordSet extends ValueObject {
	
	public static Builder newDnsRecordSet() {
		return new Builder();
	}
	
	@SuppressWarnings("unchecked")
	public static class BaseDnsRecordSetBuilder<T extends DnsRecordSet,B extends BaseDnsRecordSetBuilder<T,B>> {
		
		protected T set;
		
		protected BaseDnsRecordSetBuilder(T set) {
			this.set = set;
		}
		
		public B withDnsZoneId(DnsZoneId zoneId) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).dnsZoneId = zoneId;
			return (B) this;
		}

		public B withDnsZoneName(DnsZoneName zoneName) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).dnsZoneName = zoneName;
			return (B) this;
		}
		
		public B withDnsRecordSetId(DnsRecordSetId id) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).dnsRecordSetId = id;
			return (B)this;
		}
		
		public B withDnsName(DnsName name) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).dnsName = name;
			return (B)this;
		}
		
		public B withDnsRecordType(DnsRecordType type) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).dnsType = type;
			return (B) this;
		}
		
		public B withDnsRecordTimeToLive(int ttl) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).dnsTtl = ttl;
			return (B) this;
		}
		
		public B withDnsRecords(DnsRecord.Builder... records) {
			return withDnsRecords(stream(records)
								  .map(DnsRecord.Builder::build)
								  .collect(toList()));
		}
		
		public B withDnsRecords(List<DnsRecord> records) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).dnsRecords = new ArrayList<>(records);
			return (B)this;
		}
		
		public B withDescription(String description) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).description = description;
			return (B)this;
		}
		
		public T build() {
			try {
				assertNotInvalidated(getClass(), set);
				return set;
			} finally {
				this.set = null;
			}
		}
		
	}
	
	public static class Builder extends BaseDnsRecordSetBuilder<DnsRecordSet,Builder>{
		protected Builder() {
			super(new DnsRecordSet());
		}
	}
	
	@NotNull(message="{dns_zone_id.required}")
	@Valid
	private DnsZoneId dnsZoneId;
	@NotNull(message="{dns_zone_name.required}")
	@Valid
	private DnsZoneName dnsZoneName;
	
	@JsonbProperty("dns_recordset_id")
	@NotNull(message="{dns_recorset_id.required}")
	@Valid
	private DnsRecordSetId dnsRecordSetId = randomDnsRecordSetId();

	@NotNull(message="{dns_type.required}")
	@Valid
	private DnsRecordType dnsType;
	
	@NotNull(message="{dns_name.required}")
	@Valid
	private DnsName dnsName;
	private int dnsTtl = 3600;
	@Valid
	private List<DnsRecord> dnsRecords = emptyList();
	private String description;
	
	
	public DnsRecordSetId getDnsRecordSetId() {
		return dnsRecordSetId;
	}
	
	public DnsName getDnsName() {
		return dnsName;
	}
	
	public List<DnsRecord> getDnsRecords() {
		return dnsRecords;
	}
	
	public String getDescription() {
		return description;
	}
	
	public DnsZoneId getDnsZoneId() {
		return dnsZoneId;
	}
	
	public DnsZoneName getDnsZoneName() {
		return dnsZoneName;
	}
	
	public int getDnsTtl() {
		return dnsTtl;
	}
	
	public DnsRecordType getDnsType() {
		return dnsType;
	}
	

}
