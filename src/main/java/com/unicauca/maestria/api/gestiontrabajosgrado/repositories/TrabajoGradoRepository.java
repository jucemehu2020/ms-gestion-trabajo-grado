package com.unicauca.maestria.api.gestiontrabajosgrado.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;

public interface TrabajoGradoRepository extends JpaRepository<TrabajoGrado, Long> {

    @Query("SELECT sev.numeroEstado FROM TrabajoGrado sev WHERE sev.id = ?1")
    public Integer obtenerEstadoTrabajoGrado(Long trabajoGradoId);

}
