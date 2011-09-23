package net.lr.tutorial.configadmin;


public class MyApp {
	String title;
	
	public void setTitle(String	title) {
		this.title = title;
	}

	public void refresh() {
		System.out.println("Configuration updated (title=" + title + ")");
	}
}
