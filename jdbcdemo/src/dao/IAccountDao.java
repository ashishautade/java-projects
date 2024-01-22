package dao;

import java.sql.SQLException;

public interface IAccountDao {

	//add a method to traansfer from src to dest
	String transferFunds(int srcAccNo,int desAccNo,double amount)throws SQLException;
}
