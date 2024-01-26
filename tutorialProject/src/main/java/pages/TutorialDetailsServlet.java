package pages;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.TutorialDaoImpl;
import pojos.Tutorial;

//URL : http://localhost:9090/day5_cms/tutorial_details?tut_name=Spring%20MVC
/**
 * Servlet implementation class TutorialDetailsServlet
 */
@WebServlet("/tutorial_details")
public class TutorialDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		try (PrintWriter pw = response.getWriter()) {
			// get selected tut name from request parameter
			String tutorialName = request.getParameter("tut_name");
			// get HttpSession to retrieve tut dao instance
			HttpSession session = request.getSession();
			TutorialDaoImpl dao = (TutorialDaoImpl) session.getAttribute("tutorial_dao");
			// invoke dao's method , to get details of the selected tutorial
			Tutorial tutorial = dao.getTutorialDetails(tutorialName);
			// update visits
			System.out.println(dao.updateVisits(tutorial.getTutorialId()));
			pw.print("<h5> Tutorial : " + tutorial.getTutorialName() + "</h5>");
			pw.print("<h5> Author  : " + tutorial.getAuthor() + "</h5>");
			pw.print("<h5> Published On  : " + tutorial.getPublishDate() + "</h5>");
			pw.print("<h5> Visits  : " + tutorial.getVisits() + "</h5>");
			pw.print("<h5> Contents  : " + tutorial.getContents() + "</h5>");

			// send back link to the clnt
			pw.print("<h5> <a href='tutorials?topic_id="+tutorial.getTopicId()+"'>Back</a></h5>");
			// send log out  link to the clnt
			pw.print("<h5> <a href='logout'>Log Me Out</a></h5>");

		} catch (Exception e) {
			// re throw exc to WC , by wrapping it in ServletExc
			throw new ServletException("err in do-get of " + getClass(), e);
		}
	}

}
