package com.ypc.fifthVerticle.vercicle;

import com.ypc.fifthVerticle.service.UserService;
import com.ypc.fifthVerticle.service.constrant.SqlEnumeration;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.serviceproxy.ProxyHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class DataBaseVerticle extends AbstractVerticle {

    private static final String URL = "jdbc:postgresql://localhost:5432/pgsql?useSSL=false&characterEncoding=utf8";
    private static final String DRIVER_CLASS = "org.postgresql.Driver";
    private static final String USER_NAME = "postgres";
    private static final String PASSWORD = "123456";
    private static final int MAX_POOL_SIZE = 30;
    private JDBCClient jdbcClient;

    private static final String ADDRESS = "com.ypc";

    @Override
    public void start(Future<Void> startFuture) throws Exception {
      // 加载sql
        HashMap<SqlEnumeration, String> sqlMap = loadSqlProperties();

        System.out.println(">>>> database verticle start <<<<");
        JsonObject config = new JsonObject().put("url", URL).put("driver_class", DRIVER_CLASS)
          .put("user", USER_NAME).put("password", PASSWORD).put("max_pool_size", MAX_POOL_SIZE);

        jdbcClient = JDBCClient.createShared(vertx,config);
        UserService.create(jdbcClient,sqlMap,ar -> {
            if (ar.succeeded()){
              ProxyHelper.registerService(UserService.class, vertx, ar.result(), ADDRESS);
              startFuture.complete();
            } else {
              startFuture.fail(ar.cause());
            }
        });


    }

    private HashMap<SqlEnumeration, String> loadSqlProperties() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/db.properties");

        Properties properties = new Properties();
        properties.load(inputStream);
        inputStream.close();
        HashMap<SqlEnumeration, String> sqlMap = new HashMap<>();
        sqlMap.put(SqlEnumeration.CREATE_TABLE,properties.getProperty("create-table"));
        sqlMap.put(SqlEnumeration.INSERT_USER,properties.getProperty("insert-user"));
        sqlMap.put(SqlEnumeration.ALL_USER,properties.getProperty("all-user"));
        sqlMap.put(SqlEnumeration.DELETE_USER,properties.getProperty("delete-user"));
        sqlMap.put(SqlEnumeration.SELECT_BY_ID,properties.getProperty("select-by-id"));
        sqlMap.put(SqlEnumeration.UPDATE_USER,properties.getProperty("update-user"));
        sqlMap.put(SqlEnumeration.USERNAME_PASSWORD,properties.getProperty("query-by-username"));

        return sqlMap;
    }
}
