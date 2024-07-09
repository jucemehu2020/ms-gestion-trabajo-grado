package com.unicauca.maestria.api.gestiontrabajosgrado.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.RespuestaComiteExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;

public interface SolicitudExamenValoracionRepository extends JpaRepository<SolicitudExamenValoracion, Long> {

    @Query("SELECT COUNT(sev) FROM SolicitudExamenValoracion sev WHERE sev.idTrabajoGrado.id = ?1")
    public int countByTrabajoGradoId(Long trabajoGradoId);

    @Query("SELECT sev.idTrabajoGrado.id FROM SolicitudExamenValoracion sev WHERE sev.id = ?1")
    public Long obtenerIdTrabajoGrado(Long trabajoGradoId);

    @Query("SELECT sev FROM SolicitudExamenValoracion sev WHERE sev.idTrabajoGrado.id = ?1")
    public SolicitudExamenValoracion findByTrabajoGradoId(Long trabajoGradoId);

    public Optional<SolicitudExamenValoracion> findByIdTrabajoGradoId(Long idTrabajoGradoId);

    @Query("SELECT rc FROM SolicitudExamenValoracion sev JOIN sev.actaFechaRespuestaComite rc WHERE sev.id = :id ORDER BY rc.id DESC")
    List<RespuestaComiteExamenValoracion> findRespuestaComiteBySolicitudExamenValoracionId(@Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(sev) > 0 THEN TRUE ELSE FALSE END FROM SolicitudExamenValoracion sev WHERE sev.idTrabajoGrado.id = ?1")
    public boolean existsByTrabajoGradoId(Long trabajoGradoId);

    @Query("SELECT sev.id FROM SolicitudExamenValoracion sev WHERE sev.idTrabajoGrado.id = ?1")
    public Long findIdExamenValoracionByTrabajoGradoId(Long trabajoGradoId);

}
