package com.example.evaluacion1.services;
import com.example.evaluacion1.entities.CuotaEntity;
import com.example.evaluacion1.entities.EstudianteEntity;
import com.example.evaluacion1.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
public class EstudianteService {
    @Autowired
    EstudianteRepository estudianteRepository;
    @Autowired
    CuotaService cuotaService;

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


    public void eliminarEstudiante(Long id) {
            estudianteRepository.deleteById(id);
    }

    public int calcularPromedioTotalExamenes(EstudianteEntity estudiante){
        if(estudiante.getCantidadExamenes()==0){
            return 0;
        }
        else{
            return estudiante.getSumaPuntajes() / estudiante.getCantidadExamenes();
        }
    }

    public int montoTotalArancelPagar(EstudianteEntity estudiante){
        ArrayList<CuotaEntity> cuotas = cuotaService.obtenerCuotasPorRut(estudiante.getRut());
        int montoTotalArancel = 0;
        for(CuotaEntity cuota : cuotas){
            montoTotalArancel = montoTotalArancel + cuota.getMontoCuota();
        }
        montoTotalArancel -= 70000;
        return montoTotalArancel;
    }
    public String tipoPago(EstudianteEntity estudiante){
        ArrayList<CuotaEntity> cuotas = cuotaService.obtenerCuotasPorRut(estudiante.getRut());
        int cantidadCuotas = cuotas.size();
        if(cantidadCuotas == 1){
            return "Contado";
        }
        else{
            return "Cuotas";
        }
    }

    public int numeroCuotasPactadas(EstudianteEntity estudiante){
        ArrayList<CuotaEntity> cuotas = cuotaService.obtenerCuotasPorRut(estudiante.getRut());
        int cantidadCuotasPactadas = cuotas.size();
        return cantidadCuotasPactadas;
    }

    public int numeroCuotasPagadas(EstudianteEntity estudiante){
        ArrayList<CuotaEntity> cuotas = cuotaService.obtenerCuotasPorRut(estudiante.getRut());
        int cantidadCuotasPagadas = 0;
        for(CuotaEntity cuota : cuotas){
            if(cuota.isPagado()){
                cantidadCuotasPagadas++;
            }
        }
        return cantidadCuotasPagadas;
    }

    public int montoTotalPagado(EstudianteEntity estudiante){
        ArrayList<CuotaEntity> cuotas = cuotaService.obtenerCuotasPorRut(estudiante.getRut());
        int montoTotalPagado = 0;
        for(CuotaEntity cuota : cuotas){
            if(cuota.isPagado()){
                montoTotalPagado = montoTotalPagado + cuota.getMontoCuota();
            }
        }
        return montoTotalPagado;
    }

    public Date fechaUltimoPago(EstudianteEntity estudiante){
        ArrayList<CuotaEntity> cuotas = cuotaService.obtenerCuotasPorRut(estudiante.getRut());
        Date fechaUltimoPago = null;
        for(CuotaEntity cuota : cuotas){
            if(cuota.isPagado()){
                fechaUltimoPago = cuota.getFechaPago();
            }
        }
        return fechaUltimoPago;
    }

    public int saldoPorPagar(EstudianteEntity estudiante){
        int saldoPorPagar = montoTotalArancelPagar(estudiante) + 70000 - montoTotalPagado(estudiante);
        return saldoPorPagar;
    }

    public int numeroCuotasConRetraso(EstudianteEntity estudiante){
        ArrayList<CuotaEntity> cuotas = cuotaService.obtenerCuotasPorRut(estudiante.getRut());
        int cantidadCuotasConRetraso = 0;
        for(CuotaEntity cuota : cuotas){
            if(cuota.getInteres() > 0.0){
                cantidadCuotasConRetraso++;
            }
        }
        return cantidadCuotasConRetraso;
    }

    public ArrayList<EstudianteEntity> obtenerEstudiantesConCuotas(ArrayList<EstudianteEntity> estudiantes){
        ArrayList<EstudianteEntity> estudiantesConCuotas = new ArrayList<>();
        for (EstudianteEntity estudiante : estudiantes) {
            if (cuotaService.existeCuota(estudiante.getRut())) {
                estudiantesConCuotas.add(estudiante);
            }
        }
        return estudiantesConCuotas;
    }


}
