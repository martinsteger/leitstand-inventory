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
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.ElementPhysicalInterfaceNeighbor.newPhysicalInterfaceNeighbor;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;

import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Update;
import io.leitstand.inventory.jpa.AdministrativeStateConverter;
import io.leitstand.inventory.jpa.InterfaceNameConverter;
import io.leitstand.inventory.jpa.MACAddressConverter;
import io.leitstand.inventory.jpa.OperationalStateConverter;
import io.leitstand.inventory.service.AdministrativeState;
import io.leitstand.inventory.service.Bandwidth;
import io.leitstand.inventory.service.ElementPhysicalInterfaceNeighbor;
import io.leitstand.inventory.service.InterfaceName;
import io.leitstand.inventory.service.MACAddress;
import io.leitstand.inventory.service.OperationalState;

@Entity
@Table(schema="inventory", name="element_ifp")
@IdClass(Element_InterfacePK.class)
@NamedQuery(name="Element_PhysicalInterface.findByElement", 
			query="SELECT p FROM Element_PhysicalInterface p WHERE p.element=:element")
@NamedQuery(name="Element_PhysicalInterface.removeAll", 
			query="DELETE FROM Element_PhysicalInterface p WHERE p.element=:element")
@NamedQuery(name="Element_PhysicalInterface.findByName", 
			query="SELECT p FROM Element_PhysicalInterface p WHERE p.element=:element AND p.name=:name")
@NamedQuery(name="Element_PhysicalInterface.removeNeighbors", 
			query="UPDATE Element_PhysicalInterface p SET p.neighborElement=NULL, p.neighborElementIfpName=NULL WHERE p.neighborElement=:element")
@NamedQuery(name="Element_PhysicalInterface.updateOperationalState",
			query="UPDATE Element_PhysicalInterface p SET p.opState=:state WHERE p.element=:element")
@NamedQuery(name="Element_PhysicalInterface.countLogicalInterfaces",
			query="SELECT count(ifl) FROM Element_LogicalInterface ifl WHERE ifl.element=:element AND ifl.ifc=:ifc")
@NamedQuery(name="Element_PhysicalInterface.findByLogicalInterface",
			query="SELECT ifp FROM Element_PhysicalInterface ifp WHERE ifp.element=:element AND ifp.ifc=:ifc")
