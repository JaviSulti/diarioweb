
package com.sulti.diarioweb.controladores;

import com.sulti.diarioweb.entidades.Noticia;
import com.sulti.diarioweb.entidades.Periodista;
import com.sulti.diarioweb.entidades.Usuario;
import com.sulti.diarioweb.excepciones.MiExcepcion;
import com.sulti.diarioweb.servicios.NoticiaServicio;
import com.sulti.diarioweb.servicios.PeriodistaServicio;
import com.sulti.diarioweb.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdministradorControlador {
    
    @Autowired
    private NoticiaServicio noticiaServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private PeriodistaServicio periodistaServicio;
    
    @GetMapping("/dashboard") //Panel de administracion.
    public String panelAdministrativo(ModelMap modelo) {
        List<Noticia> noticias = noticiaServicio.listarTodasNoticias();
        modelo.addAttribute("noticias", noticias);
        return "vista_panelAdmin.html";
    }

    @GetMapping("/lectores")
    public String mostrarLectores(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        return "vista_usuarios.html";
    }

    @PostMapping("/editarNombre/{id}")
    public String modificarNombre(@PathVariable Long id, String nombreUsuario, ModelMap modelo) {
        try {
            usuarioServicio.modificarNombre(id, nombreUsuario);
            modelo.put("exito", "El nombre se modifico correctamente.");
            return "redirect:../lectores";
        } catch (MiExcepcion ex) {
            modelo.put("error", "El nombre no pudo ser modificado.");
            return "vista_usuarios.html";
        }
    }

    @GetMapping("/registrarPeriodista")
    public String registrarPeriodista() {
        return "registro_periodista.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombreUsuario, @RequestParam String email,
            @RequestParam String password, String password2, ModelMap modelo) {
        try {
            periodistaServicio.registrarNuevoPeriodista(nombreUsuario, email, password, password2);
            modelo.put("exito", "Usuario registrado exitosamente.");
            return "vista_primera.html";
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombreUsuario", nombreUsuario);
            modelo.put("email", email);
            return "vista_panelAdmin.html";
        }
    }

    @GetMapping("/periodistas")
    public String mostrarPeriodistas(ModelMap modelo) {
        List<Periodista> periodistas = periodistaServicio.listarPeriodistas();
        modelo.addAttribute("periodistas", periodistas);
        return "vista_periodistas.html";
    }

}
