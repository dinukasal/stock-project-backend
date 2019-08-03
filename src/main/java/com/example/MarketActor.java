package com.example;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;

import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MarketActor extends AbstractActor {

  LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  //#user-case-classes
  public static class Market {
    private final List<Company> companies;

    public Market() {
        Random rand = new Random(); 

        this.companies = new ArrayList<>();
        Company c1 = new Company("Hayleys",rand.nextInt(100)+1);
        Company c2 = new Company("Sampath Bank",rand.nextInt(100)+1);
        Company c3 = new Company("Singer",rand.nextInt(100)+1);
        Company c4 = new Company("NTB",rand.nextInt(100)+1);
        Company c5 = new Company("John Keells",rand.nextInt(100)+1);
        Company c6 = new Company("Cargills",rand.nextInt(100)+1);
        Company c7 = new Company("Seylan Bank",rand.nextInt(100)+1);

        companies.add(c1);
        companies.add(c2);
        companies.add(c3);
        companies.add(c4);
        companies.add(c5);
        companies.add(c6);
        companies.add(c7);
    }

    public List<Company> getCompanies(){
        return companies;
    }
  }

  public static class Company{
    private final String name;
    private final int stockValue;

    public Company(String name,int stockValue){
      this.name = name;
      this.stockValue = stockValue;
    }

    public String getName(){
      return name;
    }

    public int getStockValue(){
      return stockValue;
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
            .match(MarketMessages.GetCompanies.class, getCompanies -> {
              getSender().tell(new Market(), getSelf());
            })
            .matchAny(o -> log.info("received unknown message"))
            .build();
  }
}
