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

    public Sale addSale(Sale sale){
      boolean exists = false;

      for(Sale s:sales){
        if(s.getCompanyId()==sale.getCompanyId() && s.getUserId() == sale.getUserId()){
          exists=true;
          s.setValue(s.getValue()+sale.getValue());
          return s;
        }
      }

      if(!exists){
        sales.add(sale);
        return sale;
      }

      return new Sale();
    }

    public List<Sale> getSales(){
      return sales;
    }

    public boolean removeSale(Sale sale){
      boolean contains=false;
      for(Sale s:sales){
        if(s.getCompanyId()==sale.getCompanyId() && s.getUserId() == sale.getUserId()){
          contains=true;
          if(s.getValue()<sale.getValue()){
            return false;
          }else{
            s.setValue(s.getValue()-sale.getValue());
          }
        }
      }
      if(contains){
        return true;
      }else{
        return false;
      }
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
    private final int companyId;
    private final int userId;
    private int value;

    public Sale(){
      this.userId=0;
      this.companyId=0;
      this.value=0;
    }

    public Sale(int companyId,int userId,int value){
      this.userId=userId;
      this.companyId=companyId;
      this.value=value;
    }

    public int getUserId(){
      return userId;
    }

    public int getCompanyId(){
      return companyId;
    }

    public int getValue(){
      return value;
    }

    public void setValue(int value){
      this.value = value;
    }
  }

  static Props props() {
    return Props.create(MarketActor.class);
  }

  private final Market market = new Market();

  @Override
  public Receive createReceive(){
    return receiveBuilder()
            .match(MarketMessages.GetCompanies.class, getCompanies -> {
              getSender().tell(market, getSelf());
            })
            .match(MarketMessages.AddSale.class, addSale -> {

              Sale sale = addSale.getSale();  
              Sale modifiedSale = market.addSale(sale);
              getSender().tell(modifiedSale,getSelf());

            })
            .match(MarketMessages.Buy.class, buy -> {

              Sale sale = buy.getSale();  
              boolean exists = false;

              //removing sale from sales list
              exists = market.removeSale(sale);
              
              if(exists){
                getSender().tell(sale,getSelf());
              }else{
                getSender().tell(new Sale(),getSelf());
              }
              
            })
            .matchAny(o -> log.info("received unknown message"))
            .build();
  }
}
