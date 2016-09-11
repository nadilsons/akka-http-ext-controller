package com.typesafe.akka.http.ext.controller;

import static akka.http.javadsl.model.StatusCodes.INTERNAL_SERVER_ERROR;
import static akka.http.javadsl.server.Directives.complete;
import static akka.http.javadsl.unmarshalling.StringUnmarshallers.STRING;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.typesafe.akka.http.ext.controller.annotation.BasicAuth;
import com.typesafe.akka.http.ext.controller.annotation.Path;
import com.typesafe.akka.http.ext.controller.exception.ActionInvocationException;
import com.typesafe.akka.http.ext.controller.exception.MultipleHttpVerbException;
import com.typesafe.akka.http.ext.controller.exception.NoPathFoundException;

import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.server.Directives;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.server.directives.SecurityDirectives.ProvidedCredentials;

public class Action {

	private static final int VERIFY_AUTH = -1;

	private final Object controller;
	private final Method method;

	private final String path;
	private final Verb verb;

	public Action(Object controller, Method method, String path, Verb verb) {
		this.controller = controller;
		this.method = method;
		this.path = path;
		this.verb = verb;
	}

	public String getPath() {
		return path;
	}

	public Verb getVerb() {
		return verb;
	}

	public Route route() {
		return createDirectives(path.split("/"), VERIFY_AUTH, new ArrayList<>());
	}

	@Override
	public String toString() {
		return String.format("%s %s => %s#%s", verb, path, controller.getClass().getSimpleName(), method.getName());
	}

	public static List<Action> findActions(Object controller) {
		List<Method> methods = Arrays.asList(controller.getClass().getMethods()).stream()
			.filter(method -> httpVerbFor(method).isPresent())
			.collect(Collectors.toList());
		
		List<Action> actions = methods.stream().map(method -> {
			if (!method.isAnnotationPresent(Path.class))
				throw new NoPathFoundException();

			String path = method.getAnnotation(Path.class).value();
			Annotation verb = method.getAnnotation(httpVerbFor(method).get());
			return new Action(controller, method, path, Verb.valueOf(verb));
		}).collect(Collectors.toList());
		
		return Collections.unmodifiableList(actions);
	}

	private Route createDirectives(String[] uris, int index, List<String> pathParams) {
		if (index == VERIFY_AUTH && method.getDeclaringClass().isAnnotationPresent(BasicAuth.class)) {
			// adding basic auth
			final BasicAuth basic = method.getDeclaringClass().getAnnotation(BasicAuth.class);
			return Directives.authenticateBasic("akka-wall", auth(basic.user(), basic.pass()), userName -> createDirectives(uris, index + 1, pathParams));
		} else if (index == VERIFY_AUTH) {
			// no auth provied
			return createDirectives(uris, index + 1, pathParams);
		} else if (uris.length > index) {
			// creating directives
			String uri = uris[index];
			return (uri.startsWith(":"))
				// handle path param
				? Directives.pathPrefix(STRING, pathParam -> createDirectives(uris, index + 1, addParam(pathParams, pathParam)))
				: Directives.pathPrefix(uri, () -> createDirectives(uris, index + 1, pathParams));
		} else {
			return endDirective(pathParams);
		}
	}

	private List<String> addParam(List<String> pathParams, String pathParam) {
		pathParams.add(pathParam);
		return pathParams;
	}

	private Route endDirective(List<String> pathParams) {
		return verb.apply(() -> {
			// TODO log.info("handling " + this)
			System.out.println("handling " + this);
			try { 
				if (!pathParams.isEmpty()) {
					return (Route) method.invoke(controller, pathParams.toArray());
				} else if (method.getParameterCount() == 1) {
					return Directives.entity(Jackson.unmarshaller(method.getParameterTypes()[0]), entity -> {
						try { return (Route) method.invoke(controller, entity); } catch (Exception e) { throw new ActionInvocationException(e); }
					});
				} else {				
					return (Route) method.invoke(controller);
				}
			} catch (Exception e) {
				//TODO log.error(e);
				Throwable cause = e instanceof InvocationTargetException ? ((InvocationTargetException) e).getTargetException() : e;
				return complete(INTERNAL_SERVER_ERROR, String.format("An error %s occurred, details: %s", cause.getClass().getSimpleName(), cause.getMessage()));
			}
		});
	}

	private Function<Optional<ProvidedCredentials>, Optional<String>> auth(String user, String pass) {
		return (credentials) -> credentials
			.filter(c -> user.equals(c.identifier()) && c.verify(pass))
			.map(ProvidedCredentials::identifier);
	}

	private static Optional<Class<? extends Annotation>> httpVerbFor(Method method) {
		final List<Class<? extends Annotation>> annotations = Arrays.asList(method.getAnnotations()).stream().map(a -> a.annotationType()).collect(Collectors.toList());
		annotations.retainAll(Verb.annotationClasses());

		if (annotations.size() > 1)
			throw new MultipleHttpVerbException("multiple annotations found: " + annotations.stream().map(a -> a.getSimpleName()).collect(Collectors.joining(", ")));

		return (annotations.size() == 1) ? Optional.of(annotations.get(0)) : Optional.empty(); 
	}
}