package application.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.database.Database;
import application.entities.Unit;

public class UnitDAOImpl implements UnitDAO{
	
	private static UnitDAOImpl instance = new UnitDAOImpl();
	
	@Override
	public List<Unit> getAllUnit() {
		List<Unit> list = new LinkedList<Unit>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT *FROM tbl_Unit");
			
			while(rs.next()) {
				list.add(new Unit(rs.getInt("unitID"),rs.getString("unitName"),rs.getString("unitStatus")));
			}
			
			st.close();
			rs.close();
		}catch(Exception err) {
			err.printStackTrace();
		}
		
		return list;
	}

	@Override
	public void insert(Unit unit) {
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO tbl_Unit VALUES"+unit.toString());
			
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

	public static UnitDAOImpl getInstance() {
		return instance;
	}
}
