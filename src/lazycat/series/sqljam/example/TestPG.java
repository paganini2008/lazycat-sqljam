package lazycat.series.sqljam.example;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ConfigurationInitializer;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.SessionEngine;
import lazycat.series.sqljam.SessionOptions;

public class TestPG {

	static {
		System.setProperty("lazycat.logger.level.lazycat.series.sqljam", "DEBUG");
		System.setProperty("lazycat.logger.level.lazycat.series.jdbc", "DEBUG");
	}

	private static Session openSession() {
		String driverClassName = "org.postgresql.Driver";
		String jdbcUrl = "jdbc:postgresql://localhost:5432/db_test";
		SessionEngine sessionEngine = new SessionEngine(driverClassName, jdbcUrl, "tomcat", "12345678", new ConfigurationInitializer() {
			public void configure(Configuration configuration, SessionOptions sessionOptions) {
				configuration.scanPackages("lazycat.series.sqljam.example.model");
			}
		});
		Session session = sessionEngine.openSession();
		System.out.println(session);
		return session;
	}

	public static void main(String[] args) {
		Session session = openSession();
		System.out.println(session);
	}

}
