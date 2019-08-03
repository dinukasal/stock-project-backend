package com.stocks;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;

import java.util.*;

public class PlayerAIActor extends AbstractActor {

  LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  //#user-case-classes
  public static class PlayerAI {
    private final String name;
    private final int id;

    public PlayerAI() {
      this.name = "";
      this.id = 1;
    }

    public PlayerAI(String name, int id) {
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


//#user-case-classes

  static Props props() {
    return Props.create(PlayerAIActor.class);
  }

  private final PlayerAI player = new PlayerAI();

  @Override
  public Receive createReceive(){
    return receiveBuilder()
            // .match(UserRegistryMessages.GetUsers.class, getUsers -> getSender().tell(new Users(users),getSelf()))
            // .match(UserRegistryMessages.CreateUser.class, createUser -> {
            //   users.add(createUser.getUser());
            //   getSender().tell(new UserRegistryMessages.ActionPerformed(
            //           String.format("User %s created.", createUser.getUser().getName())),getSelf());
            // })
            // .match(UserRegistryMessages.GetUser.class, getUser -> {
            //   getSender().tell(users.stream()
            //           .filter(user -> user.getName().equals(getUser.getName()))
            //           .findFirst(), getSelf());
            // })
            // .match(UserRegistryMessages.DeleteUser.class, deleteUser -> {
            //   users.removeIf(user -> user.getName().equals(deleteUser.getName()));
            //   getSender().tell(new UserRegistryMessages.ActionPerformed(String.format("User %s deleted.", deleteUser.getName())),
            //           getSelf());

            // })
            .matchAny(o -> log.info("received unknown message"))
            .build();
  }
}
