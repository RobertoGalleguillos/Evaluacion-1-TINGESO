package com.example.evaluacion1.services;
import com.example.evaluacion1.entities.EstudianteEntity;
import com.example.evaluacion1.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class EstudianteService {
    @Autowired
    EstudianteRepository estudianteRepository;

    public ArrayList<EstudianteEntity> obtenerEstudiantes(){
        return (ArrayList<EstudianteEntity>) estudianteRepository.findAll();
    }

    public EstudianteEntity guardarEstudiante(EstudianteEntity estudiante){
        return estudianteRepository.save(estudiante);
    }

    public Optional<EstudianteEntity> obtenerPorId(Long id){
        return estudianteRepository.findById(id);
    }

    public EstudianteEntity obtenerPorRut(String rut){
        return estudianteRepository.findByRut(rut);
    }


    public boolean eliminarEstudiante( Long id) {
        try{
            estudianteRepository.deleteById(id);
            return true;
        }catch(Exception err){
            return false;
        }
    }


}
