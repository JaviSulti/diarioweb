
package com.sulti.diarioweb.servicios;

import com.sulti.diarioweb.entidades.Usuario;
import com.sulti.diarioweb.enumeraciones.Rol;
import com.sulti.diarioweb.repositorios.UsuarioRepositorio;
import java.util.Date;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sulti.diarioweb.excepciones.MiExcepcion;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioServicio implements UserDetailsService{
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Transactional
    public void registrarNuevoUsuario(String nombreUsuario, String email, String password, String password2) throws MiExcepcion {
        
        validar(nombreUsuario, email, password, password2);
        
        Usuario usuario = new Usuario();
        
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setEmail(email);
        usuario.setFechaDeAlta(new Date());
        usuario.setRol(Rol.USUARIO);
        usuario.setActivo(Boolean.TRUE);
        
        usuarioRepositorio.save(usuario);
        
    }
    
     private void validar(String nombreUsuario, String email, String password, String password2) throws MiExcepcion {
        if (nombreUsuario.isEmpty() || nombreUsuario == null) {
            throw new MiExcepcion("El nombre de usuario no puede ser nulo o estar vac??o");
        }
        if (email.isEmpty() || email == null) {
            throw new MiExcepcion("El email no puede ser nulo o estar vacio");
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiExcepcion("La contrase??a no puede estar vac??a, y debe tener m??s de 5 d??gitos");
        }
        if (!password.equals(password2)) {
            throw new MiExcepcion("Las contrase??as ingresadas deben ser iguales");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
       Usuario usuario = usuarioRepositorio.buscarPorEmail(nombreUsuario);
        if (usuario != null) {
            List <GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_"+ usuario.getRol().toString()); //Hasta ahora, tenemos permisos solo para el usuario lector.
            permisos.add(p);
            
            //Aca configuramos para atrapar los datos del usuario que ha ingresado y as?? utilziarla en las vistas.
            
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuarioSession", usuario);
            
            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }
    }

    public List<Usuario> listarUsuarios() {
        List <Usuario> usuarios = new ArrayList();
        usuarios = usuarioRepositorio.findAll();
        return usuarios;
    }

    public Usuario getoOne(Long id) {
        return usuarioRepositorio.getOne(id);
    }

    public void modificarNombre(Long id, String nombreUsuario) throws MiExcepcion {

        validar(nombreUsuario);
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();
            usuario.setNombreUsuario(nombreUsuario);
            usuarioRepositorio.save(usuario);
        }
    }

    private void validar(String nombreUsuario) throws MiExcepcion {
        if (nombreUsuario.isEmpty() || nombreUsuario == null) {
            throw new MiExcepcion("El titulo no puede ser nulo o estar vacio.");
        }
    }
}
