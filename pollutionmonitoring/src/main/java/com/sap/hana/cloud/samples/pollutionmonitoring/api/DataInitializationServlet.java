package com.sap.hana.cloud.samples.pollutionmonitoring.api;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;

import com.sap.cloud.account.Tenant;
import com.sap.cloud.account.TenantContext;
import com.sap.hana.cloud.samples.pollutionmonitoring.model.Plant;

public class DataInitializationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private String COMPANY_NAME="company";
	private String ABC_PETRO_CORP="ABC PetroCorp";
	private String XYZ_ENERGY_CORP="XYZ EnergyCorp";
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	
		
		String parameter = request.getParameter(COMPANY_NAME);
		List<Plant> plantList = null;
		if(parameter.equalsIgnoreCase(ABC_PETRO_CORP))
		{
		 fillPlantData("101","0.018","SAINT JOHN WEST","2016-10-09");
		 fillPlantData("101","0.021","SAINT JOHN WEST","2016-10-10");
		 fillPlantData("101","0.023","SAINT JOHN WEST","2016-10-11");
		 fillPlantData("101","0.024","SAINT JOHN WEST","2016-10-12");
		 fillPlantData("101","0.027","SAINT JOHN WEST","2016-10-13");
		 
		 fillPlantData("102","0.019","ALGOMA","2016-10-09");
		 fillPlantData("102","0.022","ALGOMA","2016-10-10");
		 fillPlantData("102","0.025","ALGOMA","2016-10-11");
		 fillPlantData("102","0.026","ALGOMA","2016-10-12");
		 fillPlantData("102","0.027","ALGOMA","2016-10-13");
		 
		 fillPlantData("103","0.021","Burnaby South","2016-10-09");
		 fillPlantData("103","0.022","Burnaby South","2016-10-10");
		 fillPlantData("103","0.023","Burnaby South","2016-10-11");
		 fillPlantData("103","0.025","Burnaby South","2016-10-12");
		 plantList = fillPlantData("103","0.028","Burnaby South","2016-10-13");
		}
		else if(parameter.equalsIgnoreCase(XYZ_ENERGY_CORP)){
			 
			 fillPlantData("201","0.027","Hamshire C64","2016-10-09");
			 fillPlantData("201","0.023","Hamshire C64","2016-10-10");
			 fillPlantData("201","0.021","Hamshire C64","2016-10-11");
			 fillPlantData("201","0.025","Hamshire C64","2016-10-12");
			 fillPlantData("201","0.019","Hamshire C64","2016-10-13");
			 
			 fillPlantData("202","0.021","Garyville","2016-10-09");
			 fillPlantData("202","0.025","Garyville","2016-10-10");
			 fillPlantData("202","0.022","Garyville","2016-10-11");
			 fillPlantData("202","0.021","Garyville","2016-10-12");
			 fillPlantData("202","0.023","Garyville","2016-10-13");
			 
			 fillPlantData("203","0.023","Arlington Municipal","2016-10-09");
			 fillPlantData("203","0.021","Arlington Municipal","2016-10-10");
			 fillPlantData("203","0.019","Arlington Municipal","2016-10-11");
			 fillPlantData("203","0.027","Arlington Municipal","2016-10-12");
			 plantList = fillPlantData("203","0.028","Arlington Municipal","2016-10-13");
			 
		}
		response.getWriter().println(plantList);

	}

	
	private List<Plant> fillPlantData(String id, String o3,String location,String dateString ) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		java.sql.Date date = null;
		Date parsed;
		try {
			parsed = format.parse(dateString);
			date = new java.sql.Date(parsed.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Plant plant = new Plant();
		plant.setId(id);
		plant.setDateField(date);
		plant.setO3(o3);
		plant.setLocation(location);
		String tenantId = getTenantId();
		Map<String, String> props = new HashMap<String, String>();
		props.put("elipselink.tenant.id", tenantId);
		EntityManager em = this.getEntityManagerFactory().createEntityManager(props);
		em.getTransaction().begin();
		em.persist(plant);
		em.getTransaction().commit();
		List<Plant> retVal =em.createNamedQuery("Plants").getResultList();
		return retVal;
	}
	private EntityManagerFactory getEntityManagerFactory() {
		EntityManagerFactory retVal = null;
		try {
			Map<String, DataSource> properties = new HashMap<String, DataSource>();
			DataSource ds = this.getDataSource();
			properties.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, ds);
			retVal = Persistence.createEntityManagerFactory("pollutionmonitoring", properties);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return retVal;
	}

	private DataSource getDataSource() {
		DataSource retVal = null;

		try {
			InitialContext ctx = new InitialContext();
			retVal = (DataSource) ctx.lookup("java:comp/env/jdbc/DefaultDB");
		} catch (NamingException ex) {
			ex.printStackTrace();
		}
		return retVal;
	}

	private String getTenantId() {
		TenantContext tenantContext = getTenantContext();
		String tenantId = tenantContext.getTenantId().trim();
		return tenantId;
	}

	protected TenantContext getTenantContext() {
		TenantContext tenantContext = null;
		try {
			InitialContext ctx = new InitialContext();
			tenantContext = (TenantContext) ctx.lookup("java:comp/env/TenantContext");
		} catch (NamingException ex) {
			ex.printStackTrace();
		}
		return tenantContext;
	}
}
