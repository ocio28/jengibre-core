package cl.jengibre.core;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * Created by lokaljost on 18-05-17.
 */
public class VtxContext extends JsonObject {
    public static JsonObject SUCCESS = new JsonObject().put("status", "success")
            .put("data", "success");
    private Message<JsonObject> event;
    private Object eventData;
    private JsonObject body;
    private JsonObject principal;
    private Usuario usuario;
    private int calls = 0;

    public VtxContext(Message<JsonObject> event) {
        this.event = event;
        this.body = event.body();
        this.principal = this.body.getJsonObject("principal");
        this.usuario = new Usuario(this.body.getJsonObject("usuario"));
        this.body.remove("principal");
        mergeIn(event.body());
    }

    public JsonObject principal() {
        return this.principal;
    }

    public Usuario usuario() {
      return this.usuario;
    }

    public int callId() {
        return ++calls;
    }

    public int call() {
        return --calls;
    }

    public void setEventData(Object eventData) {
        this.eventData = eventData;
    }

    public Object eventData() {
        return eventData;
    }

    public JsonObject body() {
        return this.body;
    }

    public void replySuccess() {
        if (this.eventData != null) {
            replySuccess(this.eventData);
        } else {
            event.reply(SUCCESS);
        }
    }

    public void replySuccess(Object data) {
        event.reply(new JsonObject().put("status", "success").put("data", data));
    }

    public void replyError(Object data) {
        event.reply(new JsonObject().put("status", "error").put("data", data));
    }

    public void replyFail(Object data) {
        event.reply(new JsonObject().put("status", "fail").put("data", data));
    }

    public static boolean isSuccess(JsonObject data) {
        return data.containsKey("status") && data.getString("status").equals("success");
    }
}
