package com.ymmihw.mongodb.java.api;

import static org.junit.Assert.assertEquals;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class AppIntegrationTest {

  private static final String DB_NAME = "myMongoDb";
  private final MongoContainer mongoContainer = MongoContainer.getInstance();
  private MongoClient mongoClient;
  private MongoDatabase db;
  private MongoCollection<Document> collection;

  @Before
  public void setup() throws Exception {
    mongoContainer.start();
    mongoClient = new MongoClient(mongoContainer.getContainerIpAddress(),
        mongoContainer.getFirstMappedPort());

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

    it.forEach(new Block<Document>() {
      @Override
      public void apply(Document e) {
        System.out.println(e);
        assertEquals(e.get("name"), "John");
      }
    });
  }
}
