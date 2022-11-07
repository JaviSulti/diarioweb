/*
Debemos tener los metodos para guardar, actualizar, realziar consulta o dar de baja de las noticias EN LA BASE DE DATOS.
OJO> La creacion de noticias se hace en el servicio.
 */
package com.sulti.diarioweb.repositorios;

import com.sulti.diarioweb.entidades.Noticia;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface NoticiaRepositorio extends JpaRepository <Noticia, Long>{
    
}
