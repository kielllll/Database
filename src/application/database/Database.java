package application.database;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;

public class Database {
	private Connection DBConn;
	
	private static Database instance = new Database();
	
	public Database() {
		DBConn = null;
	}
	
	public void DBSetConnection() throws SQLException{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			DBConn=DriverManager.getConnection("jdbc:mysql://localhost:3306/db_payroll","kiel","kielllll5999");
		}catch(Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void DBCloseConnection()throws SQLException {
		try {
		DBConn.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Database getInstance() {
		return instance;
	}
	
	public Connection getDBConn() {
		return DBConn;
	}
}
