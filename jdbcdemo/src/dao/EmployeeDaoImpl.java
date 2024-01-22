package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mysql.cj.xdevapi.Result;

import static utils.DBUtils.openConnection;
import pojos.Employee;
	 
public class EmployeeDaoImpl implements IEmployeeDao {
	
	
	//state: instance variable
	private Connection cn;
	private PreparedStatement pst1,pst2,pst3,pst4,pst5;
	
	
	public EmployeeDaoImpl() throws SQLException {
		//get fix db conn from dbutils
		cn=openConnection();
		pst1=cn.prepareStatement("select empid,name,salary,join_date from my_emp where deptid=? and join_date between ? and ?");
		pst2=cn.prepareStatement("insert into my_emp values(default,?,?,?,?,?)");
		pst3=cn.prepareStatement("update my_emp set salary=salary+?,deptid=? where empid=?");
		pst4=cn.prepareStatement("delete  from my_emp where empid=?");
		pst5=cn.prepareStatement("select deptid,avg(salary)from my_emp group by deptid");
		System.out.println("employee dao completed...");
		
		
	}
	@Override
	public List<Employee> getSelectedEmployee(String dept, Date beginDate, Date endDate) throws SQLException {
		List<Employee> emps=new ArrayList<>();
		//set IN params
		pst1.setString(1, dept);
		pst1.setDate(2, beginDate);
		pst1.setDate(3, endDate);
		
		try(ResultSet rst=pst1.executeQuery()){
			while(rst.next()) {
				emps.add(new Employee(rst.getInt(1), rst.getString(2),rst.getDouble(3),rst.getDate(4)));
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return emps;
	}
	
	
	@Override
	public String deleteEmp(int empid) throws SQLException {
		//set IN params
		pst4.setInt(1, empid);
		//execute query
		int updateCount=pst4.executeUpdate();
		if(updateCount==1)
			return "emp details deletion successfully";
			return "emp details deletion failed";
	}
	@Override
	public String insertEmpDetails(Employee employee) throws SQLException {
		//set IN params
		//name|addr|salry|deptid|joindate
		pst2.setString(1, employee.getName());
		pst2.setString(2, employee.getAddress());
		pst2.setDouble(3, employee.getSalary());
		pst2.setString(4, employee.getDeptId());
		pst2.setDate(5, employee.getJoinDate());
		//execute query:dml : method of pst: public int executeUpdate()throws SQLException
		int updateCount=pst2.executeUpdate();
		if(updateCount==1)
		return "emp details updated successfully";
		return "emp details insertion failed";
	}
	
	
	@Override
	public String updateEmpDeptnSal(int empid, double salary, String newDept) throws SQLException {
		//set IN params
		pst3.setInt(3, empid);
		pst3.setDouble(1, salary);
		pst3.setString(2, newDept);
		
		//execute query:dml
		int updateCount=pst3.executeUpdate();
		if(updateCount==1)
			return "emp details updated successfully";
			return "emp details updation failed";
		
	}
		//add a non static method to clean up db resources
		public  void cleanUp() throws SQLException {
			if(pst1!=null) 
				pst1.close();
			if(pst2!=null) 
				pst2.close();
			if(pst3!=null) 
				pst3.close();
			if(pst4!=null) 
				pst4.close();
			if(pst5!=null) 
				pst5.close();
			if(cn!=null)
				cn.close();
			System.out.println("emp dao cleaned up...");
		}
		@Override
		public Map<String, Double> getEmpAvgSalaryReport() throws SQLException {
			Map<String, Double>map=new LinkedHashMap<>();
			try(ResultSet rst=pst5.executeQuery()){
				while(rst.next()) {
					map.put(rst.getString(1), rst.getDouble(2));
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
			return map;
		}
}
