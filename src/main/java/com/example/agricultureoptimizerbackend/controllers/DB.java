package com.example.agricultureoptimizerbackend.controllers;

import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

public class DB {

    MongoClient mongoClient;
    com.mongodb.DB database;
    DBCollection collection;



    public void connectionDB(){
        mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));

        database = mongoClient.getDB("Agriculture");
    }

    public void createProduct(){

        collection = database.getCollection("Produtos");

        List<String> rotacao = Arrays.asList("cebolinha", "couve", "cenoura");

        DBObject produto = new BasicDBObject().append("name", "alface").append("valor", "2.0").append("rotacao", rotacao).append("tempoDeCultivo",3 );

        collection.insert(produto);
    }


}
