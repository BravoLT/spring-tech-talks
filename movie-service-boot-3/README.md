# Spring Boot 3 & Spring 6 Features/Changes
Sample movie-service-boot-3 CRUD Rest app. Demonstrates key features/changes for Spring Boot 3 & Spring 6. **Note**: A spring boot starter app was created here, that I imported into my Eclipse workspace & added components to [https://start.spring.io](https://start.spring.io):

* Java 17 Baseline (from Java 8-17 in the Spring Framework 5.3.x line)
* Native Executable Building Support (see GraalVM Native Support below)
* Jakarta EE 9+ Baseline (from Java EE 7-8 in the Spring Framework 5.3.x line)
* Spring Observability (new project in Spring 6)
* Deprecated classes and methods (will peek at spike branch we did at Amway)

Please just jot down any questions you think of along the way, to ensure we have time to cover the main features/changes. We can discuss them at the end. Thanks :) Also:
* Reference Documentation section below has a bunch of links to resources with more details on newer versions
* I'll be adding this project somewhere in our BravoLT github after any needed tweaks

## Java 17 Baseline
* Java 17 is now the minimum version with Spring 6 and Spring Boot 3.
* You must compile to and run the code with a Java 17 or higher runtime

**Demo**: Try to change the sourceCompatibility to 1.8 and build the app (Note the errors):

```
gradlew build
```

Some java features after Java 11 that you can now use under Java 17:
* Java Records [https://openjdk.org/jeps/395](https://openjdk.org/jeps/395): Immutable classes
	* Note: Only getters available
* Text Blocks (see createdMsg in getAll() controller method)
* Sealed Classes: Limit inheritance by specifying allowed subclasses

```
public abstract sealed class Pet permits Dog, Cat {}
```

## Native Executables
Huge !!! You could argue this is the biggest feature. Similar to the Quarkus framework. 2 ways to build: Cloud Native Buildpacks (this demo) or Native Build Tools (see GraalVM Native Support below). Native images provide various advantages, like an instant startup and reduced memory consumption
* With "org.graalvm.buildtools.native" plugin configured, bootBuildImage task will generate a native image

**Demo**: Running app normally with (note startup time) 

```
$ gradlew bootRun --args=--spring.datasource.url=jdbc:h2:mem:testdb,--spring.datasource.username=sa
```

**Demo**: Create a movie **Note**: Time in Postman (about 200+ milliseconds)

**Demo**: bootBuildImage (as native) **Note**: SKIP for time purposes

```
$ gradlew bootBuildImage
```

**Demo**: Run the native app (or from Docker Desktop). **Note**: Startup time !!

```
docker run --rm -p 8080:8080 docker.io/library/movie-service-boot-3:0.0.1-SNAPSHOT
```

**Demo**: Create a movie **Note**: Time in Postman (about 5-20 milliseconds)

Concerning Spring Boot, we have to be aware that features like profiles, conditional beans, and .enable properties are not fully supported at runtime anymore. If we use profiles, they have to be specified at build time.

## Jakarta EE 9 API
* Requires Tomcat 10 or Jetty 11 (i.e., the default embedded tomcat used by spring-boot-starter-web, or if you're deploying your app to a standalone container).
* Will need to refactor javax to jakarta namespace/package wherever you’re using the Servlet API, JPA, Bean Validation, etc
* Jakarta EE 9 release removes specifications from Jakarta EE 8 that were old, optional, or deprecated in order to reduce the surface area of the APIs.
* All specifications included in the Jakarta EE 9 release have been versioned to a new major version of the specification doc. Ex: JPA 2.x (in Jakarta EE 8) is now JPA 3.0
* No backward compatibility. Jakarta EE 9 is not backward compatible with Jakarta EE 8 or Java EE 8.
* **Note**: It's worth taking time to see what features in the EE stack in version 9 can be used in your application (quite a few changes, too much to cover here)

## Spring Observability
Spring 6 introduces Spring Observability – a new project that builds on Spring Cloud Sleuth. It is more for efficiently recording application metrics with Micrometer and implementing tracing through providers such as OpenZipkin or OpenTelemetry.
* spring-web module now requires io.micrometer:micrometer-observation:1.10+ as a compile dependency
* RestTemplate and WebClient are instrumented to produce HTTP client request observations.
* Spring WebFlux can be instrumented for HTTP server observations using the new ServerHttpObservationFilter

Spring Observability scores over previous agent-based observability, as it works seamlessly in natively compiled Spring applications to more effectively provide better information.

## Deprecated classes and methods
See [spike branch](https://github.com/AmwayABOIM/magic-services/compare/prod...MGC-3035_Spike_Upgrading_magic-services_apps_to_Spring_6_Spring_Boot_3)
* **Note**: Highly recommended to do a technical spike to see what changes may be needed to your application in order to upgrade to newer version of Spring/Boot. Ex: We found at amway for magic-services we'll need to update DB tests, as we needed to upgrade to a newer H2 in-memory DB library, and they changed behavior Oracle Mode, related to CHAR datatype (no padding).

### Reference Documentation
For further reference, please consider the following sections:
* [Preparing for spring-boot 3.0](https://spring.io/blog/2022/05/24/preparing-for-spring-boot-3-0)
* [Getting Ready for Spring 6](https://springframework.guru/getting-ready-for-spring-framework-6)  
* [What's New in Spring 6](https://github.com/spring-projects/spring-framework/wiki/What's-New-in-Spring-Framework-6.x)
* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.2/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.2/gradle-plugin/reference/html/#build-image)
* [GraalVM Native Image Support](https://docs.spring.io/spring-boot/docs/3.0.2/reference/html/native-image.html#native-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.2/reference/htmlsingle/#web)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.0.2/reference/htmlsingle/#data.sql.jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)
* [Configure AOT settings in Build Plugin](https://docs.spring.io/spring-boot/docs/3.0.2/gradle-plugin/reference/htmlsingle/#aot)

## GraalVM Native Support

This project has been configured to let you generate either a lightweight container or a native executable.
It is also possible to run your tests in a native image.

### Lightweight Container with Cloud Native Buildpacks
If you're already familiar with Spring Boot container images support, this is the easiest way to get started.
Docker should be installed and configured on your machine prior to creating the image.

To create the image, run the following goal:

```
$ ./gradlew bootBuildImage
```

Then, you can run the app like any other container:

```
$ docker run --rm -p 8080:8080 movie-service-boot-3:0.0.1-SNAPSHOT
```

### Executable with Native Build Tools
Use this option if you want to explore more options such as running your tests in a native image.
The GraalVM `native-image` compiler should be installed and configured on your machine.

NOTE: GraalVM 22.3+ is required.

To create the executable, run the following goal:

```
$ ./gradlew nativeCompile
```

Then, you can run the app as follows:
```
$ build/native/nativeCompile/movie-service-boot-3
```

You can also run your existing tests suite in a native image.
This is an efficient way to validate the compatibility of your application.

To run your existing tests in a native image, run the following goal:

```
$ ./gradlew nativeTest
```

