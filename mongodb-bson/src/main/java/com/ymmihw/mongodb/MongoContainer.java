package com.ymmihw.mongodb;

import org.testcontainers.containers.GenericContainer;

public class MongoContainer extends GenericContainer<MongoContainer> {
  private static final String IMAGE_VERSION = "mongo:3.6.12";
  private static MongoContainer container;

  private MongoContainer() {
    super(IMAGE_VERSION);
  }

  public static MongoContainer getInstance() {
    if (container == null) {
      container = new MongoContainer();
    }
    return container;
  }

  @Override
  public void start() {
    super.addExposedPort(27017);
    super.start();
  }


  @Override
  public void stop() {
    // do nothing, JVM handles shut down
  }
}
