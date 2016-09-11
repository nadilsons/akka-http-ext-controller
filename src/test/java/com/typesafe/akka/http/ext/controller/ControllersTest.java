package com.typesafe.akka.http.ext.controller;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.Response;

import org.junit.Ignore;
import org.junit.Test;

import com.typesafe.akka.http.ext.controller.test.BaseTest;
import com.typesafe.akka.http.ext.controller.test.support.Dummy;

public class ControllersTest extends BaseTest {

	private final Dummy dummy = new Dummy("example message", "prefix", 0, true);

	///////////////////////////////////////////////////////////////////////////
	// DummyControllerSimple

	@Test
	public void simpleGet() {
		Response response = get("simple/action");
		assertThat(response.getStatus()).isEqualTo(500);
		assertThat(response.readEntity(String.class)).isEqualTo("An error RuntimeException occurred, details: something wrong happens");
	}

	@Test
	public void simplePost() {
		Response response = post("simple/action");
		assertThat(response.getStatus()).isEqualTo(400);
		assertThat(response.readEntity(String.class)).isEqualTo("Hi! I'm simple post action");
	}

	@Test
	public void simplePut() {
		Response response = put("simple/action");
		assertThat(response.getStatus()).isEqualTo(403);
		assertThat(response.readEntity(String.class)).isEqualTo("Hi! I'm simple put action");
	}

	@Test
	public void simpleDelete() {
		Response response = delete("simple/action");
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.readEntity(String.class)).isEqualTo("Hi! I'm simple delete action");
	}

	///////////////////////////////////////////////////////////////////////////
	// DummyControllerWithBasicAuth

	@Test
	public void testWithAuthBasicWithoutCredentials() {
		Response response = get("admin/messages");
		assertThat(response.getStatus()).isEqualTo(401);
	}

	@Test
	public void testWithAuthBasicWithWrongCredentials() {
		Response response = get("admin/messages", "user164", "oldPass");
		assertThat(response.getStatus()).isEqualTo(401);
	}

	@Test
	public void testWithAuthBasic() {
		Response response = get("admin/messages", "user164", "secretPa$$");
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.readEntity(String.class)).isEqualTo("This is a secret. Must be protected");
	}

	///////////////////////////////////////////////////////////////////////////
	// DummyControllerWithJsonBody

	@Test
	public void jsonPost() {
		Response response = post("json/action", dummy);
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.readEntity(String.class)).isEqualTo("Post! The dummy is: " + dummy);
	}

	@Test
	public void jsonPut() {
		Response response = put("json/action", dummy);
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.readEntity(String.class)).isEqualTo("Put! The dummy is: " + dummy);
	}

	///////////////////////////////////////////////////////////////////////////
	// DummyControllerWithPathParams

	@Test
	public void paramsGet() {
		Response response = get("pathparams/get/action/450");
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.readEntity(String.class)).isEqualTo("Get! I was called with: 450");
	}

	@Test
	public void paramsPost() {
		Response response = post("pathparams/post/abc/sys/action");
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.readEntity(String.class)).isEqualTo("Post! I was called with abc and sys");
	}

	@Test
	public void paramsPut() {
		Response response = put("pathparams/30/put/action/afe0b5");
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.readEntity(String.class)).isEqualTo("Put! I was called with 30 and afe0b5");
	}

	@Test
	public void paramsDelete() {
		Response response = delete("152/pathparams/delete/action");
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.readEntity(String.class)).isEqualTo("Delete! I was called with: 152");
	}

	///////////////////////////////////////////////////////////////////////////
	// DummyControllerWithPathParamsAndJsonBody

	@Test
	@Ignore
	public void paramsAndJsonPost() {
		Response response = post("json/pathparams/post/x5/45/action", dummy);
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.readEntity(String.class)).isEqualTo("Post! I was called with x5, 45 and dumy: " + dummy);
	}

	@Test
	@Ignore
	public void paramsAndJsonPut() {
		Response response = put("json/pathparams/90/put/91/action/:index", dummy);
		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.readEntity(String.class)).isEqualTo("Put! I was called with 90, 91 and dumy: " + dummy);
	}
}