package application.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.database.Database;
import application.entities.Lookup;

public class LookupDAOImpl implements LookupDAO{
	private static LookupDAOImpl instance = new LookupDAOImpl();
	
	@Override
	public List<Lookup> getAllLookup() {
		List<Lookup> list = new LinkedList<Lookup>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT *FROM tbl_Lookup");
			
			while(rs.next()) {
				list.add(new Lookup(rs.getInt("lookupID"),rs.getInt("deductionID")));
			}
			
			st.close();
			rs.close();
		}catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(Lookup lookup) {
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO tbl_Lookup VALUES"+lookup.toString());
			
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

	public static LookupDAOImpl getInstance() {
		return instance;
	}
}
