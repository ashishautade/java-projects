package pages;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.TopicDaoImpl;
import dao.TutorialDaoImpl;
import dao.UserDaoImpl;
import pojos.User;
import utils.DBUtils;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(value = "/authenticate", loadOnStartup = 1)
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDaoImpl userDao;
	private TopicDaoImpl topicDao;
	private TutorialDaoImpl tutorialDao;

	@Override
	public void init() throws ServletException {

		// user , topic n tutorial dao instances
		try {
			userDao = new UserDaoImpl();
			topicDao=new TopicDaoImpl();
			tutorialDao=new TutorialDaoImpl();
		} catch (Exception e) {
			// To inform WC that init() has failed : re throw the exception , wrapped in
			// ServletException
			// ServletException(String errMesg,Throwable e)
			throw new ServletException("err in init of " + getClass(), e);
		}
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// invoke dao's cleanup
		try {
			userDao.cleanUp();
			topicDao.cleanUp();
			tutorialDao.cleanUp();
			DBUtils.closeConnection();
		} catch (Exception e) {
			// how to tell WC that destroy method failed ?
			throw new RuntimeException("err in destroy of " + getClass(), e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// set content type : resp header
		response.setContentType("text/html");
		// get PW to send response
		try (PrintWriter pw = response.getWriter()) {
			// read req params : email n pwd
			String email = request.getParameter("em");
			String pwd = request.getParameter("pass");
			// LoginServlet invokes dao's CRUD method
			User user = userDao.validateUser(email, pwd);
			// chk null => invalid login --send retry link
			if (user == null)
				pw.print("<h5> Invalid Login Please <a href='login.html'>Retry</a></h5>");
			// not null => successful login : send validated user details to clnt
			else {
//				pw.print("testing redirect scenario");
//				pw.flush();
				// =>login success
				// 1. Get javax.servlet.http.HttpSession(i/f) object from the WC
				// Method of HttpServletRequest : HttpSession getSession()
				HttpSession session=request.getSession();
				System.out.println("from login page session is new "+session.isNew());//true
				System.out.println("Jsession id "+session.getId());
				//2. Save validated user details , under session scope
				//Method of HttpSession : public void setASttribute(String attributeName,Object attaributeValue)
				session.setAttribute("clnt_details",user);
				//save topic n tutorial dao instances in the session scope 
				session.setAttribute("topic_dao", topicDao);
				session.setAttribute("tutorial_dao", tutorialDao);
				// automatically redirect the client to the topics page
				// API of HttpServletResponse
				// Method : public void sendRedirect(String redirectLocation) throws IOException
				response.sendRedirect("topics");
				// WC : sends temp redirect response
				// resp : SC 302 | header : location =topics,set-cookie : user_details :
				// tostring | body : EMPTY
				// web browser : sends NEW request
				// URL : http://host:port/day4.2/topics
				// HTTP method : GET
				// request header : cookie : user_details : tostring
				// add a TopicsServlet : with /topics
			}
		} catch (Exception e) {
			// re throw the exception to caller (WC)
			throw new ServletException("err in do-post of " + getClass(), e);
		}
	}

}
