package application.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.database.Database;
import application.entities.Salary;

public class SalaryDAOImpl implements SalaryDAO{
	
	private static SalaryDAOImpl instance = new SalaryDAOImpl();
	
	@Override
	public List<Salary> getAllSalary() {
		List<Salary> list = new LinkedList<Salary>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT *FROM tbl_Salary");
			
			while(rs.next()) {
				list.add(new Salary(rs.getInt("salaryID"),rs.getInt("empID"),rs.getString("salaryStatus"),rs.getInt("numAbsc")));
			}
			
			st.close();
			rs.close();
		}catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(Salary salary) {
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO tbl_Salary VALUES"+salary.toString());
			
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

	public static SalaryDAOImpl getInstance() {
		return instance;
	}
}
