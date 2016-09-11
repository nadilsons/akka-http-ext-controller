package com.typesafe.akka.http.ext.controller.test.support;

import com.typesafe.akka.http.ext.controller.annotation.POST;
import com.typesafe.akka.http.ext.controller.annotation.PUT;
import com.typesafe.akka.http.ext.controller.annotation.Path;

import akka.http.javadsl.server.Directives;
import akka.http.javadsl.server.Route;

public class DummyControllerWithPathParamsAndJsonBody {

	@POST
	@Path("json/pathparams/post/:id/:anotherId/action")
	public Route simplePost(String id, String anotherId, Dummy dummy) {
		return Directives.complete(String.format("Post! I was called with %s, %s and dumy: %s", id, anotherId, dummy));
	}

	@PUT
	@Path("json/pathparams/:id/put/:anotherId/action/:index")
	public Route simplePut(String id, String anotherId, Dummy dummy) {
		return Directives.complete(String.format("Put! I was called with %s, %s and dumy: %s", id, anotherId, dummy));
	}
}