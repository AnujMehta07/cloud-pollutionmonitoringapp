package com.sap.hana.cloud.samples.pollutionmonitoring.model;

public class PlantPollutionDayData {
	private Plant plant;
	private String ozoneLevel;

	public Plant getPlant() {
		return plant;
	}
	public void setPlant(Plant ogPlant) {
		this.plant = ogPlant;
	}
	public String getCityOzoneLevel() {
		return ozoneLevel;
	}
	public void setCityOzoneLevel(String ozoneLevel) {
		this.ozoneLevel = ozoneLevel;
	}
	
}
