package dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import models.DatabaseQuery;
import models.Experiment;

public class DatabaseDaoFlatImpl implements DatabaseDao {

	@Override
	public List<Experiment> get(final DatabaseQuery query) {
		return new ArrayList<Experiment>();
	}

	@Override
	public void saveOrUpdate(final Experiment planet) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> getExperimentNames() {
		return Arrays.asList("Exp 1", "Exp 2", "Exp3");
	}
}
