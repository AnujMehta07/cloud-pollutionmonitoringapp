package com.sap.hana.cloud.samples.pollutionmonitoring.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Model object representing a single {@link Plant} instance.
 * 
 * @version 0.1
 */
@Entity
@Table(name = "PLANT_POLLUTION_DATA")
@NamedQueries({ @NamedQuery(name = "Plants", query = "SELECT c FROM Plant c"),
		@NamedQuery(name = "PlantById", query = "SELECT c FROM Plant c WHERE c.id = :id") })

@XmlRootElement(name = "plantlist")
@XmlAccessorType(XmlAccessType.FIELD)
public class Plant extends BaseObject implements Serializable {
	/**
	 * The <code>serialVersionUID</code> of the {@link Plant} class.
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", length = 36, nullable = true)
	String id = null;

	@Column(name = "O3", length = 10, nullable = true)
	String o3 = null;

	@Column(name = "DATE_FIELD")
	java.sql.Date dateField = null;

	@Column(name = "location", length = 36, nullable = true)
	String location = null;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public java.sql.Date getDateField() {
		return dateField;
	}

	public void setDateField(java.sql.Date dateField) {
		this.dateField = dateField;
	}

	public String getO3() {
		return o3;
	}

	public void setO3(String o3) {
		this.o3 = o3;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Ozone level:" + o3 + " Date:" + dateField + " Location:" + location;
	}

}
