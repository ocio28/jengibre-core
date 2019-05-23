package cl.jengibre.core;

import io.vertx.core.json.JsonObject;

public class Usuario {
  private String token;
  private String email;
  private Integer permisos;
  private String rol;
  private String sucursal;

  public Usuario(JsonObject usuario) {
    if (usuario != null) {
      this.token = usuario.getString("token");
      this.email = usuario.getString("email");
      this.permisos = usuario.getInteger("permisos");
      this.rol = usuario.getString("rol");
      this.sucursal = usuario.getString("sucursal");
    }
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getPermisos() {
    return permisos;
  }

  public void setPermisos(Integer permisos) {
    this.permisos = permisos;
  }

  public String getRol() {
    return rol;
  }

  public void setRol(String rol) {
    this.rol = rol;
  }

  public String getSucursal() {
    return sucursal;
  }

  public void setSucursal(String sucursal) {
    this.sucursal = sucursal;
  }
}