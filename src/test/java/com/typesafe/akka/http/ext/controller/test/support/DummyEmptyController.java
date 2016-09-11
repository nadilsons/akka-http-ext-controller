package com.typesafe.akka.http.ext.controller.test.support;

import akka.http.javadsl.server.Route;

public class DummyEmptyController {

	public Route empty() {
		// this is not a valid action because doesnt have @Path and @HttpVerb
		return null;
	}
}