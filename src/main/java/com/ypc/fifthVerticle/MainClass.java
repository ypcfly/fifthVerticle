package com.ypc.fifthVerticle;

import io.vertx.core.Vertx;

public class MainClass {
  public static void main(String[] args) {

    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(MainVerticle.class.getName());
  }
}
