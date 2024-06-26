package com.unicauca.maestria.api.gestiontrabajosgrado.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.RespuestaComiteGeneracionResolucion;

public interface GeneracionResolucionRepository extends JpaRepository<GeneracionResolucion, Long> {
    @Query("SELECT COUNT(sev) FROM GeneracionResolucion sev WHERE sev.idTrabajoGrado.id = ?1")
    public int countByTrabajoGradoId(Long trabajoGradoId);

    Optional<GeneracionResolucion> findByIdTrabajoGradoId(Long idTrabajoGradoId);

    @Query("SELECT sev FROM GeneracionResolucion sev WHERE sev.idTrabajoGrado.id = ?1")
    public GeneracionResolucion findByTrabajoGradoId(Long trabajoGradoId);

    @Query("SELECT rc FROM GeneracionResolucion sev JOIN sev.actaFechaRespuestaComite rc WHERE sev.idGeneracionResolucion = :id ORDER BY rc.id DESC")
    List<RespuestaComiteGeneracionResolucion> findRespuestaComiteByGeneracionResolucionId(@Param("id") Long id);

    @Query("SELECT sev.idGeneracionResolucion FROM GeneracionResolucion sev WHERE sev.idTrabajoGrado.id = ?1")
    public Long findIdGeneracionResolucionByTrabajoGradoId(Long trabajoGradoId);
}
