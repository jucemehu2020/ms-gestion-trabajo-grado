package com.unicauca.maestria.api.gestiontrabajosgrado.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.RespuestaComiteExamenValoracion;

public interface RespuestaComiteSolicitudRepository extends JpaRepository<RespuestaComiteExamenValoracion, Long> {

    RespuestaComiteExamenValoracion findFirstByOrderByIdDesc();

    // Método para obtener el último registro según el id_examen_valoracion
    RespuestaComiteExamenValoracion findFirstBySolicitudExamenValoracionIdOrderByIdDesc(
            Long solicitudExamenValoracionId);
}
