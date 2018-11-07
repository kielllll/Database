package application.dao;

import java.util.List;

import application.entities.Company;

public interface CompanyDAO {
	public List<Company> getAllCompany();
	public void insert(Company company);
	public void update(String query);
	public void delete(String query);
}
