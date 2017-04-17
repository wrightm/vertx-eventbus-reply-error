package com.github.wrightm.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

/**
 * Created by levontamrazov on 2017-01-28. Main class will instantiate vertx and
 * deploy our ServiceLauncher
 */
public class Main extends AbstractVerticle {

	public static final String EVENT = "EVENT.HELLO";

	public static void main( String[] args ) {
		Vertx vertx = Vertx.vertx();

		DeploymentOptions serverOpts = new DeploymentOptions().setWorkerPoolSize( 100 );

		DeploymentOptions workerOpts = new DeploymentOptions().setWorker( true ).setWorkerPoolSize(
				100 );

		CompositeFuture.all( deploy( vertx, new ServerVerticle( vertx ), serverOpts ),
				deploy( vertx, new HelloWorker( vertx ), workerOpts ) ).setHandler( r -> {
			if ( r.succeeded() ) {
				System.out.println( "Successfully deployed all verticles" );
			} else {
				System.out.println( "Failed to deploy all verticles: " );
			}
		} );
	}

	private static Future<Void> deploy( Vertx vertx, AbstractVerticle verticle,
			DeploymentOptions opts ) {
		Future<Void> done = Future.future();

		vertx.deployVerticle( verticle, opts, r -> {
			if ( r.succeeded() ) {
				System.out.println( "Successfully deployed verticle: " + verticle.deploymentID() );
				done.complete();
			} else {
				System.out.println( "Failed to deploy verticle: " + verticle.deploymentID() );
				done.fail( r.cause() );
			}
		} );

		return done;
	}
}
