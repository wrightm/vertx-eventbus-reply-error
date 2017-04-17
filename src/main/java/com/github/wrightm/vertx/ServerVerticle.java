package com.github.wrightm.vertx;

import static com.github.wrightm.vertx.Main.EVENT;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class ServerVerticle extends AbstractVerticle {

	private final Vertx vertx;

	public ServerVerticle( Vertx vertx ) {
		this.vertx = vertx;
	}

	@Override
	public void start( Future<Void> future ) throws Exception {
		int PORT = 40001;

		Router mainRouter = Router.router( vertx );
		mainRouter.route().consumes( "application/json" );
		mainRouter.route().produces( "application/json" );

		mainRouter.route().handler( BodyHandler.create() );
		mainRouter.route().failureHandler( ctx -> System.out.println( "Handler Failed " ) );

		mainRouter.route( "/hello" ).handler(
				ctx -> {
					String data = new JsonObject().put( "name", ctx.request().getParam( "name" ) )
							.encode();

					vertx.eventBus().send( EVENT, data, res -> {
						handleEventBusResponse( res, ctx );
					} );
				} );

		// Create the http server and pass it the router
		vertx.createHttpServer().requestHandler( mainRouter::accept ).listen( PORT, res -> {
			if ( res.succeeded() ) {
				System.out.println( "Server listening on port " + PORT );
				future.complete();
			} else {
				System.out.println( "Failed to launch server" );
				future.fail( res.cause() );
			}
		} );
	}

	private void handleEventBusResponse( AsyncResult<Message<Object>> res, RoutingContext ctx ) {
		if ( res.succeeded() ) {
			ctx.response().end( res.result().body().toString() );
		} else {
			ctx.fail( res.cause() );
		}
	}

}
