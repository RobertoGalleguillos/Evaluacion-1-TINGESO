package com.example.evaluacion1;

import com.example.evaluacion1.entities.CuotaEntity;
import com.example.evaluacion1.entities.EstudianteEntity;
import com.example.evaluacion1.services.CuotaService;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CuotaTest {

    EstudianteEntity estudiante = new EstudianteEntity();
    CuotaEntity cuota = new CuotaEntity();
    CuotaService cuotaService = new CuotaService();

    @Test
    void calcularMontoDeArancelConDescuento() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaNacimiento = sdf.parse("1990-01-01");

        estudiante.setRut("12.345.678-2");
        estudiante.setApellidos("Perez Perez");
        estudiante.setNombres("Raul Alejandro");
        estudiante.setFechaNacimiento(fechaNacimiento);
        estudiante.setTipoColegioProcedencia("Municipal");
        estudiante.setNombreColegio("Colegio de Prueba");
        estudiante.setAnioEgreso(2020);
        int resultado = cuotaService.descuentoMontoCuotas(estudiante);
        assertEquals(1140000, resultado, 0.0);
    }

    @Test
    void calcularCantidadMaxDeCuotas() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaNacimiento = sdf.parse("1990-01-01");

        estudiante.setRut("12.345.678-2");
        estudiante.setApellidos("Perez Perez");
        estudiante.setNombres("Raul Alejandro");
        estudiante.setFechaNacimiento(fechaNacimiento);
        estudiante.setTipoColegioProcedencia("Municipal");
        estudiante.setNombreColegio("Colegio de Prueba");
        estudiante.setAnioEgreso(2020);
        int resultado = cuotaService.cantidadMaxCuotas(estudiante);
        assertEquals(10, resultado, 0.0);
    }


    @Test
    void calcularElInteresSinMesesAtraso() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaVencimiento = sdf.parse("2023-12-01");
        CuotaEntity cuota = new CuotaEntity();
        cuota.setFechaVencimiento(fechaVencimiento);
        double resultado = cuotaService.calcularInteres(cuota);
        assertEquals(0.0, resultado, 0.0);
    }

    @Test
    void calcularElInteresUnMesAtraso() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaVencimiento = sdf.parse("2023-09-01");
        CuotaEntity cuota = new CuotaEntity();
        cuota.setFechaVencimiento(fechaVencimiento);
        double resultado = cuotaService.calcularInteres(cuota);
        assertEquals(0.03, resultado, 0.0);
    }

    @Test
    void calcularElInteresDosMesesAtraso() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaVencimiento = sdf.parse("2023-08-01");
        CuotaEntity cuota = new CuotaEntity();
        cuota.setFechaVencimiento(fechaVencimiento);
        double resultado = cuotaService.calcularInteres(cuota);
        assertEquals(0.06, resultado, 0.0);
    }

    @Test
    void calcularElInteresTresMesesAtraso() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaVencimiento = sdf.parse("2023-07-01");
        CuotaEntity cuota = new CuotaEntity();
        cuota.setFechaVencimiento(fechaVencimiento);
        double resultado = cuotaService.calcularInteres(cuota);
        assertEquals(0.09, resultado, 0.0);
    }

    @Test
    void calcularElInteresMasDeTresMesesAtraso() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaVencimiento = sdf.parse("2023-05-01");
        CuotaEntity cuota = new CuotaEntity();
        cuota.setFechaVencimiento(fechaVencimiento);
        double resultado = cuotaService.calcularInteres(cuota);
        assertEquals(0.15, resultado, 0.0);
    }
    @Test
    void calcularElInteresCasoOneYearAtraso() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaVencimiento = sdf.parse("2022-11-01");
        CuotaEntity cuota = new CuotaEntity();
        cuota.setFechaVencimiento(fechaVencimiento);
        double resultado = cuotaService.calcularInteres(cuota);
        assertEquals(0.15, resultado, 0.0);
    }

    @Test
    void calcularElInteresCasoMasDeOneYearAtraso() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaVencimiento = sdf.parse("2020-08-01");
        CuotaEntity cuota = new CuotaEntity();
        cuota.setFechaVencimiento(fechaVencimiento);
        double resultado = cuotaService.calcularInteres(cuota);
        assertEquals(0.15, resultado, 0.0);
    }


}
