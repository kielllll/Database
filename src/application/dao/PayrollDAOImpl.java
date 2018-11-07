package application.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.database.Database;
import application.entities.Payroll;
import application.util.Date;

public class PayrollDAOImpl implements PayrollDAO {
	
	private static PayrollDAOImpl instance = new PayrollDAOImpl();
	
	@Override
	public List<Payroll> getAllPayroll() {
		List<Payroll> list = new LinkedList<Payroll>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT *FROM tbl_Payroll");
			
			while(rs.next()) {
				list.add(new Payroll(rs.getInt("payrollID"),rs.getInt("empID"),rs.getInt("salaryID")
						,rs.getInt("deductionID"),Date.parse(rs.getString("payrollDate"))
						,rs.getDouble("payrollAmount"),rs.getInt("userID")));
			}
			
			st.close();
			rs.close();
		}catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(Payroll payroll) {
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO tbl_Payroll VALUES"+payroll.toString());
			
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

	public static PayrollDAOImpl getInstance() {
		return instance;
	}
}
