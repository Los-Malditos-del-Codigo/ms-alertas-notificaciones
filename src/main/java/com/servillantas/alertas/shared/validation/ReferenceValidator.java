package com.servillantas.alertas.shared.validation;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import java.util.UUID;
@Component public class ReferenceValidator {
 private final JdbcClient jdbc; public ReferenceValidator(JdbcClient jdbc){this.jdbc=jdbc;}
 public void producto(UUID id){validar("select count(*) from productos where id=:id and activo=true",id,"Producto inexistente o inactivo");}
 public void usuario(UUID id){validar("select count(*) from perfiles where id=:id and activo=true",id,"Usuario inexistente o inactivo");}
 private void validar(String sql,UUID id,String msg){Long n=jdbc.sql(sql).param("id",id).query(Long.class).single();if(n==null||n==0)throw new ResponseStatusException(HttpStatus.NOT_FOUND,msg+": "+id);}
}
