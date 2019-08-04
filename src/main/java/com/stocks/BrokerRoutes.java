package com.stocks;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import com.stocks.BankMessages.*;
import com.stocks.MarketActor.*;
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
public class BrokerRoutes extends AllDirectives {
    final private ActorRef brokerActor;
    final private ActorRef marketActor;
    final private LoggingAdapter log;


    public BrokerRoutes(ActorSystem system, ActorRef brokerActor, ActorRef marketActor) {
        this.brokerActor = brokerActor;
        this.marketActor = marketActor;
        log = Logging.getLogger(system, this);
    }

    // Required by the `ask` (?) method below
    Duration timeout = Duration.ofSeconds(5l); // usually we'd obtain the timeout from the system's configuration

    /**
     * This method creates one route (of possibly many more that will be part of your Web App)
     */

    public Route routes() {
        return route(concat(
            pathPrefix("sell", () ->
                route(
                    addSale()
                )
            ),
            pathPrefix("buy", () ->
                route(
                    buy()
                )
            )
        ));
    }

    private Route addSale() {
        return pathEnd(() ->
            route(
                post(() -> 
                    entity(
                        Jackson.unmarshaller(MarketActor.Sale.class),
                        sale->{
                            log.info("Adding sale of user:"+sale.getUserId());
                            CompletionStage<MarketActor.Sale> addSale = Patterns
                                    .ask(marketActor, new MarketMessages.AddSale(sale), timeout)
                                    .thenApply(MarketActor.Sale.class::cast);

                            return onSuccess(() -> addSale,
                                addedSale -> {
                                    if(addedSale.getUserId()>0){
                                        return complete(StatusCodes.OK, addedSale, Jackson.marshaller());
                                    }else{
                                        return complete(StatusCodes.INTERNAL_SERVER_ERROR,"Error adding sale!");
                                    }
                                });
                        }
                    ))
            )
        );
    }

        private Route buy() {
        return pathEnd(() ->
            route(
                post(() -> 
                    entity(
                        Jackson.unmarshaller(MarketActor.Sale.class),
                        sale->{
                            log.info("Buying sale of user:"+sale.getUserId());
                            CompletionStage<MarketActor.Sale> buy = Patterns
                                    .ask(marketActor, new MarketMessages.Buy(sale), timeout)
                                    .thenApply(MarketActor.Sale.class::cast);

                            return onSuccess(() -> buy,
                                bought -> {
                                    if(bought.getUserId()>0){
                                        return complete(StatusCodes.OK, bought, Jackson.marshaller());
                                    }else{
                                        return complete(StatusCodes.INTERNAL_SERVER_ERROR,"Cannod do the sale");
                                    }
                                });
                        }
                    ))
            )
        );
    }
}
