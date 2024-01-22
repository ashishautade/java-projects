package dao;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import pojos.Employee;

public interface IEmployeeDao {
/*
 * dislay details(id,name,salary,join date) of all emps from a specific date,
 * joined between strt date and end date
 */
	List<Employee> getSelectedEmployee(String dept, Date beginDate, Date endDate)throws SQLException;
	//add a method to insert new emp details
	String insertEmpDetails(Employee employee)throws SQLException;
	//add a method to update emp dept and salary
	String updateEmpDeptnSal(int empid,double salary, String newDept)throws SQLException;
	//add a method to delete an emp from DB
	String deleteEmp(int empid) throws SQLException;
	// add a method to get emp report to manager by avg salary dept wise
	Map<String,Double> getEmpAvgSalaryReport() throws SQLException;
}
