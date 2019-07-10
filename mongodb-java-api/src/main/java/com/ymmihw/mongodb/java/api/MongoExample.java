package com.ymmihw.mongodb.java.api;

import java.util.function.Consumer;
import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoExample {
  public static void main(String[] args) {
    MongoContainer mongoContainer = MongoContainer.getInstance();
    mongoContainer.start();
    MongoClient mongoClient = new MongoClient(mongoContainer.getContainerIpAddress(),
        mongoContainer.getFirstMappedPort());

    MongoDatabase database = mongoClient.getDatabase("myMongoDb");

    // print existing databases
    mongoClient.listDatabaseNames().forEach((Consumer<String>) System.out::println);

    System.out.println("==============");

    database.drop();

    database.createCollection("customers");

    // print all collections in customers database
    database.listCollectionNames().forEach((Consumer<String>) System.out::println);

    // create data
    MongoCollection<Document> collection = database.getCollection("customers");
    Document document = new Document();
    document.put("name", "Shubham");
    document.put("company", "Baeldung");
    collection.insertOne(document);

    // update data
    BasicDBObject query = new BasicDBObject();
    query.put("name", "Shubham");
    BasicDBObject newDocument = new BasicDBObject();
    newDocument.put("name", "John");
    BasicDBObject updateObject = new BasicDBObject();
    updateObject.put("$set", newDocument);
    collection.updateOne(query, updateObject);

    // read data
    BasicDBObject searchQuery = new BasicDBObject();
    searchQuery.put("name", "John");
    FindIterable<Document> cursor = collection.find(searchQuery);
    cursor.forEach((Consumer<Document>) System.out::println);

    // delete data
    BasicDBObject deleteQuery = new BasicDBObject();
    deleteQuery.put("name", "John");
    collection.deleteOne(deleteQuery);
  }
}
