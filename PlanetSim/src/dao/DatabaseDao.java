package dao;

import java.util.List;

import models.DatabaseQuery;
import models.Experiment;

// TODO Delete once programming. I made this an interface in case you wanted to attempt different implementations of a DAO.
public interface DatabaseDao {

	/**
	 * Method for getting the planet associated with the query. The planet
	 * should have a collection of PlanetCells which contain the queried values.
	 * 
	 * @param query
	 * @return
	 */
	public Experiment get(DatabaseQuery query);

	/**
	 * Methods for saving a planet into the database.
	 * 
	 * @param planet
	 */
	public void saveOrUpdate(Experiment planet);

	public List<String> getExperimentNames();

}
