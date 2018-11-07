package application.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.database.Database;
import application.entities.Deduction;

public class DeductionDAOImpl implements DeductionDAO{

	private static DeductionDAOImpl instance = new DeductionDAOImpl();
	
	@Override
	public List<Deduction> getAllDeduction() {
		List<Deduction> list = new LinkedList<Deduction>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT *FROM tbl_Deduction");
			
			while(rs.next()) {
				list.add(new Deduction(rs.getInt("deductionID"),rs.getInt("empID"),rs.getString("deductionStatus")));
			}
			
			st.close();
			rs.close();
		}catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(Deduction deduction) {
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO tbl_Deduction VALUES"+deduction.toString());
			
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

	public static DeductionDAOImpl getInstance() {
		return instance;
	}
}
