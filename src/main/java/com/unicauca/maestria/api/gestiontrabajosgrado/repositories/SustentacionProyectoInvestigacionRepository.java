package com.unicauca.maestria.api.gestiontrabajosgrado.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_proyecto_investigacion.RespuestaComiteSustentacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacion;

public interface SustentacionProyectoInvestigacionRepository
        extends JpaRepository<SustentacionProyectoInvestigacion, Long> {

    @Query("SELECT COUNT(sev) FROM SustentacionProyectoInvestigacion sev WHERE sev.trabajoGrado.id = ?1")
    public int countByTrabajoGradoId(Long trabajoGradoId);

    Optional<SustentacionProyectoInvestigacion> findByTrabajoGradoId(Long idTrabajoGradoId);

    @Query("SELECT rc FROM SustentacionProyectoInvestigacion sev JOIN sev.actaFechaRespuestaComite rc WHERE sev.id = :id ORDER BY rc.id DESC")
    List<RespuestaComiteSustentacion> findRespuestaComiteBySustentacionId(@Param("id") Long id);

    @Query("SELECT sev.id FROM SustentacionProyectoInvestigacion sev WHERE sev.trabajoGrado.id = ?1")
    public Long findIdSustentacionProyectoInvestigacionByTrabajoGradoId(Long trabajoGradoId);
}
