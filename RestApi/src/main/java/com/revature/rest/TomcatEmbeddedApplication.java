package com.revature.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.servlet.ServletContainer;

public class TomcatEmbeddedApplication {

	public static void main(String[] args) throws ServletException, LifecycleException {
		
	
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(4242);
		Context context = tomcat.addWebapp("/rest", new File(".").getAbsolutePath());
		ServletContainer servletContainer = new ServletContainer(new HelloResource());
		tomcat.addServlet("/rest", "helloresource", servletContainer);
		context.addServletMapping("/v1/*", "helloresource");
		tomcat.start();
		tomcat.getServer().await();
		

}
}