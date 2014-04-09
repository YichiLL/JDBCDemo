package idb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.jdbc.pool.OracleDataSource;

public class StartPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// template
	private Connection conn;

	// template

	public StartPage() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// template
		PrintWriter out = new PrintWriter(response.getOutputStream());
		try {
			DB_Conn c = new DB_Conn();
			this.conn = c.getConnection();
			HtmlTemplate tpl = new HtmlTemplate("OnlineStore - Start Page",
					"Welcome!");
			Statement stmt = conn.createStatement();
			response.setContentType("text/html");
			out.println(tpl.getHead());
			out.println(tpl.getHeadline());
			// template

			ResultSet rset = stmt.executeQuery("select * from avail_com");
			Map<String, String> coms = new HashMap<String, String>();
			while (rset.next()) {
				coms.put(rset.getString("barcode"), rset.getString("name"));
			}
			out.println("<h3>Customer: Choose a product to continue!</h3>");
			out.println(" <form method = \"get\" action = \"http://localhost:9080/JDBCDemo/ReadParams\"> ");

			out.println(" <select name=\"target_com\"> ");
			for (Map.Entry<String, String> entry : coms.entrySet()) {
				out.println("  <option value=\" " + entry.getKey() + " \"> "
						+ entry.getValue() + " </option> ");
			}
			out.println("</select>");
			out.println("<p>"
					+ "View Product Detail : "
					+ "<INPUT TYPE=\"submit\" name=\"view_product\" VALUE=\"Go\">"
					+ "</p>");

			out.println("</br><h3>Manager:</h3>"
					+ "<p>Choose from above and</p><p>"
					+ "<INPUT TYPE=\"submit\" name=\"purchase\" VALUE=\"Purchase from Providers\">"
					+ "</p>" + "<h4>or<h4>");

			rset = stmt.executeQuery("select userid, uname from users");
			Map<String, String> users = new HashMap<String, String>();
			while (rset.next()) {
				users.put(rset.getString("userid"), rset.getString("uname"));
			}
			out.println(" <select name=\"target_user\"> ");
			for (Map.Entry<String, String> entry : users.entrySet()) {
				out.println("  <option value=\" " + entry.getKey() + " \"> "
						+ entry.getValue() + " </option> ");
			}
			out.println("</select></td><td>");
			out.println("<INPUT TYPE=\"submit\" name=\"view_user\" VALUE=\"View user information\">");

			out.println("</td></tr></table>");
			out.println("</form>");

			// template
			Cookie[] cookies = null;
			cookies = request.getCookies();
			String uname = "", userid = "";
			if (cookies != null) {
				for (int i = 0; i < cookies.length; i++) {
					Cookie cookie = cookies[i];
					if (cookie.getName().equals("uname"))
						uname = cookie.getValue();
					if (cookie.getName().equals("userid"))
						userid = cookie.getValue();
				}
			}
//			out.println("<br/>"+uname+" / " +userid);
			// template

			
			
			// template
			out.println("</center></body></html>");
		} catch (SQLException e) {
			out.println(e.getMessage());
			out.println(1);
		} catch (Exception e) {
			out.println(e.getMessage());
			out.println(2);
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
				out.println(e.getMessage());
			}
		}
		out.close();
		// template
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// template
		PrintWriter out = new PrintWriter(response.getOutputStream());
		try {
			DB_Conn c = new DB_Conn();
			this.conn = c.getConnection();
			HtmlTemplate tpl = new HtmlTemplate("OnlineStore - Start Page",
					"Welcome!");
			Statement stmt = conn.createStatement();
			response.setContentType("text/html");
			out.println(tpl.getHead());
			out.println(tpl.getHeadline());
			// template

			String uname = request.getParameter("uname");
			ResultSet rset = stmt
					.executeQuery("select count(*) as user_exist from users where uname='"
							+ uname + "'");

			if (rset.next()) {
				int user_exist = Integer.parseInt(rset.getString("user_exist"));
				if (user_exist == 1) {
					out.println("found user " + uname);
					rset = stmt
							.executeQuery("select userid from users where uname='"
									+ uname + "'");
					if (rset.next()) {
						String userid = rset.getString("userid");
						Cookie c_uname = new Cookie("uname", uname);
						Cookie c_userid = new Cookie("userid", userid);
						c_uname.setMaxAge(60 * 60 * 24);
						c_userid.setMaxAge(60 * 60 * 24);
						response.addCookie(c_uname);
						response.addCookie(c_userid);
						String site = "StartPage";
						response.setStatus(response.SC_MOVED_TEMPORARILY);
						response.setHeader("Location", site);
					} else {
						out.println("<a href=login.html>Database connection error.</a>");
					}

				} else {
					out.println("<a href=login.html>Username does not exist. (Case sensitive.)<br>"
							+ "Click to go back.</a>");
				}
			}

			// template
			out.println("</center></body></html>");
		} catch (SQLException e) {
			out.println(e.getMessage());
			out.println(1);
		} catch (Exception e) {
			out.println(e.getMessage());
			out.println(2);
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
				out.println(e.getMessage());
			}
		}
		out.close();
		// template

	}
}
