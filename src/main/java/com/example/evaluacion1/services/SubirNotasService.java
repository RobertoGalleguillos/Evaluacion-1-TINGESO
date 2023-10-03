package com.example.evaluacion1.services;

import com.example.evaluacion1.entities.CuotaEntity;
import com.example.evaluacion1.entities.EstudianteEntity;
import com.example.evaluacion1.entities.SubirNotasEntity;
import com.example.evaluacion1.repositories.SubirNotasRepository;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SubirNotasService {

    @Autowired
    private SubirNotasRepository dataRepository;
    @Autowired
    private CuotaService cuotasService;
    @Autowired
    private EstudianteService estudianteService;

    private final Logger logg = LoggerFactory.getLogger(SubirNotasService.class);

    public ArrayList<SubirNotasEntity> obtenerData(){
        return (ArrayList<SubirNotasEntity>) dataRepository.findAll();
    }

    @Generated
    public String guardar(MultipartFile file){
        String filename = file.getOriginalFilename();
        if(filename != null){
            if(!file.isEmpty()){
                try{
                    byte [] bytes = file.getBytes();
                    Path path  = Paths.get(file.getOriginalFilename());
                    Files.write(path, bytes);
                    logg.info("Archivo guardado");
                }
                catch (IOException e){
                    logg.error("ERROR", e);
                }
            }
            return "Archivo guardado con exito!";
        }
        else{
            return "No se pudo guardar el archivo";
        }
    }

    @Generated
    public void leerCsv(String direccion){
        String texto = "";
        BufferedReader bf = null;
        try{
            bf = new BufferedReader(new FileReader(direccion));
            String temp = "";
            String bfRead;
            int count = 1;
            String nombreArchivo = direccion;
            while((bfRead = bf.readLine()) != null){
                if (count == 1){
                    count = 0;
                }
                else{
                    SubirNotasEntity nota = guardarDataDB(bfRead.split(";")[0], bfRead.split(";")[1], bfRead.split(";")[2], nombreArchivo);
                    temp = temp + "\n" + bfRead;
                }
            }
            texto = temp;
            System.out.println("Archivo leido exitosamente");
        }catch(Exception e){
            System.err.println("No se encontro el archivo");
        }finally{
            if(bf != null){
                try{
                    bf.close();
                }catch(IOException e){
                    logg.error("ERROR", e);
                }
            }
        }
    }

    public void guardarData(SubirNotasEntity data){
        dataRepository.save(data);
    }


    public SubirNotasEntity guardarDataDB(String rut, String fechaExamen, String puntaje, String nombreArchivo){
        SubirNotasEntity newData = new SubirNotasEntity();
        newData.setRut(rut);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date fecha = dateFormat.parse(fechaExamen);
            newData.setFechaExamen(fecha);
        } catch (ParseException e) {
            logg.error("Error al convertir la fecha", e);
        }

        try {
            int puntajeInt = Integer.parseInt(puntaje);
            newData.setPuntaje(puntajeInt);
        } catch (NumberFormatException e) {
            logg.error("Error al convertir el puntaje", e);
        }
        newData.setNombreArchivo(nombreArchivo);
        guardarData(newData);
        return newData;
    }

    public void eliminarData(ArrayList<SubirNotasEntity> datas){
        dataRepository.deleteAll(datas);
    }

    public void descuentoCuotaPorNotas(int puntajePromedio, CuotaEntity cuota) {
        double descuento = calcularDescuentoPorNotas(puntajePromedio);
        double descuentoInicial = cuota.getDescuento();
        double descuentoTotal = descuentoInicial + descuento;
        cuota.setDescuento(descuentoTotal);
        int montoCuota = cuota.getMontoCuota();
        int montoDescuento = (int) (montoCuota * descuento);
        int montoFinal = montoCuota - montoDescuento;
        cuota.setMontoCuota(montoFinal);
    }


    public double calcularDescuentoPorNotas(int puntajePromedio){
        double descuento = 0;
        if(puntajePromedio >= 850 && puntajePromedio <= 899){
            descuento = 0.02;
        }
        else if(puntajePromedio >= 900 && puntajePromedio <= 949){
            descuento = 0.05;
        }
        else if(puntajePromedio >= 950 && puntajePromedio <= 1000){
            descuento = 0.1;
        }

        return descuento;
    }

    public void procesarNotasConDescuento() {
        ArrayList<EstudianteEntity> estudiantes = estudianteService.obtenerEstudiantes();
        ArrayList<CuotaEntity> cuotas = cuotasService.obtenerCuotas();
        for(EstudianteEntity estudiante : estudiantes){
            int promedio = calcularPromedioNotas(estudiante.getRut());
            Date fechaPrimerExamen = obtenerFechaPrimerExamen(estudiante.getRut());

            if (fechaPrimerExamen != null) {
                Calendar calExamen = Calendar.getInstance();
                calExamen.setTime(fechaPrimerExamen);
                int mesExamen = calExamen.get(Calendar.MONTH) + 1;
                int yearExamen = calExamen.get(Calendar.YEAR);

                for(CuotaEntity cuota : cuotas){
                    if(estudiante.getRut().equals(cuota.getRut())){
                        ArrayList<CuotaEntity> cuotasRut = cuotasService.obtenerCuotasPorRut(estudiante.getRut());
                        if(cuotasRut.size() == 1){
                            descuentoCuotaPorNotas(promedio, cuota);
                            cuotasService.guardarCuota(cuota);
                        }
                        else{
                            Calendar calVencimiento = Calendar.getInstance();
                            calVencimiento.setTime(cuota.getFechaVencimiento());
                            int mesVencimiento = calVencimiento.get(Calendar.MONTH) + 1;
                            int yearVencimiento = calVencimiento.get(Calendar.YEAR);

                            if (((mesExamen <= mesVencimiento) || (yearExamen < yearVencimiento)) && !cuota.isPagado()) {
                                descuentoCuotaPorNotas(promedio, cuota);
                                cuotasService.guardarCuota(cuota);
                            }
                        }
                    }
                }
            }
        }
    }

    public Date obtenerFechaPrimerExamen(String rut) {
        ArrayList<SubirNotasEntity> notas = dataRepository.findAllByRut(rut);
        if (!notas.isEmpty()) {
            notas.sort(Comparator.comparing(SubirNotasEntity::getFechaExamen));
            return notas.get(0).getFechaExamen();
        }
        return null;
    }

    public int calcularPromedioNotas(String rut) {
        ArrayList<SubirNotasEntity> notas = dataRepository.findAllByRut(rut);
        int promedio = 0;
        for (SubirNotasEntity nota : notas) {
            promedio += nota.getPuntaje();
        }
        promedio = promedio / 4;
        return promedio;
    }



}