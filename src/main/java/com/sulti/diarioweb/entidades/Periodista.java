
package com.sulti.diarioweb.entidades;

import com.sulti.diarioweb.enumeraciones.Rol;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.Entity;

@Entity
public class Periodista extends Usuario{
    
    ArrayList <Noticia> misNoticias;
    Integer sueldoMensual;

    public Periodista() {
    }

    public Periodista(ArrayList<Noticia> misNoticias, Integer sueldoMensual) {
        this.misNoticias = misNoticias;
        this.sueldoMensual = sueldoMensual;
    }    

    public Periodista(ArrayList<Noticia> misNoticias, Integer sueldoMensual, Long id, String nombreUsuario, String password, String password2, String email, Date fechaDeAlta, Rol rol, Boolean activo) {
        super(id, nombreUsuario, password, password2, email, fechaDeAlta, rol, activo);
        this.misNoticias = misNoticias;
        this.sueldoMensual = sueldoMensual;
    }

    public ArrayList<Noticia> getMisNoticias() {
        return misNoticias;
    }

    public void setMisNoticias(ArrayList<Noticia> misNoticias) {
        this.misNoticias = misNoticias;
    }

    public Integer getSueldoMensual() {
        return sueldoMensual;
    }

    public void setSueldoMensual(Integer sueldoMensual) {
        this.sueldoMensual = sueldoMensual;
    }

    @Override
    public String toString() {
        return "Periodista{" + "misNoticias=" + misNoticias + ", sueldoMensual=" + sueldoMensual + '}';
    }    
    
}
