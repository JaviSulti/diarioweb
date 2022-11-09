
package com.sulti.diarioweb.controladores;

import com.sulti.diarioweb.entidades.Usuario;
import com.sulti.diarioweb.excepciones.MiExcepcion;
import com.sulti.diarioweb.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @GetMapping("/")
    public String inicio() {
        return "vista_primera.html";
    }  

    //Aca vamos a registrar a todos los usuarios.
    @GetMapping("/registrar")
    public String registrar() {
        return "registro.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombreUsuario, @RequestParam String email,
            @RequestParam String password, String password2, ModelMap modelo) {
        try {
            usuarioServicio.registrarNuevoUsuario(nombreUsuario, email, password, password2);
            modelo.put("exito", "Usuario registrado exitosamente.");
            return "vista_primera.html";
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombreUsuario", nombreUsuario);
            modelo.put("email", email);
            return "registro.html";
        }
    }

    //Por aca van a acceder todos los usuarios que se han registrado.
    @GetMapping("/login")
    public String login(@RequestParam (required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario, mail o contrasenia incorrecta.");
        }
        return "login.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_PERIODISTA', 'ROLE_ADMINISTRADOR')")
    @GetMapping("/inicio")
    public String inicio2(HttpSession session) {
        
        Usuario loqueado = (Usuario) session.getAttribute("usuarioSession");
        /* Asi estaba antes
        if (loqueado.getRol().toString().equals("ADMINISTRADOR")) {
            return "redirect:/admin/dashboard";
        }
         */        
        
        if (loqueado.getRol().toString().equals("ADMINISTRADOR")||loqueado.getRol().toString().equals("PERIODISTA")) {
            return "redirect:/admin/dashboard";
        }
        
        return "inicio.html";
    }
    
}
