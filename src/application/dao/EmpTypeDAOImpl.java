package application.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.database.Database;
import application.entities.EmpType;

public class EmpTypeDAOImpl implements EmpTypeDAO{
	
	private static EmpTypeDAOImpl instance = new EmpTypeDAOImpl();
	
	@Override
	public List<EmpType> getAllEmpType() {
		List<EmpType> list = new LinkedList<EmpType>();
		try{
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT *FROM tbl_EmpType");
			
			while(rs.next()) {
				list.add(new EmpType(rs.getInt("etID"),rs.getInt("compID"),rs.getString("etName")
						,rs.getString("etDescription"),rs.getDouble("etRate")));
			}
			
			st.close();
			rs.close();
		}catch (Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(EmpType emptype) {
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO tbl_EmpType VALUES"+emptype.toString());
			
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

	public static EmpTypeDAOImpl getInstance() {
		return instance;
	}
}
