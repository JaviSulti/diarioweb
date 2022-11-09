/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sulti.diarioweb.servicios;

import com.sulti.diarioweb.entidades.Periodista;
import com.sulti.diarioweb.enumeraciones.Rol;
import com.sulti.diarioweb.excepciones.MiExcepcion;
import com.sulti.diarioweb.repositorios.PeriodistaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class PeriodistaServicio implements UserDetailsService{

    @Autowired
    private PeriodistaRepositorio periodistaRepositorio;
    
    @Transactional
    public void registrarNuevoPeriodista(String nombreUsuario, String email, String password, String password2) throws MiExcepcion {
        
        validar(nombreUsuario, email, password, password2);
        
        Periodista periodista = new Periodista();
        
        periodista.setNombreUsuario(nombreUsuario);
        periodista.setPassword(new BCryptPasswordEncoder().encode(password));
        periodista.setEmail(email);
        periodista.setFechaDeAlta(new Date());
        periodista.setRol(Rol.PERIODISTA);
        periodista.setActivo(Boolean.TRUE);
        periodista.setSueldoMensual(0);
        periodista.setMisNoticias(null);
        
        periodistaRepositorio.save(periodista);
        
    }
    
     private void validar(String nombreUsuario, String email, String password, String password2) throws MiExcepcion {
        if (nombreUsuario.isEmpty() || nombreUsuario == null) {
            throw new MiExcepcion("El nombre del periodista no puede ser nulo o estar vacío");
        }
        if (email.isEmpty() || email == null) {
            throw new MiExcepcion("El email no puede ser nulo o estar vacio");
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiExcepcion("La contraseña no puede estar vacía, y debe tener más de 5 dígitos");
        }
        if (!password.equals(password2)) {
            throw new MiExcepcion("Las contraseñas ingresadas deben ser iguales");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       Periodista periodista = periodistaRepositorio.buscarPorEmail(email);
        if (periodista != null) {
            List <GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_"+ periodista.getRol().toString()); //Hasta ahora, tenemos permisos solo para el usuario lector.
            permisos.add(p);
            
            //Aca configuramos para atrapar los datos del usuario que ha ingresado y así utilziarla en las vistas.
            
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("periodistaSession", periodista);
            
            return new User(periodista.getEmail(), periodista.getPassword(), permisos);
        } else {
            return null;
        }
    }

    public List<Periodista> listarPeriodistas() {
        List <Periodista> periodistas = new ArrayList();
        periodistas = periodistaRepositorio.findAll();
        return periodistas;
    }
    

}
