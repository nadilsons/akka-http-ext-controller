package com.typesafe.akka.http.ext.controller.exception;

public class NoPathFoundException extends RuntimeException {

	private static final long serialVersionUID = 7925941633761386067L;
	
	public NoPathFoundException() {
		super("invalid action configure: must have @Path annotation");
	}
}