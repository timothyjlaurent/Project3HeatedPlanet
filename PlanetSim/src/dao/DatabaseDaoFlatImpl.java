package dao;

import java.util.Arrays;
import java.util.List;

import models.DatabaseQuery;
import models.Experiment;

public class DatabaseDaoFlatImpl implements DatabaseDao {

	public Experiment get(final DatabaseQuery query) {
		return null;
	}

	public void saveOrUpdate(final Experiment planet) {
		// TODO Auto-generated method stub

	}

	public List<String> getExperimentNames() {
		return Arrays.asList("Exp 1", "Exp 2", "Exp3");
	}
}
