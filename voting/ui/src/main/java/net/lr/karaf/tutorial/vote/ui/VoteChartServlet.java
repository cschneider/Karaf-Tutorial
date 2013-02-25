package net.lr.karaf.tutorial.vote.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VoteChartServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 34992072289535683L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter writer = resp.getWriter();
		InputStream is = VoteChartServlet.class.getClassLoader().getResourceAsStream("/index.html");
	    while (is.available() > 0) {
	        writer.write(is.read());
	    }
	    is.close();
	}


}
