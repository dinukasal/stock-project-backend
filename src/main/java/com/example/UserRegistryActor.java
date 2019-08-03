package com.example;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;

import java.util.*;

public class UserRegistryActor extends AbstractActor {

  LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  //#user-case-classes
  public static class User {
    private final String name;
    private final int id;
    private final int balance;

    public User() {
      this.name = "Player";
      this.id = 1;
      this.balance = 100;
    }

    public User(String name, int id) {
      this.name = name;
      this.id = id;
      this.balance = 100;
    }

    public String getName() {
      return name;
    }

    public int getId(){
      return id;
    }
  }

  public static class Users{
    private final List<User> users;

    public Users() {
      this.users = new ArrayList<>();
    }

    public Users(List<User> users) {
      this.users = users;
    }

    public List<User> getUsers() {
      return users;
    }
  }
//#user-case-classes

  static Props props() {
    return Props.create(UserRegistryActor.class);
  }

  private final List<User> users = new ArrayList<>();

  @Override
  public Receive createReceive(){
    return receiveBuilder()
            .match(UserRegistryMessages.GetUsers.class, getUsers -> getSender().tell(new Users(users),getSelf()))
            .match(UserRegistryMessages.CreateUser.class, createUser -> {
              users.add(createUser.getUser());
              getSender().tell(new UserRegistryMessages.ActionPerformed(
                      String.format("User %s created.", createUser.getUser().getName())),getSelf());
            })
            .match(UserRegistryMessages.GetUser.class, getUser -> {
              getSender().tell(users.stream()
                      .filter(user -> user.getName().equals(getUser.getName()))
                      .findFirst(), getSelf());
            })
            .match(UserRegistryMessages.DeleteUser.class, deleteUser -> {
              users.removeIf(user -> user.getName().equals(deleteUser.getName()));
              getSender().tell(new UserRegistryMessages.ActionPerformed(String.format("User %s deleted.", deleteUser.getName())),
                      getSelf());

            }).matchAny(o -> log.info("received unknown message"))
            .build();
  }
}
