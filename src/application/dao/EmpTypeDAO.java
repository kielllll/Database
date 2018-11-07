package application.dao;

import java.util.List;

import application.entities.EmpType;

public interface EmpTypeDAO {
	public List<EmpType> getAllEmpType();
	public void insert(EmpType emptype);
	public void update(String query);
	public void delete(String query);
}
