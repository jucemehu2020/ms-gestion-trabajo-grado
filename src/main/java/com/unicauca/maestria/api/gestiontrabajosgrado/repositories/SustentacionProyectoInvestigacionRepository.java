package com.unicauca.maestria.api.gestiontrabajosgrado.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.SustentacionTrabajoInvestigacion;

public interface SustentacionProyectoInvestigacionRepository
        extends JpaRepository<SustentacionTrabajoInvestigacion, Long> {

    @Query("SELECT COUNT(sev) FROM SustentacionTrabajoInvestigacion sev WHERE sev.idTrabajoGrado.id = ?1")
    public int countByTrabajoGradoId(Long trabajoGradoId);

    @Query("SELECT sev FROM SustentacionTrabajoInvestigacion sev WHERE sev.idTrabajoGrado.id = ?1")
    public SustentacionTrabajoInvestigacion findByTrabajoGradoId(Long trabajoGradoId);

    Optional<SustentacionTrabajoInvestigacion> findByIdTrabajoGradoId(Long idTrabajoGradoId);
}
