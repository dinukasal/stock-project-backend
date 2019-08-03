package com.example;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;

import java.util.*;

public class MarketActor extends AbstractActor {

  LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  //#user-case-classes
  public static class Market {
    private final Map<String, Integer> stockValues;

    public Market() {
        Random rand = new Random(); 
        this.stockValues = new HashMap<String, Integer>();
        this.stockValues.put("Hayleys",1+rand.nextInt(100));
        this.stockValues.put("Singer",1+rand.nextInt(100));
        this.stockValues.put("Sampath Bank",1+rand.nextInt(100));
        this.stockValues.put("John Keells",1+rand.nextInt(100));
        this.stockValues.put("Cargills",1+rand.nextInt(100));
    }

    public Map<String,Integer> getStatus(){
      return this.stockValues;
    }
  }


//#user-case-classes

  static Props props() {
    return Props.create(MarketActor.class);
  }

  private final Market market = new Market();

  @Override
  public Receive createReceive(){
    return receiveBuilder()

            .matchAny(o -> log.info("received unknown message"))
            .build();
  }
}
