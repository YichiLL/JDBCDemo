package idb;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Logout
 */
public class Logout extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	

		// Set response content type
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();
		String title = "Log out";
		String docType = "<!doctype html public \"-//w3c//dtd html 4.0 "
				+ "transitional//en\">\n";
		out.println(docType + "<html>\n" + "<head><title>" + title
				+ "</title></head>\n" + "<body bgcolor=\"#f0f0f0\">\n");
		
		
		Cookie cookie = null;
		Cookie[] cookies = null;
		// Get an array of Cookies associated with this domain
		cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				cookie = cookies[i];
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
		}
		String site = "login.html";
		response.setStatus(response.SC_MOVED_TEMPORARILY);
		response.setHeader("Location", site);
		out.println("</body>");
		out.println("</html>");
	}
}