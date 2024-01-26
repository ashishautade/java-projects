package pages;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.TopicDaoImpl;
import pojos.Topic;
import pojos.User;

/**
 * Servlet implementation class TopicsServlet
 */
@WebServlet("/topics")
public class TopicsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		try (PrintWriter pw = response.getWriter()) {
			pw.print("<h5> Login Successful , from topics page....</h5>");
			// Steps 1. Get HttpSession object from WC (existing object , provided cookies
			// are enabled!)
			HttpSession hs = request.getSession();
			System.out.println("from topics page session is new " + hs.isNew());// false
			System.out.println("session id " + hs.getId());// will display SAME JSESSIONID for the SAME client
			// 2. Retrieve user details from the session scope
			// HttpSession API : public Object getAttribute(String attributeName)
			User retrievedUser = (User) hs.getAttribute("clnt_details");
			if (retrievedUser != null) {
				// => session tracking working fine !
				pw.print("<h5> Hello ," + retrievedUser.getName() + "</h5>");
				pw.print("<h5 align='center'> All Available Topics </h5>");
				// get topic dao instance from session scope
				TopicDaoImpl dao = (TopicDaoImpl) hs.getAttribute("topic_dao");
				// invoke topic dao's method , to fetch all topics
				List<Topic> topics = dao.getAllTopics();
				// generate a form dynamically n send it to the clnt
				pw.print("<form action='tutorials' method='get'>");
				pw.print("<h5>");
				for (Topic t : topics)
					pw.print("<input type='radio' name='topic_id' value='" + t.getTopicId() + "'/>" + t.getTopicName()
							+ "<br/>");
				pw.print("<input type='submit' value='Choose a Topic'/>");				
				pw.print("</h5>");
				pw.print("</form>");

			} else
				pw.print("<h5> Session Tracking based upon HttpSession Failed : No Cookies!!!!!!!</h5>");
			// send log out link to the clnt
			pw.print("<h5> <a href='logout'>Log Me Out</a></h5>");

		} catch (Exception e) {
			// re throw the exc to WC , by wrapping it ServletException(mesg, rootCause)
			throw new ServletException("err in do-get of " + getClass(), e);
		}
	}

}
