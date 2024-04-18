package com.unicauca.maestria.api.gestiontrabajosgrado.services.respuesta_examen_valoracion;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.poi.ss.formula.atp.Switch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.ConvertString;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.rta_examen_valoracion.RespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.CamposUnicosSolicitudExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldUniqueException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.RespuestaExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion.InformacionUnicaSolicitudExamenValoracion;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RespuestaExamenValoracionServiceImpl implements RespuestaExamenValoracionService {

        private final RespuestaExamenValoracionRepository respuestaExamenValoracionRepository;
        private final RespuestaExamenValoracionMapper respuestaExamenValoracionMapper;
        private final TrabajoGradoRepository trabajoGradoRepository;

        @Override
        @Transactional
        public RespuestaExamenValoracionDto crear(RespuestaExamenValoracionDto respuestaExamenValoracionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(respuestaExamenValoracionDto.getIdTrabajoGrados())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: "
                                                                + respuestaExamenValoracionDto.getIdTrabajoGrados()
                                                                + " No encontrado"));

                // Obtener iniciales del trabajo de grado
                String procesoVa = "Respuesta_Examen_Valoracion";
                String tituloTrabajoGrado = ConvertString.obtenerIniciales(trabajoGrado.getTitulo());

                Long idenficiacionEstudiante = trabajoGrado.getEstudiante().getPersona().getIdentificacion();
                String nombreEstudiante = trabajoGrado.getEstudiante().getPersona().getNombre();
                String apellidoEstudiante = trabajoGrado.getEstudiante().getPersona().getApellido();
                String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

                // Mapear DTO a entidad
                RespuestaExamenValoracion rtaExamenValoracion = respuestaExamenValoracionMapper
                                .toEntity(respuestaExamenValoracionDto);

                // Se cambia el numero de estado
                int numEstado = validarEstado(respuestaExamenValoracionDto.getRespuestaExamenValoracion());
                trabajoGrado.setNumeroEstado(numEstado);

                // Guardar la entidad ExamenValoracion
                rtaExamenValoracion.setLinkFormatoB(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                rtaExamenValoracion.getLinkFormatoB(), nombreCarpeta));
                rtaExamenValoracion.setLinkFormatoC(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                rtaExamenValoracion.getLinkFormatoC(), nombreCarpeta));
                rtaExamenValoracion.setLinkObservaciones(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                rtaExamenValoracion.getLinkObservaciones(), nombreCarpeta));

                //Se asigna al trabajo de grado
                rtaExamenValoracion.setTrabajoGrado(trabajoGrado);

                RespuestaExamenValoracion examenValoracionRes = respuestaExamenValoracionRepository
                                .save(rtaExamenValoracion);

                return respuestaExamenValoracionMapper.toDto(examenValoracionRes);
        }

        @Override
        @Transactional(readOnly = true)
        public List<RespuestaExamenValoracionDto> buscarPorId(Long idTrabajoGrado) {
                return respuestaExamenValoracionRepository.findByTrabajoGradoId(idTrabajoGrado)
                                .stream()
                                .map(respuestaExamenValoracionMapper::toDto)
                                .collect(Collectors.toList());
        }

        @Override
        public RespuestaExamenValoracionDto actualizar(Long id,
                        RespuestaExamenValoracionDto respuestaExamenValoracionDto,
                        BindingResult result) {

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                RespuestaExamenValoracion respuestaExamenValoracionTmp = respuestaExamenValoracionRepository
                                .findById(id).orElseThrow(() -> new ResourceNotFoundException(
                                                "Respuesta examen de valoracion con id: " + id + " no encontrado"));

                // Busca el trabajo de grado
                // TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(id).orElseThrow(
                //                 () -> new ResourceNotFoundException(
                //                                 "Trabajo de grado con id: " + id + " no encontrado"));

                String procesoVa = "Respuesta_Examen_Valoracion";
                String tituloTrabajoGrado = ConvertString.obtenerIniciales(respuestaExamenValoracionTmp.getTrabajoGrado().getTitulo());
                Long idenficiacionEstudiante = respuestaExamenValoracionTmp.getTrabajoGrado().getEstudiante().getPersona().getIdentificacion();
                String nombreEstudiante = respuestaExamenValoracionTmp.getTrabajoGrado().getEstudiante().getPersona().getNombre();
                String apellidoEstudiante = respuestaExamenValoracionTmp.getTrabajoGrado().getEstudiante().getPersona().getApellido();
                String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

                RespuestaExamenValoracion responseExamenValoracion = null;

                if (respuestaExamenValoracionTmp != null) {
                        if (respuestaExamenValoracionDto.getLinkFormatoB()
                                        .compareTo(respuestaExamenValoracionTmp.getLinkFormatoB()) != 0) {
                                respuestaExamenValoracionDto.setLinkFormatoB(FilesUtilities.guardarArchivoNew(
                                                tituloTrabajoGrado, procesoVa,
                                                respuestaExamenValoracionDto.getLinkFormatoB(), nombreCarpeta));
                                FilesUtilities.deleteFileExample(respuestaExamenValoracionTmp.getLinkFormatoB());
                        }
                        if (respuestaExamenValoracionDto.getLinkFormatoC()
                                        .compareTo(respuestaExamenValoracionTmp.getLinkFormatoC()) != 0) {
                                respuestaExamenValoracionDto.setLinkFormatoC(FilesUtilities.guardarArchivoNew(
                                                tituloTrabajoGrado, procesoVa,
                                                respuestaExamenValoracionDto.getLinkFormatoC(), nombreCarpeta));
                                FilesUtilities.deleteFileExample(respuestaExamenValoracionTmp.getLinkFormatoC());
                        }
                        if (respuestaExamenValoracionDto.getLinkObservaciones()
                                        .compareTo(respuestaExamenValoracionTmp.getLinkObservaciones()) != 0) {
                                respuestaExamenValoracionDto.setLinkObservaciones(FilesUtilities.guardarArchivoNew(
                                                tituloTrabajoGrado, procesoVa,
                                                respuestaExamenValoracionDto.getLinkObservaciones(), nombreCarpeta));
                                FilesUtilities.deleteFileExample(respuestaExamenValoracionTmp.getLinkObservaciones());
                        }
                        // Repetir esto
                        updateRtaExamenValoracionValues(respuestaExamenValoracionTmp, respuestaExamenValoracionDto);
                        responseExamenValoracion = respuestaExamenValoracionRepository
                                        .save(respuestaExamenValoracionTmp);
                }
                return respuestaExamenValoracionMapper.toDto(responseExamenValoracion);
        }

        @Override
        @Transactional(readOnly = true)
        public String descargarArchivo(RutaArchivoDto rutaArchivo) {
                return FilesUtilities.recuperarArchivo(rutaArchivo.getRutaArchivo());
        }

        // Funciones privadas
        private void updateRtaExamenValoracionValues(RespuestaExamenValoracion respuestaExamenValoracion,
                        RespuestaExamenValoracionDto respuestaExamenValoracionDto) {

                respuestaExamenValoracion.setRespuestaExamenValoracion(
                                respuestaExamenValoracionDto.getRespuestaExamenValoracion());
                respuestaExamenValoracion.setFechaMaximaEntrega(respuestaExamenValoracionDto.getFechaMaximaEntrega());
                respuestaExamenValoracion.setObservacion(respuestaExamenValoracionDto.getObservacion());
                //Update archivos
                respuestaExamenValoracion.setLinkFormatoB(respuestaExamenValoracionDto.getLinkFormatoB());
                respuestaExamenValoracion.setLinkFormatoC(respuestaExamenValoracionDto.getLinkFormatoC());
                respuestaExamenValoracion.setLinkObservaciones(respuestaExamenValoracionDto.getLinkObservaciones());
        }

        private int validarEstado(String estado) {
                int numEstado = 0;
                switch (estado) {
                        case "No aprobado":
                                numEstado = 3;
                                break;
                        case "Aplazado":
                                numEstado = 4;
                                break;
                        default:
                                numEstado = 5;
                                break;
                }
                return numEstado;
        }

}
