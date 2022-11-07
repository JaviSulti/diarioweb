
package com.sulti.diarioweb.comparadores;

import com.sulti.diarioweb.entidades.Noticia;
import java.util.Comparator;

public class Comparador {
    
    public static Comparator <Noticia> compararAlta = new Comparator <Noticia> () {
        @Override
        public int compare(Noticia t, Noticia t1) {
            return t1.getIdentificador().compareTo(t.getIdentificador());
        }
    };
    
}
