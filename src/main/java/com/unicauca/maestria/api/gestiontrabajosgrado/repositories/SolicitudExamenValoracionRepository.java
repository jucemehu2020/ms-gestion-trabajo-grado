package com.unicauca.maestria.api.gestiontrabajosgrado.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;

public interface SolicitudExamenValoracionRepository extends JpaRepository<SolicitudExamenValoracion, Long> {

    @Query("SELECT COUNT(sev) FROM SolicitudExamenValoracion sev WHERE sev.idTrabajoGrado.id = ?1")
    public int countByTrabajoGradoId(Long trabajoGradoId);

    List<SolicitudExamenValoracion> findByIdTrabajoGradoId(Long idTrabajoGradoId);
}
