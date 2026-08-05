package com.nocta.myown.controller.web;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nocta.myown.entity.Usuario;
import com.nocta.myown.enums.EstadoEspecialidadPendiente;
import com.nocta.myown.exception.RecursoNoEncontradoException;
import com.nocta.myown.repository.UsuarioRepository;
import com.nocta.myown.request.RechazarEspecialidadPendienteRequest;
import com.nocta.myown.response.AdminEspecialidadPendienteResponse;
import com.nocta.myown.service.EspecialidadPendienteService;

@Controller
@RequestMapping("/admin/especialidades")
public class AdminEspecialidadWebController {

    private final EspecialidadPendienteService especialidadPendienteService;
    private final UsuarioRepository usuarioRepository;

    public AdminEspecialidadWebController( EspecialidadPendienteService especialidadPendienteService,
            UsuarioRepository usuarioRepository) {
        this.especialidadPendienteService = especialidadPendienteService;

        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String listar(@RequestParam( required = false, defaultValue = "PENDIENTE") EstadoEspecialidadPendiente estado,
            Authentication authentication, Model model ) {
    	
        List<AdminEspecialidadPendienteResponse> sugerencias = especialidadPendienteService
        		.obtenerSugerenciasAdmin(estado);

        model.addAttribute("sugerencias", sugerencias);
        model.addAttribute("estadoSeleccionado", estado);
        model.addAttribute("adminEmail", authentication != null  ? authentication.getName() : "");

        return "admin/especialidades";
    }

    @PostMapping("/{id}/aprobar")
    public String aprobar(
            @PathVariable Integer id, Authentication authentication, RedirectAttributes redirectAttributes) {
        
    	Usuario administrador = obtenerAdministrador(authentication);

        especialidadPendienteService.aprobarSugerencia(id, administrador.getUsuarioId());

        redirectAttributes.addFlashAttribute( "mensajeExito", "La especialidad fue aprobada correctamente.");

        return "redirect:/admin/especialidades?estado=PENDIENTE";
    }

    @PostMapping("/{id}/rechazar")
    public String rechazar(@PathVariable Integer id,  @RequestParam String motivo, Authentication authentication,
            RedirectAttributes redirectAttributes) {
    	
        Usuario administrador = obtenerAdministrador(authentication);

        RechazarEspecialidadPendienteRequest request =  new RechazarEspecialidadPendienteRequest(motivo);

        especialidadPendienteService.rechazarSugerencia(id, administrador.getUsuarioId(), request);

        redirectAttributes.addFlashAttribute("mensajeExito", "La sugerencia fue rechazada correctamente.");

        return "redirect:/admin/especialidades?estado=PENDIENTE";
    }

    private Usuario obtenerAdministrador( Authentication authentication) {
        String email = authentication.getName();

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("No se encontró el administrador autenticado")
                );
    }
}