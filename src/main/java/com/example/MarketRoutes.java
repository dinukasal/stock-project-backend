package com.example;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import com.example.MarketActor.Market;
import com.example.MarketMessages.ActionPerformed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;

/**
 * Routes can be defined in separated classes like shown in here
 */
//#user-routes-class
public class MarketRoutes extends AllDirectives {
    //#user-routes-class
    final private ActorRef marketActor;
    final private LoggingAdapter log;


    public MarketRoutes(ActorSystem system, ActorRef marketActor) {
        this.marketActor = marketActor;
        log = Logging.getLogger(system, this);
    }

    // Required by the `ask` (?) method below
    Duration timeout = Duration.ofSeconds(5l); // usually we'd obtain the timeout from the system's configuration

    /**
     * This method creates one route (of possibly many more that will be part of your Web App)
     */
    //#all-routes
    //#users-get-delete
    public Route routes() {
        return route(pathPrefix("market-status", () ->
            route(
                getStatus()
            )
        ));
    }
    //#all-routes

    //#users-get-delete

    //#users-get-delete
    private Route getStatus() {
        return pathEnd(() ->
            route(
                get(() -> {
                // #retrieve-user-info
                CompletionStage<Optional<Market>> marketStatus = Patterns
                        .ask(marketActor, new MarketMessages.GetMessages(), timeout)
                        .thenApply(Optional.class::cast);

                return onSuccess(() -> marketStatus,
                    performed -> {
                        if (performed.isPresent())
                            return complete(StatusCodes.OK, performed.get(), Jackson.marshaller());
                        else
                            return complete(StatusCodes.NOT_FOUND);
                    }
                    );
                //#retrieve-user-info
                })
            )
        );
    }

    //#users-get-delete
}
