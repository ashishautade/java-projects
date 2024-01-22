package tester;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import dao.EmployeeDaoImpl;
import pojos.Employee;

public class TestEmpCRUD {

	private static List<Employee> selectedEmployee;

	public static void main(String[] args) {
		try (Scanner sc = new Scanner(System.in)) {
			// create emp dao instance:: initialisation phase
			EmployeeDaoImpl dao = new EmployeeDaoImpl();
			boolean exit = false;
			while (!exit) {
				System.out.println(
						"1.get emp details 2.insert emp details. 3. update emp details 4.delete emp details 5.get report by dept 10.exit");
				try {
					switch (sc.nextInt()) {
					case 1:// get emp details
						System.out.println("Enter dept begin and end date (yr-mon-day)");
						List<Employee> empList = dao.getSelectedEmployee(sc.next(), Date.valueOf(sc.next()),
								Date.valueOf(sc.next()));
						empList.forEach(System.out::println);
						break;
					case 2:// insert emp details into DB
						System.out.println("Enter employee details: name,addr,salary,deptid,join_date,(yr_mon_day)");
						System.out.println(dao.insertEmpDetails(new Employee(sc.next(), sc.next(), sc.nextDouble(),
								sc.next(), Date.valueOf(sc.next()))));
						break;
					case 3:// update details
						System.out.println("enter empid, salinc,newdept");
						System.out.println(dao.updateEmpDeptnSal(sc.nextInt(), sc.nextDouble(), sc.next()));
						break;
					case 4:// delete emp details
						System.out.println("enter empid");
						System.out.println(dao.deleteEmp(sc.nextInt()));
						break;
					case 5:// get report
						dao.getEmpAvgSalaryReport().forEach((dept,sal)->System.out.println("Dept:"+dept+" Avg salary: "+sal));
						break;
					case 10:
						// destroy phase(shut down)
						dao.cleanUp();
						break;
					default:
						break;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
