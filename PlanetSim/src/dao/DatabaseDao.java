package dao;

import models.DatabaseQuery;
import models.Planet;

// TODO Delete once programming. I made this an interface in case you wanted to attempt different implementations of a DAO.
public interface DatabaseDao {

	/**
	 * Method for getting the planet associated with the query. The planet
	 * should have a collection of PlanetCells which contain the queried values.
	 * 
	 * @param query
	 * @return
	 */
	public Planet get(DatabaseQuery query);

	/**
	 * Methods for saving a planet into the database.
	 * 
	 * @param planet
	 */
	public void saveOrUpdate(Planet planet);

}
