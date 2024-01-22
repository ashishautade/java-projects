package utils;

import java.sql.*;

public class DBUtils {
	//modify the code below to ensure singleton instance of DB connection(not a scalable solution, will be replaced by connection pool in hibernate onwards)
	
	private static Connection cn;
	
	
	public static Connection openConnection() throws SQLException {
		if(cn==null) {
		String url = "jdbc:mysql://localhost:3306/dac24?useSSL=false&allowPublicKeyRetrieval=true";
		cn= DriverManager.getConnection(url,"ashish","cdac");
		}
		return cn;
	}
}
