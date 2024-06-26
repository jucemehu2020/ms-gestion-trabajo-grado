package com.unicauca.maestria.api.gestiontrabajosgrado.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.RespuestaComiteSustentacion;

public interface RespuestaComiteSustentacionRepository
        extends JpaRepository<RespuestaComiteSustentacion, Long> {

    RespuestaComiteSustentacion findFirstByOrderByIdRespuestaComiteSustentacionDesc();
}
