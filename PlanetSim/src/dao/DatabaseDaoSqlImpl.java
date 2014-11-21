package dao;

import static java.util.Collections.sort;
import static org.hibernate.criterion.Restrictions.between;
import static org.hibernate.criterion.Restrictions.eq;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import models.DatabaseQuery;
import models.Experiment;
import models.GridPoint;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
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
			System.err.println(ex);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Experiment> get(final DatabaseQuery query) {
		session = sessionFactory.openSession();
		session.beginTransaction();
		final Criteria criteria = createCriteria(session, query);

		final List<Experiment> experiments = criteria.list();
		session.close();
		return experiments;
	}

	private Criteria createCriteria(final Session curSession, final DatabaseQuery query) {
		final Criteria cr = curSession.createCriteria(Experiment.class);
		cr.add(eq("commandLineParam.geographicPrecision", query.getGeoPrecision()));
		cr.add(eq("commandLineParam.temporalPrecision", query.getTemporalPrecision()));
		cr.add(eq("commandLineParam.dataPrecision", query.getDataPrecision()));
		cr.add(eq("simulationSettings.experimentName", query.getExpirementName()));
		cr.add(eq("simulationSettings.gridSpacing", query.getGridSpacing()));
		cr.add(eq("simulationSettings.timeStep", query.getTimeStep()));
		cr.add(eq("physicalFactors.axialTilt", query.getAxialTilt()));
		cr.add(eq("physicalFactors.orbitalEccentricity", query.getOrbitalEccentricity()));
		cr.createAlias("gridPoints", "gridPoints");
		cr.add(between("gridPoints.dateTime", query.getStartDateTime(), query.getEndDateTime()));
		cr.add(between("gridPoints.topLatitude", query.getCoordinateLatitudeOne(), query.getCoordinateLatitudeTwo()));
		cr.add(between("gridPoints.leftLongitude", query.getCoordinateLongitudeOne(), query.getCoordinateLongitudeTwo()));
		return cr;
	}

	@Override
	public void saveOrUpdate(final Experiment experiment) {
		session = sessionFactory.openSession();
		final Transaction tx = session.beginTransaction();

		// Remove dates due to precision
		final List<Date> dates = new ArrayList<Date>(experiment.getGridPointMap().keySet());
		sort(dates);

		final int tempPrecision = experiment.getCommandLineParam().getTemporalPrecision();
		final int numOfDatesToKeep = dates.size() * (tempPrecision / 100);
		for (int i = 0; i < dates.size(); i++) {
			if (i % numOfDatesToKeep != 0) {
				experiment.getGridPointMap().remove(dates.get(i));
			}
		}
		experiment.setGridPointMap(experiment.getGridPointMap());

		final int geoPrecision = experiment.getCommandLineParam().getGeographicPrecision();
		final int numOfGeoToKeep = experiment.getGridPoints().size() * geoPrecision / 100;

		final List<GridPoint> gridPoints = new ArrayList<GridPoint>(experiment.getGridPoints());
		sort(gridPoints);

		final int pos = 0;
		final Set<GridPoint> pointsToSave = new HashSet<GridPoint>();
		for (final Iterator<GridPoint> iterator = gridPoints.iterator(); iterator.hasNext();) {
			final GridPoint gridPoint = iterator.next();
			if (pos % numOfGeoToKeep != 0) {
				iterator.remove();
			} else {
				final double temp = gridPoint.getTemperature();
				final BigDecimal value = new BigDecimal(temp);
				// value.setScale(experiment.getCommandLineParam().getDataPrecision());
				// //TODO
				gridPoint.setTemperature(value.doubleValue());
				pointsToSave.add(gridPoint);
			}
		}
		experiment.setGridPoints(pointsToSave);

		session.saveOrUpdate(experiment);
		tx.commit();
		session.flush();
		session.close();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> getExperimentNames() {
		session = sessionFactory.openSession();
		session.beginTransaction();

		final Query query = session.createSQLQuery("select distinct(experiment_name) from experiment");
		final List<String> list = query.list();

		session.close();
		return list.isEmpty() ? new ArrayList<String>() : list;
	}

}
