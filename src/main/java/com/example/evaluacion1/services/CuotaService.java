package com.example.evaluacion1.services;
import com.example.evaluacion1.entities.CuotaEntity;
import com.example.evaluacion1.entities.EstudianteEntity;
import com.example.evaluacion1.repositories.CuotaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Generated;
import java.util.*;
import java.time.LocalDate;
@Service
public class CuotaService {
    @Autowired
    CuotaRepository cuotaRepository;

    @Generated
    public ArrayList<CuotaEntity> obtenerCuotas() {
        return (ArrayList<CuotaEntity>) cuotaRepository.findAll();
    }

    @Generated
    public CuotaEntity guardarCuota(CuotaEntity cuota) {
        return cuotaRepository.save(cuota);
    }

    @Generated
    public Optional<CuotaEntity> obtenerPorId(Long id) {
        return cuotaRepository.findById(id);
    }

    @Generated
    public CuotaEntity obtenerPorRut(String rut) {
        return cuotaRepository.findByRut(rut);
    }

    @Generated
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

    @Generated
    public void generarCuotas(int cantidadCuotasSeleccionada, int montoTotal, String rut){
        int montoCuota = montoTotal / cantidadCuotasSeleccionada;
        Date fechaVencimiento = new Date();
        for(int i = 0; i < cantidadCuotasSeleccionada; i++){
            CuotaEntity cuota = new CuotaEntity();
            if(i==0){
                fechaVencimiento = sumarMesesAFecha(fechaVencimiento, 1);
                cuota.setRut(rut);
                cuota.setFechaVencimiento(fechaVencimiento);
                cuota.setPagado(false);
                cuota.setDescuento(0);
                cuota.setInteres(0);
                cuota.setMontoCuota(montoCuota+70000);
            }
            else{
                fechaVencimiento = sumarMesesAFecha(fechaVencimiento, 1);
                cuota.setRut(rut);
                cuota.setFechaVencimiento(fechaVencimiento);
                cuota.setPagado(false);
                cuota.setDescuento(0);
                cuota.setInteres(0);
                cuota.setMontoCuota(montoCuota);
            }
            cuotaRepository.save(cuota);
        }
    }

    @Generated
    private Date sumarMesesAFecha(Date fecha, int meses) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.MONTH, meses);
        return calendar.getTime();
    }

    @Generated
    public boolean existeCuota(String rut) {
        Long count = cuotaRepository.countByRut(rut);
        return count > 0;
    }

    @Generated
    public void generarPagoAlContado(String rut){
        CuotaEntity cuota = new CuotaEntity();
        cuota.setRut(rut);
        cuota.setFechaVencimiento(new Date());
        cuota.setPagado(true);
        cuota.setDescuento(0);
        cuota.setInteres(0);
        cuota.setMontoCuota(750000+70000);
        cuota.setFechaPago(new Date());
        cuotaRepository.save(cuota);
    }

    @Generated
    @Transactional
    public void eliminarCuotasPorRut(String rut){
        cuotaRepository.deleteAllByRut(rut);
    }

    @Generated
    public ArrayList<CuotaEntity> obtenerCuotasPorRut(String rut){
        return cuotaRepository.findAllByRut(rut);

    }

    @Generated
    public void pagarCuota(CuotaEntity cuota){
        cuota.setPagado(true);
        cuota.setFechaPago(new Date());
        cuotaRepository.save(cuota);
    }

    public double calcularInteres(CuotaEntity cuota) {
        Date fechaActual = new Date();
        Date fechaVencimiento = cuota.getFechaVencimiento();

        int mesesDiferencia = calcularDiferenciaMeses(fechaVencimiento, fechaActual);

        double interes = 0.0;

        if ((mesesDiferencia == 1) && !cuota.isPagado()) {
            interes = 0.03;
        }
        else if ((mesesDiferencia == 2) && !cuota.isPagado()) {
            interes = 0.06;
        }
        else if ((mesesDiferencia == 3) && !cuota.isPagado()) {
            interes = 0.09;
        }
        else if ((mesesDiferencia > 3) && !cuota.isPagado()) {
            interes = 0.15;
        }

        return interes;
    }

    private int calcularDiferenciaMeses(Date fechaInicio, Date fechaFin) {
        Calendar calFechaInicio = Calendar.getInstance();
        Calendar calFechaFin = Calendar.getInstance();
        calFechaInicio.setTime(fechaInicio);
        calFechaFin.setTime(fechaFin);

        int years = calFechaFin.get(Calendar.YEAR) - calFechaInicio.get(Calendar.YEAR);
        int months = calFechaFin.get(Calendar.MONTH) - calFechaInicio.get(Calendar.MONTH);

        if(years <= 0){
            return months;
        }
        else if(years == 1){
            months = Math.abs((12-calFechaFin.get(Calendar.MONTH)) + (12-calFechaInicio.get(Calendar.MONTH)));
            return months;
        }
        else{
            months = Math.abs((12-calFechaFin.get(Calendar.MONTH)) + (12-calFechaInicio.get(Calendar.MONTH)));
            months = months + ((years-1)*12);
            return months;
        }
    }


}

