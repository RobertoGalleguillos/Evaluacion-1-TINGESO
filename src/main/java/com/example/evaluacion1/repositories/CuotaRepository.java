package com.example.evaluacion1.repositories;

import com.example.evaluacion1.entities.CuotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface CuotaRepository extends JpaRepository<CuotaEntity, Long>{
    public CuotaEntity findByRut(String rut);

    Long countByRut(String rut);

    ArrayList<CuotaEntity> findAllByRut(String rut);

    void deleteAllByRut(String rut);
}
