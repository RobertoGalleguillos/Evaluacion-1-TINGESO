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

}
