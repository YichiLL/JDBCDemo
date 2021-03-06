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
 * Servlet implementation class Purchase
 */
public class Purchase extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection conn;
	private Statement stmt;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = new PrintWriter(response.getOutputStream());
		try {
			DB_Conn c = new DB_Conn();
			this.conn = c.getConnection();
			HtmlTemplate tpl = new HtmlTemplate(
					"OnlineStore - Purchase Products",
					"Purchase Products from Suppliers");
			this.stmt = conn.createStatement();
			response.setContentType("text/html");
			out.println(tpl.getHead());
			out.println(tpl.getHeadline());
			// template

//			// shanchu
//			Cookie cookie1 = null;
//			Cookie[] cookies1 = null;
//			cookies1 = request.getCookies();
//			if (cookies1 != null) {
//				for (int i = 0; i < cookies1.length; i++) {
//					cookie1 = cookies1[i];
//					out.println(cookie1.getName());
//					out.println("<br/>");
//					out.println(cookie1.getValue());
//					out.println("<br/>");
//				}
//			}

			Cookie[] cookies = null;
			cookies = request.getCookies();
			String uname = "", userid = "", authority = "", category = "", target_com = "";
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
					if (cookie.getName().equals("target_com"))
						target_com = cookie.getValue();
				}
			}

			ResultSet rset = stmt
					.executeQuery("select distinct AC.CATEGORY, AC.NAME, S.SNAME, PP.PRICE, P.DAY, P.AMOUNT from PURCHASE P, PORDER_PROVIDEDBY PP, SUPPLIER S, AVAIL_COM AC where PP.PID=P.PID and AC.BARCODE=P.BARCODE and S.sid=PP.sid and ac.barcode="
							+ target_com);
			out.println("<table dir=\"ltr\" width=\"700\" border=\"1\" class=\"default\"><caption><h3>Purchase Order History</h3></caption><colgroup id=\"colgroup\" class=\"colgroup\" align=\"center\" valign=\"middle\" title=\"title\" width=\"1*\" span=\"2\" style=\"\" /><thead><tr><th scope=\"col\">Category</th><th scope=\"col\">Product</th><th scope=\"col\">Supplier</th><th scope=\"col\">Purchase Price</th><th scope=\"col\">Date</th><th scope=\"col\">Purchase Amount</th></tr></thead>");
			out.println("<tbody>");
			while (rset.next()) {
				out.println("<tr>");
				out.println("<td>" + rset.getString("CATEGORY") + "</td>");
				out.println("<td>" + rset.getString("NAME") + "</td>");
				out.println("<td>" + rset.getString("SNAME") + "</td>");
				out.println("<td>$ " + rset.getString("PRICE") + "</td>");
				out.println("<td>" + rset.getDate("DAY") + "</td>");
				out.println("<td>" + rset.getString("AMOUNT") + "</td>");
				out.println("</tr>");
			}
			;
			out.println("</tbody></table>");

			// template
			out.println("<br/><br/><br/><a href=\"StartPage\">&lt;&lt;Go Back</a>&nbsp;&nbsp;<a href=\"Logout\">[ Log Out ]</a>");
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
	}

}
