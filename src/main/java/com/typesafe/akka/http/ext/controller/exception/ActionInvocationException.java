package com.typesafe.akka.http.ext.controller.exception;

public class ActionInvocationException extends RuntimeException {
	
	private static final long serialVersionUID = 4965135803905009335L;

	public ActionInvocationException(Exception cause) {
		super(cause);
	}
}