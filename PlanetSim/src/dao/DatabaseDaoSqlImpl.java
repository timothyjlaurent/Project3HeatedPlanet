package dao;

import static java.util.Collections.sort;
import static org.hibernate.criterion.Projections.projectionList;
import static org.hibernate.criterion.Projections.property;
import static org.hibernate.criterion.Restrictions.between;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.transform.Transformers.aliasToBean;
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
		final int id = query.getExperimentId();
		cr.add(eq("experimentId", id));
		cr.createAlias("gridPoints", "gridPoints");
		cr.add(between("gridPoints.dateTime", query.getStartDateTime(), query.getEndDateTime()));
		cr.add(between("gridPoints.topLatitude", query.getCoordinateLatitudeOne(), query.getCoordinateLatitudeTwo()));
		cr.add(between("gridPoints.leftLongitude", query.getCoordinateLongitudeOne(), query.getCoordinateLongitudeTwo()));
		return cr;
	}

	@Override
	public void saveOrUpdate(final Experiment experiment) {

		// Remove dates due to precision
		new Thread(new Runnable() {
			@Override
			public void run() {

				session = sessionFactory.openSession();
				final Transaction tx = session.beginTransaction();
				System.out.println("Saving Experiment");
				final Map<Date, Set<GridPoint>> map = convertSetToMap(experiment.getGridPoints());

				// Remove dates due to precision
				final List<Date> dates = new ArrayList<Date>(map.keySet());
				sort(dates);

				final int tempPrecision = experiment.getCommandLineParam().getTemporalPrecision();
				final int numOfDatesToKeep = (int) (dates.size() * tempPrecision / 100.00);

				final ArrayList<Integer> dateIndexesToKeep = new ArrayList<Integer>();

				for (int i = 0; i < numOfDatesToKeep; i += 1) {
					dateIndexesToKeep.add((int) Math.round((double) i / numOfDatesToKeep * dates.size()));
				}

				for (int i = 0; i < dates.size(); i++) {
					if (!dateIndexesToKeep.contains(i)) {
						map.remove(dates.get(i));
					}
				}

				final int geoPrecision = experiment.getCommandLineParam().getGeographicPrecision();

				final List<GridPoint> pointsToSave = new ArrayList<GridPoint>();

				for (final Entry<Date, Set<GridPoint>> gridPointEntry : map.entrySet()) {
					final List<GridPoint> list = new ArrayList<GridPoint>(gridPointEntry.getValue());
					sort(list);

					final int numOfGeoToKeep = list.size() * geoPrecision / 100;

					final ArrayList<Integer> geoIndexesToKeep = new ArrayList<Integer>();

					for (int i = 0; i < numOfGeoToKeep; i += 1) {
						geoIndexesToKeep.add((int) Math.round((double) i / numOfGeoToKeep * list.size()));
					}

					int pos = 0;
					for (final Iterator<GridPoint> iterator = list.iterator(); iterator.hasNext();) {
						final GridPoint gridPoint = iterator.next();
						if (!geoIndexesToKeep.contains(pos)) {
							iterator.remove();
						} else {
							final double temp = gridPoint.getTemperature();
							final BigDecimal value = new BigDecimal(temp);
							value.setScale(experiment.getCommandLineParam().getDataPrecision(), RoundingMode.HALF_UP);
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
		}).start();
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

	@Override
	@SuppressWarnings("unchecked")
	public List<Experiment> getAllExperiments() {
		session = sessionFactory.openSession();
		session.beginTransaction();
		// Add Projections to not get back the list of grid points
		final Criteria criteria = session.createCriteria(Experiment.class)
				.setProjection(projectionList()
						.add(property("experimentId"), "experimentId")
						.add(property("simulationSettings"), "simulationSettings")
						.add(property("simulationSettings.experimentName"))
						.add(property("simulationSettings.gridSpacing"))
						.add(property("simulationSettings.timeStep"))
						.add(property("simulationSettings.simulationLength"))
						.add(property("commandLineParam"), "commandLineParam")
						.add(property("commandLineParam.geographicPrecision"))
						.add(property("commandLineParam.temporalPrecision"))
						.add(property("commandLineParam.dataPrecision"))
						.add(property("physicalFactors"), "physicalFactors")
						.add(property("physicalFactors.axialTilt"))
						.add(property("physicalFactors.orbitalEccentricity")))
				.setResultTransformer(aliasToBean(Experiment.class));

		final List<Experiment> list = criteria.list();
		session.close();
		return list.isEmpty() ? new ArrayList<Experiment>() : list;
	}
}
