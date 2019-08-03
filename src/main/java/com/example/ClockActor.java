package com.example;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;

import java.util.*;

public class ClockActor extends AbstractActor {

  LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  //#user-case-classes
  public static class Clock {
    private final long time;

    public Clock() {
      this.time = System.currentTimeMillis();
    }

    public Clock(long time) {
      this.time = time;
    }

    public long getTime() {
      return time;
    }
  }


//#user-case-classes

  static Props props() {
    return Props.create(ClockActor.class);
  }

  @Override
  public Receive createReceive(){
    return receiveBuilder()
            .match(ClockMessages.GetTime.class, getTime -> {
              getSender().tell(new Clock(), getSelf());
            })
            .matchAny(o -> log.info("received unknown message"))
            .build();
  }
}
