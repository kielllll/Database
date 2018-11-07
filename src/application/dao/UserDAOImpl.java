package application.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.database.Database;
import application.entities.User;

public class UserDAOImpl implements UserDAO{

	private static UserDAOImpl instance = new UserDAOImpl();
	
	@Override
	public List<User> getAllUser() {
		List<User> list = new LinkedList<User>();
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT *FROM tbl_User");
			
			while(rs.next()) {
				list.add(new User(rs.getInt("userID"), rs.getString("userFName"), rs.getString("userLName"), rs.getString("userUName")
						, rs.getString("userPWord"), rs.getString("userContact"), rs.getString("userStatus")));
			}
			
			st.close();
			rs.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		return list;
	}

	@Override
	public void insert(User user) {
		try {
			Statement st = Database.getInstance().getDBConn().createStatement();
			st.executeUpdate("INSERT INTO tbl_User VALUES"+user.toString());
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

	public static UserDAOImpl getInstance() {
		return instance;
	}
}
