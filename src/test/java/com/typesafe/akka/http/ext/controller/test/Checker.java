package com.typesafe.akka.http.ext.controller.test;

import static org.assertj.core.api.Assertions.assertThat;

import com.typesafe.akka.http.ext.controller.Action;
import com.typesafe.akka.http.ext.controller.Verb;

public class Checker {

	private final Action action;

	public Checker(Action action) {
		this.action = action;
	}

	public void isEqualTo(Verb verb, String path) {
		assertThat(action.getVerb()).isEqualTo(verb);
		assertThat(action.getPath()).isEqualTo(path);
	}
}