public class Element_PhysicalInterface implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public static Query<Long> countLogicalInterfaces(Element_PhysicalInterface ifp){
		return em -> em.createNamedQuery("Element_PhysicalInterface.countLogicalInterfaces",Long.class)
					   .setParameter("element", ifp.getElement())
					   .setParameter("ifc", ifp.getContainerInterface())
					   .getSingleResult();
	}
	
	public static Query<List<Element_PhysicalInterface>> findIfpOfIfl(Element_LogicalInterface ifl){
		return em -> em.createNamedQuery("Element_PhysicalInterface.findByLogicalInterface",Element_PhysicalInterface.class)
					   .setParameter("element", ifl.getElement())
					   .setParameter("ifc",ifl.getContainerInterface())
					   .getResultList();
	}
	
	public static Query<List<Element_PhysicalInterface>> findIfps(Element element){
		return em -> em.createNamedQuery("Element_PhysicalInterface.findByElement",Element_PhysicalInterface.class)
					   .setParameter("element", element)
					   .getResultList();
	}
	
	public static Query<Element_PhysicalInterface> findIfpByName(Element element, InterfaceName name){
		return em -> em.createNamedQuery("Element_PhysicalInterface.findByName",Element_PhysicalInterface.class)
					   .setParameter("element", element)
					   .setParameter("name", name)
					   .getSingleResult();
	}
	
	public static Update removeIfps(Element element) {
		return em -> em.createNamedQuery("Element_PhysicalInterface.removeAll",int.class)
					   .setParameter("element", element)
					   .executeUpdate();
	}
	
	public static Update removeNeighbors(Element element) {
		return em -> em.createNamedQuery("Element_PhysicalInterface.removeNeighbors",int.class)
					   .setParameter("element", element)
					   .executeUpdate();
	}
	
	public static Update updateIfpOperationalState(Element element, OperationalState state) {
		return em -> em.createNamedQuery("Element_PhysicalInterface.updateOperationalState")
					   .setParameter("element", element)
					   .setParameter("state",state)
					   .executeUpdate();
	}

	@Id
	@ManyToOne
	@JoinColumn(name="element_id")
	private Element element;
	@Id
	@Convert(converter=InterfaceNameConverter.class)
	@Column(name="name")
	private InterfaceName name;
	
	@AttributeOverrides({
		@AttributeOverride(name="value", column=@Column(name="bwvalue")),
		@AttributeOverride(name="unit", column=@Column(name="bwunit"))
	})
	private Bandwidth bandwidth;
	
	@Convert(converter=OperationalStateConverter.class)
	@Column(name="opstate")
	private OperationalState opState;
	
	@Convert(converter=AdministrativeStateConverter.class)
	@Column(name="admstate")
	private AdministrativeState admState;
	
	@Convert(converter=MACAddressConverter.class)
	@Column(name="macaddr")
	private MACAddress mac;
	
	@Temporal(TIMESTAMP)
	private Date tsModified;
	
	@Temporal(TIMESTAMP)
	private Date tsCreated;
	
	@Column(name="alias")
	private String ifpAlias;
	
	private String category;
	
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="element_id", referencedColumnName="element_id", updatable=false, insertable=false),
		@JoinColumn(name="element_ifc_name", referencedColumnName="name")
	})
	private Element_ContainerInterface ifc;
	
	
	@ManyToOne
	@JoinColumn(name="neighbor_element_id")
	private Element neighborElement;
	
	@Convert(converter=InterfaceNameConverter.class)
	@Column(name="neighbor_element_ifp_name")
	private InterfaceName neighborElementIfpName;
	
	
	
	protected Element_PhysicalInterface(){
		//JPA
	}
	
	public Element_PhysicalInterface(Element element, 
	                                 InterfaceName name, 
	                                 Bandwidth bandwidth, 
	                                 Element_ContainerInterface ifc){
		this.element = element;
		this.name  = name;
		this.bandwidth    = bandwidth;
		this.ifc = ifc;
		long now = System.currentTimeMillis();
		this.tsCreated = new Date(now);
		this.tsModified = new Date(now);
	}
	
	@PreUpdate
	protected void updateLastModified() {
		this.tsModified = new Date();
	}
	
	
	public Element getElement() {
		return element;
	}
	
	public InterfaceName getIfpName() {
		return name;
	}
	
	public Bandwidth getBandwidth() {
		return bandwidth;
	}
	
	public void setMacAddress(MACAddress mac) {
		this.mac = mac;
	}
	
	public MACAddress getMacAddress() {
		return mac;
	}
	
	public void setOperationalState(OperationalState state) {
		this.opState = state;
	}
	
	public OperationalState getOperationalState(){
		return opState;
	}
	
	public void setAdministrativeState(AdministrativeState admState) {
		this.admState = admState;
	}
	
	public AdministrativeState getAdministrativeState() {
		return admState;
	}
	

	public void setContainerInterface(Element_ContainerInterface containerInterface) {
		this.ifc = containerInterface;
	}

	public Element_ContainerInterface getContainerInterface() {
		return ifc;
	}

	public boolean linkTo(Element element, InterfaceName ifpName) {
		if(Objects.equals(this.neighborElement, element) && Objects.equals(this.neighborElementIfpName, ifpName)) {
			return false;
		}
		this.neighborElement = element;
		this.neighborElementIfpName = ifpName;
		return true;
	}
	
	public Element getNeighborElement() {
		return neighborElement;
	}
	
	public InterfaceName getNeighborElementIfpName() {
		return neighborElementIfpName;
	}

	public void removeNeighbor() {
		this.neighborElement = null;
		this.neighborElementIfpName = null;
	}

	public ElementPhysicalInterfaceNeighbor getNeighbor() {
		if(neighborElement == null) {
			return null;
		}
		return newPhysicalInterfaceNeighbor().withElementId(neighborElement.getElementId())
							    .withElementName(neighborElement.getElementName())
							    .withInterfaceName(neighborElementIfpName)
							    .build();
	}
	
	public void setIfpAlias(String ifpAlias) {
		this.ifpAlias = ifpAlias;
	}
	
	public String getIfpAlias() {
		return ifpAlias;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getCategory() {
		return category;
	}
	
	public Date getDateModified(){
	    return new Date(tsModified.getTime());
	}

    public void setBandwidth(Bandwidth bandwidth) {
        this.bandwidth = bandwidth;
    }

}
