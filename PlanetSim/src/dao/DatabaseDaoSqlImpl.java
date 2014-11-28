package dao;

import static java.util.Collections.sort;
import static org.hibernate.criterion.Restrictions.between;
import static org.hibernate.criterion.Restrictions.eq;
import static util.SimulationUtil.convertSetToMap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
		// TODO make this threaded

		session = sessionFactory.openSession();
		final Transaction tx = session.beginTransaction();
		System.out.println("Saving Experiment");
		final Map<Date, Set<GridPoint>> map = convertSetToMap(experiment.getGridPoints());

		// Remove dates due to precision
		final List<Date> dates = new ArrayList<Date>(map.keySet());
		sort(dates);

		final int tempPrecision = experiment.getCommandLineParam().getTemporalPrecision();
		final int numOfDatesToKeep = (int) (dates.size() * tempPrecision / 100.00);

		for (int i = 1; i < dates.size(); i++) {
			if (i % numOfDatesToKeep == 0) {
				map.remove(dates.get(i));
			}
		}

		final int geoPrecision = experiment.getCommandLineParam().getGeographicPrecision();

		final List<GridPoint> pointsToSave = new ArrayList<GridPoint>();

		for (final Entry<Date, Set<GridPoint>> gridPointEntry : map.entrySet()) {
			final List<GridPoint> list = new ArrayList<GridPoint>(gridPointEntry.getValue());
			sort(list);

			final int numOfGeoToKeep = list.size() * geoPrecision / 100;

			int pos = 0;
			for (final Iterator<GridPoint> iterator = list.iterator(); iterator.hasNext();) {
				final GridPoint gridPoint = iterator.next();
				if (pos != 0 && pos % numOfGeoToKeep == 0) {
					iterator.remove();
				} else {
					final double temp = gridPoint.getTemperature();
					final BigDecimal value = new BigDecimal(temp);
					value.setScale(experiment.getCommandLineParam().getDataPrecision(), RoundingMode.HALF_UP);
					// TODO Look into
					gridPoint.setTemperature(value.doubleValue());
					pointsToSave.add(gridPoint);
				}
				pos++;
			}
		}
		experiment.setGridPoints(new HashSet<GridPoint>(pointsToSave));
		System.out.println("Number of Points Saved: " + pointsToSave.size());
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
