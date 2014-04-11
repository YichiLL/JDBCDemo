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

/**
 * Servlet implementation class ChooseCategory
 */
public class ChooseCategory extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection conn;
	private Statement stmt;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChooseCategory() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = new PrintWriter(response.getOutputStream());
		try {
			DB_Conn c = new DB_Conn();
			this.conn = c.getConnection();
			HtmlTemplate tpl = new HtmlTemplate(
					"OnlineStore - Choose Category", "Select from Our Categories");
			this.stmt = conn.createStatement();
			response.setContentType("text/html");
			out.println(tpl.getHead());
			out.println(tpl.getHeadline());
			// template

			ResultSet rset = stmt
					.executeQuery("select distinct category from AVAIL_COM");
			Map<String, String> coms = new HashMap<String, String>();
			while (rset.next()) {
				coms.put(rset.getString("category"), rset.getString("category"));
			}

			Cookie[] cookies = null;
			cookies = request.getCookies();
			String uname = "", userid = "", authority = "",category="";
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

			out.println("<h2>Hi, " + uname + "!</h2>");
			out.println("<h3>Choose a category to continue!</h3>");
			// out.println(" <form method = \"get\" action = \"http://localhost:9080/JDBCDemo/ReadParams\"> ");
			// out.println(" <form method = \"get\" action = \"http://localhost:9080/JDBCDemo/ProductView\"> ");
			out.println(" <form method = \"post\" action = \"http://localhost:9080/JDBCDemo/StartPage\"> ");
			out.println(" <select name=\"category\"> ");
			for (Map.Entry<String, String> entry : coms.entrySet()) {
				out.println("  <option value=\" " + entry.getKey() + " \"> "
						+ entry.getValue() + " </option> ");
			}
			out.println("</select>");
			out.println("<INPUT TYPE=\"submit\" name=\"submit_category\" VALUE=\"Go\">");
			out.println("</form>");

			// template
			out.println("<br/><br/><br/><a href=\"login.html\">&lt;&lt;Go Back</a>&nbsp;&nbsp;<a href=\"Logout\">[ Log Out ]</a>");
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// template
		PrintWriter out = new PrintWriter(response.getOutputStream());
		try {
			DB_Conn c = new DB_Conn();
			this.conn = c.getConnection();
			HtmlTemplate tpl = new HtmlTemplate(
					"OnlineStore - Choose Category", "Welcome!");
			this.stmt = conn.createStatement();
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
					rset = stmt
							.executeQuery("select userid, authority from users where uname='"
									+ uname + "'");
					if (rset.next()) {
						String userid = rset.getString("userid");
						Cookie c_uname = new Cookie("uname", uname);
						Cookie c_userid = new Cookie("userid", userid);
						Cookie c_authority = new Cookie("authority",
								rset.getString("authority"));
						c_uname.setMaxAge(60 * 60 * 24);
						c_userid.setMaxAge(60 * 60 * 24);
						c_authority.setMaxAge(60 * 60 * 24);
						response.addCookie(c_uname);
						response.addCookie(c_userid);
						response.addCookie(c_authority);

					} else {
						out.println("<a href=login.html>Database connection error.</a>");
					}

					String site = "ChooseCategory";
					response.setStatus(response.SC_MOVED_TEMPORARILY);
					response.setHeader("Location", site);

				} else {
					out.println("<a href=login.html>Username does not exist. (Case sensitive.)<br>"
							+ "Click to go back.</a>");
				}
			}

			// template
			out.println("<br/><br/><br/><a href=\"login.html\">&lt;&lt;Go Back</a>&nbsp;&nbsp;<a href=\"Logout\">[ Log Out ]</a>");
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
