package com.stocks;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;

import java.util.*;

public class AnalystActor extends AbstractActor {

  LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  public static class Analyst {
    private final String name;
    private final int id;

    public Analyst() {
      this.name = "";
      this.id = 1;
    }

    public Analyst(String name, int id) {
      this.name = name;
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public int getId(){
      return id;
    }
  }


  static Props props() {
    return Props.create(AnalystActor.class);
  }

  private final Analyst analyst = new Analyst();

  @Override
  public Receive createReceive(){
    return receiveBuilder()

            .matchAny(o -> log.info("received unknown message"))
            .build();
  }
}
