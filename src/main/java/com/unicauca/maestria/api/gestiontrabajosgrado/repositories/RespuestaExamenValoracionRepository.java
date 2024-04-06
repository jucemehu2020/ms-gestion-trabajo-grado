package com.unicauca.maestria.api.gestiontrabajosgrado.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.rta_examen_valoracion.RespuestaExamenValoracion;

public interface RespuestaExamenValoracionRepository extends JpaRepository<RespuestaExamenValoracion, Long> {

    @Query("SELECT COUNT(sev) FROM RespuestaExamenValoracion sev WHERE sev.idTrabajoGrado.id = ?1")
    public int countByTrabajoGradoId(Long trabajoGradoId);

    List<RespuestaExamenValoracion> findByIdTrabajoGradoId(Long idTrabajoGradoId);
}
