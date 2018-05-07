package io.verticon.dmescripts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Hashtable;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import io.verticon.dmescripts.model.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Factory implements ServletContextListener {

	static final String persistanceUnit = "dmeScriptsDemo";
	public static final EntityManagerFactory emf = getFactory();

	private static EntityManagerFactory getFactory() {
		
		// If the connection information has been provided via system properties
		// then use those values to override the values provided by persistence.xml

		Map<String, String> overrides = new Hashtable<String,String>();

		String dbName = System.getProperty("RDS_DB_NAME");
		String userName = System.getProperty("RDS_USERNAME");
		String password = System.getProperty("RDS_PASSWORD");
		String hostName = System.getProperty("RDS_HOSTNAME");
		String port = System.getProperty("RDS_PORT");

		if (dbName != null && userName != null && password != null && hostName != null && port != null) {
			overrides.put("javax.persistence.jdbc.url", String.format("jdbc:mysql://%s:%s/%s", hostName, port, dbName));
			overrides.put("javax.persistence.jdbc.user", userName);
			overrides.put("javax.persistence.jdbc.password", password);
			//System.out.printf("Connection URL is %s - user = %s, password = %s", String.format("jdbc:mysql://%s:%s/%s", hostName, port, dbName), userName, password);
		}

		return Persistence.createEntityManagerFactory(persistanceUnit, overrides);
	}

    //***************************************************************************************

	public static final List<String> insuranceCompanies = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{
			add("Prudential");
			add("State Farm");
			add("BCBS");
		}
	};

	private static Random random = new Random();
	public static String getRandomInsurance() {
		return Factory.insuranceCompanies.get(random.nextInt(insuranceCompanies.size()));
	}

	// Careful, the DataAccessService implementations depend upon the insurance
	// companies and the EMF; so initialize the data service last. KLUDGY! Must fix.
	public static final DataAccessService sDataService = new DefaultDataAccessService();

	
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Servlet has been started.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Servlet has been stopped.");
    }

}
