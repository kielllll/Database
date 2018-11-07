package application.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.database.Database;
import application.entities.DTR;
import application.util.Date;

public class DTRDAOImpl implements DTRDAO{
	private static DTRDAOImpl instance = new DTRDAOImpl();
	
	@Override
	public List<DTR> getAllDTR() {
		List<DTR> list = new LinkedList<DTR>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT *FROM tbl_DTR");
			
			while(rs.next()) {
				list.add(new DTR(rs.getInt("dtrID"),rs.getInt("salaryID"),Date.parse(rs.getString("dtrDate")),
						rs.getInt("dtrHour")));
			}
			
			st.close();
			rs.close();
		}catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(DTR dtr) {
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO tbl_DTR VALUES"+dtr.toString());
			
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
	
	public static DTRDAOImpl getInstance() {
		return instance;
	}
}
