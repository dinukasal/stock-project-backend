package com.stocks;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import com.stocks.UserRegistryActor.User;
import java.util.concurrent.TimeUnit;
import java.util.*;

public class PlayerAIActor extends AbstractActor {
  ActorRef userRegistryActor;
  ActorRef clockActor;
  ActorRef brokerActor;
  LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  public static class AIPlayers{
    private final List<User> players;

    public AIPlayers() {
      this.players = new ArrayList<>();
    }

    public AIPlayers(List<User> players) {
      this.players = players;
    }

    public List<User> getUsers() {
      return players;
    }

    public User addAIPlayer(User user){
      players.add(user);
      return user;
    }
  }


  static Props props() {
    return Props.create(PlayerAIActor.class);
  }

  private final AIPlayers players = new AIPlayers();

  @Override
  public Receive createReceive(){
    return receiveBuilder()
            .match(PlayerAIMessages.SetActors.class, actors -> {
              log.info(">>> setting actor refs in AIActor");
              userRegistryActor = actors.getUserRegistryActor();
              clockActor = actors.getClockActor();
              brokerActor = actors.getBrokerActor();
            })
            .match(PlayerAIMessages.LookPlayerCompletion.class, look -> {
              TimeUnit.SECONDS.sleep(59);
              getSender().tell("ok",getSelf());
            })
            .matchAny(o -> log.info("received unknown message"))
            .build();
  }
}
