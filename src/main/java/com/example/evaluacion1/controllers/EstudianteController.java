package com.example.evaluacion1.controllers;

import com.example.evaluacion1.entities.EstudianteEntity;
import com.example.evaluacion1.services.CuotaService;
import com.example.evaluacion1.services.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping
public class EstudianteController {
    @Autowired
    EstudianteService estudianteService;
    @Autowired
    CuotaService cuotaService;

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

    @GetMapping("/lista_estudiantes_para_resumen")
    public String listarEstudiantesParaResumen(Model model) {
        ArrayList<EstudianteEntity> estudiantes = estudianteService.obtenerEstudiantes();
        ArrayList<EstudianteEntity> estudiantesConCuotas = estudianteService.obtenerEstudiantesConCuotas(estudiantes);
        model.addAttribute("estudiantes", estudiantesConCuotas);
        return "lista_estudiantes_para_resumen";
    }

    @GetMapping("/resumen/{id}")
    public ModelAndView resumen(@PathVariable Long id, Model model) {
        ModelAndView modelAndView = new ModelAndView("resumen");

        Optional<EstudianteEntity> estudianteOptional = estudianteService.obtenerPorId(id);
        if(estudianteOptional.isPresent()){
            EstudianteEntity estudiante = estudianteOptional.get();
            int promedioExamenesTotales = estudianteService.calcularPromedioTotalExamenes(estudiante);
            int montoTotalArancelPagar = estudianteService.montoTotalArancelPagar(estudiante);
            String tipoPago = estudianteService.tipoPago(estudiante);
            int numeroCuotasPactadas = estudianteService.numeroCuotasPactadas(estudiante);
            int numeroCuotasPagadas = estudianteService.numeroCuotasPagadas(estudiante);
            int montoTotalPagado = estudianteService.montoTotalPagado(estudiante);
            Date fechaUltimoPago = estudianteService.fechaUltimoPago(estudiante);
            int saldoPorPagar = estudianteService.saldoPorPagar(estudiante);
            int numeroCuotasConRetraso = estudianteService.numeroCuotasConRetraso(estudiante);
            modelAndView.addObject("numeroCuotasConRetraso", numeroCuotasConRetraso);
            modelAndView.addObject("saldoPorPagar", saldoPorPagar);
            modelAndView.addObject("fechaUltimoPago", fechaUltimoPago);
            modelAndView.addObject("montoTotalPagado", montoTotalPagado);
            modelAndView.addObject("numeroCuotasPagadas", numeroCuotasPagadas);
            modelAndView.addObject("numeroCuotasPactadas", numeroCuotasPactadas);
            modelAndView.addObject("tipoPago", tipoPago);
            modelAndView.addObject("montoTotalArancelPagar", montoTotalArancelPagar);
            modelAndView.addObject("promedioExamenesTotales", promedioExamenesTotales);
            modelAndView.addObject("estudiante", estudiante);
        }
        return modelAndView;
    }

}
