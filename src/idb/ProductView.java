package idb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Product
 */
public class ProductView extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// template
	private Connection conn;

	// template

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductView() {
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
		// template
		PrintWriter out = new PrintWriter(response.getOutputStream());
		try {
			DB_Conn c = new DB_Conn();
			this.conn = c.getConnection();
			HtmlTemplate tpl = new HtmlTemplate("OnlineStore - View Product",
					"Online Store");
			Statement stmt = conn.createStatement();
			response.setContentType("text/html");
			out.println(tpl.getHead());
			out.println(tpl.getHeadline());
			// template

			// template
			Cookie[] cookies = null;
			cookies = request.getCookies();
			String uname = "", userid = "", authority = "";
			if (cookies != null) {
				for (int i = 0; i < cookies.length; i++) {
					Cookie cookie = cookies[i];
					if (cookie.getName().equals("uname"))
						uname = cookie.getValue();
					if (cookie.getName().equals("userid"))
						userid = cookie.getValue();
					if (cookie.getName().equals("authority"))
						authority = cookie.getValue();
				}
			} else {
				String site = "login.html";
				response.setStatus(response.SC_MOVED_TEMPORARILY);
				response.setHeader("Location", site);
			}
			// out.println("<br/>" + uname + " / " + userid + " / " + authority
			// + "<br/>");
			// template

			String target_com = "", product_mode = "";
			Enumeration paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()) {
				String paramName = (String) paramNames.nextElement();
				if (paramName.equals("target_com"))
					target_com = request.getParameter("target_com");
				if (paramName.equals("view_product"))
					product_mode = "view_product";
				if (paramName.equals("purchase_product"))
					product_mode = "purchase_product";
			}

			if (!target_com.equals("") & !product_mode.equals("")) {
				if (product_mode.equals("view_product")) {

				}
				if (product_mode.equals("purchase_product")) {
					if (authority.equals("1")) {

					} else {
						out.println("<a href=StartPage>Sorry, you do not have permission to access.<br>"
								+ "Click to go back.</a>");
					}
				}
			} else {
				out.println("<a href=StartPage>Please select a product.<br>"
						+ "Click to go back.</a>");
			}

			// template
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
	}

}
