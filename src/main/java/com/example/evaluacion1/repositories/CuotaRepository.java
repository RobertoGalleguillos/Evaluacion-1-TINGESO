package com.example.evaluacion1.repositories;

import com.example.evaluacion1.entities.CuotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CuotaRepository extends JpaRepository<CuotaEntity, Long>{
    public CuotaEntity findByRut(String rut);

}