package com.typesafe.akka.http.ext.controller.test.support;

import static akka.http.javadsl.model.StatusCodes.BAD_REQUEST;
import static akka.http.javadsl.model.StatusCodes.FORBIDDEN;

import com.typesafe.akka.http.ext.controller.annotation.DELETE;
import com.typesafe.akka.http.ext.controller.annotation.GET;
import com.typesafe.akka.http.ext.controller.annotation.POST;
import com.typesafe.akka.http.ext.controller.annotation.PUT;
import com.typesafe.akka.http.ext.controller.annotation.Path;

import akka.http.javadsl.server.Directives;
import akka.http.javadsl.server.Route;

public class DummyControllerSimple {

	@GET
	@Path("simple/action")
	public Route simpleGet() {
		throw new RuntimeException("something wrong happens");
	}

	@POST
	@Path("simple/action")
	public Route simplePost() {
		return Directives.complete(BAD_REQUEST, "Hi! I'm simple post action");
	}

	@PUT
	@Path("simple/action")
	public Route simplePut() {
		return Directives.complete(FORBIDDEN, "Hi! I'm simple put action");
	}

	@DELETE
	@Path("simple/action")
	public Route simpleDelete() {
		return Directives.complete("Hi! I'm simple delete action");
	}

	/*
	// Maybe later
	@HEAD
	@Path("simple/head/action")
	public Route simpleHead() {
		return Directives.complete("Hi! I'm simple head action");
	}

	@OPTIONS
	@Path("simple/options/action")
	public Route simpleOptions() {
		return Directives.complete("Hi! I'm simple options action");
	}
	*/
}