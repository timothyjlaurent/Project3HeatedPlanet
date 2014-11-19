package dao;

import java.util.ArrayList;
import java.util.List;

import models.DatabaseQuery;
import models.Experiment;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;

public class DatabaseDaoSqlImpl implements DatabaseDao {

	private Session session;
	private static SessionFactory sessionFactory;

	public DatabaseDaoSqlImpl() {
		sessionFactory = buildSessionFactory();
	}

	private static SessionFactory buildSessionFactory() {
		try {
			final Configuration configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml");
			final ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			return configuration.buildSessionFactory(serviceRegistry);
		} catch (final Throwable ex) {
			System.out.println(ex);
		}
		return null;
	}

	public Experiment get(final DatabaseQuery query) {
		session = sessionFactory.openSession();
		session.beginTransaction();
		createCriteria(session, query);

		session.close();
		return null;
	}

	private Criteria createCriteria(final Session curSession, final DatabaseQuery query) {
		final Criteria cr = curSession.createCriteria(Experiment.class);
		cr.add(Restrictions.eq("equipement_id", query.getExpirementName()));

		return cr;
	}

	public void saveOrUpdate(final Experiment expirement) {
		session = sessionFactory.openSession();
		session.beginTransaction();
		session.saveOrUpdate(expirement);
		session.close();
	}

	@SuppressWarnings("unchecked")
	public List<String> getExperimentNames() {
		session = sessionFactory.openSession();
		session.beginTransaction();

		final Query query = session.createQuery("from " + Experiment.class.getName());
		final List<Experiment> list = query.list();

		session.close();
		return list.isEmpty() ? new ArrayList<String>() : new ArrayList<String>();
	}

}
