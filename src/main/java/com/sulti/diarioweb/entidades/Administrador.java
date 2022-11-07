
package com.sulti.diarioweb.entidades;

import com.sulti.diarioweb.enumeraciones.Rol;
import java.util.Date;
import javax.persistence.Entity;

@Entity
public class Administrador extends Usuario{

    public Administrador() {
    }

    public Administrador(Long id, String nombreUsuario, String password, String password2, String email, Date fechaDeAlta, Rol rol, Boolean activo) {
        super(id, nombreUsuario, password, password2, email, fechaDeAlta, rol, activo);
    }

    @Override
    public String toString() {
        return "Administrador{" + '}';
    }   
    
}
