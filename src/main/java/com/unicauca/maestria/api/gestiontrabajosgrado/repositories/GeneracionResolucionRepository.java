package com.unicauca.maestria.api.gestiontrabajosgrado.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;

public interface GeneracionResolucionRepository extends JpaRepository<GeneracionResolucion, Long> {
    @Query("SELECT COUNT(sev) FROM RespuestaExamenValoracion sev WHERE sev.idTrabajoGrado.id = ?1")
    public int countByTrabajoGradoId(Long trabajoGradoId);

    List<GeneracionResolucion> findByIdTrabajoGradoId(Long idTrabajoGradoId);
}
