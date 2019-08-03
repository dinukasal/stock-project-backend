package com.stocks;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;

import java.util.*;

public class BankActor extends AbstractActor {

  LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  public static class Bank {
    private final List<Account> accounts;

    public Bank() {
      accounts = new ArrayList<>();
    }

    public void addAccount(Account account){
      accounts.add(account);
    }

    public List<Account> getAccounts(){
      return accounts;
    }
  }

  public static class Account{
    private final float balance;
    private final int userId;


    public Account(){
      this.balance = 0;
      this.userId=0;
    }

    public Account(float balance,int userId){
      this.balance = balance;
      this.userId = userId;
    }

    public float getBalance(){
      return balance;
    }

    public int getUserId(){
      return userId;
    }
  }

  static Props props() {
    return Props.create(BankActor.class);
  }

  private final Bank bank = new Bank();

  @Override
  public Receive createReceive(){
    return receiveBuilder()
            .match(BankMessages.GetBalance.class, getBalance -> {
              getSender().tell(bank, getSelf());
            })
            .match(BankMessages.CreateAccount.class, createAccount -> {
              log.info("======== creating account");
              bank.addAccount(createAccount.getAccount());
              // getSender().tell(createAccount.getAccount(), getSelf());
            })
            .matchAny(o -> log.info("received unknown message"))
            .build();
  }
}
