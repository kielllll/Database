package application.dao;

import java.util.List;

import application.entities.Lookup;

public interface LookupDAO {
	public List<Lookup> getAllLookup();
	public void insert(Lookup lookup);
	public void update(String query);
	public void delete(String query);
}
