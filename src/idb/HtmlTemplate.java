package idb;

public class HtmlTemplate {
	static String head="";
	String title;
	String headline;
	
	public HtmlTemplate (String title, String headline){
		this.title=title;
		this.headline=headline;
	}
	
	public String getHead() {
		String head="<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">\n"
			      + "<html>\n"
			      + "<head> <title>" + this.title + "</title> </head>\n"
			      + "<body bgcolor=\"#FDF5E6\">\n"
			      + "<center>" ;
		return head;
	}
	
	public String getHeadline() {
		String headline="<h1 style=\"font-family:Cursive\"><strong>"+this.headline+"</strong></h1><br />" ;
		return headline;
	}
	
}
