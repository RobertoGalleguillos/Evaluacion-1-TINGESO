package com.example.evaluacion1.repositories;

import com.example.evaluacion1.entities.EstudianteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudianteRepository extends JpaRepository<EstudianteEntity, Long>{
    public EstudianteEntity findByRut(String rut);

    @Query("select e from EstudianteEntity e where e.nombres = :nombres")
    EstudianteEntity findByNameCustomQuery(@Param("nombres") String nombre);

    @Query(value = "select * from estudiantes as e where e.nombres = :nombres",
            nativeQuery = true)
    EstudianteEntity findByNameNativeQuery(@Param("nombres") String nombres);

}
