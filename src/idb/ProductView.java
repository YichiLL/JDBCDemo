package idb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.org.apache.bcel.internal.generic.SALOAD;

import oracle.jdbc.pool.OracleDataSource;

/**
 * Servlet implementation class Product
 */
public class ProductView extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// template
	private Connection conn;
	private Statement stmt;

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
			this.stmt = conn.createStatement();
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

					ResultSet rset = stmt
							.executeQuery("select DISTINCT AC.barcode, AC.stock, AC.name, AC.category, SP.SALE_PRICE, W.LOCATION "
									+ "from AVAIL_COM AC,SORDER_PLACEDBY SP,CONTAINS C,STORE_IN SI,WAREHOUSE W where AC.barcode="
									+ target_com
									+ " and  SP.OID=C.OID and SI.wid=W.wid and AC.BARCODE=C.BARCODE and AC.BARCODE=SI.BARCODE");
					String name = "", category = "", sale_price = "", stock = "", location = "";
					if (rset.next()) {
						stock = rset.getString("stock");
						name = rset.getString("name");
						category = rset.getString("category");
						sale_price = rset.getString("SALE_PRICE");
						location = rset.getString("location");
					}
					out.println("<table dir=\"ltr\" width=\"800\" border=\"1\" class=\"default\">"
							+ "<caption><h3>Product Information</h3></caption>	"
							+ "<colgroup width=\"50%\"/>"
							+ "	<colgroup id=\"colgroup\" class=\"colgroup\" align=\"center\" valign=\"middle\" title=\"title\" width=\"1*\" span=\"2\" style=\"\" />"
							+ "	<thead>	<tr><th scope=\"col\"><font size=\"2\" color=\"grey\"> "
							+ category
							+ " -> &nbsp;</font>"
							+ name
							+ "</th><th scope=\"col\"><font size=\"2\" color=\"grey\">Price: </font><font color=\"#F25567\">&nbsp;$"
							+ sale_price
							+ "</font></th></tr><tr><th scope=\"col\"><font color=\"#38B060\">In Stock. <font color=\"#F25567\">"
							+ stock
							+ "</font> left.</font></th><th scope=\"col\"><font size=\"2\" color=\"grey\">Shipping from <font color=\"#F25567\">"
							+ location
							+ "</font>.</font></th></tr><tr><th colspan=\"2\"><font size=\"3\"><em>Review from users</em></font></th></tr></thead><tbody>");

					rset = stmt
							.executeQuery("select DISTINCT AC.BARCODE, WHR.REVIEW, WHR.rdate, U.UNAME "
									+ "from AVAIL_COM AC,WR_HAS_REVIEW WHR,USERS U "
									+ "where WHR.USERID = U.USERID and AC.BARCODE=WHR.BARCODE and AC.BARCODE="
									+ target_com);

					String reviewer_name = "", review = "";
					Date rdate = null;
					while (rset.next()) {
						reviewer_name = rset.getString("uname");
						rdate = rset.getDate("rdate");
						review = rset.getString("review");
						out.println("<tr><td colspan=\"2\"><font size=\"2\"> <font color=\"grey\"><strong>"
								+ reviewer_name
								+ "</strong> on <strong>"
								+ rdate
								+ "</strong> wrote: </font>"
								+ review
								+ "</font></td></tr>");
					}
					out.println("</tbody></table><br/><br/>");

					// out.println("<form method = \"post\" action = \"http://localhost:9080/JDBCDemo/ReadParams\">");
					out.println("<h3>Write a review for this product</h3>");
					out.println("<form method = \"post\" action = \"http://localhost:9080/JDBCDemo/ProductView\">");
					out.println("<textarea id=\"id_remarks\" name=\"review\" rows=5 cols=60>Please leave your review here.</textarea><br/>");
					out.println("<br/><input type=\"submit\" name=\"submit_review\" value=\"Submit\" onclick=\"verify()\">");

					out.println("<script>");
					out.println("function verify(){");
					out.println("if(!document.getElementById('id_remarks').value.trim().length){");
					out.println("alert(\"Please enter the review\");return false;}}");
					out.println("</script>");

					out.println("<input type=\"hidden\" name=\"target_com\" value=\""
							+ target_com
							+ "\"><input type=\"hidden\" name=\"view_product\" value=\"redirect\"><input type=\"hidden\" name=\"userid\" value=\""
							+ userid + "\">" + "</form><br/><br/>");

					// buying products
					rset = stmt
							.executeQuery("select DISTINCT PAYMENTMETHOD from SORDER_PLACEDBY");
					ArrayList<String> paymentmethods = new ArrayList<String>();
					while (rset.next()) {
						paymentmethods.add(rset.getString("paymentmethod"));
					}
					out.println("<h3>Please specify order information</h3>");
					// out.println(" <form method = \"get\" action = \"http://localhost:9080/JDBCDemo/ReadParams\"> ");
					out.println(" <form method = \"post\" action = \"http://localhost:9080/JDBCDemo/ProductView\"> ");

					out.println("Quantity : <input type=\"number\" name=\"amount\" min=\"1\" max=\"100\">");
					out.println("&nbsp;&nbsp;Payment method : <select name=\"payment\"> ");
					for (String payment : paymentmethods) {
						out.println("  <option value=\" " + payment + " \"> "
								+ payment + " </option> ");
					}
					out.println("</select><br/><br/>");
					out.println("<input type=\"submit\" name=\"submit_order\" value=\"Place Order\">");
					out.println("<input type=\"hidden\" name=\"target_com\" value=\""
							+ target_com
							+ "\"><input type=\"hidden\" name=\"view_product\" value=\"redirect\"><input type=\"hidden\" name=\"userid\" value=\""
							+ userid
							+ "\">"
							+ "<input type=\"hidden\" name=\"sale_price\" value=\""
							+ sale_price
							+ "\">"
							+ "<input type=\"hidden\" name=\"stock\" value=\""
							+ stock + "\">");
					out.println("</form>");

				}
				if (product_mode.equals("purchase_product")) {
					if (authority.equals("1")) {

						Cookie c_barcode = new Cookie("target_com", target_com);
						c_barcode.setMaxAge(60 * 60 * 24);
						response.addCookie(c_barcode);

						String site = "Purchase";
						response.setStatus(response.SC_MOVED_TEMPORARILY);
						response.setHeader("Location", site);

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
			out.println("<br/><br/><br/><a href=\"StartPage\">&lt;&lt;Go Back</a>&nbsp;&nbsp;<a href=\"Logout\">[ Log Out ]</a>");
			out.println("</center></body></html>");
		} catch (SQLException e) {
			out.println("<br/><br/><a href=\"StartPage\">&lt;&lt;Go Back</a>&nbsp;&nbsp;<a href=\"Logout\">[ Log Out ]</a>");
			out.println("<br/>Thanks.");
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
		// template
		PrintWriter out = new PrintWriter(response.getOutputStream());
		try {
			DB_Conn c = new DB_Conn();
			this.conn = c.getConnection();
			HtmlTemplate tpl = new HtmlTemplate("OnlineStore - Write Review",
					"Online Store");
			this.stmt = conn.createStatement();
			response.setContentType("text/html");
			out.println(tpl.getHead());
			out.println(tpl.getHeadline());
			// template
			int flag = 0;

			// get mode and form data
			String target_com = "", product_mode = "", userid = "", review = "";
			String payment = "", sale_price = "";
			int amount = 0, stock = 0;
			Enumeration paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()) {
				String paramName = (String) paramNames.nextElement();
				if (paramName.equals("target_com"))
					target_com = request.getParameter("target_com");
				if (paramName.equals("userid"))
					userid = request.getParameter("userid");
				if (paramName.equals("review"))
					review = request.getParameter("review");
				if (paramName.equals("submit_review"))
					product_mode = "submit_review";
				if (paramName.equals("submit_order"))
					product_mode = "submit_order";
				if (paramName.equals("submit_purchase"))
					product_mode = "submit_purchase";
				if (paramName.equals("sale_price"))
					sale_price = request.getParameter("sale_price");
				if (paramName.equals("payment"))
					payment = request.getParameter("payment");
				if (paramName.equals("stock"))
					stock = Integer.parseInt(request.getParameter("stock"));

				if (product_mode.equals("submit_order")) {
					String[] paramValues = request.getParameterValues("amount");
					String paramValue = paramValues[0];
					if (paramValue.length() == 0) {
						flag=2;
					} else
						amount = Integer.parseInt(paramValue);
				}
			}

			if (!target_com.equals("") & !product_mode.equals("")) {
				if (product_mode.equals("submit_review")) {
					ResultSet rset = stmt
							.executeQuery("select max(rid) as max_rid from WR_HAS_REVIEW");
					int max_rid = -2;
					if (rset.next()) {
						max_rid = rset.getInt("max_rid");
					}
					java.util.Date today = new java.util.Date();
					SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
							"dd-MMM-yy");
					String rdate = DATE_FORMAT.format(today);
					PreparedStatement p = conn
							.prepareStatement("INSERT INTO WR_HAS_REVIEW (RID,RDATE,REVIEW,BARCODE,USERID) VALUES (?,?,?,?,?)");
					p.setInt(1, max_rid + 1);
					p.setString(2, rdate);
					p.setString(3, review);
					p.setString(4, target_com);
					p.setString(5, userid);
					ResultSet rs = p.executeQuery();
				}

				else if (product_mode.equals("submit_order")) {

					ResultSet rset = stmt
							.executeQuery("select max(oid) as max_oid from sorder_placedby");
					int max_oid = -2;
					if (rset.next()) {
						max_oid = rset.getInt("max_oid");
					}
					java.util.Date today = new java.util.Date();
					SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
							"dd-MMM-yy");
					String rdate = DATE_FORMAT.format(today);

					// out.println(amount + " / " + target_com + " / " + payment
					// + " / " + sale_price + " / " + userid+ " / " + rdate+
					// " / " + max_oid+" / "+stock);

					if (stock - amount >= 0) {
						rset = stmt.executeQuery("update avail_com set stock="
								+ Integer.toString(stock - amount)
								+ " where barcode=" + target_com);
					} else {
						out.println("<script>alert(\"Sorry, it seems like we don't have that many in stock.\")</script>");
						flag=1;
					}
				} else if (product_mode.equals("submit_purchase")) {

					// Not used any more

				} else {
					out.println("<a href=StartPage>Sorry, you do not have permission to access.<br>"
							+ "Click to go back.</a>");
				}
			}

			else {
				out.println("<a href=StartPage>Please make a selection.<br>"
						+ "Click to go back.</a>");
			}

			// close this when debugging
			if (flag == 0) {
				this.doGet(request, response);
			} else if (flag==2){out.println("<script>alert(\"Please enter a number.\")</script>");
			out.println("<br/><br/><a href=\"StartPage\">&lt;&lt;Go Back</a>&nbsp;&nbsp;<a href=\"Logout\">[ Log Out ]</a>");} 
			else {
				out.println("Please choose again.");
			}

			// template
			out.println("<br/><br/><br/><a href=\"StartPage\">&lt;&lt;Go Back</a>&nbsp;&nbsp;<a href=\"Logout\">[ Log Out ]</a>");
			out.println("</center></body></html>");
		} catch (SQLException e) {
			out.println("<br/>Please enter a valid review.");
			out.println("<br/><br/><a href=\"StartPage\">&lt;&lt;Go Back</a>&nbsp;&nbsp;<a href=\"Logout\">[ Log Out ]</a>");
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
