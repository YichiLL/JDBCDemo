package idb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
			HtmlTemplate tpl = new HtmlTemplate("OnlineStore - Start Page");
			Statement stmt = conn.createStatement();
			response.setContentType("text/html");
			// template

			// ResultSet rset = stmt.executeQuery("select * from users");
			out.println(tpl.getHead());
			out.println("<h2>Welcome!</h2>");
			Cookie[] cookies = null;
			cookies = request.getCookies();
			if( cookies != null ){
		         out.println("<h2> Found Cookies Name and Value</h2>");
		         for (int i = 0; i < cookies.length; i++){
		            Cookie cookie = cookies[i];
		            out.print("Name : " + cookie.getName( ) + ",  ");
		            out.print("Value: " + cookie.getValue( )+" <br/>");
		         }
		      }else{
		          out.println(
		            "<h2>No cookies founds</h2>");
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

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// template
		PrintWriter out = new PrintWriter(response.getOutputStream());
		try {
			DB_Conn c = new DB_Conn();
			this.conn = c.getConnection();
			HtmlTemplate tpl = new HtmlTemplate("OnlineStore - Start Page");
			Statement stmt = conn.createStatement();
			response.setContentType("text/html");
			// template

			String uname = request.getParameter("uname");
			ResultSet rset = stmt
					.executeQuery("select count(*) as user_exist from users where uname='"
							+ uname + "'");
			out.println(tpl.getHead());
			out.println("<h2>Welcome!</h2>");

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
