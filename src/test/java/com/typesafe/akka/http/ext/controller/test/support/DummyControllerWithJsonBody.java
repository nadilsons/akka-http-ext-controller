package com.typesafe.akka.http.ext.controller.test.support;

import com.typesafe.akka.http.ext.controller.annotation.POST;
import com.typesafe.akka.http.ext.controller.annotation.PUT;
import com.typesafe.akka.http.ext.controller.annotation.Path;

import akka.http.javadsl.server.Directives;
import akka.http.javadsl.server.Route;

public class DummyControllerWithJsonBody {

	@POST
	@Path("json/action")
	public Route simplePost(Dummy dummy) {
		return Directives.complete("Post! The dummy is: " + dummy);
	}

	@PUT
	@Path("json/action")
	public Route simplePut(Dummy dummy) {
		return Directives.complete("Put! The dummy is: " + dummy);
	}
}