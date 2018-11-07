package application.dao;

import java.util.List;

import application.entities.EmpInfo;

public interface EmpInfoDAO {
	public List<EmpInfo> getAllEmpInfo();
	public void insert(EmpInfo empinfo);
	public void update(String query);
	public void delete(String query);
}
