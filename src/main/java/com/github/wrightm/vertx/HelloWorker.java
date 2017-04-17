package com.github.wrightm.vertx;

import static com.github.wrightm.vertx.Main.EVENT;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

public class HelloWorker extends AbstractVerticle {

	private final Vertx vertx;

	public HelloWorker( Vertx vertx ) {
		this.vertx = vertx;
	}

	@Override
	public void start( Future<Void> done ) {
		MessageConsumer<String> consumer = vertx.eventBus().consumer( EVENT );

		consumer.handler( m -> {

			JsonObject data = new JsonObject( m.body() );
			String message = String.format( "Hello %s! How are you", data.getString( "name" ) );

			try {
				m.reply( message, result -> {
					if ( result.succeeded() ) {
						System.out.println( "We answered" );
					} else {
						System.out.println( "We failed answered: " + result.cause().getMessage() );
					}
				} );
			} catch ( Exception e ) {
				m.fail( HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), "Failed to answer data." );
			}
		} );

		done.complete();
	}
}
