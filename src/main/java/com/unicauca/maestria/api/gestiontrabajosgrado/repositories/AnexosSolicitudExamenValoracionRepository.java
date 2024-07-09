package com.unicauca.maestria.api.gestiontrabajosgrado.repositories;

import org.springframework.data.jpa.repository.Query;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.AnexoSolicitudExamenValoracion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnexosSolicitudExamenValoracionRepository extends JpaRepository<AnexoSolicitudExamenValoracion, Long> {

    @Query("SELECT asev FROM AnexoSolicitudExamenValoracion asev WHERE asev.solicitudExamenValoracion.id = ?1")
    List<AnexoSolicitudExamenValoracion> obtenerAnexosPorId(Long examenValoracionId);

}
