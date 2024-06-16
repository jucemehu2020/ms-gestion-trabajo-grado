package com.unicauca.maestria.api.gestiontrabajosgrado.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionResponseDto;

public interface SolicitudExamenValoracionRepository extends JpaRepository<SolicitudExamenValoracion, Long> {

    @Query("SELECT COUNT(sev) FROM SolicitudExamenValoracion sev WHERE sev.idTrabajoGrado.id = ?1")
    public int countByTrabajoGradoId(Long trabajoGradoId);

    @Query("SELECT sev.idTrabajoGrado.id FROM SolicitudExamenValoracion sev WHERE sev.id = ?1")
    public Long obtenerIdTrabajoGrado(Long trabajoGradoId);

    @Query("SELECT sev FROM SolicitudExamenValoracion sev WHERE sev.idTrabajoGrado.id = ?1")
    public SolicitudExamenValoracion findByTrabajoGradoId(Long trabajoGradoId);

    public Optional<SolicitudExamenValoracion> findByIdTrabajoGradoId(Long idTrabajoGradoId);

}
