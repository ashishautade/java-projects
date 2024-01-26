package pages;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.TutorialDaoImpl;
//URL : http://localhost:9090/day5_cms/tutorials?topic_id=1
/**
 * Servlet implementation class TutorialsServlet
 */
@WebServlet("/tutorials")
public class TutorialsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//set content type 
		response.setContentType("text/html");
		//get PW to send resp
		try(PrintWriter pw=response.getWriter())
		{
			//get topic id chosen from the clnt : using request param
			int topicId=Integer.parseInt(request.getParameter("topic_id"));//currently skipping the validations
			
			//print the title : tuts available
			pw.print("<h4 align='center'>Tutorials published under Topic ID "+topicId+"</h4>");
			//get HttpSession , to retrieve tut dao instance
			HttpSession session =request.getSession();
			TutorialDaoImpl dao=(TutorialDaoImpl) session.getAttribute("tutorial_dao");
			//invoke dao's method , to get all tuts under chosen topic
			List<String> tutorials = dao.getTutorialsByTopicId(topicId);
			//iterate over the list , creating links
			for(String s : tutorials)
				pw.print("<h5> <a href='tutorial_details?tut_name="+s+"'>"+s+"</a></h5>");
		} catch (Exception e) {
			//re throw exc to WC , by wrapping it in ServletExc
			throw new ServletException("err in do-get of "+getClass(), e);
		}
	}

}
