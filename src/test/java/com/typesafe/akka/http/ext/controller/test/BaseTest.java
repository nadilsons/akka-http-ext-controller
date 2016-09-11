package com.typesafe.akka.http.ext.controller.test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.BeforeClass;

import com.typesafe.akka.http.ext.controller.Action;
import com.typesafe.akka.http.ext.controller.Routes;
import com.typesafe.akka.http.ext.controller.test.support.DummyControllerSimple;
import com.typesafe.akka.http.ext.controller.test.support.DummyControllerWithBasicAuth;
import com.typesafe.akka.http.ext.controller.test.support.DummyControllerWithJsonBody;
import com.typesafe.akka.http.ext.controller.test.support.DummyControllerWithPathParams;
import com.typesafe.akka.http.ext.controller.test.support.DummyControllerWithPathParamsAndJsonBody;
import com.typesafe.akka.http.ext.controller.test.support.DummyEmptyController;

import akka.actor.ActorSystem;

public abstract class BaseTest {

	private final JerseyClient client = new JerseyClientBuilder().build();
	private static ActorSystem system;

	@BeforeClass
	public static final void beforeAllFromBaseTest() {
		if (system == null)
			system = ActorSystem.create();

		Routes.init(system, 1080, 
			new DummyControllerSimple(),
			new DummyControllerWithBasicAuth(),
			new DummyControllerWithJsonBody(),
			new DummyControllerWithPathParams(),
			new DummyControllerWithPathParamsAndJsonBody(),
			new DummyEmptyController()
		);
	}

	public Checker assertAction(Action action) {
		return new Checker(action);
	}

	// http helper methods
	public Response get(String uri) {
		return builder(uri).get();
	}

	public Response get(String uri, String user, String pass) {
		return client.target("http://localhost:1080/" + uri)
			.register(HttpAuthenticationFeature.basic(user, pass))
			.request().get();
	}

	public Response post(String uri) {
		return post(uri, null);
	}

	public Response post(String uri, Object model) {
		Entity<?> entity = model == null ? Entity.text("") : Entity.json(model.toString());
		return builder(uri).post(entity);
	}

	public Response put(String uri) {
		return put(uri, null);
	}

	public Response put(String uri, Object model) {
		Entity<?> entity = model == null ? Entity.text("") : Entity.json(model.toString());
		return builder(uri).put(entity);
	}

	public Response delete(String uri) {
		return builder(uri).delete();
	}

	private Builder builder(String uri) {
		return client.target("http://localhost:1080/" + uri)
			//.register(HttpAuthenticationFeature.basic(user, pass))
			.request();
	}
}