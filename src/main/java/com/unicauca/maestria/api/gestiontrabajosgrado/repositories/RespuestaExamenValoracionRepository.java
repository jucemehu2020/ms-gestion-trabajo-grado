package com.unicauca.maestria.api.gestiontrabajosgrado.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.RespuestaExamenValoracion;

public interface RespuestaExamenValoracionRepository extends JpaRepository<RespuestaExamenValoracion, Long> {

    List<RespuestaExamenValoracion> findByTrabajoGradoId(Long idTrabajoGradoId);
    
}
