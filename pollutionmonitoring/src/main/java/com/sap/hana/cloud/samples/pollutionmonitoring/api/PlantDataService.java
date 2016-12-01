package com.sap.hana.cloud.samples.pollutionmonitoring.api;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.text.MessageFormat;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import com.sap.cloud.account.TenantContext;
import com.sap.core.connectivity.api.configuration.ConnectivityConfiguration;
import com.sap.core.connectivity.api.configuration.DestinationConfiguration;

@Path("/plantdata")
@Produces({ MediaType.APPLICATION_JSON })
public class PlantDataService {
	private static final int COPY_CONTENT_BUFFER_SIZE = 1024;
	private static final String ON_PREMISE_PROXY = "OnPremise";

	@GET
	@Path("/")
	@Produces({ MediaType.APPLICATION_JSON })
	public String getPlantsOnPremiseData() throws Exception {
		DestinationConfiguration destConfiguration = getDestConfiguration();
		// Get the destination URL
		String value = destConfiguration.getProperty("URL");
		URL destinationURL = new URL(value);
		String result = retrieveOnPremisePlantDetails(destConfiguration, destinationURL);
		return result;

	}

	private DestinationConfiguration getDestConfiguration() throws NamingException {
		// Look up the connectivity configuration API
		javax.naming.Context ctx = new InitialContext();
		ConnectivityConfiguration configuration = (ConnectivityConfiguration) ctx
				.lookup("java:comp/env/connectivityConfiguration");
		// Get destination configuration for "destinationName"
		DestinationConfiguration destConfiguration = configuration.getConfiguration("onprem-plantdata-dest");
		return destConfiguration;
	}
	
	public String getPlantOnPremiseData(String plant_id) throws Exception {
		DestinationConfiguration destConfiguration = getDestConfiguration();
		// Get the destination URL
		String baseURL = destConfiguration.getProperty("URL");
		String formattedURL = null;
		formattedURL = MessageFormat.format("{0}?id={1}", baseURL, plant_id);
		URL destinationURL=new URL(formattedURL);
		String result = retrieveOnPremisePlantDetails(destConfiguration, destinationURL);
		return result;
	}

	private String retrieveOnPremisePlantDetails(DestinationConfiguration destConfiguration, URL destinationURL)
			throws MalformedURLException, IOException, Exception {
		HttpURLConnection urlConnection;
		String proxyType = destConfiguration.getProperty("ProxyType");
		Proxy proxy = getProxy(proxyType);
		urlConnection = (HttpURLConnection) destinationURL.openConnection(proxy);
		injectHeader(urlConnection, proxyType);
		// Copy content from the incoming response to the outgoing response
		InputStream instream = urlConnection.getInputStream();
		String result = getResponseBodyAsString(instream);
		return result;
	}
	/**
	 * Extracts the response body from the specified {@link HttpEntity} and
	 * returns it as a UTF-8 encoded JSON Array.retrieve 
	 * 
	 * @param entity
	 *            The {@link HttpEntity} to extract the message body
	 *            from @return The UTF-8 encoded JSON Array representation of
	 *            the message body
	 */
	static String getResponseBodyAsString(InputStream instream) throws Exception {
		String retVal = null;
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		try {
			byte[] buffer = new byte[COPY_CONTENT_BUFFER_SIZE];
			int len;
			while ((len = instream.read(buffer)) != -1) {
				outstream.write(buffer, 0, len);
			}
		} catch (IOException e) {
			// In case of an IOException the connection will be released
			// back to the connection manager automatically
			throw e;
		} finally {
			// Closing the input stream will trigger connection release
			try {
				instream.close();
			} catch (Exception e) {
				// Ignore
			}
		}
		retVal = outstream.toString("UTF-8");
		return retVal;

	}
	
	private void injectHeader(HttpURLConnection urlConnection, String proxyType) {
        if (ON_PREMISE_PROXY.equals(proxyType)) {
            // Insert header for on-premise connectivity with the consumer account Id
            urlConnection.setRequestProperty("SAP-Connectivity-ConsumerAccount",
                    getTenantContext().getTenant().getAccount().getId());
        }
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

	private Proxy getProxy(String proxyType) {
		 String proxyHost = null;
	        int proxyPort;
	        if (ON_PREMISE_PROXY.equals(proxyType)) {
	            // Get proxy for on-premise destinations
	            proxyHost = System.getenv("HC_OP_HTTP_PROXY_HOST");
	            proxyPort = Integer.parseInt(System.getenv("HC_OP_HTTP_PROXY_PORT"));
	        } else {
	            // Get proxy for internet destinations
	            proxyHost = System.getProperty("https.proxyHost");
	            proxyPort = Integer.parseInt(System.getProperty("https.proxyPort"));
	        }
	        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
	}

}
