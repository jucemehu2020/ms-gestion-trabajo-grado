package com.unicauca.maestria.api.gestiontrabajosgrado.repositories;

import org.springframework.data.jpa.repository.Query;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.AnexoRespuestaExamenValoracion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnexosRespuestaExamenValoracionRepository extends JpaRepository<AnexoRespuestaExamenValoracion, Long> {

    @Query("SELECT asev FROM AnexoRespuestaExamenValoracion asev WHERE asev.respuestaExamenValoracion.idRespuestaExamenValoracion = ?1")
    List<AnexoRespuestaExamenValoracion> obtenerAnexosPorId(Long idRespuestaExamenValoracion);

}
