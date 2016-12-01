package com.sap.hana.cloud.samples.pollutionmonitoring.model;

import java.util.HashMap;
import java.util.List;

public class CompanyPollutionData {
	
	HashMap<String, List<PlantPollutionDayData>> plantsPollutionWeeklyData =new HashMap<>(); 
	
	public HashMap<String, List<PlantPollutionDayData>> getPlantsPollutionWeeklyData() {
		return plantsPollutionWeeklyData;
	}
	
}
