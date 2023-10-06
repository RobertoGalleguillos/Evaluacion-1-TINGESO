package com.example.evaluacion1;

import com.example.evaluacion1.entities.CuotaEntity;
import com.example.evaluacion1.entities.EstudianteEntity;
import com.example.evaluacion1.entities.SubirNotasEntity;
import com.example.evaluacion1.services.CuotaService;
import com.example.evaluacion1.services.EstudianteService;
import com.example.evaluacion1.services.SubirNotasService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SubirNotasTest {

    SubirNotasEntity subirNotas = new SubirNotasEntity();
    EstudianteEntity estudiante = new EstudianteEntity();
    CuotaEntity cuota = new CuotaEntity();
    @Autowired
    private SubirNotasService subirNotasService;
    @Autowired
    private EstudianteService estudianteService;
    @Autowired
    private CuotaService cuotaService;

    @Test
    void calcularDescuentosPorNotasPrimerCaso(){
        int promedio = 800;
        double resultado = subirNotasService.calcularDescuentoPorNotas(promedio);
        assertEquals(0.0, resultado, 0.0);
    }

    @Test
    void calcularDescuentosPorNotasSegundoCaso(){
        int promedio = 860;
        double resultado = subirNotasService.calcularDescuentoPorNotas(promedio);
        assertEquals(0.02, resultado, 0.0);
    }

    @Test
    void calcularDescuentosPorNotasTercerCaso(){
        int promedio = 900;
        double resultado = subirNotasService.calcularDescuentoPorNotas(promedio);
        assertEquals(0.05, resultado, 0.0);
    }

    @Test
    void calcularDescuentosPorNotasCuartoCaso(){
        int promedio = 1000;
        double resultado = subirNotasService.calcularDescuentoPorNotas(promedio);
        assertEquals(0.1, resultado, 0.0);
    }

    @Test
    void comprobarObtenerFechaPrimerExamen() throws ParseException {
        subirNotas.setRut("12.345.678-2");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaExamen = sdf.parse("2023-07-01");
        subirNotas.setFechaExamen(fechaExamen);
        subirNotasService.guardarData(subirNotas);
        Date resultado = subirNotasService.obtenerFechaPrimerExamen(subirNotas.getRut());
        subirNotasService.eliminarData(subirNotasService.obtenerData());
        assertEquals(fechaExamen, resultado);
    }

    @Test
    void comprobarObtenerFechaPrimerExamenCasoNull(){
        Date resultado = subirNotasService.obtenerFechaPrimerExamen(subirNotas.getRut());
        assertNull(resultado);
    }

    @Test
    void comprobarCalcularPromedioNotas(){
        estudiante.setRut("12.345.678-2");
        estudianteService.guardarEstudiante(estudiante);
        subirNotas.setRut("12.345.678-2");
        subirNotas.setPuntaje(800);
        subirNotasService.guardarData(subirNotas);
        SubirNotasEntity nota2 = new SubirNotasEntity();
        nota2.setRut("12.345.678-2");
        nota2.setPuntaje(900);
        subirNotasService.guardarData(nota2);
        SubirNotasEntity nota3 = new SubirNotasEntity();
        nota3.setRut("12.345.678-2");
        nota3.setPuntaje(1000);
        subirNotasService.guardarData(nota3);
        SubirNotasEntity nota4 = new SubirNotasEntity();
        nota4.setRut("12.345.678-2");
        nota4.setPuntaje(950);
        subirNotasService.guardarData(nota4);
        int resultado = subirNotasService.calcularPromedioNotas(estudiante.getRut());
        assertEquals(912, resultado);
        subirNotasService.eliminarData(subirNotasService.obtenerData());
        estudianteService.eliminarEstudiante(estudiante.getId());
    }

    @Test
    void comprobarDescuentoCuotaPorNotas(){
        estudiante.setRut("12.345.678-2");
        estudianteService.guardarEstudiante(estudiante);
        subirNotas.setRut("12.345.678-2");
        subirNotas.setPuntaje(800);
        subirNotasService.guardarData(subirNotas);
        SubirNotasEntity nota2 = new SubirNotasEntity();
        nota2.setRut("12.345.678-2");
        nota2.setPuntaje(900);
        subirNotasService.guardarData(nota2);
        SubirNotasEntity nota3 = new SubirNotasEntity();
        nota3.setRut("12.345.678-2");
        nota3.setPuntaje(1000);
        subirNotasService.guardarData(nota3);
        SubirNotasEntity nota4 = new SubirNotasEntity();
        nota4.setRut("12.345.678-2");
        nota4.setPuntaje(950);
        subirNotasService.guardarData(nota4);
        cuota.setRut("12.345.678-2");
        cuota.setMontoCuota(150000);
        subirNotasService.descuentoCuotaPorNotas(subirNotasService.calcularPromedioNotas(estudiante.getRut()), cuota);
        assertEquals(142500, cuota.getMontoCuota());
        subirNotasService.eliminarData(subirNotasService.obtenerData());
        estudianteService.eliminarEstudiante(estudiante.getId());
    }

    @Test
    void comprobarGuardarDataDB(){
        subirNotasService.guardarDataDB("12.345.678-2", "01/07/2023", "800", "archivo.pdf");
        assertEquals(1, subirNotasService.obtenerData().size());
        subirNotasService.eliminarData(subirNotasService.obtenerData());
    }

    @Test
    void comprobarFechaAceptadaCalcularPlantilla() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaActual = sdf.parse("2023-12-01");
        boolean resultado = subirNotasService.fechaAceptadaParaCalcularPlantilla(fechaActual);
        assertTrue(resultado);
    }

    @Test
    void comprobarFechaRechazadaCalcularPlantilla() throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaActual = sdf.parse("2023-11-07");
        boolean resultado = subirNotasService.fechaAceptadaParaCalcularPlantilla(fechaActual);
        assertFalse(resultado);
    }

    @Test
    void comprobarAplicarDescuentoPorNotasCasoCuotas(){
        Calendar calVencimiento = Calendar.getInstance();
        calVencimiento.set(2023, Calendar.OCTOBER, 1);
        int mesExamen = 7;
        int yearExamen = 2023;
        CuotaEntity cuota1 = new CuotaEntity();
        cuota1.setRut("12.345.678-2");
        cuota1.setMontoCuota(150000);
        cuota1.setFechaVencimiento(calVencimiento.getTime());
        cuota1.setPagado(false);
        cuota1.setDescuento(0.0);
        cuota1.setInteres(0.0);
        int promedio = 900;
        subirNotasService.aplicarDescuentoPorNotasCasoCuotas(calVencimiento, mesExamen, yearExamen, cuota1, promedio);
        assertEquals(0.05, cuota1.getDescuento());
        cuotaService.eliminarCuota(cuota1.getId());
    }

    @Test
    void comprobarAplicarDescuentoPorNotasCasoCuotasCaso2(){
        Calendar calVencimiento = Calendar.getInstance();
        calVencimiento.set(2023, Calendar.DECEMBER, 1);
        int mesExamen = 7;
        int yearExamen = 2023;
        CuotaEntity cuota1 = new CuotaEntity();
        cuota1.setRut("12.345.678-2");
        cuota1.setMontoCuota(150000);
        cuota1.setFechaVencimiento(calVencimiento.getTime());
        cuota1.setPagado(false);
        cuota1.setDescuento(0.0);
        cuota1.setInteres(0.0);
        int promedio = 900;
        subirNotasService.aplicarDescuentoPorNotasCasoCuotas(calVencimiento, mesExamen, yearExamen, cuota1, promedio);
        assertEquals(0.05, cuota1.getDescuento());
        cuotaService.eliminarCuota(cuota1.getId());
    }

    @Test
    void comprobarAplicarDescuentoPorNotasCasoContado(){
        Calendar calVencimiento = Calendar.getInstance();
        calVencimiento.set(2023, Calendar.OCTOBER, 1);
        int mesExamen = 7;
        int yearExamen = 2023;
        CuotaEntity cuota1 = new CuotaEntity();
        cuota1.setRut("12.345.678-2");
        cuota1.setMontoCuota(150000);
        cuota1.setFechaVencimiento(calVencimiento.getTime());
        cuota1.setPagado(false);
        cuota1.setDescuento(0.0);
        cuota1.setInteres(0.0);
        int promedio = 900;
        subirNotasService.aplicarDescuentoPorNotasCasoContado(promedio, cuota1);
        assertEquals(0.05, cuota1.getDescuento());
        cuotaService.eliminarCuota(cuota1.getId());
    }

    @Test
    void comprobarLeerCsv(){
        subirNotasService.leerCsv("NotasMatematicas.csv");
        assertTrue(!subirNotasService.obtenerData().isEmpty());
        subirNotasService.eliminarData(subirNotasService.obtenerData());
    }

    @Test
    void comprobarProcesarNotasConDescuento() throws ParseException {
        SubirNotasEntity nota1 = new SubirNotasEntity();
        nota1.setRut("12.345.678-2");
        nota1.setPuntaje(900);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaExamen = sdf.parse("2023-11-20");
        nota1.setFechaExamen(fechaExamen);
        subirNotasService.guardarData(nota1);
        SubirNotasEntity nota2 = new SubirNotasEntity();
        nota2.setRut("12.345.678-2");
        nota2.setPuntaje(950);
        nota2.setFechaExamen(fechaExamen);
        subirNotasService.guardarData(nota2);
        SubirNotasEntity nota3 = new SubirNotasEntity();
        nota3.setRut("12.345.678-2");
        nota3.setPuntaje(1000);
        nota3.setFechaExamen(fechaExamen);
        subirNotasService.guardarData(nota3);
        SubirNotasEntity nota4 = new SubirNotasEntity();
        nota4.setRut("12.345.678-2");
        nota4.setPuntaje(950);
        nota4.setFechaExamen(fechaExamen);
        subirNotasService.guardarData(nota4);
        EstudianteEntity estudiante1 = new EstudianteEntity();
        estudiante1.setRut("12.345.678-2");
        estudianteService.guardarEstudiante(estudiante1);
        CuotaEntity cuota1 = new CuotaEntity();
        cuota1.setRut("12.345.678-2");
        cuota1.setMontoCuota(150000);
        cuota1.setPagado(false);
        cuota1.setDescuento(0.0);
        cuota1.setInteres(0.0);
        Date fechaVencimiento = sdf.parse("2023-12-20");
        cuota1.setFechaVencimiento(fechaVencimiento);
        cuotaService.guardarCuota(cuota1);
        subirNotasService.procesarNotasConDescuento();
        double resultado = cuotaService.obtenerCuotasPorRut(estudiante1.getRut()).get(0).getDescuento();
        assertEquals(0.1, resultado);
        subirNotasService.eliminarData(subirNotasService.obtenerData());
        cuotaService.eliminarCuota(cuota1.getId());
        estudianteService.eliminarEstudiante(estudiante1.getId());

    }

}
