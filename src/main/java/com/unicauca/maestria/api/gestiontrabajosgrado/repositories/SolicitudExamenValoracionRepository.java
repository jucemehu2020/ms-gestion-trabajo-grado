package com.unicauca.maestria.api.gestiontrabajosgrado.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;

public interface SolicitudExamenValoracionRepository extends JpaRepository<SolicitudExamenValoracion, Long>{
    
}
