
package com.sulti.diarioweb.controladores;

import com.sulti.diarioweb.entidades.Noticia;
import com.sulti.diarioweb.excepciones.MiExcepcion;
import com.sulti.diarioweb.servicios.NoticiaServicio;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/noticia")
public class NoticiaControlador {

    @Autowired
    private NoticiaServicio noticiaServicio;

    @GetMapping("/todasNoticias")
    public String index(ModelMap modelo) {
        List<Noticia> noticias = noticiaServicio.listarTodasNoticias();
        modelo.addAttribute("noticias", noticias);
        return "vista_inicio.html";
    }

    @GetMapping("/registrarNoticia") //localhost:8080/registrarNoticia
    public String registrarNoticia() {
        return "noticia_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String tituloNoticia, @RequestParam String cuerpoNoticia,
            @RequestParam(name = "imagenNoticia", required = false) MultipartFile imagenNoticia,
            ModelMap modelo) throws IOException {

        try {

            String ruta = "C://Temp//uploads"; //Aca menciono el lugar donde voy a almancenar las fotos.Es una ruta relativa.
            byte[] bytes = imagenNoticia.getBytes(); //Aca almaceno los bytes de la foto.
            Path rutaAbsoluta = Paths.get(ruta + "//" + imagenNoticia.getOriginalFilename()); //Ruta final: ruta relativa + concatenacion del nombre de la foto.
            Files.write(rutaAbsoluta, bytes); //Aca lo que hago es escribir la ruta y los bytes al file.

            noticiaServicio.crearNoticia(tituloNoticia, cuerpoNoticia, imagenNoticia);
            modelo.put("exito", "La noticia se cargo correctamente.");
            return "vista_panelAdmin.html";

        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            return "noticia_form.html";
        }
    }

    //Me parece que este metodo esta de mas.
    @GetMapping("/administrador")
    public String listarNoticias(ModelMap modelo) {
        List<Noticia> noticias = noticiaServicio.listarTodasNoticias();
        modelo.addAttribute("noticias", noticias);
        return "vista_panelAdmin.html";
    }

    @GetMapping("/editarNoticia/{identificador}")
    public String modificar(@PathVariable Long identificador, ModelMap modelo) {
        modelo.put("noticia", noticiaServicio.getoOne(identificador));
        return "noticia_editar.html";
    }

    @PostMapping("/editarNoticia/{identificador}")
    public String modificar(@PathVariable Long identificador, String titulo, String cuerpo, MultipartFile foto, ModelMap modelo) {
        try {
            noticiaServicio.modificarNoticiaEntera(identificador, titulo, cuerpo, foto);
            modelo.put("exito", "La noticia se modifico correctamente.");
            return "redirect:../administrador";
        } catch (MiExcepcion ex) {
            modelo.put("error", "La noticia no pudo ser modificada.");
            return "noticia_editar.html";
        }

    }

    @GetMapping("/eliminarNoticia/{identificador}")
    public String eliminar(@PathVariable Long identificador) {
        try {
            noticiaServicio.eliminarNoticia(identificador);
            return "redirect:../administrador";
        } catch (MiExcepcion ex) {
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "vista_panelAdmin.html";
        }
    }

    @GetMapping("/leerNoticia/{identificador}")
    public String mostrarNoticia(@PathVariable Long identificador, ModelMap modelo) {
        modelo.put("noticia", noticiaServicio.getoOne(identificador));
        return "leer_noticia.html";
    }

}
