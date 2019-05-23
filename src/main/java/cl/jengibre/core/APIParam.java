package cl.jengibre.core;

/**
 * Created by coloro on 29-05-17.
 */
public class APIParam {
    private String name;
    private Class<?> type;
    private boolean optional = false;
    private boolean empty = false;

    public APIParam(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public static APIParam create(String name, Class<?> type) {
        return new APIParam(name, type);
    }

    public APIParam optional() {
        this.optional = true;
        return this;
    }

    public APIParam empty() {
        this.empty = true;
        return this;
    }

    public boolean isValid(VtxContext context) {
        Object value = context.getValue(this.name);
        if (value == null && optional) {
            return true;
        } else if (value != null) {
            if (type.isInstance(value)) {
                if (!this.empty && value instanceof String) {
                    if (((String) value).trim().isEmpty()) {
                        context.replyFail(String.format("EL campo %s no puede ser vacio", this.name));
                        return false;
                    }
                }
                return true;
            } else {
                context.replyFail(String.format("%s es de tipo %s", this.name, this.type.getSimpleName()));
                return false;
            }
        }

        context.replyFail(String.format("El campo %s es requerido.", this.name));
        return false;
    }
}
