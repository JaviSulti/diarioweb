
package com.sulti.diarioweb.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdministradorControlador {
    
    @GetMapping("/dashboard") //Panel de administracion.
    public String panelAdministrativo(){
        return "vista_panelAdmin.html";
    }
    
}
