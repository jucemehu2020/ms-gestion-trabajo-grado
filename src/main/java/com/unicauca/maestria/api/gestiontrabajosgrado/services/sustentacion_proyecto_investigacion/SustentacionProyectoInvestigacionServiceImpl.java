package com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.ConvertString;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.SustentacionTrabajoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.CamposUnicosGenerarResolucionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.GeneracionResolucionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.CamposUnicosSustentacionProyectoInvestigacionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.SustentacionTrabajoInvestigacionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.comite.SustentacionTrabajoInvestigacionComiteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.comite.SustentacionTrabajoInvestigacionComiteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.SustentacionTrabajoInvestigacionCoordinadorDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.SustentacionTrabajoInvestigacionCoordinadorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldUniqueException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SustentacionProyectoInvestigacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.generacion_resolucion.InformacionUnicaGeneracionResolucion;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SustentacionProyectoInvestigacionServiceImpl implements SustentacionProyectoInvestigacionService {

        private final SustentacionProyectoInvestigacionRepository sustentacionProyectoInvestigacionRepository;
        private final SustentacionProyectoInvestigacionMapper sustentacionProyectoIngestigacionMapper;
        private final SustentacionProyectoInvestigacionResponseMapper sustentacionProyectoIngestigacionResponseMapper;
        private final TrabajoGradoRepository trabajoGradoRepository;
        private final InformacionUnicaSustentacionProyectoInvestigacion informacionUnicaSustentacionProyectoInvestigacion;

        // @Override
        // @Transactional
        // public SustentacionTrabajoInvestigacionDto
        // crear(SustentacionTrabajoInvestigacionDto sustentacionDto,
        // BindingResult result) {
        // if (result.hasErrors()) {
        // throw new FieldErrorException(result);
        // }

        // TrabajoGrado trabajoGrado =
        // trabajoGradoRepository.findById(sustentacionDto.getIdTrabajoGrados())
        // .orElseThrow(() -> new ResourceNotFoundException(
        // "TrabajoGrado con id: " + sustentacionDto.getIdTrabajoGrados()
        // + " No encontrado"));

        // Map<String, String> validacionCamposUnicos = validacionCampoUnicos(
        // obtenerCamposUnicos(sustentacionDto),
        // null);
        // if (!validacionCamposUnicos.isEmpty()) {
        // throw new FieldUniqueException(validacionCamposUnicos);
        // }

        // // Obtener iniciales del trabajo de grado
        // String procesoVa = "Sustentacion_Proyecto_Investigacion";
        // String tituloTrabajoGrado =
        // ConvertString.obtenerIniciales(trabajoGrado.getTitulo());

        // Long idenficiacionEstudiante =
        // trabajoGrado.getEstudiante().getPersona().getIdentificacion();
        // String nombreEstudiante =
        // trabajoGrado.getEstudiante().getPersona().getNombre();
        // String apellidoEstudiante =
        // trabajoGrado.getEstudiante().getPersona().getApellido();
        // String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_"
        // + apellidoEstudiante;

        // // Mapear DTO a entidad
        // SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion =
        // sustentacionProyectoIngestigacionMapper
        // .toEntity(sustentacionDto);

        // // Establecer la relación uno a uno
        // sustentacionProyectoInvestigacion.setIdTrabajoGrado(trabajoGrado);
        // trabajoGrado.setIdSustentacionProyectoInvestigacion(sustentacionProyectoInvestigacion);

        // // Se cambia el numero de estado
        // int numEstado = validarEstado(sustentacionDto.getRespuestaSustentacion());
        // trabajoGrado.setNumeroEstado(numEstado);

        // // Guardar la entidad SustentacionProyectoInvestigacion
        // sustentacionProyectoInvestigacion.setLinkRemisionDocumentoFinal(
        // FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
        // sustentacionProyectoInvestigacion.getLinkRemisionDocumentoFinal(),
        // nombreCarpeta));
        // sustentacionProyectoInvestigacion.setLinkRemisionDocumentoFinalCF(
        // FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
        // sustentacionProyectoInvestigacion.getLinkRemisionDocumentoFinalCF(),
        // nombreCarpeta));
        // sustentacionProyectoInvestigacion.setLinkConstanciaDocumentoFinal(
        // FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
        // sustentacionProyectoInvestigacion.getLinkConstanciaDocumentoFinal(),
        // nombreCarpeta));
        // sustentacionProyectoInvestigacion
        // .setLinkActaSustentacion(FilesUtilities
        // .guardarArchivoNew(tituloTrabajoGrado, procesoVa,
        // sustentacionProyectoInvestigacion.getLinkActaSustentacion(),
        // nombreCarpeta));
        // sustentacionProyectoInvestigacion.setLinkActaSustentacionPublica(
        // FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
        // sustentacionProyectoInvestigacion.getLinkActaSustentacionPublica(),
        // nombreCarpeta));
        // sustentacionProyectoInvestigacion.setLinkEstudioHojaVidaAcademica(
        // FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
        // sustentacionProyectoInvestigacion.getLinkEstudioHojaVidaAcademica(),
        // nombreCarpeta));

        // SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionRes =
        // sustentacionProyectoInvestigacionRepository
        // .save(sustentacionProyectoInvestigacion);

        // return
        // sustentacionProyectoIngestigacionMapper.toDto(sustentacionProyectoInvestigacionRes);
        // }

        @Override
        @Transactional
        public SustentacionTrabajoInvestigacionDocenteResponseDto insertarInformacionDocente(
                        SustentacionTrabajoInvestigacionDocenteDto sustentacionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(sustentacionDto.getIdTrabajoGrados())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + sustentacionDto.getIdTrabajoGrados()
                                                                + " No encontrado"));

                // Map<String, String> validacionCamposUnicos = validacionCampoUnicos(
                // obtenerCamposUnicos(sustentacionDto),
                // null);
                // if (!validacionCamposUnicos.isEmpty()) {
                // throw new FieldUniqueException(validacionCamposUnicos);
                // }

                // Obtener iniciales del trabajo de grado
                String procesoVa = "Sustentacion_Proyecto_Investigacion";
                String tituloTrabajoGrado = ConvertString.obtenerIniciales(trabajoGrado.getTitulo());

                Long idenficiacionEstudiante = trabajoGrado.getEstudiante().getPersona().getIdentificacion();
                String nombreEstudiante = trabajoGrado.getEstudiante().getPersona().getNombre();
                String apellidoEstudiante = trabajoGrado.getEstudiante().getPersona().getApellido();
                String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

                // Mapear DTO a entidad
                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoIngestigacionMapper
                                .toEntity(sustentacionDto);

                // Establecer la relación uno a uno
                sustentacionProyectoInvestigacion.setIdTrabajoGrado(trabajoGrado);
                trabajoGrado.setIdSustentacionProyectoInvestigacion(sustentacionProyectoInvestigacion);

                // Se cambia el numero de estado
                // int numEstado = validarEstado(sustentacionDto.getRespuestaSustentacion());
                // trabajoGrado.setNumeroEstado(numEstado);

                // Guardar la entidad SustentacionProyectoInvestigacion
                sustentacionProyectoInvestigacion.setLinkRemisionDocumentoFinal(
                                FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                sustentacionProyectoInvestigacion.getLinkRemisionDocumentoFinal(),
                                                nombreCarpeta));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacion);

                return sustentacionProyectoIngestigacionResponseMapper
                                .toDocenteDto(sustentacionProyectoInvestigacionRes);
        }

        @Override
        @Transactional
        public SustentacionTrabajoInvestigacionComiteResponseDto insertarInformacionComite(
                        SustentacionTrabajoInvestigacionComiteDto sustentacionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(sustentacionDto.getIdTrabajoGrados())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + sustentacionDto.getIdTrabajoGrados()
                                                                + " No encontrado"));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findByTrabajoGradoId(sustentacionDto.getIdTrabajoGrados());

                // Map<String, String> validacionCamposUnicos = validacionCampoUnicos(
                // obtenerCamposUnicos(sustentacionDto),
                // null);
                // if (!validacionCamposUnicos.isEmpty()) {
                // throw new FieldUniqueException(validacionCamposUnicos);
                // }

                // Obtener iniciales del trabajo de grado
                String procesoVa = "Sustentacion_Proyecto_Investigacion";
                String tituloTrabajoGrado = ConvertString.obtenerIniciales(trabajoGrado.getTitulo());

                Long idenficiacionEstudiante = trabajoGrado.getEstudiante().getPersona().getIdentificacion();
                String nombreEstudiante = trabajoGrado.getEstudiante().getPersona().getNombre();
                String apellidoEstudiante = trabajoGrado.getEstudiante().getPersona().getApellido();
                String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

                // Mapear DTO a entidad
                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoIngestigacionMapper
                                .toEntity(sustentacionDto);

                // Establecer la relación uno a uno
                // sustentacionProyectoInvestigacion.setIdTrabajoGrado(trabajoGrado);
                // trabajoGrado.setIdSustentacionProyectoInvestigacion(sustentacionProyectoInvestigacion);

                // Se cambia el numero de estado
                // int numEstado = validarEstado(sustentacionDto.getRespuestaSustentacion());
                // trabajoGrado.setNumeroEstado(numEstado);

                // Guardar la entidad SustentacionProyectoInvestigacion

                sustentacionProyectoInvestigacion.setLinkRemisionDocumentoFinalCF(
                                FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                sustentacionProyectoInvestigacion.getLinkRemisionDocumentoFinalCF(),
                                                nombreCarpeta));

                agregarInformacionComite(sustentacionProyectoInvestigacionTmp, sustentacionDto);

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacionTmp);

                return sustentacionProyectoIngestigacionResponseMapper
                                .toComiteDto(sustentacionProyectoInvestigacionRes);
        }

        private void agregarInformacionComite(
                        SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion,
                        SustentacionTrabajoInvestigacionComiteDto sustentacionDto) {
                sustentacionProyectoInvestigacion
                                .setLinkRemisionDocumentoFinalCF(sustentacionDto.getLinkRemisionDocumentoFinalCF());
        }

        @Override
        @Transactional
        public SustentacionTrabajoInvestigacionCoordinadorResponseDto insertarInformacionCoordinador(
                        SustentacionTrabajoInvestigacionCoordinadorDto sustentacionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(sustentacionDto.getIdTrabajoGrados())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + sustentacionDto.getIdTrabajoGrados()
                                                                + " No encontrado"));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findByTrabajoGradoId(sustentacionDto.getIdTrabajoGrados());

                // Map<String, String> validacionCamposUnicos = validacionCampoUnicos(
                // obtenerCamposUnicos(sustentacionDto),
                // null);
                // if (!validacionCamposUnicos.isEmpty()) {
                // throw new FieldUniqueException(validacionCamposUnicos);
                // }

                // Obtener iniciales del trabajo de grado
                String procesoVa = "Sustentacion_Proyecto_Investigacion";
                String tituloTrabajoGrado = ConvertString.obtenerIniciales(trabajoGrado.getTitulo());

                Long idenficiacionEstudiante = trabajoGrado.getEstudiante().getPersona().getIdentificacion();
                String nombreEstudiante = trabajoGrado.getEstudiante().getPersona().getNombre();
                String apellidoEstudiante = trabajoGrado.getEstudiante().getPersona().getApellido();
                String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

                // Mapear DTO a entidad
                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoIngestigacionMapper
                                .toEntity(sustentacionDto);

                // Establecer la relación uno a uno
                // sustentacionProyectoInvestigacion.setIdTrabajoGrado(trabajoGrado);
                // trabajoGrado.setIdSustentacionProyectoInvestigacion(sustentacionProyectoInvestigacion);

                // Se cambia el numero de estado
                // int numEstado = validarEstado(sustentacionDto.getRespuestaSustentacion());
                // trabajoGrado.setNumeroEstado(numEstado);

                // Guardar la entidad SustentacionProyectoInvestigacion
                sustentacionProyectoInvestigacion.setLinkConstanciaDocumentoFinal(
                                FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                sustentacionProyectoInvestigacion.getLinkConstanciaDocumentoFinal(),
                                                nombreCarpeta));
                sustentacionProyectoInvestigacion
                                .setLinkActaSustentacion(FilesUtilities
                                                .guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                                sustentacionProyectoInvestigacion
                                                                                .getLinkActaSustentacion(),
                                                                nombreCarpeta));
                sustentacionProyectoInvestigacion.setLinkActaSustentacionPublica(
                                FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                sustentacionProyectoInvestigacion.getLinkActaSustentacionPublica(),
                                                nombreCarpeta));
                sustentacionProyectoInvestigacion.setLinkEstudioHojaVidaAcademica(
                                FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                sustentacionProyectoInvestigacion.getLinkEstudioHojaVidaAcademica(),
                                                nombreCarpeta));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacionTmp);

                agregarInformacionCoordinador(sustentacionProyectoInvestigacionTmp, sustentacionDto);

                return sustentacionProyectoIngestigacionResponseMapper
                                .toCoordinadorDto(sustentacionProyectoInvestigacionRes);
        }

        private void agregarInformacionCoordinador(
                        SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion,
                        SustentacionTrabajoInvestigacionCoordinadorDto sustentacionDto) {
                sustentacionProyectoInvestigacion.setRespuestaSustentacion(sustentacionDto.getRespuestaSustentacion());
                sustentacionProyectoInvestigacion
                                .setNumeroActaTrabajoFinal(sustentacionDto.getNumeroActaTrabajoFinal());
                sustentacionProyectoInvestigacion.setFechaActa(sustentacionDto.getFechaActa());
                // Update archivos
                sustentacionProyectoInvestigacion
                                .setLinkConstanciaDocumentoFinal(sustentacionDto.getLinkConstanciaDocumentoFinal());
                sustentacionProyectoInvestigacion.setLinkActaSustentacion(sustentacionDto.getLinkActaSustentacion());
                sustentacionProyectoInvestigacion
                                .setLinkActaSustentacionPublica(sustentacionDto.getLinkActaSustentacionPublica());
                sustentacionProyectoInvestigacion
                                .setLinkEstudioHojaVidaAcademica(sustentacionDto.getLinkEstudioHojaVidaAcademica());
        }

        // @Override
        // @Transactional(readOnly = true)
        // public SustentacionTrabajoInvestigacionDto buscarPorId(Long idTrabajoGrado) {
        // return
        // sustentacionProyectoInvestigacionRepository.findByIdTrabajoGradoId(idTrabajoGrado)
        // .stream()
        // .map(sustentacionProyectoIngestigacionMapper::toDto)
        // .findFirst()
        // .orElse(null);
        // }

        @Override
        @Transactional(readOnly = true)
        public SustentacionTrabajoInvestigacionDocenteResponseDto listarInformacionDocente(Long idTrabajoGrado) {
                Optional<SustentacionTrabajoInvestigacion> responseDto = sustentacionProyectoInvestigacionRepository
                                .findByIdTrabajoGradoId(idTrabajoGrado);
                if (responseDto.isPresent()) {
                        return sustentacionProyectoIngestigacionResponseMapper.toDocenteDto(responseDto.get());
                } else {
                        return null;
                }
        }

        @Override
        @Transactional(readOnly = true)
        public SustentacionTrabajoInvestigacionComiteResponseDto listarInformacionComite(Long idTrabajoGrado) {
                Optional<SustentacionTrabajoInvestigacion> responseDto = sustentacionProyectoInvestigacionRepository
                                .findByIdTrabajoGradoId(idTrabajoGrado);
                if (responseDto.isPresent()) {
                        return sustentacionProyectoIngestigacionResponseMapper.toComiteDto(responseDto.get());
                } else {
                        return null;
                }
        }

        @Override
        @Transactional(readOnly = true)
        public SustentacionTrabajoInvestigacionDto listarInformacionCoordinador(Long idTrabajoGrado) {
                return sustentacionProyectoInvestigacionRepository.findByIdTrabajoGradoId(idTrabajoGrado)
                                .stream()
                                .map(sustentacionProyectoIngestigacionMapper::toDto)
                                .findFirst()
                                .orElse(null);
        }

        @Override
        public SustentacionTrabajoInvestigacionDto actualizar(Long id,
                        SustentacionTrabajoInvestigacionDto sustentacionDto, BindingResult result) {

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion trabajo de investigacion con id: " + id
                                                                + " no encontrado"));

                // Busca el trabajo de grado
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(id).orElseThrow(
                                () -> new ResourceNotFoundException(
                                                "Trabajo de grado con id: " + id + " no encontrado"));

                String procesoVa = "Sustentacion_Proyecto_Investigacion";
                String tituloTrabajoGrado = ConvertString.obtenerIniciales(trabajoGrado.getTitulo());
                Long idenficiacionEstudiante = trabajoGrado.getEstudiante().getPersona().getIdentificacion();
                String nombreEstudiante = trabajoGrado.getEstudiante().getPersona().getNombre();
                String apellidoEstudiante = trabajoGrado.getEstudiante().getPersona().getApellido();
                String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

                SustentacionTrabajoInvestigacion responseSustentacionProyectoInvestigacion = null;
                if (sustentacionProyectoInvestigacionTmp != null) {
                        if (sustentacionDto.getLinkRemisionDocumentoFinal()
                                        .compareTo(sustentacionProyectoInvestigacionTmp
                                                        .getLinkRemisionDocumentoFinal()) != 0) {
                                sustentacionDto
                                                .setLinkRemisionDocumentoFinal(FilesUtilities.guardarArchivoNew(
                                                                tituloTrabajoGrado, procesoVa,
                                                                sustentacionDto.getLinkRemisionDocumentoFinal(),
                                                                nombreCarpeta));
                                FilesUtilities.deleteFileExample(
                                                sustentacionProyectoInvestigacionTmp.getLinkRemisionDocumentoFinal());
                        }
                        if (sustentacionDto.getLinkRemisionDocumentoFinalCF()
                                        .compareTo(sustentacionProyectoInvestigacionTmp
                                                        .getLinkRemisionDocumentoFinalCF()) != 0) {
                                sustentacionDto
                                                .setLinkRemisionDocumentoFinalCF(FilesUtilities.guardarArchivoNew(
                                                                tituloTrabajoGrado, procesoVa,
                                                                sustentacionDto.getLinkRemisionDocumentoFinalCF(),
                                                                nombreCarpeta));
                                FilesUtilities.deleteFileExample(
                                                sustentacionProyectoInvestigacionTmp.getLinkRemisionDocumentoFinalCF());
                        }
                        if (sustentacionDto.getLinkConstanciaDocumentoFinal()
                                        .compareTo(sustentacionProyectoInvestigacionTmp
                                                        .getLinkConstanciaDocumentoFinal()) != 0) {
                                sustentacionDto
                                                .setLinkConstanciaDocumentoFinal(FilesUtilities.guardarArchivoNew(
                                                                tituloTrabajoGrado, procesoVa,
                                                                sustentacionDto.getLinkConstanciaDocumentoFinal(),
                                                                nombreCarpeta));
                                FilesUtilities.deleteFileExample(
                                                sustentacionProyectoInvestigacionTmp.getLinkConstanciaDocumentoFinal());
                        }
                        if (sustentacionDto.getLinkActaSustentacion()
                                        .compareTo(sustentacionProyectoInvestigacionTmp
                                                        .getLinkActaSustentacion()) != 0) {
                                sustentacionDto
                                                .setLinkActaSustentacion(FilesUtilities.guardarArchivoNew(
                                                                tituloTrabajoGrado, procesoVa,
                                                                sustentacionDto.getLinkActaSustentacion(),
                                                                nombreCarpeta));
                                FilesUtilities.deleteFileExample(
                                                sustentacionProyectoInvestigacionTmp.getLinkActaSustentacion());
                        }
                        if (sustentacionDto.getLinkActaSustentacionPublica()
                                        .compareTo(sustentacionProyectoInvestigacionTmp
                                                        .getLinkActaSustentacionPublica()) != 0) {
                                sustentacionDto
                                                .setLinkActaSustentacionPublica(FilesUtilities.guardarArchivoNew(
                                                                tituloTrabajoGrado, procesoVa,
                                                                sustentacionDto.getLinkActaSustentacionPublica(),
                                                                nombreCarpeta));
                                FilesUtilities.deleteFileExample(
                                                sustentacionProyectoInvestigacionTmp.getLinkActaSustentacionPublica());
                        }
                        if (sustentacionDto.getLinkEstudioHojaVidaAcademica()
                                        .compareTo(sustentacionProyectoInvestigacionTmp
                                                        .getLinkEstudioHojaVidaAcademica()) != 0) {
                                sustentacionDto
                                                .setLinkEstudioHojaVidaAcademica(FilesUtilities.guardarArchivoNew(
                                                                tituloTrabajoGrado, procesoVa,
                                                                sustentacionDto.getLinkEstudioHojaVidaAcademica(),
                                                                nombreCarpeta));
                                FilesUtilities.deleteFileExample(
                                                sustentacionProyectoInvestigacionTmp.getLinkEstudioHojaVidaAcademica());
                        }
                        updateSustentacionProyectoInvestigacionValues(sustentacionProyectoInvestigacionTmp,
                                        sustentacionDto);
                        responseSustentacionProyectoInvestigacion = sustentacionProyectoInvestigacionRepository
                                        .save(sustentacionProyectoInvestigacionTmp);
                }
                return sustentacionProyectoIngestigacionMapper.toDto(responseSustentacionProyectoInvestigacion);
        }

        @Override
        @Transactional(readOnly = true)
        public String descargarArchivo(RutaArchivoDto rutaArchivo) {
                return FilesUtilities.recuperarArchivo(rutaArchivo.getRutaArchivo());
        }

        // Funciones privadas
        private void updateSustentacionProyectoInvestigacionValues(
                        SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion,
                        SustentacionTrabajoInvestigacionDto sustentacionDto) {
                sustentacionProyectoInvestigacion.setUrlDocumentacion(sustentacionDto.getUrlDocumentacion());
                sustentacionProyectoInvestigacion.setRespuestaSustentacion(sustentacionDto.getRespuestaSustentacion());
                sustentacionProyectoInvestigacion
                                .setNumeroActaTrabajoFinal(sustentacionDto.getNumeroActaTrabajoFinal());
                sustentacionProyectoInvestigacion.setFechaActa(sustentacionDto.getFechaActa());
                // Update archivos
                sustentacionProyectoInvestigacion
                                .setLinkRemisionDocumentoFinal(sustentacionDto.getLinkRemisionDocumentoFinal());
                sustentacionProyectoInvestigacion
                                .setLinkRemisionDocumentoFinalCF(sustentacionDto.getLinkRemisionDocumentoFinalCF());
                sustentacionProyectoInvestigacion
                                .setLinkConstanciaDocumentoFinal(sustentacionDto.getLinkConstanciaDocumentoFinal());
                sustentacionProyectoInvestigacion.setLinkActaSustentacion(sustentacionDto.getLinkActaSustentacion());
                sustentacionProyectoInvestigacion
                                .setLinkActaSustentacionPublica(sustentacionDto.getLinkActaSustentacionPublica());
                sustentacionProyectoInvestigacion
                                .setLinkEstudioHojaVidaAcademica(sustentacionDto.getLinkEstudioHojaVidaAcademica());
        }

        private int validarEstado(Boolean estado) {
                int numEstado = 0;
                if (!estado) {
                        numEstado = 8;
                } else {
                        numEstado = 9;
                }
                return numEstado;
        }

        private CamposUnicosSustentacionProyectoInvestigacionDto obtenerCamposUnicos(
                        SustentacionTrabajoInvestigacionDto respuesta) {
                return informacionUnicaSustentacionProyectoInvestigacion.apply(respuesta);
        }

        private Map<String, String> validacionCampoUnicos(CamposUnicosSustentacionProyectoInvestigacionDto camposUnicos,
                        CamposUnicosSustentacionProyectoInvestigacionDto camposUnicosBD) {

                Map<String, Function<CamposUnicosSustentacionProyectoInvestigacionDto, Boolean>> mapCamposUnicos = new HashMap<>();

                mapCamposUnicos.put("idTrabajoGrados",
                                dto -> (camposUnicosBD == null || !dto.getIdTrabajoGrados()
                                                .equals(camposUnicosBD.getIdTrabajoGrados()))
                                                && sustentacionProyectoInvestigacionRepository
                                                                .countByTrabajoGradoId(dto.getIdTrabajoGrados()) > 0);

                Predicate<Field> existeCampoUnico = campo -> mapCamposUnicos.containsKey(campo.getName());
                Predicate<Field> existeCampoBD = campoBD -> mapCamposUnicos.get(campoBD.getName()).apply(camposUnicos);
                Predicate<Field> campoInvalido = existeCampoUnico.and(existeCampoBD);

                return Arrays.stream(camposUnicos.getClass().getDeclaredFields())
                                .filter(campoInvalido)
                                .peek(field -> field.setAccessible(true))
                                .collect(Collectors.toMap(Field::getName, field -> {
                                        Object valorCampo = null;
                                        try {
                                                valorCampo = field.get(camposUnicos);
                                        } catch (IllegalAccessException e) {
                                                e.printStackTrace();
                                        }
                                        return mensajeException(field.getName(), valorCampo);
                                }));

        }

        private <T> String mensajeException(String nombreCampo, T valorCampo) {
                return "Campo único, ya se ha registrado una SUSTENTACION PROYECTO DE INVESTIGACION al trabajo de grado: "
                                + valorCampo;
        }
}
