
package com.sulti.diarioweb.servicios;

import com.sulti.diarioweb.comparadores.Comparador;
import com.sulti.diarioweb.entidades.Noticia;
import com.sulti.diarioweb.excepciones.MiExcepcion;
import com.sulti.diarioweb.repositorios.NoticiaRepositorio;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NoticiaServicio {
    
    @Autowired
    private NoticiaRepositorio noticiaRepositorio;
    
    //Crear una noticia.
    
    @Transactional /*Para todos los metodos que generen un cambio en la base de datos.*/
    public void crearNoticia(String titulo, String cuerpo, MultipartFile foto) throws MiExcepcion{ /*Estos datos llegan mediante un formulario.*/
        
        validar(titulo, cuerpo, foto);
        
        Noticia noticia = new Noticia();
        
        noticia.setTitulo(titulo);
        noticia.setCuerpo(cuerpo);
        noticia.setFoto(foto.getOriginalFilename());
        
        noticia.setAlta(new Date());
        
        /*Teniendo la noticia creada, debemos llamar al repositorio para persistir el objeto.*/
        
        noticiaRepositorio.save(noticia);        
        
    }
    
    //Listar todas las noticias que hay en nuestra base de datos.
    
    public List <Noticia> listarTodasNoticias () {        
        List <Noticia> noticias = new ArrayList();
        noticias = noticiaRepositorio.findAll();
        Collections.sort(noticias, Comparador.compararAlta);
        return noticias;        
    }
    
    //Modificar todos los datos de una noticia. Recordar colocarle el @Transactional
    
    @Transactional
    public void modificarNoticiaEntera(Long identificador, String titulo, String cuerpo, MultipartFile foto) throws MiExcepcion{
        
        validar(titulo, cuerpo, foto);
       //validarIdentificador(identificador);
        
        Optional <Noticia> respuesta = noticiaRepositorio.findById(identificador);
        
        if (respuesta.isPresent()) {
            
            Noticia noticia = respuesta.get();
            
            //No seteo ni el alta ni el id.
            noticia.setTitulo(titulo);
            noticia.setCuerpo(cuerpo);
            noticia.setFoto(foto.getOriginalFilename());

            noticiaRepositorio.save(noticia);
        }
    }

    public Noticia getoOne(Long id) {
        return noticiaRepositorio.getOne(id);
    }
   
    //Habilitar o deshabilitar una noticia. @Transactional
    
    
    
    
    //Borrar una noticia. @Transactional.

    @Transactional
    public void eliminarNoticia(Long identificador) throws MiExcepcion{
        /*validarIdentificador(identificador);*/
        Optional <Noticia> respuesta = noticiaRepositorio.findById(identificador);
        if (respuesta.isPresent()) {
            noticiaRepositorio.deleteById(identificador);
        }
    }
    
    
    
    
    
    private void validar(String titulo, String cuerpo, MultipartFile foto) throws MiExcepcion {
        if (titulo.isEmpty() || titulo == null) {
            throw new MiExcepcion("El titulo no puede ser nulo o estar vacio.");
        }
        if (cuerpo.isEmpty() || cuerpo == null) {
            throw new MiExcepcion("El cuerpo no puede ser nulo o estar vacio.");
        }
        if (foto.isEmpty() || foto == null) {
            throw new MiExcepcion("La foto no puede estar vacia.");
        }
    }
    
    /*private void validarIdentificador (Long identificador) throws MiExcepcion{
        if (identificador == null) {
            throw new MiExcepcion("El id no puede estar vacio.");
        }
    }*/

}
