package com.example.evaluacion1.repositories;

import com.example.evaluacion1.entities.SubirNotasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Repository
public interface SubirNotasRepository extends JpaRepository <SubirNotasEntity, Long>{
    ArrayList<SubirNotasEntity> findAllByRut(String rut);
}