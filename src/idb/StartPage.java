package idb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
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
	private Statement stmt;

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
					"Choose a Product to See More");
			this.stmt = conn.createStatement();
			response.setContentType("text/html");
			out.println(tpl.getHead());
			out.println(tpl.getHeadline());
			// template

			// // 2B shanchu
			// Cookie cookie1 = null;
			// Cookie[] cookies1 = null;
			// // Get an array of Cookies associated with this domain
			// cookies1 = request.getCookies();
			// if (cookies1 != null) {
			// for (int i = 0; i < cookies1.length; i++) {
			// cookie1 = cookies1[i];
			// out.println(cookie1.getName());
			// out.println("<br/>");
			// out.println(cookie1.getValue());
			// out.println("<br/>");
			// }
			// }

			Cookie[] cookies = null;
			cookies = request.getCookies();
			String uname = "", userid = "", authority = "", category = "";
			if (cookies != null) {
				for (int i = 0; i < cookies.length; i++) {
					Cookie cookie = cookies[i];
					if (cookie.getName().equals("uname"))
						uname = cookie.getValue();
					if (cookie.getName().equals("userid"))
						userid = cookie.getValue();
					if (cookie.getName().equals("authority"))
						authority = cookie.getValue();
					if (cookie.getName().equals("category"))
						category = cookie.getValue();
				}
			}

			ResultSet rset = stmt
					.executeQuery("select * from avail_com where category='"
							+ category + "'");
			Map<String, String> coms = new HashMap<String, String>();
			while (rset.next()) {
				coms.put(rset.getString("barcode"), rset.getString("name"));
				// out.println("get "+rset.getString("barcode")+
				// rset.getString("name"));
			}
			
			out.println("<h2>Hi, " + uname + "!</h2>");
			out.println("<h3>Customer: Choose a product to continue!</h3>");
			// out.println(" <form method = \"get\" action = \"http://localhost:9080/JDBCDemo/ReadParams\"> ");
			out.println(" <form method = \"get\" action = \"http://localhost:9080/JDBCDemo/ProductView\"> ");
			// out.println(" <form method = \"post\" action = \"http://localhost:9080/JDBCDemo/ProductView\"> ");

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
					+ "<INPUT TYPE=\"submit\" name=\"purchase_product\" VALUE=\"Purchase from Providers\">"
					+ "</p>");
			
			 out.println("</form>");


			// not implemented now
			// out.println("<h4>or<h4>");
			// rset = stmt.executeQuery("select userid, uname from users");
			// Map<String, String> users = new HashMap<String, String>();
			// while (rset.next()) {
			// users.put(rset.getString("userid"), rset.getString("uname"));
			// }
			// out.println("</form>");
			// out.println("<form method = \"get\" action = \"http://localhost:9080/JDBCDemo/ReadParams\">");
			// out.println("<select name=\"target_user\"> ");
			// for (Map.Entry<String, String> entry : users.entrySet()) {
			// out.println("  <option value=\" " + entry.getKey() + " \"> "
			// + entry.getValue() + " </option> ");
			// }
			// out.println("</select></td><td>");
			// out.println("<INPUT TYPE=\"submit\" name=\"manage_user\" VALUE=\"View user information\">");
			//
			// out.println("</td></tr></table>");
			// out.println("</form>");

			// template
			out.println("<br/><br/><br/><a href=\"ChooseCategory\">&lt;&lt;Go Back</a>&nbsp;&nbsp;<a href=\"Logout\">[ Log Out ]</a>");
			out.println("</center></body></html>");
		} catch (SQLException e) {
			out.println(e.getMessage());
			out.println("e1");
		} catch (Exception e) {
			out.println(e.getMessage());
			out.println("e2");
		} finally {
			try {
				conn.close();
				stmt.close();
			} catch (Exception e) {
				out.println(e.getMessage());
			}
		}
		out.close();
		// template
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = new PrintWriter(response.getOutputStream());
		try {
			DB_Conn c = new DB_Conn();
			this.conn = c.getConnection();
			HtmlTemplate tpl = new HtmlTemplate("OnlineStore - Choose Start",
					"Welcome!");
			this.stmt = conn.createStatement();
			response.setContentType("text/html");
			out.println(tpl.getHead());
			out.println(tpl.getHeadline());
			// template

			String category;
			Enumeration paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()) {
				String paramName = (String) paramNames.nextElement();
				if (paramName.equals("category")) {
					category = request.getParameter("category");
					Cookie c_category = new Cookie("category", category.trim());
					c_category.setMaxAge(60 * 60 * 24);
					response.addCookie(c_category);
				}
			}

			String site = "StartPage";
			response.setStatus(response.SC_MOVED_TEMPORARILY);
			response.setHeader("Location", site);

			// template
			out.println("<br/><br/><br/><a href=\"ChooseCategory\">&lt;&lt;Go Back</a>&nbsp;&nbsp;<a href=\"Logout\">[ Log Out ]</a>");
			out.println("</center></body></html>");
		} catch (SQLException e) {
			out.println(e.getMessage());
			out.println("e1");
		} catch (Exception e) {
			out.println(e.getMessage());
			out.println("e2");
		} finally {
			try {
				conn.close();
				stmt.close();
			} catch (Exception e) {
				out.println(e.getMessage());
			}
		}
		out.close();
		// template

	}
}
