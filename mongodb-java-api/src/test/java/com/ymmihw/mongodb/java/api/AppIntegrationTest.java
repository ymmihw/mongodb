package com.ymmihw.mongodb.java.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.ymmihw.mongodb.MongoContainer;

public class AppIntegrationTest {

  private static final String DB_NAME = "myMongoDb";
  private final MongoContainer mongoContainer = MongoContainer.getInstance();
  private MongoClient mongoClient;
  private MongoDatabase db;
  private MongoCollection<Document> collection;

  @BeforeEach
  public void setup() throws Exception {
    mongoContainer.start();

    mongoClient = MongoClients.create("mongodb://" + mongoContainer.getContainerIpAddress() + ":"
        + mongoContainer.getFirstMappedPort());

    // Creating DB
    db = mongoClient.getDatabase(DB_NAME);

    // Creating collection Object and adding values
    collection = db.getCollection("customers");
  }


  @Test
  public void testAddressPersistance() {
    Document contact = new Document();
    contact.put("name", "John");
    contact.put("company", "Baeldung");

    // Inserting document
    collection.insertOne(contact);
    FindIterable<Document> it = collection.find();

    it.forEach(e -> {
      System.out.println(e);
      assertEquals(e.get("name"), "John");
    });
  }
}
