package com.revature.rest;

import org.glassfish.jersey.server.ResourceConfig;

public class HelloResource extends ResourceConfig {
	public HelloResource() {
		packages("com.revature.rest");
	}
}
