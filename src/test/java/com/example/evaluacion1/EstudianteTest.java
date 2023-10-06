package com.example.evaluacion1;

import com.example.evaluacion1.entities.CuotaEntity;
import com.example.evaluacion1.entities.EstudianteEntity;
import com.example.evaluacion1.repositories.EstudianteRepository;
import com.example.evaluacion1.services.CuotaService;
import com.example.evaluacion1.services.EstudianteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
public class EstudianteTest {
    EstudianteEntity estudiante = new EstudianteEntity();

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private CuotaService cuotaService;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Test
    void calcularPromedioTotalExamenes(){
        estudiante.setCantidadExamenes(13);
        estudiante.setSumaPuntajes(11700);
        int resultado = estudianteService.calcularPromedioTotalExamenes(estudiante);
        assertEquals(900, resultado, 0.0);
    }

    @Test
    void calcularPromedioTotalExamenesCasoCero(){
        estudiante.setCantidadExamenes(0);
        estudiante.setSumaPuntajes(0);
        int resultado = estudianteService.calcularPromedioTotalExamenes(estudiante);
        assertEquals(0, resultado, 0.0);
    }

    @Test
    void calcularMontoTotalArancelPagar(){
        estudiante.setRut("12.345.678-2");
        CuotaEntity cuota1 = new CuotaEntity();
        cuota1.setRut("12.345.678-2");
        cuota1.setMontoCuota(150000);
        cuotaService.guardarCuota(cuota1);
        CuotaEntity cuota2 = new CuotaEntity();
        cuota2.setRut("12.345.678-2");
        cuota2.setMontoCuota(150000);
        cuotaService.guardarCuota(cuota2);
        int resultado = estudianteService.montoTotalArancelPagar(estudiante);
        assertEquals(230000, resultado, 0.0);
        cuotaService.eliminarCuota(cuota1.getId());
        cuotaService.eliminarCuota(cuota2.getId());
    }

    @Test
    void calcularTipoPagoCasoContado(){
        estudiante.setRut("12.345.678-2");
        CuotaEntity cuota1 = new CuotaEntity();
        cuota1.setRut("12.345.678-2");
        cuota1.setMontoCuota(150000);
        cuotaService.guardarCuota(cuota1);
        String resultado = estudianteService.tipoPago(estudiante);
        assertEquals("Contado", resultado);
        cuotaService.eliminarCuota(cuota1.getId());
    }

    @Test
    void calcularTipoPagoCasoCuotas(){
        estudiante.setRut("12.345.678-2");
        CuotaEntity cuota1 = new CuotaEntity();
        cuota1.setRut("12.345.678-2");
        cuota1.setMontoCuota(150000);
        cuotaService.guardarCuota(cuota1);
        CuotaEntity cuota2 = new CuotaEntity();
        cuota2.setRut("12.345.678-2");
        cuota2.setMontoCuota(160000);
        cuotaService.guardarCuota(cuota2);
        String resultado = estudianteService.tipoPago(estudiante);
        assertEquals("Cuotas", resultado);
        cuotaService.eliminarCuota(cuota1.getId());
        cuotaService.eliminarCuota(cuota2.getId());
    }

    @Test
    void calcularNumeroCuotasPactadas(){
        estudiante.setRut("12.345.678-2");
        CuotaEntity cuota1 = new CuotaEntity();
        cuota1.setRut("12.345.678-2");
        cuota1.setMontoCuota(150000);
        cuotaService.guardarCuota(cuota1);
        CuotaEntity cuota2 = new CuotaEntity();
        cuota2.setRut("12.345.678-2");
        cuota2.setMontoCuota(160000);
        cuotaService.guardarCuota(cuota2);
        int resultado = estudianteService.numeroCuotasPactadas(estudiante);
        assertEquals(2, resultado, 0.0);
        cuotaService.eliminarCuota(cuota1.getId());
        cuotaService.eliminarCuota(cuota2.getId());
    }

    @Test
    void calcularNumeroCuotasPagadas(){
        estudiante.setRut("12.345.678-2");
        CuotaEntity cuota1 = new CuotaEntity();
        cuota1.setRut("12.345.678-2");
        cuota1.setMontoCuota(150000);
        cuota1.setPagado(true);
        cuotaService.guardarCuota(cuota1);
        CuotaEntity cuota2 = new CuotaEntity();
        cuota2.setRut("12.345.678-2");
        cuota2.setMontoCuota(160000);
        cuotaService.guardarCuota(cuota2);
        int resultado = estudianteService.numeroCuotasPagadas(estudiante);
        assertEquals(1, resultado, 0.0);
        cuotaService.eliminarCuota(cuota1.getId());
        cuotaService.eliminarCuota(cuota2.getId());
    }

    @Test
    void calcularMontoTotalPagado(){
        estudiante.setRut("12.345.678-2");
        CuotaEntity cuota1 = new CuotaEntity();
        cuota1.setRut("12.345.678-2");
        cuota1.setMontoCuota(150000);
        cuota1.setPagado(true);
        cuotaService.guardarCuota(cuota1);
        CuotaEntity cuota2 = new CuotaEntity();
        cuota2.setRut("12.345.678-2");
        cuota2.setMontoCuota(160000);
        cuotaService.guardarCuota(cuota2);
        int resultado = estudianteService.montoTotalPagado(estudiante);
        assertEquals(150000, resultado, 0.0);
        cuotaService.eliminarCuota(cuota1.getId());
        cuotaService.eliminarCuota(cuota2.getId());
    }

