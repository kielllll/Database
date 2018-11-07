package application.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.database.Database;
import application.entities.Company;

public class CompanyDAOImpl implements CompanyDAO{
	private static CompanyDAOImpl instance = new CompanyDAOImpl();
	
	@Override
	public List<Company> getAllCompany() {
		List<Company> list = new LinkedList<Company>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT *FROM tbl_Company");
			
			while(rs.next()) {
				list.add(new Company(rs.getInt("compID"),rs.getString("compName"),rs.getString("compLocation"),rs.getString("compContact")));
			}
			
			st.close();
			rs.close();
		}catch(Exception err) {
			err.printStackTrace();
		}
		
		return list;
	}

	@Override
	public void insert(Company company) {
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO tbl_company VALUES"+company.toString());
			
			st.close();
		}catch(Exception err) {
			err.printStackTrace();
		}
	}

	@Override
	public void update(String query) {
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate(query);
			
			st.close();
		}catch(Exception err) {
			err.printStackTrace();
		}
	}

	@Override
	public void delete(String query) {
		// TODO Auto-generated method stub
		
	}
	
	public static CompanyDAOImpl getInstance() {
		return instance;
	}
}
