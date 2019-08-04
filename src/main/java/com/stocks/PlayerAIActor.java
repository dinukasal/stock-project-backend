package com.stocks;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import com.stocks.UserRegistryActor.User;

import java.util.*;

public class PlayerAIActor extends AbstractActor {

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


//#user-case-classes

  static Props props() {
    return Props.create(PlayerAIActor.class);
  }

  private final AIPlayers players = new AIPlayers();

  @Override
  public Receive createReceive(){
    return receiveBuilder()
            .matchAny(o -> log.info("received unknown message"))
            .build();
  }
}
