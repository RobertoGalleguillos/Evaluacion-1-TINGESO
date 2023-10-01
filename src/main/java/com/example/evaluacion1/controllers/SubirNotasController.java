package com.example.evaluacion1.controllers;

import com.example.evaluacion1.entities.SubirNotasEntity;
import com.example.evaluacion1.services.SubirNotasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

@Controller
@RequestMapping
public class SubirNotasController {

    @Autowired
    private SubirNotasService subirData;

    @GetMapping("/subirNotas")
    public String main() {
        return "index_subirNotas";
    }

    @PostMapping("/subirNotas")
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        subirData.guardar(file);
        redirectAttributes.addFlashAttribute("mensaje", "¡Archivo cargado correctamente!");
        subirData.leerCsv("Notas.csv");
        return "redirect:/subirNotas";
    }

    @GetMapping("/notasInformation")
    public String listar(Model model) {
        ArrayList<SubirNotasEntity> notas = subirData.obtenerData();
        model.addAttribute("notas", notas);
        return "notasInformation";
    }

    @PostMapping("/eliminarNotas")
    public String eliminarTodaLaData() {
        ArrayList<SubirNotasEntity> notas = subirData.obtenerData();
        if (!notas.isEmpty()) {
            subirData.eliminarData(notas);
        }
        return "redirect:/subirNotas";
    }
}