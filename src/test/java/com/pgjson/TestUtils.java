package com.pgjson;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class TestUtils {
	private static Session session = getSession();


	public synchronized static Session getSession() {
		if (session == null) {
			Properties properties = new Properties();
			properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
			properties.put("hibernate.hbm2ddl.auto", "update");
			properties.put("hibernate.show_sql", "true");
			properties.put("hibernate.connection.driver_class", "org.postgresql.Driver");
			properties.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/pgjsonb");
			properties.put("hibernate.connection.username", "test");
			properties.put("hibernate.connection.password", "test");

			SessionFactory sessionFactory = new Configuration()
					.addProperties(properties)
					.addAnnotatedClass(User.class)
					.buildSessionFactory(
							new StandardServiceRegistryBuilder()
									.applySettings(properties)
									.build()
					);
			session = sessionFactory.openSession();
		}
		return session;
	}
}
