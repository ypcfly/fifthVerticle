package com.ypc.fifthVerticle.vercicle;

import com.google.gson.Gson;
import com.ypc.fifthVerticle.entity.User;
import com.ypc.fifthVerticle.service.UserService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;

public class HttpServerVerticle extends AbstractVerticle {

    private final FreeMarkerTemplateEngine templateEngine = FreeMarkerTemplateEngine.create();

    private static final String ADDRESS = "com.ypc";

    private WebClient webClient;

    private UserService userService;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        System.out.println(">>>> http verticle start <<<<");

        userService = UserService.createProxy(vertx,ADDRESS);

        HttpServer httpServer = vertx.createHttpServer(new HttpServerOptions()
                                .setSsl(true)
                                .setKeyStoreOptions(new JksOptions().setPath("server-keystore.jks").setPassword("123456")));

        Router router = Router.router(vertx);
        router.route().last().handler(this::errorHandler);

        router.route().failureHandler(this::failureHandler);
        // 不配置拿不到表单数据
        router.route().handler(BodyHandler.create());

        router.get("/user/:id").handler(this::queryByIdHandler);
        router.post("/user/insert").handler(this::insertHandler);
        router.post("/user/delete").handler(this::deleteHandler);
        router.post("/user/update").handler(this::updateHandler);
        router.get("/login").handler(this::loginHandler);
        router.post("/loginAction").handler(this::loginActionHandler);
        router.route("/success").handler(this::successHandler);

        httpServer.requestHandler(router::accept).listen(8090,ar -> {
          if (ar.succeeded()){
            startFuture.succeeded();
            System.out.println(">>>> http server start at 8090 <<<<");
          } else {
            startFuture.fail(ar.cause());
          }
        });
  
    }

  private void successHandler(RoutingContext routingContext) {
      System.out.println(">>>> success handler <<<<");
      routingContext.put("title","成功页面");
      templateEngine.render(routingContext,"templates","/success.ftl",ar -> {
          if (ar.succeeded()){
              routingContext.response().putHeader("content-type","text/html");
              routingContext.response().end(ar.result());
          } else {
              routingContext.fail(ar.cause());
          }
      });

  }

  private void failureHandler(RoutingContext routingContext) {
    routingContext.put("title","系统内部异常");
    int code = routingContext.response().getStatusCode();
    templateEngine.render(routingContext,"templates","/500.ftl",ar -> {
      if (ar.succeeded()){
        routingContext.response().putHeader("content-type","text/html");
        routingContext.response().end(ar.result());
      } else {
        routingContext.fail(ar.cause());
      }
    });

  }

  private void errorHandler(RoutingContext routingContext) {
      System.out.println(">>>> 404 error handler <<<<");
      routingContext.put("title","404页面");
      templateEngine.render(routingContext,"templates","/404.ftl",ar -> {
          if (ar.succeeded()){
              routingContext.response().putHeader("content-type","text/html");
              routingContext.response().end(ar.result());
          } else {
              routingContext.fail(ar.cause());
          }
      });
  }

  private void loginActionHandler(RoutingContext routingContext) {
      System.out.println(">>>> login action handler <<<<");
      String username = routingContext.request().getParam("username");
      String password = routingContext.request().getParam("password");
      userService.queryByUserName(username,password,res -> {
          if (res.succeeded()){
              JsonObject jsonObject = res.result();
              Gson gson = new Gson();
              User user = gson.fromJson(String.valueOf(jsonObject), User.class);
              List<User> list = new ArrayList<>();
              list.add(user);
              boolean flag = false;
              if (user != null && "manager".equals(user.getRole())){
                  flag = true;
              }
              routingContext.put("title","用户首页");
              routingContext.put("list",list);
              routingContext.put("flag",flag);
              templateEngine.render(routingContext,"templates","/home.ftl",ar -> {
                  if (ar.succeeded()){
                      routingContext.response().putHeader("content-type","text/html");
                      routingContext.response().end(ar.result());
                  } else {
                      routingContext.fail(ar.cause());
                  }
              });
          } else {
              routingContext.fail(res.cause());
          }

      });

  }


  private void loginHandler(RoutingContext routingContext) {
    System.out.println(">>>> login handler <<<<");

    routingContext.put("title","登录页面");
    templateEngine.render(routingContext,"templates","/login.ftl",ar -> {
      if (ar.succeeded()){
        routingContext.response().putHeader("content-type","text/html");
        routingContext.response().end(ar.result());
      } else {
        routingContext.fail(ar.cause());
      }
    });

  }

  private void updateHandler(RoutingContext routingContext) {
  }

  private void deleteHandler(RoutingContext routingContext) {
  }

  private void insertHandler(RoutingContext routingContext) {
      System.out.println(">>>> insert user handler <<<<");
      String username = routingContext.request().getParam("username");
      String password = routingContext.request().getParam("password");
      String sex = routingContext.request().getParam("sex");
      String mobile = routingContext.request().getParam("mobile");
      String address = routingContext.request().getParam("address");
      JsonArray jsonArray = new JsonArray().add(username).add(password).add(address).add(mobile).add(sex);
      userService.insertNewUser(jsonArray,res -> {
          if (res.succeeded()){
              Integer insert = res.result();
              routingContext.response().setStatusCode(303);
              routingContext.response().putHeader("Location","/success");
              routingContext.response().end();
          } else {
              routingContext.fail(res.cause());
          }
      });
  }

  private void queryByIdHandler(RoutingContext routingContext) {
    Integer id = Integer.valueOf(routingContext.request().getParam("id"));
    routingContext.put("title","用户详情");
    userService.selectUserById(id,res -> {
      if (res.succeeded()){
        JsonObject jsonObject = res.result();
        System.out.println(jsonObject);
        routingContext.put("title"," 用户首页");
        templateEngine.render(routingContext,"templates","/user.ftl",ar -> {
          if (ar.succeeded()){
            routingContext.response().putHeader("content-type","text/html");
            routingContext.response().end(ar.result());
          } else {
            routingContext.fail(ar.cause());
          }
        });
      } else {
        routingContext.fail(res.cause());
      }

    });

  }

}
