package com.unicauca.maestria.api.gestiontrabajosgrado.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.RespuestaExamenValoracion;

public interface RespuestaExamenValoracionRepository extends JpaRepository<RespuestaExamenValoracion, Long> {

    List<RespuestaExamenValoracion> findByIdTrabajoGrado(Long idTrabajoGradoId);

    @Query("SELECT COUNT(r) FROM RespuestaExamenValoracion r WHERE r.idTrabajoGrado.id = :idTrabajoGrado AND r.respuestaExamenValoracion = 'No aprobado'")
    Long countByTrabajoGradoIdAndRespuestaNoAprobado(@Param("idTrabajoGrado") Long idTrabajoGrado);

    @Query("SELECT sev.idRespuestaExamenValoracion FROM RespuestaExamenValoracion sev WHERE sev.idTrabajoGrado.id = ?1")
    public List<Long> findIdRespuestaExamenValoracionByTrabajoGradoId(Long trabajoGradoId);

}
