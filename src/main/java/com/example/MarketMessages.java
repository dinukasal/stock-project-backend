package com.example;

import com.example.UserRegistryActor.User;

import java.io.Serializable;
import java.util.*;  
import com.fasterxml.jackson.databind.ObjectMapper;

public interface MarketMessages {

    class GetUsers implements Serializable {
    }

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
        // private final Map<String,Integer> stockValues;

        // public GetStatus(Map<String,Integer> stockValues) {
        //     this.stockValues = stockValues;
        // }

        // public String getStatus() {
        //     String str ="";
            
        //     try{
        //         str= new ObjectMapper().writeValueAsString(stockValues);
        //     }catch(Exception e){

        //     }

        //     return str;
        // }
    }

    class DeleteUser implements Serializable {
        private final String name;

        public DeleteUser(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}