package com.typesafe.akka.http.ext.controller.test.support;

import com.typesafe.akka.http.ext.controller.annotation.DELETE;
import com.typesafe.akka.http.ext.controller.annotation.GET;
import com.typesafe.akka.http.ext.controller.annotation.POST;
import com.typesafe.akka.http.ext.controller.annotation.PUT;
import com.typesafe.akka.http.ext.controller.annotation.Path;

import akka.http.javadsl.server.Directives;
import akka.http.javadsl.server.Route;

public class DummyControllerWithPathParams {

	@GET
	@Path("pathparams/get/action/:id")
	public Route simpleGet(String id) {
		return Directives.complete("Get! I was called with: " + id);
	}

	@POST
	@Path("pathparams/post/:id/:anotherId/action")
	public Route simplePost(String id, String anotherId) {
		return Directives.complete(String.format("Post! I was called with %s and %s", id, anotherId));
	}

	@PUT
	@Path("pathparams/:id/put/action/:index")
	public Route simplePut(String id, String index) {
		return Directives.complete(String.format("Put! I was called with %s and %s", id, index));
	}

	@DELETE
	@Path(":id/pathparams/delete/action")
	public Route simpleDelete(String id) {
		return Directives.complete("Delete! I was called with: " + id);
	}
}