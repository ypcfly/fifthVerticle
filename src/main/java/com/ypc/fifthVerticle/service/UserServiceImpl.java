package com.ypc.fifthVerticle.service;

import com.ypc.fifthVerticle.service.constrant.SqlEnumeration;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

import java.util.HashMap;
import java.util.List;

public class UserServiceImpl implements UserService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  private final JDBCClient jdbcClient;

  private final HashMap<SqlEnumeration,String> sqlMap;

  public UserServiceImpl(JDBCClient jdbcClient, HashMap<SqlEnumeration, String> sqlMap, Handler<AsyncResult<UserService>> readyHandler) {
    this.jdbcClient = jdbcClient;
    this.sqlMap = sqlMap;
    jdbcClient.getConnection(ar -> {
      if (ar.succeeded()) {
        SQLConnection connection = ar.result();
        connection.execute(sqlMap.get(SqlEnumeration.CREATE_TABLE), result -> {
          connection.close();
          if (result.succeeded()) {
            LOGGER.info(">>>> execute success <<<<");
            readyHandler.handle(Future.succeededFuture(this));
          } else {
            LOGGER.error(">>>> execute failed <<<<");
            readyHandler.handle(Future.failedFuture(result.cause()));
          }
        });
      } else {
        readyHandler.handle(Future.failedFuture(ar.cause()));
        LOGGER.error(">>>> get database connection failed <<<<", ar.cause());
      }
    });
  }

  @Override
  public UserService selectUserById(Integer id, Handler<AsyncResult<JsonObject>> resultHandler) {
    jdbcClient.queryWithParams(sqlMap.get(SqlEnumeration.SELECT_BY_ID),new JsonArray().add(id), asyncResult -> {
      if (asyncResult.succeeded()){
        JsonObject jsonObject = asyncResult.result().getRows().get(0);
        resultHandler.handle(Future.succeededFuture(jsonObject));
      } else {
        resultHandler.handle(Future.failedFuture(asyncResult.cause()));
      }
    });

    return this;
  }

  @Override
  public UserService selectUserConditional(String address, Integer age, Handler<AsyncResult<List<JsonObject>>> resultHandler) {
//    jdbcClient.queryWithParams(sqlMap.get(SqlEnumeration.SELECT_BY_ID),new JsonArray().add(id), asyncResult -> {
//      if (asyncResult.succeeded()){
//        JsonObject jsonObject = asyncResult.result().getRows().get(0);
//        resultHandler.handle(Future.succeededFuture(jsonObject));
//      } else {
//        resultHandler.handle(Future.failedFuture(asyncResult.cause()));
//      }
//    });

    return this;
  }

  @Override
  public UserService insertNewUser(JsonArray jsonArray, Handler<AsyncResult<Integer>> resultHandler) {
    jdbcClient.updateWithParams(sqlMap.get(SqlEnumeration.INSERT_USER),jsonArray,asyncResult -> {
      if (asyncResult.succeeded()){
         Integer insert = asyncResult.result().getUpdated();
         resultHandler.handle(Future.succeededFuture(insert));
      } else {
         resultHandler.handle(Future.failedFuture(asyncResult.cause()));
      }
    });

    return this;
  }

  @Override
  public UserService queryByUserName(String username, String password, Handler<AsyncResult<JsonObject>> resultHandler) {
    jdbcClient.queryWithParams(sqlMap.get(SqlEnumeration.USERNAME_PASSWORD),new JsonArray().add(username).add(password),asyncResult -> {
      if (asyncResult.succeeded()){
        JsonObject jsonObject = asyncResult.result().getRows().get(0);
        resultHandler.handle(Future.succeededFuture(jsonObject));
      } else {
        resultHandler.handle(Future.failedFuture(asyncResult.cause()));
      }
    });

    return this;
  }

  @Override
  public UserService queryAllUser(Handler<AsyncResult<JsonArray>> resultHandler) {

    return null;
  }
}
