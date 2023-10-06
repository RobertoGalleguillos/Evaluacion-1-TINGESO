package com.example.evaluacion1;

import com.example.evaluacion1.entities.CuotaEntity;
import com.example.evaluacion1.entities.EstudianteEntity;
import com.example.evaluacion1.services.CuotaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CuotaTest {

    EstudianteEntity estudiante = new EstudianteEntity();
    CuotaEntity cuota = new CuotaEntity();
    @Autowired
    private CuotaService cuotaService;


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
    void calcularMontoDeArancelConDescuentoCaso2() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaNacimiento = sdf.parse("1990-01-01");

        estudiante.setRut("12.345.678-2");
        estudiante.setApellidos("Perez Perez");
        estudiante.setNombres("Raul Alejandro");
        estudiante.setFechaNacimiento(fechaNacimiento);
        estudiante.setTipoColegioProcedencia("Subvencionado");
        estudiante.setNombreColegio("Colegio de Prueba");
        estudiante.setAnioEgreso(2022);
        int resultado = cuotaService.descuentoMontoCuotas(estudiante);
        assertEquals(1230000, resultado, 0.0);
    }

    @Test
    void calcularMontoDeArancelConDescuentoCaso3() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaNacimiento = sdf.parse("1990-01-01");

        estudiante.setRut("12.345.678-2");
        estudiante.setApellidos("Perez Perez");
        estudiante.setNombres("Raul Alejandro");
        estudiante.setFechaNacimiento(fechaNacimiento);
        estudiante.setTipoColegioProcedencia("Subvencionado");
        estudiante.setNombreColegio("Colegio de Prueba");
        estudiante.setAnioEgreso(2023);
        int resultado = cuotaService.descuentoMontoCuotas(estudiante);
        assertEquals(1125000, resultado, 0.0);
    }

    @Test
    void calcularCantidadMaxDeCuotasCasoMunicipal() throws ParseException {
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
    void calcularCantidadMaxDeCuotasCasoSubvencionado() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaNacimiento = sdf.parse("1990-01-01");

        estudiante.setRut("12.345.678-2");
        estudiante.setApellidos("Perez Perez");
        estudiante.setNombres("Raul Alejandro");
        estudiante.setFechaNacimiento(fechaNacimiento);
        estudiante.setTipoColegioProcedencia("Subvencionado");
        estudiante.setNombreColegio("Colegio de Prueba");
        estudiante.setAnioEgreso(2020);
        int resultado = cuotaService.cantidadMaxCuotas(estudiante);
        assertEquals(7, resultado, 0.0);
    }

    @Test
    void calcularCantidadMaxDeCuotasCasoPrivado() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaNacimiento = sdf.parse("1990-01-01");

        estudiante.setRut("12.345.678-2");
        estudiante.setApellidos("Perez Perez");
        estudiante.setNombres("Raul Alejandro");
        estudiante.setFechaNacimiento(fechaNacimiento);
        estudiante.setTipoColegioProcedencia("Privado");
        estudiante.setNombreColegio("Colegio de Prueba");
        estudiante.setAnioEgreso(2020);
        int resultado = cuotaService.cantidadMaxCuotas(estudiante);
        assertEquals(4, resultado, 0.0);
    }


    @Test
    void calcularElInteresSinMesesAtraso() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaVencimiento = sdf.parse("2023-12-01");
        CuotaEntity cuota = new CuotaEntity();
        cuota.setFechaVencimiento(fechaVencimiento);
        Date fechaActual = sdf.parse("2023-10-01");
        double resultado = cuotaService.calcularInteres(cuota, fechaActual);
        assertEquals(0.0, resultado, 0.0);
    }

    @Test
    void calcularElInteresSinMesesAtraso2() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaVencimiento = sdf.parse("2024-12-01");
        CuotaEntity cuota = new CuotaEntity();
        cuota.setFechaVencimiento(fechaVencimiento);
        Date fechaActual = sdf.parse("2023-10-01");
        double resultado = cuotaService.calcularInteres(cuota, fechaActual);
        assertEquals(0.0, resultado, 0.0);
    }

    @Test
    void calcularElInteresUnMesAtraso() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaVencimiento = sdf.parse("2023-09-01");
        CuotaEntity cuota = new CuotaEntity();
        cuota.setFechaVencimiento(fechaVencimiento);
        Date fechaActual = sdf.parse("2023-10-01");
        double resultado = cuotaService.calcularInteres(cuota, fechaActual);
        assertEquals(0.03, resultado, 0.0);
    }

    @Test
    void calcularElInteresDosMesesAtraso() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaVencimiento = sdf.parse("2023-08-01");
        CuotaEntity cuota = new CuotaEntity();
        cuota.setFechaVencimiento(fechaVencimiento);
        Date fechaActual = sdf.parse("2023-10-01");
        double resultado = cuotaService.calcularInteres(cuota, fechaActual);
        assertEquals(0.06, resultado, 0.0);
    }

    @Test
    void calcularElInteresTresMesesAtraso() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaVencimiento = sdf.parse("2023-07-01");
        CuotaEntity cuota = new CuotaEntity();
        cuota.setFechaVencimiento(fechaVencimiento);
        Date fechaActual = sdf.parse("2023-10-01");
        double resultado = cuotaService.calcularInteres(cuota, fechaActual);
        assertEquals(0.09, resultado, 0.0);
    }

    @Test
    void calcularElInteresMasDeTresMesesAtraso() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaVencimiento = sdf.parse("2023-05-01");
        CuotaEntity cuota = new CuotaEntity();
        cuota.setFechaVencimiento(fechaVencimiento);
        Date fechaActual = sdf.parse("2023-10-01");
        double resultado = cuotaService.calcularInteres(cuota, fechaActual);
        assertEquals(0.15, resultado, 0.0);
    }
    @Test
    void calcularElInteresCasoOneYearAtraso() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaVencimiento = sdf.parse("2022-11-01");
        CuotaEntity cuota = new CuotaEntity();
        cuota.setFechaVencimiento(fechaVencimiento);
        Date fechaActual = sdf.parse("2023-10-01");
        double resultado = cuotaService.calcularInteres(cuota, fechaActual);
        assertEquals(0.15, resultado, 0.0);
    }

    @Test
    void calcularElInteresCasoMasDeOneYearAtraso() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaVencimiento = sdf.parse("2020-08-01");
        CuotaEntity cuota = new CuotaEntity();
        cuota.setFechaVencimiento(fechaVencimiento);
        Date fechaActual = sdf.parse("2023-10-01");
        double resultado = cuotaService.calcularInteres(cuota, fechaActual);
        assertEquals(0.15, resultado, 0.0);
    }

    @Test
    void verSiExisteCuota(){
        estudiante.setRut("12.345.678-2");
        CuotaEntity cuota = new CuotaEntity();
        cuota.setRut("12.345.678-2");
        cuota.setMontoCuota(150000);
        cuota.setPagado(true);
        cuotaService.guardarCuota(cuota);
        boolean resultado = cuotaService.existeCuota(estudiante.getRut());
        assertTrue(resultado);
        cuotaService.eliminarCuota(cuota.getId());
    }

    @Test
    void verSiNoExisteCuota(){
        estudiante.setRut("12.345.678-2");
        CuotaEntity cuota = new CuotaEntity();
        cuota.setRut("12.333.444-5");
        cuota.setMontoCuota(150000);
        cuota.setPagado(true);
        cuotaService.guardarCuota(cuota);
        boolean resultado = cuotaService.existeCuota("12.345.678-3");
        assertFalse(resultado);
        cuotaService.eliminarCuota(cuota.getId());
    }

    @Test
    void calcularFechaRechazadaPago() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaActual = sdf.parse("2023-12-01");
        boolean resultado = cuotaService.fechaAceptadaParaPago(fechaActual);
        assertFalse(resultado);
    }

    @Test
    void calcularFechaAceptadaPago() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaActual = sdf.parse("2023-11-06");
        boolean resultado = cuotaService.fechaAceptadaParaPago(fechaActual);
        assertTrue(resultado);
    }

    @Test
    void comprobarPagoDeCuota(){
        cuotaService.pagarCuota(cuota);
        assertTrue(cuota.isPagado());
        cuotaService.eliminarCuota(cuota.getId());
    }

    @Test

    void comprobarPagoAlCOntado(){
        cuotaService.generarPagoAlContado("12.345.678-2");
        cuota = cuotaService.obtenerCuotasPorRut("12.345.678-2").get(0);
        assertTrue(cuota.isPagado());
        cuotaService.eliminarCuotasPorRut("12.345.678-2");
    }

    @Test
    void comprobarGenerarCuotas(){
cuotaService.generarCuotas(10, 1500000, "12.345.678-2");
        assertEquals(10, cuotaService.obtenerCuotasPorRut("12.345.678-2").size());
        cuotaService.eliminarCuotasPorRut("12.345.678-2");
    }


    @Test
    void comprobarObtenerCuotas(){
        estudiante.setRut("12.345.678-2");
        CuotaEntity cuota1 = new CuotaEntity();
        cuota1.setRut("12.345.678-2");
        cuotaService.guardarCuota(cuota1);
        CuotaEntity cuota2 = new CuotaEntity();
        cuota2.setRut("12.345.678-2");
        cuotaService.guardarCuota(cuota2);
        int resultado = cuotaService.obtenerCuotas().size();
        assertTrue(resultado > 0);
        cuotaService.eliminarCuota(cuota1.getId());
        cuotaService.eliminarCuota(cuota2.getId());
    }

    @Test
    void comprobarRevisarIntereses() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaActual = sdf.parse("2023-11-06");
        Date fechaVencimiento = sdf.parse("2023-10-01");
        estudiante.setRut("12.345.678-2");
        CuotaEntity cuota1 = new CuotaEntity();
        cuota1.setRut("12.345.678-2");
        cuota1.setMontoCuota(150000);
        cuota1.setPagado(false);
        cuota1.setFechaPago(fechaActual);
        cuota1.setInteres(0.03);
        cuota1.setFechaVencimiento(fechaVencimiento);
        cuotaService.guardarCuota(cuota1);
        cuotaService.revisarIntereses();
        cuota1 = cuotaService.obtenerCuotasPorRut("12.345.678-2").get(0);
        assertEquals(0.03, cuota1.getInteres(), 0.0);
        cuotaService.eliminarCuota(cuota1.getId());
    }

    @Test
    void comprobarRevisarInteresesCaso2() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaVencimiento = sdf.parse("2023-07-01");
        estudiante.setRut("12.345.678-2");
        CuotaEntity cuota1 = new CuotaEntity();
        cuota1.setRut("12.345.678-2");
        cuota1.setMontoCuota(150000);
        cuota1.setPagado(false);
        cuota1.setInteres(0.03);
        cuota1.setFechaVencimiento(fechaVencimiento);
        cuotaService.guardarCuota(cuota1);
        cuotaService.revisarIntereses();
        cuota1 = cuotaService.obtenerCuotasPorRut("12.345.678-2").get(0);
        assertEquals(0.09, cuota1.getInteres(), 0.0);
        cuotaService.eliminarCuota(cuota1.getId());
    }




}
