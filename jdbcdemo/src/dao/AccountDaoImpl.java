package dao;

import java.sql.*;
import java.sql.SQLException;
import static utils.DBUtils.openConnection;

public class AccountDaoImpl implements IAccountDao {
	private static Connection cn;
	private CallableStatement cst1;
	
	
	public AccountDaoImpl() throws SQLException {
		// get the databaase connection from db utils
		cn=openConnection();
		cst1=cn.prepareCall("{call transfer_funds(?,?,?,?,?)}");
		//register out params: api
		cst1.registerOutParameter(4, Types.DOUBLE);
		cst1.registerOutParameter(5, Types.DOUBLE);
		System.out.println("acct dao created");
	}
	
	@Override
	public String transferFunds(int srcAccNo, int desAccNo, double amount) throws SQLException {
		//set IN params
		cst1.setInt(1,srcAccNo);
		cst1.setInt(2, desAccNo);
		cst1.setDouble(3, amount);
		//execute stored procedure
		cst1.execute();
		
		return "updated src balance "+cst1.getDouble(4)+" Dest balance"+cst1.getDouble(5);
	}
	
	public void cleanUp() throws SQLException {
		if(cst1!=null)
			cst1.close();
		if(cn!=null)
			cn.close();
		System.out.println("acc dao closed");
	}

}
