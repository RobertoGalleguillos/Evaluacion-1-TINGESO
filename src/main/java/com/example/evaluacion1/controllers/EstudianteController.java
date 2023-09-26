package com.example.evaluacion1.controllers;

import com.example.evaluacion1.entities.EstudianteEntity;
import com.example.evaluacion1.services.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping
public class EstudianteController {
    @Autowired
    EstudianteService estudianteService;

    @GetMapping("/listar")
    public String listar(Model model) {
        ArrayList<EstudianteEntity> estudiantes = estudianteService.obtenerEstudiantes();
        model.addAttribute("estudiantes", estudiantes);
        return "index";
    }

    @PostMapping("/estudiante/")
    public EstudianteEntity guardar(@RequestBody EstudianteEntity estudianteEntityNuevo) {
        return estudianteService.guardarEstudiante(estudianteEntityNuevo);
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        estudianteService.eliminarEstudiante(id);
        return "redirect:/listar";
    }

    @GetMapping("/agregar")
    public String agregar(Model model) {
        model.addAttribute("estudiante", new EstudianteEntity());
        return "formulario";
    }

    @PostMapping("/guardar")
    public String guardar(EstudianteEntity estudiante, Model model) {
        estudianteService.guardarEstudiante(estudiante);
        return "redirect:/listar";
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable Long id, Model model) {
        ModelAndView modelAndView = new ModelAndView("editar_estudiante");
        Optional<EstudianteEntity> estudianteOptional = estudianteService.obtenerPorId(id);
        if(estudianteOptional.isPresent()){
            EstudianteEntity estudiante = estudianteOptional.get();
            modelAndView.addObject("estudiante", estudiante);
        }
        return modelAndView;
    }

    @GetMapping("/generar_cuotas")
    public String buscar(Model model) {
        ArrayList<EstudianteEntity> estudiantes = estudianteService.obtenerEstudiantes();
        model.addAttribute("estudiantes", estudiantes);
        return "generar_cuotas";
    }

}
