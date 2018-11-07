package application.dao;

import java.util.List;

import application.entities.Unit;

public interface UnitDAO {
	public List<Unit> getAllUnit();
	public void insert(Unit unit);
	public void update(String query);
	public void delete(String query);
}
