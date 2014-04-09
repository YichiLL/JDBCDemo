package idb;

public class HtmlTemplate {
	static String head="";
	String title;
	
	public HtmlTemplate (String title){
		this.title=title;
	}
	
	public String getHead() {
		String head="<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">\n"
			      + "<html>\n"
			      + "<head> <title>" + this.title + "</title> </head>\n"
			      + "<body bgcolor=\"#FDF5E6\">\n"
			      + "<center>" ;
		return head;
	}
	
}
