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

    @GetMapping("/listar_cuotas")
    public String listar(Model model) {
        ArrayList<CuotaEntity> cuotas = cuotaService.obtenerCuotas();
        model.addAttribute("cuotas", cuotas);
        return "index_cuotas";
    }

    @PostMapping("/cuota/")
    public CuotaEntity guardar(@RequestBody CuotaEntity cuotaEntityNuevo) {
        return cuotaService.guardarCuota(cuotaEntityNuevo);
    }

    @GetMapping("/eliminar_cuota/{id}")
    public String eliminar(@PathVariable Long id) {
        cuotaService.eliminarCuota(id);
        return "redirect:/listar_cuotas";
    }

    @GetMapping("/generar_cuota/{id}")
    public ModelAndView agregar(@PathVariable Long id, Model model) {
        ModelAndView modelAndView = new ModelAndView("formulario_cuotas");
        Optional<EstudianteEntity> estudianteOptional = estudianteService.obtenerPorId(id);
        if (estudianteOptional.isPresent()) {
            EstudianteEntity estudiante = estudianteOptional.get();

            int descuento = cuotaService.descuentoMontoCuotas(estudiante);
            int cantidadMaxCuotas = cuotaService.cantidadMaxCuotas(estudiante);

            modelAndView.addObject("estudiante", estudiante);
            modelAndView.addObject("descuento", descuento);
            modelAndView.addObject("cantidadMaxCuotas", cantidadMaxCuotas);
        }
        return modelAndView;
    }


    @PostMapping("/guardar_cuota")
    public String guardar(CuotaEntity cuota, Model model) {
        cuotaService.guardarCuota(cuota);
        return "redirect:/listar_cuotas";
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
}
