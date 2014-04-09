package idb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.jdbc.pool.OracleDataSource;

public class StartPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String connect_string = "jdbc:oracle:thin:tx2135/Coms4111@//w4111b.cs.columbia.edu:1521/ADB";
	private Connection conn;

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
			// template

			ResultSet rset = stmt.executeQuery("select * from users");
			response.setContentType("text/html"); // template

			// template
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
			// template

			String uname = request.getParameter("uname");
			ResultSet rset = stmt
					.executeQuery("select count(*) as user_exist from users where uname='"
							+ uname + "'");
			response.setContentType("text/html");
			out.println(tpl.getHead());
			out.println("<h2>Welcome!</h2>");

			if (rset.next()) {
				int user_exist;
				user_exist = Integer.parseInt(rset.getString("user_exist"));
				if (user_exist == 1) {
					out.println("found user " + uname);
				} else {
					out.println("login failed");
					// TODO: redirect to login page
				}
			}

			out.println("</center></body></html>");

			// template
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
