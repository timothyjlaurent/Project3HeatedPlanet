package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import models.DatabaseQuery;
import models.Experiment;

public class DatabaseDaoSqlImpl implements DatabaseDao {

	private Session session;
	private static SessionFactory sessionFactory;
	
	public DatabaseDaoSqlImpl() {
		this.sessionFactory = buildSessionFactory();
	}

	private static SessionFactory buildSessionFactory() {
		try {
			Configuration configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml");
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties())
				.build();
			return configuration.buildSessionFactory(serviceRegistry);
		} catch (Throwable ex) {
			System.out.println(ex);
		}
		return null;
	}

	@Override
	public Experiment get(DatabaseQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveOrUpdate(Experiment planet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> getExperimentNames() {
		System.out.println(sessionFactory);
		session = sessionFactory.openSession();
		session.beginTransaction();
		
//		Query query = session.createQuery("select name from experiment");	
        Query query = session.createQuery("from " + Experiment.class.getName());

		List<Experiment> list = (List<Experiment>) query.list();
		
		session.close();
		return list.isEmpty() ? new ArrayList<String>() : new ArrayList<String>();
	}

}
