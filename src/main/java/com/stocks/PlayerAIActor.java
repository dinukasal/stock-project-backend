package com.stocks;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import com.stocks.UserRegistryActor.User;
import com.stocks.UserRegistryActor.Users;
import com.stocks.UserRegistryActor.InitUser;
import java.util.concurrent.TimeUnit;
import java.util.*;
import java.time.Duration;
import akka.pattern.Patterns;
import java.util.concurrent.CompletionStage;
// import akka.dispatch.Await;
// import akka.dispatch.Future;
import scala.concurrent.Future;
import scala.concurrent.Await;

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
              log.info("##### AI Creating players ## ");
              
              int missingCount = 0;

              Duration timeout = Duration.ofSeconds(1l);
              
              CompletionStage<UserRegistryActor.Users> getUsers = Patterns
                      .ask(userRegistryActor, new UserRegistryMessages.GetUsers(), timeout).thenApply(UserRegistryActor.Users.class::cast);

              Users users = getUsers.toCompletableFuture().get();
              int usercount = users.getUsers().size();
              missingCount = 4-usercount;

              for(int i=0;i<missingCount;i++){
                userRegistryActor.tell(new UserRegistryMessages.CreateUser(new InitUser("AIPlayer"+Integer.toString(i))),userRegistryActor);
              }
            })
            .matchAny(o -> log.info("received unknown message"))
            .build();
  }
}
