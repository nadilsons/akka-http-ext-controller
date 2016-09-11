package com.typesafe.akka.http.ext.controller.test.support;

import com.typesafe.akka.http.ext.controller.annotation.BasicAuth;
import com.typesafe.akka.http.ext.controller.annotation.GET;
import com.typesafe.akka.http.ext.controller.annotation.Path;

import akka.http.javadsl.server.Directives;
import akka.http.javadsl.server.Route;

@BasicAuth(user="user164", pass="secretPa$$")
public class DummyControllerWithBasicAuth {

	@GET
	@Path("admin/messages")
	public Route simpleGet() {
		return Directives.complete("This is a secret. Must be protected");
	}
}