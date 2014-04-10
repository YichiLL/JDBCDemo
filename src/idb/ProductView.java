package idb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
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
							+ "</font> left.</font></th><th scope=\"col\"><font size=\"2\" color=\"grey\">Shipping from "
							+ location
							+ ".</font></th></tr><tr><th colspan=\"2\"><font size=\"3\"><em>Review from users</em></font></th></tr></thead>");
					out.println("</table>");

					rset = stmt
							.executeQuery("select DISTINCT AC.BARCODE, WHR.REVIEW, WHR.rdate, U.UNAME "
									+ "from AVAIL_COM AC,WR_HAS_REVIEW WHR,USERS U "
									+ "where WHR.USERID = U.USERID and AC.BARCODE=WHR.BARCODE and AC.BARCODE="
									+ target_com);
					while (rset.next()) {
						out.println(rset.getString("barcode") + " / "
								+ rset.getString("uname") + " / "
								+ rset.getDate("rdate") + "<br />"
								+ rset.getString("review") + "<br />");
					}

				}
				if (product_mode.equals("purchase_product")) {
					if (authority.equals("1")) {
						// TODO add purchase here
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
