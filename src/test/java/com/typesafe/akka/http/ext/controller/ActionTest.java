package com.typesafe.akka.http.ext.controller;

import static com.typesafe.akka.http.ext.controller.Verb.HTTP_DELETE;
import static com.typesafe.akka.http.ext.controller.Verb.HTTP_GET;
import static com.typesafe.akka.http.ext.controller.Verb.HTTP_POST;
import static com.typesafe.akka.http.ext.controller.Verb.HTTP_PUT;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.Test;

import com.typesafe.akka.http.ext.controller.exception.MultipleHttpVerbException;
import com.typesafe.akka.http.ext.controller.exception.NoPathFoundException;
import com.typesafe.akka.http.ext.controller.test.BaseTest;
import com.typesafe.akka.http.ext.controller.test.support.DummyControllerSimple;
import com.typesafe.akka.http.ext.controller.test.support.DummyControllerWithJsonBody;
import com.typesafe.akka.http.ext.controller.test.support.DummyControllerWithPathParams;
import com.typesafe.akka.http.ext.controller.test.support.DummyControllerWithPathParamsAndJsonBody;
import com.typesafe.akka.http.ext.controller.test.support.DummyEmptyController;
import com.typesafe.akka.http.ext.controller.test.support.DummyProblematicController;

public class ActionTest extends BaseTest {

	@Test
	public void testForDummyControllerSimple() {
		List<Action> actions = Action.findActions(new DummyControllerSimple());

		assertThat(actions.size()).isEqualTo(4);
		assertAction(actions.get(0)).isEqualTo(HTTP_GET, "simple/action");
		assertAction(actions.get(1)).isEqualTo(HTTP_POST, "simple/action");
		assertAction(actions.get(2)).isEqualTo(HTTP_PUT, "simple/action");
		assertAction(actions.get(3)).isEqualTo(HTTP_DELETE, "simple/action");
	}

	@Test
	public void testForDummyControllerWithJsonBody() {
		List<Action> actions = Action.findActions(new DummyControllerWithJsonBody());

		assertThat(actions.size()).isEqualTo(2);
		assertAction(actions.get(0)).isEqualTo(HTTP_POST, "json/action");
		assertAction(actions.get(1)).isEqualTo(HTTP_PUT, "json/action");
	}

	@Test
	public void testForDummyControllerWithPathParams() {
		List<Action> actions = Action.findActions(new DummyControllerWithPathParams());

		assertThat(actions.size()).isEqualTo(4);
		assertAction(actions.get(0)).isEqualTo(HTTP_GET, "pathparams/get/action/:id");
		assertAction(actions.get(1)).isEqualTo(HTTP_POST, "pathparams/post/:id/:anotherId/action");
		assertAction(actions.get(2)).isEqualTo(HTTP_PUT, "pathparams/:id/put/action/:index");
		assertAction(actions.get(3)).isEqualTo(HTTP_DELETE, ":id/pathparams/delete/action");
	}

	@Test
	public void testForDummyControllerWithPathParamsAndJsonBody() {
		List<Action> actions = Action.findActions(new DummyControllerWithPathParamsAndJsonBody());

		assertThat(actions.size()).isEqualTo(2);
		assertAction(actions.get(0)).isEqualTo(HTTP_POST, "json/pathparams/post/:id/:anotherId/action");
		assertAction(actions.get(1)).isEqualTo(HTTP_PUT, "json/pathparams/:id/put/:anotherId/action/:index");
	}

	@Test
	public void testForDummyEmptyController() {
		assertThat(Action.findActions(new DummyEmptyController()).isEmpty()).isTrue();
	}

	@Test
	public void testForDummyProblematicController() {
		assertThatThrownBy(() -> { Action.findActions(new DummyProblematicController.ActionWithoutPath()); })
			.isInstanceOf(NoPathFoundException.class)
			.hasMessage("invalid action configure: must have @Path annotation");
		
		//Action.findActions(new DummyProblematicController.ActionWithoutHttpVerb());
		
		assertThatThrownBy(() -> { Action.findActions(new DummyProblematicController.ActionWithMultiplesHttpVerb()); })
			.isInstanceOf(MultipleHttpVerbException.class)
			.hasMessage("multiple annotations found: GET, POST");
	}
}