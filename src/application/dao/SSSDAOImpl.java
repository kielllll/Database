package application.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.database.Database;
import application.entities.SSS;

public class SSSDAOImpl implements SSSDAO{
	private static SSSDAOImpl instance = new SSSDAOImpl();
	
	@Override
	public List<SSS> getAllSSS() {
		List<SSS> list = new LinkedList<SSS>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT *FROM tbl_SSS");
			
			while(rs.next()) {
				list.add(new SSS(rs.getInt("sssID"),rs.getInt("empID"),rs.getDouble("sssAmount")));
			}
			
			st.close();
			rs.close();
		}catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(SSS sss) {
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO tbl_SSS VALUES"+sss.toString());
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
	
	public static SSSDAOImpl getInstance() {
		return instance;
	}
}
