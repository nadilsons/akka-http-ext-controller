package com.typesafe.akka.http.ext.controller;

import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.server.Directives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;

public class Routes {
	
	private Routes() { }
	
	public static void init(ActorSystem system, int port, Object... controllers) {
		final List<Route> routes = new ArrayList<>();

		for (Object controller : controllers) {
			Action.findActions(controller).stream().forEach((action) -> routes.add(action.route()));
		}
		
		Route route = Directives.route(routes.toArray(new Route[routes.size()]));
		
		ActorMaterializer materializer = ActorMaterializer.create(system);
		Http.get(system).bindAndHandle(
			route.flow(system, materializer), 
			ConnectHttp.toHost("0.0.0.0", port), 
		materializer);
	}
}