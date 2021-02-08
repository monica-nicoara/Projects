package com.main;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.entities.IEntity;
import com.factory.EntityFactory;
import com.repository.EntityManagerImpl;
import com.service.EntityService;

import io.advantageous.boon.json.JsonFactory;
import io.advantageous.qbit.admin.ManagedServiceBuilder;
import io.advantageous.qbit.http.HTTP;
import io.advantageous.qbit.http.config.HttpServerConfig;
import io.advantageous.qbit.http.server.HttpServer;
import io.advantageous.qbit.system.QBitSystemManager;
import io.advantageous.qbit.vertx.http.VertxHttpServerBuilder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class VertxQbitMainVerticle extends AbstractVerticle {

	/** Used to startup QBit */
	private final ManagedServiceBuilder managedServiceBuilder;
	/** Used to shutdown QBit services cleanly. */
	private QBitSystemManager systemManager;

	/**
	 * Create this verticle
	 * @param managedServiceBuilder managedServiceBuilder
	 */
	public VertxQbitMainVerticle(ManagedServiceBuilder managedServiceBuilder) {
		this.managedServiceBuilder = managedServiceBuilder;
	}

	public static void main(final String... args) throws Exception{
		final ManagedServiceBuilder managedServiceBuilder = createManagedServiceBuilder();

		final CountDownLatch latch = new CountDownLatch(1);
		final Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new VertxQbitMainVerticle(managedServiceBuilder), result -> {
			if (result.succeeded()) {
				System.out.println("Deployment id is: " + result.result());
			} else {
				System.out.println("Deployment failed!");
				result.cause().printStackTrace();
			}
			latch.countDown();
		});


		latch.await(5, TimeUnit.SECONDS);

		createEntities();

		System.out.println("QBit and Vertx are open.");
	}


	private static void createEntities() {
		Map<String, String> rootData = new HashMap<>();
		rootData.put("rootKey", "rootValue");

		Map<String, String> subentityData = new HashMap<>();
		subentityData.put("subentityKey", "subentityValue");

		IEntity parentEntity = EntityFactory.getInstance().createEntity("null", rootData);
		IEntity childEntity = EntityFactory.getInstance().createEntity("1", subentityData);

		HTTP.postJSON("http://localhost:8888/v1/entity-service/add",
				JsonFactory.toJson(parentEntity));
		HTTP.postJSON("http://localhost:8888/v1/entity-service/add",
				JsonFactory.toJson(childEntity));

	}

	@Override
	public void start() throws Exception {
		/* Vertx HTTP Server. */
		final io.vertx.core.http.HttpServer vertxHttpServer =
				this.getVertx().createHttpServer();

		/* Route one call to a vertx handler. */
		final Router router = createRouterAndVertxOnlyRoute();

		final HttpServer httpServer = getHttpServerAndRoutes(vertxHttpServer, router);

		systemManager = managedServiceBuilder.getSystemManager();

		managedServiceBuilder.addEndpointService(new EntityService(new EntityManagerImpl()));

		/*
		 * Create and start new service endpointServer.
		 */
		managedServiceBuilder
		.getEndpointServerBuilder()
		.setHttpServer(httpServer)
		.build()
		.startServer();

		/*
		 * Associate the router as a request handler for the vertxHttpServer.
		 */
		vertxHttpServer.requestHandler(router::accept).listen(
				managedServiceBuilder.getPort());
	}

	private HttpServer getHttpServerAndRoutes(final io.vertx.core.http.HttpServer vertxHttpServer, final Router router) {

		/* Route everything under /v1 to QBit http server. */
		final Route qbitRoute = router.route().path("/v1/*");

		/*
		 * Use the VertxHttpServerBuilder which is a special builder for Vertx/Qbit integration.
		 */
		return VertxHttpServerBuilder.vertxHttpServerBuilder()
				.setRoute(qbitRoute)
				.setHttpServer(vertxHttpServer)
				.setVertx(getVertx())
				.setConfig(new HttpServerConfig())
				.build();
	}

	private Router createRouterAndVertxOnlyRoute() {
		final Router router = Router.router(getVertx()); //Vertx router
		router.route("/svr/rout1/").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.setStatusCode(202);
			response.end("route1 this does not have very much data to transfer");
		});
		return router;
	}

	@Override
	public void stop() throws Exception {
		if (systemManager!=null) {
			systemManager.shutDown();
		}
	}


	private static ManagedServiceBuilder createManagedServiceBuilder() {
		/* Create the ManagedServiceBuilder which manages a clean shutdown, health, stats, etc. */
		final ManagedServiceBuilder managedServiceBuilder =
				ManagedServiceBuilder.managedServiceBuilder()
				.setRootURI("/v1")
				.setPort(8888);

		managedServiceBuilder.getStatsDReplicatorBuilder().setHost("192.168.59.103");
		managedServiceBuilder.setEnableStatsD(true);
		return managedServiceBuilder;
	}


}

