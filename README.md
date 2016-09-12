# akka-http-ext-controller
creating controllers with akka-http easily

Supports the http verbs: `@GET`, `@POST`, `@PUT`, `@DELETE`. Also can handle json body or/and path params.

## Prerequisities
* akka-http-core
* akka-http-jackson
* java 1.8

## Getting Started

1) Add maven dependency in your project:

	<dependency>
		<groupId>todo</groupId>
		<artifactId>akka-http-ext-controller</artifactId>
		<version>0.0.l</version>
	</dependency>


2) Create controllers in your application:
```java
public class UserController {

	@GET
	@Path("users/:id/details")
	public Route simpleGet(String id) {
		// getting pathParams
		return Directives.complete("Showing details for user " + id);
	}

	@POST
	@Path("users")
	public Route simpleGet(User user) {
		// parsing json body
		return Directives.complete("Saving user " + user.getName());
	}
}
```

3) Register your controllers with the Routes.init(ActorSystem, port, controllers...), as follows:
```java
system = ActorSystem.create();
Routes.init(system, 8080, new UserController());
```
   Please note that all controllers must be declared at once.

4) Done! You can access your controller at [http://localhost:8080/users/1/details](http://localhost:8080/users/1/details)

## Framework conventions:
All actions must have:

1. annotation to indicate the http verb
2. annotation `@Path` to indicate the path of action
3. return a `akka.http.javadsl.server.Route` object, usually you will use `akka.http.javadsl.server.Directives` for this.

**using PathParams**

When you use pathParams, the framework inject the arguments in the same order as received, for example:
For `@Path("user/:id/purchases/:purchaseId/items/:itemId/details")` must have a controller with 3 String arguments like this `public Route details(String id, String purchaseId, String itemId)`

**using JSON Body**

When you use json body, the framework will try convert body content to object. An action in controller must receive a object, for example `public Route update(User user)`. An error 500 will raise if parse fails.

**using QueryString**

TODO

## Running the tests

	$ mvn test

## Task List
- [x] add support to pathParams
- [x] add support to json body
- [ ] add artifacts to the Central Repository
- [ ] add support to queryString
- [ ] add support to pathParams, json body and queryString when used in same time
- [ ] create annotation `@Controller` and make package scan to register controllers
- [ ] create annotation `@Produces` to use with custom content-types like `application/vnd.api+json`

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Authors

* **Nadilson** - *Initial work* - [nadilsons](https://github.com/nadilsons)

See also the list of [contributors](https://github.com/nadilsons/akka-http-ext-controller/graphs/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
