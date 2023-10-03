package com.example.evaluacion1.controllers;

import com.example.evaluacion1.entities.CuotaEntity;
import com.example.evaluacion1.entities.EstudianteEntity;
import com.example.evaluacion1.services.CuotaService;
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
public class CuotaController {
    @Autowired
    CuotaService cuotaService;
    @Autowired
    EstudianteService estudianteService;

    @GetMapping("/listar_cuotas/{id}")
    public String listar(@PathVariable Long id,Model model) {
        Optional<EstudianteEntity> estudianteOptional = estudianteService.obtenerPorId(id);
        if (estudianteOptional.isPresent()) {
            EstudianteEntity estudiante = estudianteOptional.get();
            ArrayList<CuotaEntity> cuotas = cuotaService.obtenerCuotasPorRut(estudiante.getRut());
            model.addAttribute("cuotas", cuotas);
        }
        return "index_cuotas";
    }

    @GetMapping("/listar_estudiantes_para_ver_cuotas")
    public String listarEstudiantesParaVerCuotas(Model model) {
        ArrayList<EstudianteEntity> todosLosEstudiantes = estudianteService.obtenerEstudiantes();
        ArrayList<EstudianteEntity> estudiantesConCuotas = new ArrayList<>();

        for (EstudianteEntity estudiante : todosLosEstudiantes) {
            if (cuotaService.existeCuota(estudiante.getRut())) {
                estudiantesConCuotas.add(estudiante);
            }
        }
        model.addAttribute("estudiantes", estudiantesConCuotas);

        return "lista_estudiantes_cuota";
    }



    @GetMapping("/eliminar_cuotas/{id}")
    public String eliminar(@PathVariable Long id) {
        Optional<EstudianteEntity> estudianteOptional = estudianteService.obtenerPorId(id);
        if (estudianteOptional.isPresent()) {
            EstudianteEntity estudiante = estudianteOptional.get();
            cuotaService.eliminarCuotasPorRut(estudiante.getRut());
        }
        return "redirect:/listar_estudiantes_para_ver_cuotas";
    }



    @GetMapping("/generar_cuota/{id}")
    public ModelAndView agregar(@PathVariable Long id, Model model) {
        ModelAndView modelAndView = new ModelAndView("formulario_cuotas");

        Optional<EstudianteEntity> estudianteOptional = estudianteService.obtenerPorId(id);
        if (estudianteOptional.isPresent()) {
            EstudianteEntity estudiante = estudianteOptional.get();

            boolean cuotasExisten = cuotaService.existeCuota(estudiante.getRut());

            if (cuotasExisten) {
                modelAndView = new ModelAndView("redirect:/generar_cuotas");
            } else {
                int descuento = cuotaService.descuentoMontoCuotas(estudiante);
                int cantidadMaxCuotas = cuotaService.cantidadMaxCuotas(estudiante);
                modelAndView = new ModelAndView("formulario_cuotas");
                modelAndView.addObject("estudiante", estudiante);
                modelAndView.addObject("descuento", descuento);
                modelAndView.addObject("cantidadMaxCuotas", cantidadMaxCuotas);
            }
        }

        return modelAndView;
    }


    @PostMapping("/guardar_cuotas")
    public String generarCuotas(
            @RequestParam("rut") String rut,
            @RequestParam("cantidadCuotasSeleccionadas") int cantidadCuotas,
            @RequestParam("montoTotal") int montoTotal) {

        cuotaService.generarCuotas(cantidadCuotas, montoTotal, rut);

        return "redirect:/generar_cuotas";
    }

    @PostMapping("/guardar_pago_al_contado")
    public String guardarPagoAlContado(
            @RequestParam("rut") String rut) {
        cuotaService.generarPagoAlContado(rut);

        return "redirect:/generar_cuotas";
    }

    @GetMapping("/editar_cuota/{id}")
    public ModelAndView editar(@PathVariable Long id, Model model) {
        ModelAndView modelAndView = new ModelAndView("editar_cuota");
        Optional<CuotaEntity> cuotaOptional = cuotaService.obtenerPorId(id);
        if(cuotaOptional.isPresent()){
            CuotaEntity cuota = cuotaOptional.get();
            modelAndView.addObject("cuota", cuota);
        }
        return modelAndView;
    }

    @GetMapping("/pagar_al_contado/{id}")
    public ModelAndView pagarAlContado(@PathVariable Long id, Model model) {
        ModelAndView modelAndView = new ModelAndView("formulario_caso_al_contado");
        Optional<EstudianteEntity> estudianteOptional = estudianteService.obtenerPorId(id);
        if (estudianteOptional.isPresent()) {
            EstudianteEntity estudiante = estudianteOptional.get();
            boolean cuotasExisten = cuotaService.existeCuota(estudiante.getRut());
            if (cuotasExisten) {
                modelAndView = new ModelAndView("redirect:/generar_cuotas");
            } else {
                modelAndView.addObject("estudiante", estudiante);
            }
        }
        return modelAndView;
    }

    @GetMapping("/pagar_cuota/{id}")
    public String pagarCuota(@PathVariable Long id, Model model) {
        Optional<CuotaEntity> cuotaOptional = cuotaService.obtenerPorId(id);
        if(cuotaOptional.isPresent()){
            CuotaEntity cuota = cuotaOptional.get();
            cuotaService.pagarCuota(cuota);
        }
        return "redirect:/listar_estudiantes_para_ver_cuotas";

    }
}
