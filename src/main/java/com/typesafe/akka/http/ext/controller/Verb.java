package com.typesafe.akka.http.ext.controller;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.typesafe.akka.http.ext.controller.annotation.DELETE;
import com.typesafe.akka.http.ext.controller.annotation.GET;
import com.typesafe.akka.http.ext.controller.annotation.POST;
import com.typesafe.akka.http.ext.controller.annotation.PUT;
import com.typesafe.akka.http.ext.controller.exception.UnkownHttpVerbException;

import akka.http.javadsl.server.Directives;
import akka.http.javadsl.server.Route;

public enum Verb {

	HTTP_GET(GET.class), HTTP_POST(POST.class), HTTP_PUT(PUT.class), HTTP_DELETE(DELETE.class);

	private final Class<? extends Annotation> annotationClass;

	private Verb(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}
	
	public static Verb valueOf(Annotation verb) {
		return valueOf("HTTP_" + verb.annotationType().getSimpleName());
	}

	public static List<?> annotationClasses() {
		return Arrays.asList(values()).stream().map((v) -> v.annotationClass).collect(Collectors.toList());
	}

	public Route apply(Supplier<Route> route) {
		switch (this) {
			case HTTP_GET: return wrapper(Directives.get(route));
			case HTTP_POST: return wrapper(Directives.post(route));
			case HTTP_PUT: return wrapper(Directives.put(route));
			case HTTP_DELETE: return wrapper(Directives.delete(route));
			default: throw new UnkownHttpVerbException();
		}
	}

	@Override
	public String toString() {
		return annotationClass.getSimpleName();
	}

	private Route wrapper(Route route) {
		return Directives.pathEndOrSingleSlash(() -> route);
	}
}