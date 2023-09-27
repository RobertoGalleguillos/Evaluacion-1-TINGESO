package com.example.evaluacion1.services;
import com.example.evaluacion1.entities.CuotaEntity;
import com.example.evaluacion1.entities.EstudianteEntity;
import com.example.evaluacion1.repositories.CuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
        int montoOriginal = 1500000;
        int year_actual = LocalDate.now().getYear();
        if(Objects.equals(estudiante.getTipoColegioProcedencia(), "Municipal")){
            montoTotal = montoTotal - (montoOriginal*20/100);
        }
        if(Objects.equals(estudiante.getTipoColegioProcedencia(), "Subvencionado")){
            montoTotal = montoTotal - (montoOriginal*10/100);
        }
        if(year_actual - estudiante.getAnioEgreso()==0){
            montoTotal = montoTotal - (montoOriginal*15/100);
        }
        if((year_actual - estudiante.getAnioEgreso()==1) || (year_actual - estudiante.getAnioEgreso()==2)){
            montoTotal = montoTotal - (montoOriginal*8/100);
        }
        if((year_actual - estudiante.getAnioEgreso()==3) || (year_actual - estudiante.getAnioEgreso()==4)){
            montoTotal = montoTotal - (montoOriginal*4/100);
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

    public void generarCuotas(int cantidadCuotasSeleccionada, int montoTotal, String rut){
        int montoCuota = montoTotal / cantidadCuotasSeleccionada;
        Date fechaVencimiento = new Date();

        for(int i = 0; i < cantidadCuotasSeleccionada; i++){
            fechaVencimiento = sumarMesesAFecha(fechaVencimiento, 1);
            CuotaEntity cuota = new CuotaEntity();
            cuota.setRut(rut);
            cuota.setFechaVencimiento(fechaVencimiento);
            cuota.setPagado(false);
            cuota.setDescuento(0);
            cuota.setInteres(0);
            cuota.setMontoCuota(montoCuota);
            cuotaRepository.save(cuota);
        }
    }

    private Date sumarMesesAFecha(Date fecha, int meses) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.MONTH, meses);
        return calendar.getTime();
    }

    public boolean existeCuota(String rut) {
        Long count = cuotaRepository.countByRut(rut);
        return count > 0;
    }

}

