package application.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.database.Database;
import application.entities.CashAdvance;
import application.util.Date;

public class CashAdvanceDAOImpl implements CashAdvanceDAO{

	private static CashAdvanceDAOImpl instance = new CashAdvanceDAOImpl();
	
	@Override
	public List<CashAdvance> getAllCashAdvance() {
		List<CashAdvance> list = new LinkedList<CashAdvance>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT *FROM tbl_CashAdvance");
			
			while(rs.next()) {
				list.add(new CashAdvance(rs.getInt("caID"),rs.getInt("lookupID"),Date.parse(rs.getString("caDate"))
						,rs.getString("caDescription"),rs.getDouble("caAmount"),rs.getString("caStatus")));
			}
			
			st.close();
			rs.close();
		}catch(Exception err) {
			err.printStackTrace();
		}
		return list;
	}

	@Override
	public void insert(CashAdvance ca) {
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO tbl_CashAdvance VALUES"+ca.toString());
			
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
	
	public static CashAdvanceDAOImpl getInstance() {
		return instance;
	}
}
