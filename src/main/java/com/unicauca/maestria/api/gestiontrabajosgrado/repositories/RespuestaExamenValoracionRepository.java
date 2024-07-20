package com.unicauca.maestria.api.gestiontrabajosgrado.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptosVarios;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.respuesta_examen_valoracion.TipoEvaluador;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.RespuestaExamenValoracion;

public interface RespuestaExamenValoracionRepository extends JpaRepository<RespuestaExamenValoracion, Long> {

        @Query("SELECT r FROM RespuestaExamenValoracion r WHERE r.trabajoGrado.id = :trabajoGrado")
        List<RespuestaExamenValoracion> findByTrabajoGrado(@Param("trabajoGrado") Long trabajoGrado);

        @Query("SELECT COUNT(r) FROM RespuestaExamenValoracion r WHERE r.trabajoGrado.id = :trabajoGrado AND r.respuestaExamenValoracion = 'NO_APROBADO'")
        Long countByTrabajoGradoIdAndRespuestaNoAprobado(@Param("trabajoGrado") Long trabajoGrado);

        @Query("SELECT COUNT(r) FROM RespuestaExamenValoracion r WHERE r.trabajoGrado.id = :trabajoGrado AND r.respuestaExamenValoracion = 'APLAZADO'")
        Long countByTrabajoGradoIdAndRespuestaAplazado(@Param("trabajoGrado") Long trabajoGrado);

        @Query("SELECT sev.id FROM RespuestaExamenValoracion sev WHERE sev.trabajoGrado.id = ?1")
        public List<Long> findIdRespuestaExamenValoracionByTrabajoGradoId(Long trabajoGradoId);

        @Query("SELECT r.id FROM RespuestaExamenValoracion r WHERE r.idEvaluador = :idEvaluador AND r.tipoEvaluador = :tipoEvaluador ORDER BY r.id DESC")
        Long findLatestIdByIdEvaluadorAndTipoEvaluador(@Param("idEvaluador") Long idEvaluador,
                        @Param("tipoEvaluador") TipoEvaluador tipoEvaluador);

        @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END " +
                        "FROM RespuestaExamenValoracion r " +
                        "WHERE r.idEvaluador IN (" +
                        "  SELECT e.idEvaluador FROM RespuestaExamenValoracion e WHERE e.tipoEvaluador = :tipoEvaluador1"
                        +
                        ") " +
                        "AND r.idEvaluador IN (" +
                        "  SELECT e.idEvaluador FROM RespuestaExamenValoracion e WHERE e.tipoEvaluador = :tipoEvaluador2"
                        +
                        ")")
        boolean existsByEvaluadorTypes(@Param("tipoEvaluador1") TipoEvaluador tipoEvaluador1,
                        @Param("tipoEvaluador2") TipoEvaluador tipoEvaluador2);

        @Query("SELECT COUNT(r) FROM RespuestaExamenValoracion r WHERE r.idEvaluador = :idEvaluador AND r.tipoEvaluador = :tipoEvaluador AND r.respuestaExamenValoracion = 'NO_APROBADO'")
        Long countNoAprobadoByIdEvaluadorAndTipoEvaluador(@Param("idEvaluador") Long idEvaluador,
                        @Param("tipoEvaluador") TipoEvaluador tipoEvaluador);

        @Query("SELECT r.respuestaExamenValoracion FROM RespuestaExamenValoracion r WHERE r.tipoEvaluador = :tipoEvaluador AND r.trabajoGrado.id = :idTrabajoGrado")
        ConceptosVarios findConceptoByTipoEvaluadorAndTrabajoGrado(@Param("tipoEvaluador") TipoEvaluador tipoEvaluador,
                        @Param("idTrabajoGrado") Long idTrabajoGrado);

        @Query("SELECT r.linkFormatoB FROM RespuestaExamenValoracion r WHERE r.trabajoGrado.id = :idTrabajoGrado AND r.respuestaExamenValoracion = 'APROBADO'")
        List<String> findLinkFormatoBByIdTrabajoGradoAndRespuestaExamenValoracion(Long idTrabajoGrado);
}
