package com.typesafe.akka.http.ext.controller.exception;

public class UnkownHttpVerbException extends RuntimeException {
	
	private static final long serialVersionUID = 4573079466777701047L;

	public UnkownHttpVerbException() {
		super("Unkown http verb");
	}
}