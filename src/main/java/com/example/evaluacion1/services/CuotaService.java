package com.example.evaluacion1.services;
import com.example.evaluacion1.entities.CuotaEntity;
import com.example.evaluacion1.entities.EstudianteEntity;
import com.example.evaluacion1.repositories.CuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.time.LocalDate;
@Service
public class CuotaService {
    @Autowired
    CuotaRepository cuotaRepository;

    public ArrayList<CuotaEntity> obtenerCuotas() {
        return (ArrayList<CuotaEntity>) cuotaRepository.findAll();
    }

    public CuotaEntity guardarCuota(CuotaEntity cuota) {
        return cuotaRepository.save(cuota);
    }

    public Optional<CuotaEntity> obtenerPorId(Long id) {
        return cuotaRepository.findById(id);
    }

    public CuotaEntity obtenerPorRut(String rut) {
        return cuotaRepository.findByRut(rut);
    }

    public boolean eliminarCuota(Long id) {
        try {
            cuotaRepository.deleteById(id);
            return true;
        } catch (Exception err) {
            return false;
        }
    }

    public int descuentoMontoCuotas(EstudianteEntity estudiante){
        int montoTotal = 1500000;
        int year_actual = LocalDate.now().getYear();
        if(Objects.equals(estudiante.getTipoColegioProcedencia(), "Municipal")){
            montoTotal = montoTotal - (montoTotal*20/100);
        }
        if(Objects.equals(estudiante.getTipoColegioProcedencia(), "Subvencionado")){
            montoTotal = montoTotal - (montoTotal*10/100);
        }
        if(year_actual - estudiante.getAnioEgreso()==0){
            montoTotal = montoTotal - (montoTotal*15/100);
        }
        if((year_actual - estudiante.getAnioEgreso()==1) || (year_actual - estudiante.getAnioEgreso()==2)){
            montoTotal = montoTotal - (montoTotal*8/100);
        }
        if((year_actual - estudiante.getAnioEgreso()==3) || (year_actual - estudiante.getAnioEgreso()==4)){
            montoTotal = montoTotal - (montoTotal*4/100);
        }
        return montoTotal;
    }

    public int cantidadMaxCuotas(EstudianteEntity estudiante){
        int cantidadMaxCuotas = 0;
        if(Objects.equals(estudiante.getTipoColegioProcedencia(), "Municipal")){
            cantidadMaxCuotas = 10;
        }
        if(Objects.equals(estudiante.getTipoColegioProcedencia(), "Subvencionado")){
            cantidadMaxCuotas = 7;
        }
        if(Objects.equals(estudiante.getTipoColegioProcedencia(), "Privado")){
            cantidadMaxCuotas = 4;
        }
        return cantidadMaxCuotas;
    }
}

