package com.stocks;

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
    private final List<Sale> sales;

    public Market() {
        Random rand = new Random(); 

        this.companies = new ArrayList<>();
        Company c1 = new Company(1,"Hayleys",rand.nextInt(100)+1);
        Company c2 = new Company(2,"Sampath Bank",rand.nextInt(100)+1);
        Company c3 = new Company(3,"Singer",rand.nextInt(100)+1);
        Company c4 = new Company(4,"NTB",rand.nextInt(100)+1);
        Company c5 = new Company(5,"John Keells",rand.nextInt(100)+1);
        Company c6 = new Company(6,"Cargills",rand.nextInt(100)+1);
        Company c7 = new Company(7,"Seylan Bank",rand.nextInt(100)+1);

        companies.add(c1);
        companies.add(c2);
        companies.add(c3);
        companies.add(c4);
        companies.add(c5);
        companies.add(c6);
        companies.add(c7);

        this.sales = new ArrayList<>();

    }

    public List<Company> getCompanies(){
        return companies;
    }
  }

  public static class Company{
    private final int id;
    private final String name;
    private final int stockValue;

    public Company(int id,String name,int stockValue){
      this.name = name;
      this.stockValue = stockValue;
      this.id=id;
    }

    public String getName(){
      return name;
    }

    public int getStockValue(){
      return stockValue;
    }

    public int getId(){
      return id;
    }
  }

  public static class Sale{
    private final int userId;
    private final int companyId;
    private final int value;

    public Sale(int userId,int companyId,int value){
      this.userId=userId;
      this.companyId=companyId;
      this.value=value;
    }

    public int getUserId(){
      return userId;
    }
  }

//#user-case-classes

  static Props props() {
    return Props.create(MarketActor.class);
  }

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
