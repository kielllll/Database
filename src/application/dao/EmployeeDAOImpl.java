package application.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.database.Database;
import application.entities.Employee;

public class EmployeeDAOImpl implements EmployeeDAO {
	private static EmployeeDAOImpl instance = new EmployeeDAOImpl();
	
	@Override
	public List<Employee> getAllEmployee() {
		List<Employee> list = new LinkedList<Employee>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT *FROM tbl_Employee");
			
			while(rs.next()) {
				list.add(new Employee(rs.getInt("empID"), rs.getInt("unitID"),rs.getInt("compID"),rs.getInt("etID")));
			}
			
			st.close();
			rs.close();
		}catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(Employee employee) {
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO tbl_Employee VALUES"+employee.toString());
			
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
	
	public static EmployeeDAOImpl getInstance() {
		return instance;
	}
}
