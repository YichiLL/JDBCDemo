package idb;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SampleServlet extends HttpServlet {
   public void doGet(HttpServletRequest request,
      HttpServletResponse response)
   throws IOException, ServletException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      out.println("<html>");
      out.println("<head>");
      String title = "Hello World";
      out.println("<title>" + title + "</title>");
      out.println("</head>");
      out.println("<body bgcolor=white>");
      out.println("<h1>" + title + "</h1>");
      String param = request.getParameter("firstName");
                
      if (param != null)
         out.println("Thanks for the lovely param='" + param + "' binding.");
      out.println(param);

      out.println("");
      out.println("");
   }
}
