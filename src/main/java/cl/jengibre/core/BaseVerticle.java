package cl.jengibre.core;

import io.vertx.core.Future;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * Created by ocio28 on 03-05-19.
 */
public abstract class BaseVerticle extends AbstractVerticle {
  protected Logger log = LoggerFactory.getLogger(getClass());
  protected EventBus eventBus;

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    eventBus = vertx.eventBus();
    startup(startFuture);
    log.info("{} Inicializado.", getClass().getSimpleName());
  }

  public abstract void startup(Future<Void> startFuture) throws Exception;

  protected void consumer(String address, Consumer<VtxContext> callback, APIParam... params) {
    eventBus.consumer(address, handleEvent(callback, params));
  }

  protected Handler<Message<JsonObject>> handleEvent(Consumer<VtxContext> callback, APIParam... params) {
    return event -> {
      VtxContext context = new VtxContext(event);
      try {
        for (APIParam p : params) {
          if (!p.isValid(context)) {
            return;
          }
        }
        callback.accept(context);
      } catch (Exception e) {
        log.error(event.address(), e);
        context.replyError(e.getMessage());
      }
    };
  }

  protected void send(String address, JsonObject message, VtxContext context, Consumer<VtxContext> callback) {
    this.eventBus.send(address, message, handle(context, callback));
  }

  private Handler<AsyncResult<Message<JsonObject>>> handle(VtxContext context, Consumer<VtxContext> callback) {
    return event -> {
      if (event.succeeded()) {
        JsonObject body = event.result().body();
        if (body.getString("status").equals("success")) {
          context.setEventData(body.getValue("data"));
          try {
            callback.accept(context);
          } catch (Exception e) {
            log.error(event.result().address(), e);
            context.replyError(e.getMessage() != null ? e.getMessage() : "Error Interno");
          }
        } else {
          log.error(body.getString("data"));
          context.replyError(body.getString("data"));
        }
      } else {
        log.error(event.cause().getMessage(), event.cause());
        context.replyError(event.cause().getMessage());
      }
    };
  }
}
