package com.ypc.fifthVerticle.service;

import com.ypc.fifthVerticle.service.constrant.SqlEnumeration;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;

import java.util.HashMap;
import java.util.List;

@ProxyGen
public interface UserService {

  @Fluent
  UserService selectUserById(Integer id, Handler<AsyncResult<JsonObject>> resultHandler);

  @Fluent
  UserService selectUserConditional(String address, Integer age, Handler<AsyncResult<List<JsonObject>>> resultHandler);

  @Fluent
  UserService insertNewUser(JsonArray jsonArray, Handler<AsyncResult<Integer>> resultHandler);

  @Fluent
  UserService queryByUserName(String username,String password,Handler<AsyncResult<JsonObject>> resultHandler);

  @Fluent
  UserService queryAllUser(Handler<AsyncResult<JsonArray>> resultHandler);

  static UserService create(JDBCClient jdbcClient, HashMap<SqlEnumeration,String> sqlMap, Handler<AsyncResult<UserService>> readyHandler) {
    return new UserServiceImpl(jdbcClient, sqlMap, readyHandler);
  }

  static UserServiceVertxEBProxy createProxy(Vertx vertx, String address) {
    return new UserServiceVertxEBProxy(vertx, address);
  }

}
