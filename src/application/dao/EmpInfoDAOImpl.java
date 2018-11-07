package application.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.database.Database;
import application.entities.EmpInfo;
import application.util.Date;

public class EmpInfoDAOImpl implements EmpInfoDAO{
	private static EmpInfoDAOImpl instance = new EmpInfoDAOImpl();
	
	@Override
	public List<EmpInfo> getAllEmpInfo() {
		List<EmpInfo> list = new LinkedList<EmpInfo>();
		
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT *FROM tbl_EmpInfo");
			
			while(rs.next()) {
				list.add(new EmpInfo(rs.getInt("empID"), rs.getString("empFName"), rs.getString("empLName"), rs.getString("empContact")
						, Date.parse(rs.getString("empBDate")), rs.getString("empGender"), rs.getString("empStatus")));
			}
			
			st.close();
			rs.close();
		}catch(Exception e) {
			
		}
		return list;
	}

	@Override
	public void insert(EmpInfo empinfo) {
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO tbl_EmpInfo VALUES"+empinfo.toString());
			
			st.close();
		}catch(Exception err) {
			err.printStackTrace();
		}
	}

	@Override
	public void update(String query) {
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.execute(query);
			
			st.close();
		}catch(Exception err) {
			err.printStackTrace();
		}
	}

	@Override
	public void delete(String query) {
		// TODO Auto-generated method stub
		
	}
	
	public static EmpInfoDAOImpl getInstance() {
		return instance;
	}
}
