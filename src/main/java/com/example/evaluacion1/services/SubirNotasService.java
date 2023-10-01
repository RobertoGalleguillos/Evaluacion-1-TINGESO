package com.example.evaluacion1.services;

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
import java.util.ArrayList;
import java.util.Date;

@Service
public class SubirNotasService {

    @Autowired
    private SubirNotasRepository dataRepository;

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
        dataRepository.deleteAll();
        try{
            bf = new BufferedReader(new FileReader(direccion));
            String temp = "";
            String bfRead;
            int count = 1;
            while((bfRead = bf.readLine()) != null){
                if (count == 1){
                    count = 0;
                }
                else{
                    guardarDataDB(bfRead.split(";")[0], bfRead.split(";")[1], bfRead.split(";")[2]);
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


    public void guardarDataDB(String rut, String fechaExamen, String puntaje){
        SubirNotasEntity newData = new SubirNotasEntity();
        newData.setRut(rut);
        // Convertir fechaExamen a Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date fecha = dateFormat.parse(fechaExamen);
            newData.setFechaExamen(fecha);
        } catch (ParseException e) {
            logg.error("Error al convertir la fecha", e);
        }

        // Convertir puntaje a int
        try {
            int puntajeInt = Integer.parseInt(puntaje);
            newData.setPuntaje(puntajeInt);
        } catch (NumberFormatException e) {
            logg.error("Error al convertir el puntaje", e);
        }

        guardarData(newData);
    }

    public void eliminarData(ArrayList<SubirNotasEntity> datas){
        dataRepository.deleteAll(datas);
    }

}