    @Test
    void calcularFechaUltimoPago(){
        estudiante.setRut("12.345.678-2");
        CuotaEntity cuota1 = new CuotaEntity();
        cuota1.setRut("12.345.678-2");
        cuota1.setMontoCuota(150000);
        cuota1.setPagado(true);
        cuota1.setFechaPago(new Date());
        cuotaService.guardarCuota(cuota1);
        CuotaEntity cuota2 = new CuotaEntity();
        cuota2.setRut("12.345.678-2");
        cuota2.setMontoCuota(160000);
        cuotaService.guardarCuota(cuota2);
        Date resultado = estudianteService.fechaUltimoPago(estudiante);
        assertEquals(cuota1.getFechaPago(), resultado);
        cuotaService.eliminarCuota(cuota1.getId());
        cuotaService.eliminarCuota(cuota2.getId());
    }

    @Test
    void calcularSaldoPorPagar(){
        estudiante.setRut("12.345.678-2");
        CuotaEntity cuota1 = new CuotaEntity();
        cuota1.setRut("12.345.678-2");
        cuota1.setMontoCuota(150000);
        cuota1.setPagado(true);
        cuota1.setFechaPago(new Date());
        cuotaService.guardarCuota(cuota1);
        CuotaEntity cuota2 = new CuotaEntity();
        cuota2.setRut("12.345.678-2");
        cuota2.setMontoCuota(160000);
        cuotaService.guardarCuota(cuota2);
        int resultado = estudianteService.saldoPorPagar(estudiante);
        assertEquals(160000, resultado, 0.0);
        cuotaService.eliminarCuota(cuota1.getId());
        cuotaService.eliminarCuota(cuota2.getId());
    }

    @Test
    void calcularNumeroCuotasConRetraso(){
        estudiante.setRut("12.345.678-2");
        CuotaEntity cuota1 = new CuotaEntity();
        cuota1.setRut("12.345.678-2");
        cuota1.setMontoCuota(150000);
        cuota1.setPagado(true);
        cuota1.setFechaPago(new Date());
        cuota1.setInteres(0.03);
        cuotaService.guardarCuota(cuota1);
        CuotaEntity cuota2 = new CuotaEntity();
        cuota2.setRut("12.345.678-2");
        cuota2.setMontoCuota(160000);
        cuotaService.guardarCuota(cuota2);
        int resultado = estudianteService.numeroCuotasConRetraso(estudiante);
        assertEquals(1, resultado, 0.0);
        cuotaService.eliminarCuota(cuota1.getId());
        cuotaService.eliminarCuota(cuota2.getId());
    }

    @Test
    void comprobarObtenerEstudiantesConCuotas(){
        estudiante.setRut("12.345.678-2");
        estudianteService.guardarEstudiante(estudiante);
        CuotaEntity cuota1 = new CuotaEntity();
        cuota1.setRut("12.345.678-2");
        cuotaService.guardarCuota(cuota1);
        EstudianteEntity estudiante2 = new EstudianteEntity();
        estudiante2.setRut("12.345.678-3");
        estudianteService.guardarEstudiante(estudiante2);
        ArrayList<EstudianteEntity> estudiantes = new ArrayList<>();
        estudiantes.add(estudiante);
        estudiantes.add(estudiante2);
        int resultado = estudianteService.obtenerEstudiantesConCuotas(estudiantes).size();
        assertEquals(1, resultado, 0.0);
        cuotaService.eliminarCuota(cuota1.getId());
        estudianteService.eliminarEstudiante(estudiante.getId());
        estudianteService.eliminarEstudiante(estudiante2.getId());
    }

    @Test
    void comprobarObtenerEstudiantes(){
        estudiante.setRut("12.345.678-2");
        estudianteService.guardarEstudiante(estudiante);
        EstudianteEntity estudiante2 = new EstudianteEntity();
        estudiante2.setRut("12.345.678-3");
        estudianteService.guardarEstudiante(estudiante2);
        ArrayList<EstudianteEntity> estudiantes = estudianteService.obtenerEstudiantes();
        assertFalse(estudiantes.isEmpty());
        estudianteService.eliminarEstudiante(estudiante.getId());
        estudianteService.eliminarEstudiante(estudiante2.getId());
    }

    @Test
    void comprobarObtenerPorId(){
        estudiante.setRut("12.345.678-2");
        estudianteService.guardarEstudiante(estudiante);
        Optional<EstudianteEntity> estudiante2Optional = estudianteService.obtenerPorId(estudiante.getId());
        assertTrue(estudiante2Optional.isPresent());
        EstudianteEntity estudiante2 = estudiante2Optional.get();
        assertEquals(estudiante, estudiante2);
        estudianteService.eliminarEstudiante(estudiante.getId());
    }

}
