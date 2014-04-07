import java.io.*;
import java.util.*;

public class HTMLParser
{
    private String content;

    public HTMLParser()
    {
	content = null;
    }

    public HTMLParser(String content)
    {
	this.content = content;
    }

    public String getContent() { return content;}
    public void setContent(String newContent) { content = newContent;}

    public void parse(PrintWriter out, String jScript, String title)
   {
	out.println("<html>");
	out.println("<head>");
	
	if(jScript != null)
	    {
		out.println("<script language=\"Javascript\">");
		out.println("<!-- //begin hiding");
		out.println(jScript);
		out.println("//end hiding -->");
	    }

	out.println("<title>" + title + "</title>");
	out.println("</head>");

	out.println("<body bgcolor=\"white\">");
	out.println("<table cellspacing=0 cellpadding=0 width=\"800\">");

	out.println("<table width=800>");

	out.println("<tr>");
	out.println("<td>");

	out.println("<table width=15%><tr><td>");
	out.println("<a href=\"Artist\">Artist Pages</a><br>");
	out.println("</td></tr>");

	out.println("<tr><td>");
	out.println("<a href=\"Album\">Album Pages</a><br>");
	out.println("</td></tr>");

	out.println("<tr><td>");
	out.println("<a href=\"Musician\">Musician Pages</a><br>");
	out.println("</td></tr>");

	out.println("<tr><td>");
	out.println("<a href=\"Tab\">Tablature</a><br>");
	out.println("</td></tr>");

	out.println("<tr><td>");
	out.println("<a href=\"RC\">Record Company Pages</a><br>");
	out.println("</td></tr></table></td>");

	out.println("<td align=left>");

	if (content != null) out.println(content);

	out.println("</td>");
	out.println("</tr>");

	out.println("<tr valign=\"bottom\"><td colspan=2 align=\"center\">");
	out.println("<a href=\"pdamusic\">Home</a>");
	out.println("</td></tr>");

	out.println("</table>");
	out.println("</body>");
	
	out.println("</html>");
    }

    public void outputError(Exception e, PrintWriter out)
    {
	setContent("<h1>Error:</h1><p>\n" + e.getMessage());
	parse(out, null, "ERROR");
	System.exit(-1);
    }


    public String insertPic(String name, String picType)
    {
	try {
	    name = name.toLowerCase();

	    String path = "/afs/ir.stanford.edu/users/s/e/sevls/" +
		"WWW/images/" + name + ".jpg";

	    new FileReader(path);

	    return ("<td align=\"right\">" +
		    "<img src=\"http://www.stanford.edu/~sevls/images/"
		    + name + ".jpg\"></td>\n");
	}
	catch (FileNotFoundException e) {
	    return ("<td align=\"right\">Would you like a picture " +
		    "of your " + picType + " here?<br>\n"
		    + "email one to the <a href=\"mailto:" +
		    "sevls@leland.stanford.edu\">Webmaster</a></td>\n");
	}    
    }
}