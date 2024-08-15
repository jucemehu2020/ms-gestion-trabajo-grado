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

        @Query("SELECT COUNT(r) FROM RespuestaExamenValoracion r WHERE r.trabajoGrado.id = :trabajoGrado AND r.respuestaExamenValoracion = 'NO_APROBADO' AND r.idEvaluador = :idEvaluador AND r.tipoEvaluador = :tipoEvaluador")
        Long countByTrabajoGradoIdAndRespuestaNoAprobadoAndEvaluador(@Param("trabajoGrado") Long trabajoGrado,
                        @Param("idEvaluador") Long idEvaluador, @Param("tipoEvaluador") TipoEvaluador tipoEvaluador);

        @Query("SELECT COUNT(r) FROM RespuestaExamenValoracion r WHERE r.trabajoGrado.id = :trabajoGrado AND r.respuestaExamenValoracion = 'APLAZADO' AND r.idEvaluador = :idEvaluador AND r.tipoEvaluador = :tipoEvaluador")
        Long countByTrabajoGradoIdAndRespuestaAplazadoAndEvaluador(@Param("trabajoGrado") Long trabajoGrado,
                        @Param("idEvaluador") Long idEvaluador, @Param("tipoEvaluador") TipoEvaluador tipoEvaluador);

        @Query("SELECT COUNT(r) FROM RespuestaExamenValoracion r WHERE r.trabajoGrado.id = :trabajoGrado AND r.respuestaExamenValoracion = 'NO_APROBADO'")
        Long countByTrabajoGradoIdAndRespuestaNoAprobado(@Param("trabajoGrado") Long trabajoGrado);

        @Query("SELECT COUNT(r) FROM RespuestaExamenValoracion r WHERE r.trabajoGrado.id = :trabajoGrado AND r.respuestaExamenValoracion = 'APLAZADO'")
        Long countByTrabajoGradoIdAndRespuestaAplazado(@Param("trabajoGrado") Long trabajoGrado);

        @Query("SELECT sev.id FROM RespuestaExamenValoracion sev WHERE sev.trabajoGrado.id = ?1")
        public List<Long> findIdRespuestaExamenValoracionByTrabajoGradoId(Long trabajoGradoId);

        @Query("SELECT r.id FROM RespuestaExamenValoracion r WHERE r.idEvaluador = :idEvaluador AND r.tipoEvaluador = :tipoEvaluador ORDER BY r.id DESC")
        List<Long> findLatestIdByIdEvaluadorAndTipoEvaluador(@Param("idEvaluador") Long idEvaluador,
                        @Param("tipoEvaluador") TipoEvaluador tipoEvaluador);

        @Query("SELECT (COUNT(r) > 0) " +
                        "FROM RespuestaExamenValoracion r " +
                        "WHERE r.trabajoGrado.id = :idTrabajoGrado " +
                        "AND EXISTS (" +
                        "  SELECT 1 FROM RespuestaExamenValoracion e WHERE e.tipoEvaluador = 'EXTERNO' AND e.trabajoGrado.id = :idTrabajoGrado"
                        +
                        ") " +
                        "AND EXISTS (" +
                        "  SELECT 1 FROM RespuestaExamenValoracion e WHERE e.tipoEvaluador = 'INTERNO' AND e.trabajoGrado.id = :idTrabajoGrado"
                        +
                        ")")
        boolean existsByEvaluadorTypes(@Param("idTrabajoGrado") Long idTrabajoGrado);

        @Query("SELECT COUNT(r) FROM RespuestaExamenValoracion r WHERE r.idEvaluador = :idEvaluador AND r.tipoEvaluador = :tipoEvaluador AND r.respuestaExamenValoracion = 'NO_APROBADO'")
        Long countNoAprobadoByIdEvaluadorAndTipoEvaluador(@Param("idEvaluador") Long idEvaluador,
                        @Param("tipoEvaluador") TipoEvaluador tipoEvaluador);

        @Query("SELECT r.respuestaExamenValoracion FROM RespuestaExamenValoracion r WHERE r.tipoEvaluador = :tipoEvaluador AND r.trabajoGrado.id = :idTrabajoGrado ORDER BY r.id DESC")
        List<ConceptosVarios> findConceptoByTipoEvaluadorAndTrabajoGrado(
                        @Param("tipoEvaluador") TipoEvaluador tipoEvaluador,
                        @Param("idTrabajoGrado") Long idTrabajoGrado);

        @Query("SELECT r.linkFormatoB FROM RespuestaExamenValoracion r WHERE r.trabajoGrado.id = :idTrabajoGrado AND r.respuestaExamenValoracion = 'APROBADO'")
        List<String> findLinkFormatoBByIdTrabajoGradoAndRespuestaExamenValoracion(Long idTrabajoGrado);

        @Query("SELECT r FROM RespuestaExamenValoracion r WHERE r.idEvaluador = :idEvaluador AND r.tipoEvaluador = :tipoEvaluador AND r.id = :idRespuestaExamen ORDER BY r.id DESC")
        List<RespuestaExamenValoracion> findLatestByIdEvaluadorAndTipoEvaluadorAndId(
                        @Param("idEvaluador") Long idEvaluador, @Param("tipoEvaluador") TipoEvaluador tipoEvaluador,
                        @Param("idRespuestaExamen") Long idRespuestaExamen);

}
