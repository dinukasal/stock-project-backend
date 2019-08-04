package com.stocks;

import com.stocks.UserRegistryActor.User;
import com.stocks.MarketActor.Sale;

import java.io.Serializable;
import java.util.*;  
import com.fasterxml.jackson.databind.ObjectMapper;

public interface MarketMessages {

    class ActionPerformed implements Serializable {
        private final String description;

        public ActionPerformed(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    class CreateUser implements Serializable {
        private final User user;

        public CreateUser(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }

    class GetCompanies implements Serializable {
    }

    class AddSale implements Serializable{
        private final Sale sale;

        public Sale getSale(){
            return sale;
        }

        public AddSale(Sale sale){
            this.sale=sale;
        }
    }

    class Buy implements Serializable{
        private final Sale sale;

        public Sale getSale(){
            return sale;
        }

        public Buy(Sale sale){
            this.sale=sale;
        }
    }
}