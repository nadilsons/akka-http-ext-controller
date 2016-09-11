package com.typesafe.akka.http.ext.controller.exception;

public class MultipleHttpVerbException extends RuntimeException {
	
	private static final long serialVersionUID = -1590584606958762547L;

	public MultipleHttpVerbException(String message) {
		super(message);
	}
}