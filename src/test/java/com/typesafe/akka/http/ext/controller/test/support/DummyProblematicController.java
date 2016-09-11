package com.typesafe.akka.http.ext.controller.test.support;

import com.typesafe.akka.http.ext.controller.annotation.GET;
import com.typesafe.akka.http.ext.controller.annotation.POST;
import com.typesafe.akka.http.ext.controller.annotation.Path;

import akka.http.javadsl.server.Route;

public abstract class DummyProblematicController {

	public static class ActionPathAlreadyExistsInAnotherController {
		@GET @Path("simple/action") public Route actionWithoudPath() { return null; }
	}

	public static class ActionWithoutPath {
		@GET public Route actionWithoudPath() { return null; }
	}

	public static class ActionWithoutHttpVerb {
		@Path("/test") public Route actionWithoudPath() { return null; }
	}

	public static class ActionWithMultiplesHttpVerb {
		@GET @POST public Route actionWithoudPath() { return null; }
	}
}