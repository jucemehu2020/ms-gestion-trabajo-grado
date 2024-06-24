package com.unicauca.maestria.api.gestiontrabajosgrado.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.RespuestaComite;

public interface RespuestaComiteSolicitudRepository extends JpaRepository<RespuestaComite, Long> {

    RespuestaComite findFirstByOrderByIdAnexoExamenValoracionDesc();
}
