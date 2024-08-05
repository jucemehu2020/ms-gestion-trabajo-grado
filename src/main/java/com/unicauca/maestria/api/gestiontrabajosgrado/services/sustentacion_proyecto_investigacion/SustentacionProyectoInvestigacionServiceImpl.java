package com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientEgresados;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.Concepto;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptosVarios;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.Constants;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.ValidationUtils;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.TiemposPendientes;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.AnexoSolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_proyecto_investigacion.AnexoSustentacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_proyecto_investigacion.RespuestaComiteSustentacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.egresado.CursoSaveDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.egresado.EmpresaSaveDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.AnexoSolicitudExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.DocenteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.ExpertoInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.STICoordinadorFase1ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.SustentacionTrabajoInvestigacionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.STICoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.SustentacionTrabajoInvestigacionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3.STICoordinadorFase3ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3.SustentacionTrabajoInvestigacionCoordinadorFase3Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_4.STICoordinadorFase4ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_4.SustentacionTrabajoInvestigacionCoordinadorFase4Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.AnexoSustentacionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionListDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.estudiante.SustentacionTrabajoInvestigacionEstudianteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.estudiante.SustentacionTrabajoInvestigacionEstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.AnexoSustentacionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.AnexosSustentacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaComiteSustentacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SustentacionProyectoInvestigacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TiemposPendientesRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SustentacionProyectoInvestigacionServiceImpl implements SustentacionProyectoInvestigacionService {

        private final SustentacionProyectoInvestigacionRepository sustentacionProyectoInvestigacionRepository;
        private final SustentacionProyectoInvestigacionMapper sustentacionProyectoIngestigacionMapper;
        private final SustentacionProyectoInvestigacionResponseMapper sustentacionProyectoInvestigacionResponseMapper;
        private final RespuestaComiteSustentacionRepository respuestaComiteSustentacionRepository;
        private final AnexosSustentacionRepository anexosSustentacionRepository;
        private final AnexoSustentacionMapper anexoSustentacionMapper;
        private final TrabajoGradoRepository trabajoGradoRepository;
        private final TiemposPendientesRepository tiemposPendientesRepository;
        private final ArchivoClient archivoClient;
        private final ArchivoClientEgresados archivoClientEgresados;

        @Autowired
        private EnvioCorreos envioCorreos;

        @Override
        @Transactional(readOnly = true)
        public List<DocenteInfoDto> listarDocentes() {
                List<DocenteResponseDto> listadoDocentes = archivoClient.listarDocentesRes();
                if (listadoDocentes.size() == 0) {
                        throw new InformationException("No hay docentes registrados");
                }
                List<DocenteInfoDto> docentes = listadoDocentes.stream()
                                .map(docente -> new DocenteInfoDto(
                                                docente.getId(),
                                                docente.getPersona().getNombre(),
                                                docente.getPersona().getApellido(),
                                                docente.getPersona().getCorreoElectronico(),
                                                "Universidad del Cauca"))
                                .collect(Collectors.toList());
                return docentes;
        }

        @Override
        @Transactional(readOnly = true)
        public List<ExpertoInfoDto> listarExpertos() {
                List<ExpertoResponseDto> listadoExperto = archivoClient.listarExpertos();
                if (listadoExperto.size() == 0) {
                        throw new InformationException("No hay expertos registrados");
                }
                List<ExpertoInfoDto> expertos = listadoExperto.stream()
                                .map(experto -> new ExpertoInfoDto(
                                                experto.getId(),
                                                experto.getPersona().getNombre(),
                                                experto.getPersona().getApellido(),
                                                experto.getPersona().getCorreoElectronico(),
                                                experto.getUniversidadtitexp()))
                                .collect(Collectors.toList());
                return expertos;
        }

        @Override
        @Transactional(readOnly = true)
        public DocenteInfoDto obtenerDocente(Long id) {
                DocenteResponseDto docente = archivoClient.obtenerDocentePorId(id);
                return new DocenteInfoDto(
                                docente.getId(),
                                docente.getPersona().getNombre(),
                                docente.getPersona().getApellido(),
                                docente.getPersona().getCorreoElectronico(),
                                docente.getUltimaUniversidad());
        }

        @Override
        @Transactional(readOnly = true)
        public ExpertoInfoDto obtenerExperto(Long id) {
                ExpertoResponseDto experto = archivoClient.obtenerExpertoPorId(id);
                return new ExpertoInfoDto(
                                experto.getId(),
                                experto.getPersona().getNombre(),
                                experto.getPersona().getApellido(),
                                experto.getPersona().getCorreoElectronico(),
                                experto.getUniversidadtitexp());
        }

        @Override
        @Transactional
        public SustentacionTrabajoInvestigacionDocenteResponseDto insertarInformacionDocente(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionDocenteDto sustentacionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                validarLink(sustentacionDto.getLinkMonografia());
                validarLink(sustentacionDto.getLinkFormatoF());
                for (AnexoSustentacionDto anexo : sustentacionDto.getAnexos()) {
                        validarLink(anexo.getLinkAnexo());
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado
                                                                + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 23) {
                        throw new InformationException("No es permitido registrar la información");
                }

                archivoClient.obtenerDocentePorId(sustentacionDto.getIdJuradoInterno());
                archivoClient.obtenerExpertoPorId(sustentacionDto.getIdJuradoExterno());

                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoIngestigacionMapper
                                .toEntity(sustentacionDto);

                sustentacionProyectoInvestigacion.setTrabajoGrado(trabajoGrado);
                trabajoGrado.setSustentacionProyectoInvestigacion(sustentacionProyectoInvestigacion);

                trabajoGrado.setNumeroEstado(24);

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                sustentacionProyectoInvestigacion.setLinkMonografia(
                                FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                sustentacionProyectoInvestigacion.getLinkMonografia()));

                sustentacionProyectoInvestigacion.setLinkFormatoF(
                                FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                sustentacionProyectoInvestigacion.getLinkFormatoF()));

                if (sustentacionProyectoInvestigacion.getAnexos() != null) {
                        List<AnexoSustentacion> anexosActualizados = new ArrayList<>();
                        for (AnexoSustentacionDto anexoDto : sustentacionDto.getAnexos()) {
                                String rutaAnexo = FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                anexoDto.getLinkAnexo());
                                AnexoSustentacion anexo = new AnexoSustentacion();
                                anexo.setLinkAnexo(rutaAnexo);
                                anexo.setSustentacionProyectoInvestigacion(sustentacionProyectoInvestigacion);
                                anexosActualizados.add(anexo);
                        }
                        sustentacionProyectoInvestigacion.setAnexos(anexosActualizados);
                }

                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacion);

                SustentacionTrabajoInvestigacionDocenteResponseDto sustentacionResponseDto = sustentacionProyectoInvestigacionResponseMapper
                                .toDocenteDto(sustentacionProyectoInvestigacionRes);

                return sustentacionResponseDto;
        }

        @Override
        @Transactional
        public STICoordinadorFase1ResponseDto insertarInformacionCoordinadoFase1(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase1Dto sustentacionDto,
                        BindingResult result) {

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                if (sustentacionDto.getEnvioEmail() == null && sustentacionDto.getConceptoCoordinador()
                                .equals(ConceptoVerificacion.RECHAZADO)) {
                        throw new InformationException("Faltan atributos para el registro");
                }

                if (sustentacionDto.getEnvioEmail() != null && sustentacionDto.getConceptoCoordinador()
                                .equals(ConceptoVerificacion.ACEPTADO)) {
                        throw new InformationException("Envio de atributos no permitido");
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 24) {
                        throw new InformationException("No es permitido registrar la información");
                }

                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: "
                                                                + trabajoGrado.getSustentacionProyectoInvestigacion()
                                                                                .getId()
                                                                + " no encontrado"));

                if (sustentacionDto.getConceptoCoordinador().equals(ConceptoVerificacion.ACEPTADO)) {
                        trabajoGrado.setNumeroEstado(26);
                } else {
                        ArrayList<String> correos = new ArrayList<>();
                        EstudianteResponseDtoAll estudiante = archivoClient
                                        .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                        correos.add(estudiante.getCorreoUniversidad());
                        correos.add(trabajoGrado.getCorreoElectronicoTutor());
                        envioCorreos.enviarCorreosCorrecion(correos,
                                        sustentacionDto.getEnvioEmail().getAsunto(),
                                        sustentacionDto.getEnvioEmail().getMensaje());
                        trabajoGrado.setNumeroEstado(25);
                }

                sustentacionProyectoInvestigacionTmp.setConceptoCoordinador(sustentacionDto.getConceptoCoordinador());

                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacionTmp);

                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase1Dto(sustentacionProyectoInvestigacionRes);
        }

        @Override
        @Transactional
        public STICoordinadorFase2ResponseDto insertarInformacionCoordinadoFase2(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionDto,
                        BindingResult result) {

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                if (sustentacionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                .equals(Concepto.NO_APROBADO)
                                && (sustentacionDto.getLinkEstudioHojaVidaAcademica() != null
                                                || sustentacionDto.getLinkFormatoG() != null
                                                || sustentacionDto.getInformacionEnvioConsejo() != null)) {
                        throw new InformationException("Envio de atributos no permitido");
                }

                if (sustentacionDto.getActaFechaRespuestaComite().get(0).getConceptoComite().equals(Concepto.APROBADO)
                                && (sustentacionDto.getLinkEstudioHojaVidaAcademica() == null
                                                || sustentacionDto.getLinkFormatoG() == null
                                                || sustentacionDto.getInformacionEnvioConsejo() == null)) {
                        throw new InformationException("Atributos incorrectos");
                }

                if (sustentacionDto.getActaFechaRespuestaComite().get(0).getFechaActa() != null
                                && sustentacionDto.getActaFechaRespuestaComite().get(0).getFechaActa()
                                                .isAfter(LocalDate.now())) {
                        throw new InformationException(
                                        "La fecha de registro del comite no puede ser mayor a la fecha actual.");
                }

                if (sustentacionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                .equals(Concepto.APROBADO)) {
                        validarLink(sustentacionDto.getLinkEstudioHojaVidaAcademica());
                        validarLink(sustentacionDto.getLinkFormatoG());
                        List<String> anexos = sustentacionDto.getInformacionEnvioConsejo().getB64Anexos();
                        if (anexos != null && !anexos.isEmpty()) {
                                for (String anexo : anexos) {
                                        ValidationUtils.validarBase64(anexo);
                                }
                        }
                }

                ArrayList<String> correos = new ArrayList<>();

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id "
                                                                + idTrabajoGrado
                                                                + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 26) {
                        throw new InformationException("No es permitido registrar la información");
                }

                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion()
                                                .getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: "
                                                                + trabajoGrado.getSustentacionProyectoInvestigacion()
                                                                                .getId()
                                                                + " no encontrado"));

                for (RespuestaComiteSustentacion respuesta : sustentacionProyectoInvestigacion
                                .getActaFechaRespuestaComite()) {
                        if (respuesta.getConceptoComite().equals(Concepto.APROBADO)) {
                                throw new InformationException("El concepto ya es APROBADO");
                        }
                }

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                if (sustentacionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                .equals(Concepto.APROBADO)) {
                        correos.add(Constants.correoConsejo);
                        Map<String, Object> documentosParaConsejo = sustentacionDto
                                        .getInformacionEnvioConsejo().getDocumentos();
                        envioCorreos.enviarCorreoConAnexos(correos, sustentacionDto.getEnvioEmail().getAsunto(),
                                        sustentacionDto.getEnvioEmail().getMensaje(), documentosParaConsejo);

                        sustentacionDto.setLinkEstudioHojaVidaAcademica(FilesUtilities.guardarArchivoNew2(
                                        rutaArchivo,
                                        sustentacionDto.getLinkEstudioHojaVidaAcademica()));

                        sustentacionDto.setLinkFormatoG(FilesUtilities.guardarArchivoNew2(
                                        rutaArchivo,
                                        sustentacionDto.getLinkFormatoG()));

                        trabajoGrado.setNumeroEstado(28);
                } else {
                        EstudianteResponseDtoAll estudiante = archivoClient
                                        .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                        correos.add(estudiante.getCorreoUniversidad());
                        correos.add(trabajoGrado.getCorreoElectronicoTutor());
                        envioCorreos.enviarCorreosCorrecion(correos,
                                        sustentacionDto.getEnvioEmail().getAsunto(),
                                        sustentacionDto.getEnvioEmail().getMensaje());
                        trabajoGrado.setNumeroEstado(27);
                }

                agregarInformacionCoordinadorFase2(sustentacionProyectoInvestigacion, sustentacionDto);

                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacion);

                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase2Dto(sustentacionProyectoInvestigacionRes);
        }

        private void agregarInformacionCoordinadorFase2(
                        SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacion,
                        SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionTrabajoInvestigacionCoordinadorFase2Dto) {

                RespuestaComiteSustentacion respuestaComite = RespuestaComiteSustentacion.builder()
                                .conceptoComite(sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                                .getActaFechaRespuestaComite().get(0)
                                                .getConceptoComite())
                                .numeroActa(sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                                .getActaFechaRespuestaComite().get(0)
                                                .getNumeroActa())
                                .fechaActa(sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                                .getActaFechaRespuestaComite().get(0).getFechaActa())
                                .sustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacion)
                                .build();

                // Si la colección está vacía, inicializarla
                if (sustentacionTrabajoInvestigacion.getActaFechaRespuestaComite() == null) {
                        sustentacionTrabajoInvestigacion.setActaFechaRespuestaComite(new ArrayList<>());
                }

                sustentacionTrabajoInvestigacion.getActaFechaRespuestaComite().add(respuestaComite);
                sustentacionTrabajoInvestigacion.setLinkEstudioHojaVidaAcademica(
                                sustentacionTrabajoInvestigacionCoordinadorFase2Dto.getLinkEstudioHojaVidaAcademica());
                sustentacionTrabajoInvestigacion.setLinkFormatoG(
                                sustentacionTrabajoInvestigacionCoordinadorFase2Dto.getLinkFormatoG());
        }

        @Override
        @Transactional
        public STICoordinadorFase3ResponseDto insertarInformacionCoordinadoFase3(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                if (sustentacionDto.getJuradosAceptados().equals(ConceptoVerificacion.ACEPTADO)
                                && (!sustentacionDto.getIdJuradoInterno().equals("Sin cambios")
                                                || !sustentacionDto.getIdJuradoExterno().equals("Sin cambios"))) {
                        throw new InformationException("Envio de atributos no permitido");
                }

                if (sustentacionDto.getJuradosAceptados().equals(ConceptoVerificacion.RECHAZADO)
                                && (sustentacionDto.getIdJuradoInterno().equals("Sin cambios")
                                                || sustentacionDto.getIdJuradoExterno().equals("Sin cambios"))) {
                        throw new InformationException("Atributos incorrectos");
                }

                if (sustentacionDto.getFechaActaConsejo() != null
                                && sustentacionDto.getFechaActaConsejo().isAfter(LocalDate.now())) {
                        throw new InformationException(
                                        "La fecha del consejo no puede ser mayor a la fecha actual.");
                }

                if (sustentacionDto.getFechaSustentacion() != null
                                && sustentacionDto.getFechaSustentacion().isBefore(LocalDate.now())) {
                        throw new InformationException(
                                        "La fecha máxima de la sustentacion no puede ser mayor a la fecha actual.");
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id "
                                                                + idTrabajoGrado
                                                                + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 28) {
                        throw new InformationException("No es permitido registrar la información");
                }

                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion()
                                                .getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id "
                                                                + trabajoGrado.getSustentacionProyectoInvestigacion()
                                                                                .getId()
                                                                + " no encontrado"));

                String directorioArchivos = identificacionArchivo(trabajoGrado);

                sustentacionDto.setLinkOficioConsejo(
                                FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                                sustentacionDto.getLinkOficioConsejo()));

                agregarInformacionCoordinadorFase3(sustentacionProyectoInvestigacionTmp, sustentacionDto, trabajoGrado);

                if (sustentacionDto.getFechaSustentacion() == null) {
                        trabajoGrado.setNumeroEstado(36);
                } else {
                        trabajoGrado.setNumeroEstado(29);
                }

                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacionTmp);

                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase3Dto(sustentacionProyectoInvestigacionRes);
        }

        private void agregarInformacionCoordinadorFase3(
                        SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion,
                        SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionDto,
                        TrabajoGrado trabajoGrado) {

                if (sustentacionDto.getJuradosAceptados().equals(ConceptoVerificacion.RECHAZADO)) {
                        archivoClient.obtenerDocentePorId(Long.parseLong(sustentacionDto.getIdJuradoInterno()));
                        archivoClient.obtenerExpertoPorId(Long.parseLong(sustentacionDto.getIdJuradoExterno()));

                        sustentacionProyectoInvestigacion
                                        .setIdJuradoInterno(Long.parseLong(sustentacionDto.getIdJuradoInterno()));
                        sustentacionProyectoInvestigacion
                                        .setIdJuradoExterno(Long.parseLong(sustentacionDto.getIdJuradoExterno()));
                }
                sustentacionProyectoInvestigacion.setJuradosAceptados(sustentacionDto.getJuradosAceptados());
                sustentacionProyectoInvestigacion.setFechaSustentacion(sustentacionDto.getFechaSustentacion());
                sustentacionProyectoInvestigacion.setNumeroActaConsejo(sustentacionDto.getNumeroActaConsejo());
                sustentacionProyectoInvestigacion.setFechaActaConsejo(sustentacionDto.getFechaActaConsejo());
                sustentacionProyectoInvestigacion.setLinkOficioConsejo(sustentacionDto.getLinkOficioConsejo());

        }

        @Override
        @Transactional
        public SustentacionTrabajoInvestigacionEstudianteResponseDto insertarInformacionEstudiante(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionEstudianteDto sustentacionDto,
                        BindingResult result) {

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                validarLink(sustentacionDto.getLinkFormatoH());
                validarLink(sustentacionDto.getLinkFormatoI());
                validarLink(sustentacionDto.getLinkEstudioHojaVidaAcademicaGrado());

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id "
                                                                + idTrabajoGrado
                                                                + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 29) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // List<CursoSaveDto> cursos = archivoClientEgresados
                // .obtenerCursosPorIdEstudiante(trabajoGrado.getIdEstudiante());
                List<EmpresaSaveDto> empresas = archivoClientEgresados
                                .obtenerEmpresasPorIdEstudiante(trabajoGrado.getIdEstudiante());

                if (empresas.size() == 0) {
                        throw new InformationException(
                                        "No es permitido registrar la información debido a que el estudiante no ha completado los datos de egresado");
                }

                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion()
                                                .getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: "
                                                                + trabajoGrado.getSustentacionProyectoInvestigacion()
                                                                                .getId()
                                                                + " no encontrado"));

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                trabajoGrado.setNumeroEstado(30);

                sustentacionProyectoInvestigacionTmp.setLinkFormatoH(FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                sustentacionDto.getLinkFormatoH()));

                sustentacionProyectoInvestigacionTmp.setLinkFormatoI(FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                sustentacionDto.getLinkFormatoI()));

                sustentacionProyectoInvestigacionTmp.setLinkEstudioHojaVidaAcademicaGrado(
                                FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                sustentacionDto.getLinkEstudioHojaVidaAcademicaGrado()));

                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacionTmp);

                return sustentacionProyectoInvestigacionResponseMapper
                                .toEstudianteDto(sustentacionProyectoInvestigacionRes);
        }

        @Override
        @Transactional
        public STICoordinadorFase4ResponseDto insertarInformacionCoordinadoFase4(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionDto,
                        BindingResult result) {

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                if (sustentacionDto.getRespuestaSustentacion().equals(ConceptosVarios.APROBADO)
                                && (sustentacionDto.getLinkActaSustentacionPublica() == null
                                                || sustentacionDto.getNumeroActaFinal() == null
                                                || sustentacionDto.getFechaActaFinal() == null)) {
                        throw new InformationException("Atributos incorrectos");
                }

                if ((sustentacionDto.getRespuestaSustentacion().equals(ConceptosVarios.NO_APROBADO) ||
                                sustentacionDto.getRespuestaSustentacion().equals(ConceptosVarios.APLAZADO))
                                && (sustentacionDto.getLinkActaSustentacionPublica() != null
                                                || sustentacionDto.getNumeroActaFinal() != null
                                                || sustentacionDto.getFechaActaFinal() != null)) {
                        throw new InformationException("Envio de atributos no permitido");
                }

                if (sustentacionDto.getRespuestaSustentacion().equals(ConceptosVarios.APROBADO)) {
                        validarLink(sustentacionDto.getLinkActaSustentacionPublica());
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 30) {
                        throw new InformationException("No es permitido registrar la información");
                }

                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion()
                                                .getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: "
                                                                + trabajoGrado.getSustentacionProyectoInvestigacion()
                                                                                .getId()
                                                                + " no encontrado"));

                if (sustentacionDto.getRespuestaSustentacion().equals(ConceptosVarios.APROBADO)) {
                        Optional<TiemposPendientes> tiemposPendientesOpt = tiemposPendientesRepository
                                        .findByTrabajoGradoId(idTrabajoGrado);
                        if (tiemposPendientesOpt.isPresent()) {
                                tiemposPendientesRepository.delete(tiemposPendientesOpt.get());
                        }
                        trabajoGrado.setNumeroEstado(31);
                } else if (sustentacionDto.getRespuestaSustentacion().equals(ConceptosVarios.NO_APROBADO)) {
                        trabajoGrado.setNumeroEstado(32);
                } else {
                        trabajoGrado.setNumeroEstado(33);
                        insertarInformacionTiempos(trabajoGrado);
                }

                agregarInformacionCoordinadorFase4(sustentacionProyectoInvestigacionTmp, sustentacionDto, trabajoGrado);

                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacionTmp);

                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase4Dto(sustentacionProyectoInvestigacionRes);
        }

        private void insertarInformacionTiempos(TrabajoGrado trabajoGrado) {
                Optional<TiemposPendientes> optionalTiemposPendientes = tiemposPendientesRepository
                                .findByTrabajoGradoId(trabajoGrado.getId());

                TiemposPendientes tiemposPendientes = new TiemposPendientes();
                if (optionalTiemposPendientes.isPresent()) {
                        // Si el registro ya existe, lo actualizamos
                        tiemposPendientes = optionalTiemposPendientes.get();
                } else {
                        // Si no existe, creamos uno nuevo
                        tiemposPendientes = new TiemposPendientes();
                        tiemposPendientes.setTrabajoGrado(trabajoGrado);
                }

                LocalDate fechaActual = LocalDate.now();
                tiemposPendientes.setFechaRegistro(LocalDate.now());
                tiemposPendientes.setEstado(trabajoGrado.getNumeroEstado());
                tiemposPendientes.setFechaLimite(fechaActual.plusDays(60));

                tiemposPendientesRepository.save(tiemposPendientes);
        }

        private void agregarInformacionCoordinadorFase4(
                        SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion,
                        SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionDto,
                        TrabajoGrado trabajoGrado) {

                if (sustentacionDto.getRespuestaSustentacion().equals(ConceptosVarios.APROBADO)) {
                        String rutaArchivo = identificacionArchivo(trabajoGrado);

                        sustentacionProyectoInvestigacion.setLinkActaSustentacionPublica(
                                        FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                        sustentacionDto
                                                                        .getLinkActaSustentacionPublica()));
                } else {
                        sustentacionProyectoInvestigacion
                                        .setLinkActaSustentacionPublica(
                                                        sustentacionDto.getLinkActaSustentacionPublica());
                }
                sustentacionProyectoInvestigacion
                                .setRespuestaSustentacion(sustentacionDto.getRespuestaSustentacion());
                sustentacionProyectoInvestigacion
                                .setNumeroActaFinal(sustentacionDto.getNumeroActaFinal());
                sustentacionProyectoInvestigacion
                                .setFechaActaFinal(sustentacionDto.getFechaActaFinal());
        }

        @Override
        @Transactional(readOnly = true)
        public SustentacionTrabajoInvestigacionListDocenteDto listarInformacionDocente(Long idTrabajoGrado) {

                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findByTrabajoGradoId(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con ID trabajo de grado " + idTrabajoGrado
                                                                + " no encontrado"));

                DocenteResponseDto docente = archivoClient
                                .obtenerDocentePorId(sustentacionProyectoInvestigacionTmp.getIdJuradoInterno());
                String nombre_docente = docente.getPersona().getNombre() + " " + docente.getPersona().getApellido();
                Map<String, String> evaluadorInternoMap = new HashMap<>();
                evaluadorInternoMap.put("nombres", nombre_docente);
                evaluadorInternoMap.put("universidad", "Universidad del Cauca");
                evaluadorInternoMap.put("correo", docente.getPersona().getCorreoElectronico());

                ExpertoResponseDto experto = archivoClient
                                .obtenerExpertoPorId(sustentacionProyectoInvestigacionTmp.getIdJuradoExterno());
                String nombre_experto = experto.getPersona().getNombre() + " " + experto.getPersona().getApellido();
                Map<String, String> evaluadorExternoMap = new HashMap<>();
                evaluadorExternoMap.put("nombres", nombre_experto);
                evaluadorExternoMap.put("universidad", experto.getUniversidadtitexp());
                evaluadorExternoMap.put("correo", experto.getPersona().getCorreoElectronico());

                SustentacionTrabajoInvestigacionListDocenteDto sustentacionTrabajoInvestigacionDocenteResponseDto = new SustentacionTrabajoInvestigacionListDocenteDto();
                sustentacionTrabajoInvestigacionDocenteResponseDto.setId(
                                sustentacionProyectoInvestigacionTmp.getId());
                sustentacionTrabajoInvestigacionDocenteResponseDto
                                .setLinkFormatoF(sustentacionProyectoInvestigacionTmp.getLinkFormatoF());
                sustentacionTrabajoInvestigacionDocenteResponseDto
                                .setLinkMonografia(sustentacionProyectoInvestigacionTmp.getLinkMonografia());

                List<AnexoSustentacion> anexoSustentacion = anexosSustentacionRepository
                                .obtenerAnexosPorId(sustentacionTrabajoInvestigacionDocenteResponseDto.getId());

                ArrayList<String> listaAnexos = new ArrayList<>();
                for (AnexoSustentacion anexo : anexoSustentacion) {
                        listaAnexos.add(anexo.getLinkAnexo());
                }

                sustentacionTrabajoInvestigacionDocenteResponseDto.setAnexos(listaAnexos);
                sustentacionTrabajoInvestigacionDocenteResponseDto.setJuradoInterno(evaluadorInternoMap);
                sustentacionTrabajoInvestigacionDocenteResponseDto.setJuradoExterno(evaluadorExternoMap);

                return sustentacionTrabajoInvestigacionDocenteResponseDto;
        }

        @Override
        @Transactional(readOnly = true)
        public STICoordinadorFase1ResponseDto listarInformacionCoordinadorFase1(
                        Long idTrabajoGrado) {

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findByTrabajoGradoId(idTrabajoGrado).orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado
                                                                + " no encontrado"));

                if (sustentacionTrabajoInvestigacion.getConceptoCoordinador() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase1Dto(sustentacionTrabajoInvestigacion);
        }

        @Override
        @Transactional(readOnly = true)
        public STICoordinadorFase2ResponseDto listarInformacionCoordinadorFase2(Long idTrabajoGrado) {

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findByTrabajoGradoId(idTrabajoGrado).orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado
                                                                + " no encontrado"));

                boolean actaFechaRespuestaComiteEmpty = sustentacionTrabajoInvestigacion
                                .getActaFechaRespuestaComite() == null ||
                                sustentacionTrabajoInvestigacion.getActaFechaRespuestaComite().isEmpty();

                if (actaFechaRespuestaComiteEmpty
                                && sustentacionTrabajoInvestigacion.getLinkEstudioHojaVidaAcademica() == null
                                && sustentacionTrabajoInvestigacion.getLinkFormatoG() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                STICoordinadorFase2ResponseDto responseDto = sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase2Dto(sustentacionTrabajoInvestigacion);

                return responseDto;
        }

        @Override
        @Transactional(readOnly = true)
        public STICoordinadorFase3ResponseDto listarInformacionCoordinadorFase3(
                        Long idTrabajoGrado) {

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findByTrabajoGradoId(idTrabajoGrado).orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado
                                                                + " no encontrado"));

                if (sustentacionTrabajoInvestigacion.getJuradosAceptados() == null
                                && sustentacionTrabajoInvestigacion.getNumeroActaConsejo() == null
                                && sustentacionTrabajoInvestigacion.getFechaActaConsejo() == null
                                && sustentacionTrabajoInvestigacion.getLinkOficioConsejo() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase3Dto(sustentacionTrabajoInvestigacion);
        }

        @Override
        @Transactional(readOnly = true)
        public SustentacionTrabajoInvestigacionEstudianteResponseDto listarInformacionEstudiante(Long idTrabajoGrado) {
                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findByTrabajoGradoId(idTrabajoGrado).orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado
                                                                + " no encontrado"));

                if (sustentacionTrabajoInvestigacion.getLinkFormatoH() == null
                                && sustentacionTrabajoInvestigacion.getLinkFormatoI() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                return sustentacionProyectoInvestigacionResponseMapper
                                .toEstudianteDto(sustentacionTrabajoInvestigacion);
        }

        @Override
        @Transactional(readOnly = true)
        public STICoordinadorFase4ResponseDto listarInformacionCoordinadorFase4(
                        Long idTrabajoGrado) {
                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findByTrabajoGradoId(idTrabajoGrado).orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado
                                                                + " no encontrado"));

                if (sustentacionTrabajoInvestigacion.getRespuestaSustentacion() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase4Dto(sustentacionTrabajoInvestigacion);
        }

        @Override
        @Transactional(readOnly = true)
        public Boolean verificarEgresado(Long idTrabajoGrado) {
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + idTrabajoGrado + " no encontrado"));

                // List<CursoSaveDto> cursos = archivoClientEgresados
                // .obtenerCursosPorIdEstudiante(trabajoGrado.getIdEstudiante());
                List<EmpresaSaveDto> empresas = archivoClientEgresados
                                .obtenerEmpresasPorIdEstudiante(trabajoGrado.getIdEstudiante());

                if (empresas.size() == 0) {
                        return false;
                }

                return true;
        }

        private String identificacionArchivo(TrabajoGrado trabajoGrado) {
                EstudianteResponseDtoAll informacionEstudiantes = archivoClient
                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

                String procesoVa = "Sustentacion_Proyecto_Investigacion";

                // Obtener la fecha actual
                LocalDate fechaActual = LocalDate.now();
                int anio = fechaActual.getYear();
                int mes = fechaActual.getMonthValue();

                Long identificacionEstudiante = informacionEstudiantes.getPersona().getIdentificacion();
                String nombreEstudiante = informacionEstudiantes.getPersona().getNombre();
                String apellidoEstudiante = informacionEstudiantes.getPersona().getApellido();
                String informacionEstudiante = identificacionEstudiante + "-" + nombreEstudiante + "_"
                                + apellidoEstudiante;
                String rutaCarpeta = anio + "/" + mes + "/" + informacionEstudiante + "/" + procesoVa;

                return rutaCarpeta;
        }

        @Override
        @Transactional
        public SustentacionTrabajoInvestigacionDocenteResponseDto actualizarInformacionDocente(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionDocenteDto sustentacionDto,
                        BindingResult result) {

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 24 && trabajoGrado.getNumeroEstado() != 25
                                && trabajoGrado.getNumeroEstado() != 27) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Valida si el docente y experto existen
                archivoClient.obtenerDocentePorId(sustentacionDto.getIdJuradoInterno());
                archivoClient.obtenerExpertoPorId(sustentacionDto.getIdJuradoExterno());

                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion()
                                                .getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: "
                                                                + trabajoGrado.getSustentacionProyectoInvestigacion()
                                                                                .getId()
                                                                + " no encontrado"));

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                trabajoGrado.setNumeroEstado(24);

                if (!sustentacionDto.getLinkMonografia()
                                .equals(sustentacionProyectoInvestigacionTmp.getLinkMonografia())) {
                        validarLink(sustentacionDto.getLinkMonografia());
                        sustentacionDto.setLinkMonografia(FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                        sustentacionDto.getLinkMonografia()));
                        FilesUtilities.deleteFileExample(
                                        sustentacionProyectoInvestigacionTmp.getLinkMonografia());
                }
                if (!sustentacionDto.getLinkFormatoF().equals(sustentacionProyectoInvestigacionTmp.getLinkFormatoF())) {
                        validarLink(sustentacionDto.getLinkFormatoF());
                        sustentacionDto.setLinkFormatoF(FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                        sustentacionDto.getLinkFormatoF()));
                        FilesUtilities.deleteFileExample(
                                        sustentacionProyectoInvestigacionTmp.getLinkFormatoF());
                }

                if (sustentacionDto.getAnexos() != null) {
                        List<AnexoSustentacion> anexosEntidades = sustentacionDto.getAnexos().stream()
                                        .map(anexoSustentacionMapper::toEntity)
                                        .collect(Collectors.toList());

                        actualizarAnexos(sustentacionProyectoInvestigacionTmp, anexosEntidades, rutaArchivo);
                }

                updateExamenValoracionDocenteValues(sustentacionProyectoInvestigacionTmp, sustentacionDto,
                                trabajoGrado);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacionTmp);

                SustentacionTrabajoInvestigacionDocenteResponseDto sustentacionResponseDto = sustentacionProyectoInvestigacionResponseMapper
                                .toDocenteDto(sustentacionTrabajoInvestigacion);

                return sustentacionResponseDto;
        }

        private void updateExamenValoracionDocenteValues(
                        SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacion,
                        SustentacionTrabajoInvestigacionDocenteDto sustentacionTrabajoInvestigacionDocenteDto,
                        TrabajoGrado trabajoGrado) {

                sustentacionTrabajoInvestigacion.setConceptoCoordinador(null);

                sustentacionTrabajoInvestigacion
                                .setIdJuradoInterno(sustentacionTrabajoInvestigacionDocenteDto.getIdJuradoInterno());
                sustentacionTrabajoInvestigacion
                                .setIdJuradoExterno(sustentacionTrabajoInvestigacionDocenteDto.getIdJuradoExterno());
                // Update archivos
                sustentacionTrabajoInvestigacion
                                .setLinkMonografia(sustentacionTrabajoInvestigacionDocenteDto.getLinkMonografia());
                sustentacionTrabajoInvestigacion
                                .setLinkFormatoF(sustentacionTrabajoInvestigacionDocenteDto.getLinkFormatoF());
        }

        private void actualizarAnexos(SustentacionProyectoInvestigacion sustentacionTmp,
                        List<AnexoSustentacion> anexosNuevos, String rutaArchivo) {
                List<AnexoSustentacion> anexosActuales = sustentacionTmp.getAnexos();

                // Mapa para buscar anexos actuales por enlace
                Map<String, AnexoSustentacion> mapaAnexosActuales = anexosActuales.stream()
                                .collect(Collectors.toMap(AnexoSustentacion::getLinkAnexo,
                                                Function.identity()));

                // Lista de anexos actualizados
                List<AnexoSustentacion> anexosActualizados = new ArrayList<>();

                for (AnexoSustentacion anexoNuevo : anexosNuevos) {
                        AnexoSustentacion anexoActual = mapaAnexosActuales.get(anexoNuevo.getLinkAnexo());

                        if (anexoActual != null) {
                                // El anexo no ha cambiado, mantener el actual
                                anexosActualizados.add(anexoActual);
                        } else {
                                // El anexo ha cambiado o es nuevo, validar y agregar
                                validarLink(anexoNuevo.getLinkAnexo());
                                String rutaAnexoNueva = FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                anexoNuevo.getLinkAnexo());
                                anexoNuevo.setLinkAnexo(rutaAnexoNueva);
                                anexoNuevo.setSustentacionProyectoInvestigacion(sustentacionTmp);
                                anexosActualizados.add(anexoNuevo);
                        }
                }

                // Eliminar archivos de los anexos que ya no están en la lista nueva y eliminar
                // el anexo de la entidad
                Iterator<AnexoSustentacion> iterator = anexosActuales.iterator();
                while (iterator.hasNext()) {
                        AnexoSustentacion anexoActual = iterator.next();
                        if (!anexosActualizados.contains(anexoActual)) {
                                FilesUtilities.deleteFileExample(anexoActual.getLinkAnexo());
                                iterator.remove();
                        }
                }

                // Agregar los nuevos anexos a la colección existente
                anexosActuales.clear();
                anexosActuales.addAll(anexosActualizados);
        }

        @Override
        @Transactional
        public STICoordinadorFase1ResponseDto actualizarInformacionCoordinadoFase1(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase1Dto sustentacionDto,
                        BindingResult result) {

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                if (sustentacionDto.getEnvioEmail() == null
                                && sustentacionDto.getConceptoCoordinador()
                                                .equals(ConceptoVerificacion.RECHAZADO)) {
                        throw new InformationException("Faltan atributos para el registro");
                }

                if (sustentacionDto.getEnvioEmail() != null && sustentacionDto.getConceptoCoordinador()
                                .equals(ConceptoVerificacion.ACEPTADO)) {
                        throw new InformationException("Envio de atributos no permitido");
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 24 && trabajoGrado.getNumeroEstado() != 25
                                && trabajoGrado.getNumeroEstado() != 26) {
                        throw new InformationException("No es permitido registrar la información");
                }

                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion()
                                                .getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: "
                                                                + trabajoGrado.getSustentacionProyectoInvestigacion()
                                                                                .getId()
                                                                + " no encontrado"));

                if (sustentacionProyectoInvestigacionTmp.getConceptoCoordinador() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                if (sustentacionDto.getConceptoCoordinador() != sustentacionProyectoInvestigacionTmp
                                .getConceptoCoordinador()) {
                        ArrayList<String> correos = new ArrayList<>();
                        // Si pasa de aprobado a no aprobado
                        if (sustentacionDto.getConceptoCoordinador().equals(ConceptoVerificacion.RECHAZADO)) {
                                EstudianteResponseDtoAll estudiante = archivoClient
                                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                                correos.add(estudiante.getCorreoUniversidad());
                                correos.add(trabajoGrado.getCorreoElectronicoTutor());
                                envioCorreos.enviarCorreosCorrecion(correos,
                                                sustentacionDto.getEnvioEmail().getAsunto(),
                                                sustentacionDto.getEnvioEmail().getMensaje());
                                trabajoGrado.setNumeroEstado(25);
                        } else {
                                trabajoGrado.setNumeroEstado(26);
                        }
                }

                sustentacionProyectoInvestigacionTmp.setConceptoCoordinador(sustentacionDto.getConceptoCoordinador());
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacionTmp);

                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase1Dto(sustentacionProyectoInvestigacionRes);
        }

        @Override
        @Transactional
        public STICoordinadorFase2ResponseDto actualizarInformacionCoordinadoFase2(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                if (sustentacionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                .equals(Concepto.NO_APROBADO)
                                && (sustentacionDto.getLinkEstudioHojaVidaAcademica() != null
                                                || sustentacionDto.getLinkFormatoG() != null
                                                || sustentacionDto.getInformacionEnvioConsejo() != null)) {
                        throw new InformationException("Envio de atributos no permitido");
                }

                if (sustentacionDto.getActaFechaRespuestaComite().get(0).getConceptoComite().equals(Concepto.APROBADO)
                                && (sustentacionDto.getLinkEstudioHojaVidaAcademica() == null
                                                || sustentacionDto.getLinkFormatoG() == null
                                                || sustentacionDto.getInformacionEnvioConsejo() == null)) {
                        throw new InformationException("Atributos incorrectos");
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 26 && trabajoGrado.getNumeroEstado() != 27
                                && trabajoGrado.getNumeroEstado() != 28) {
                        throw new InformationException("No es permitido registrar la información");
                }

                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionOld = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion()
                                                .getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: "
                                                                + trabajoGrado.getSustentacionProyectoInvestigacion()
                                                                                .getId()
                                                                + " no encontrado"));

                boolean actaFechaRespuestaComiteEmpty = sustentacionProyectoInvestigacionOld
                                .getActaFechaRespuestaComite() == null ||
                                sustentacionProyectoInvestigacionOld.getActaFechaRespuestaComite().isEmpty();
                if (actaFechaRespuestaComiteEmpty
                                && sustentacionProyectoInvestigacionOld.getLinkEstudioHojaVidaAcademica() == null
                                && sustentacionProyectoInvestigacionOld.getLinkFormatoG() == null
                                && sustentacionDto.getInformacionEnvioConsejo() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacion = null;
                List<RespuestaComiteSustentacion> respuestaComiteList = sustentacionProyectoInvestigacionRepository
                                .findRespuestaComiteBySustentacionId(
                                                sustentacionProyectoInvestigacionOld
                                                                .getId());
                RespuestaComiteSustentacion ultimoRegistro = respuestaComiteList.isEmpty() ? null
                                : respuestaComiteList.get(0);

                if (ultimoRegistro != null
                                && ultimoRegistro.getConceptoComite() != sustentacionDto
                                                .getActaFechaRespuestaComite().get(0)
                                                .getConceptoComite()) {
                        ArrayList<String> correos = new ArrayList<>();
                        // Si pasa de aprobado a no aprobado
                        if (sustentacionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                        .equals(Concepto.NO_APROBADO)) {
                                EstudianteResponseDtoAll estudiante = archivoClient
                                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                                correos.add(estudiante.getCorreoUniversidad());
                                correos.add(trabajoGrado.getCorreoElectronicoTutor());
                                envioCorreos.enviarCorreosCorrecion(correos,
                                                sustentacionDto.getEnvioEmail().getAsunto(),
                                                sustentacionDto.getEnvioEmail().getMensaje());
                                FilesUtilities.deleteFileExample(
                                                sustentacionProyectoInvestigacionOld
                                                                .getLinkEstudioHojaVidaAcademica());
                                FilesUtilities.deleteFileExample(
                                                sustentacionProyectoInvestigacionOld
                                                                .getLinkFormatoG());

                                trabajoGrado.setNumeroEstado(27);
                        } else {
                                validarLink(sustentacionDto.getLinkEstudioHojaVidaAcademica());
                                validarLink(sustentacionDto.getLinkFormatoG());
                                correos.add(Constants.correoComite);
                                Map<String, Object> documentosParaConsejo = new HashMap<>();
                                correos.add(Constants.correoComite);
                                String[] solicitudConsejo = sustentacionDto
                                                .getLinkEstudioHojaVidaAcademica()
                                                .split("-");
                                documentosParaConsejo.put("historiaAcademica", solicitudConsejo[1]);
                                String[] formatoG = sustentacionDto
                                                .getLinkFormatoG()
                                                .split("-");
                                documentosParaConsejo.put("formatoG", formatoG[1]);
                                envioCorreos.enviarCorreoConAnexos(correos,
                                                sustentacionDto.getEnvioEmail().getAsunto(),
                                                sustentacionDto.getEnvioEmail().getMensaje(),
                                                documentosParaConsejo);

                                sustentacionDto.setLinkEstudioHojaVidaAcademica(FilesUtilities.guardarArchivoNew2(
                                                rutaArchivo,
                                                sustentacionDto.getLinkEstudioHojaVidaAcademica()));

                                sustentacionDto.setLinkFormatoG(FilesUtilities.guardarArchivoNew2(
                                                rutaArchivo,
                                                sustentacionDto.getLinkFormatoG()));

                                trabajoGrado.setNumeroEstado(28);
                        }
                } else {
                        if (sustentacionProyectoInvestigacionOld != null
                                        && sustentacionDto.getLinkEstudioHojaVidaAcademica() != null) {
                                if (sustentacionDto != null && sustentacionDto.getLinkEstudioHojaVidaAcademica()
                                                .compareTo(sustentacionProyectoInvestigacionOld
                                                                .getLinkEstudioHojaVidaAcademica()) != 0) {
                                        validarLink(sustentacionDto.getLinkEstudioHojaVidaAcademica());
                                        sustentacionDto.setLinkEstudioHojaVidaAcademica(FilesUtilities
                                                        .guardarArchivoNew2(rutaArchivo, sustentacionDto
                                                                        .getLinkEstudioHojaVidaAcademica()));
                                        FilesUtilities.deleteFileExample(
                                                        sustentacionProyectoInvestigacionOld
                                                                        .getLinkEstudioHojaVidaAcademica());
                                }
                                if (sustentacionDto.getLinkFormatoG() != null && sustentacionDto.getLinkFormatoG()
                                                .compareTo(sustentacionProyectoInvestigacionOld
                                                                .getLinkFormatoG()) != 0) {
                                        validarLink(sustentacionDto.getLinkFormatoG());
                                        sustentacionDto.setLinkFormatoG(
                                                        FilesUtilities.guardarArchivoNew2(rutaArchivo, sustentacionDto
                                                                        .getLinkFormatoG()));
                                        FilesUtilities.deleteFileExample(
                                                        sustentacionProyectoInvestigacionOld
                                                                        .getLinkFormatoG());
                                }
                        }
                        trabajoGrado.setNumeroEstado(26);
                }
                updateSustentacionCoordinadorFase2Values(sustentacionProyectoInvestigacionOld, sustentacionDto,
                                trabajoGrado);
                sustentacionTrabajoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacionOld);
                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase2Dto(sustentacionTrabajoInvestigacion);
        }

        private void updateSustentacionCoordinadorFase2Values(
                        SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacion,
                        SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionTrabajoInvestigacionCoordinadorFase2Dto,
                        TrabajoGrado trabajoGrado) {

                List<RespuestaComiteSustentacion> respuestaComiteList = sustentacionProyectoInvestigacionRepository
                                .findRespuestaComiteBySustentacionId(sustentacionTrabajoInvestigacion.getId());
                RespuestaComiteSustentacion ultimoRegistro = respuestaComiteList.isEmpty() ? null
                                : respuestaComiteList.get(0);

                if (ultimoRegistro != null) {
                        // Actualizar los valores de ultimoRegistro
                        ultimoRegistro.setNumeroActa(sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                        .getActaFechaRespuestaComite().get(0).getNumeroActa());
                        ultimoRegistro.setFechaActa(sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                        .getActaFechaRespuestaComite().get(0).getFechaActa());

                        // Actualizar la lista actaFechaRespuestaComite de examenValoracion
                        RespuestaComiteSustentacion actaFechaRespuestaComite = respuestaComiteSustentacionRepository
                                        .findFirstByOrderByIdDesc();

                        actaFechaRespuestaComite.setConceptoComite(sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                        .getActaFechaRespuestaComite().get(0)
                                        .getConceptoComite());
                        actaFechaRespuestaComite.setNumeroActa(sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                        .getActaFechaRespuestaComite().get(0)
                                        .getNumeroActa());
                        actaFechaRespuestaComite.setFechaActa(sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                        .getActaFechaRespuestaComite().get(0)
                                        .getFechaActa());

                        // Actualizar otros campos de examenValoracion
                        sustentacionTrabajoInvestigacion.setLinkEstudioHojaVidaAcademica(
                                        sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                                        .getLinkEstudioHojaVidaAcademica());
                        sustentacionTrabajoInvestigacion.setLinkFormatoG(
                                        sustentacionTrabajoInvestigacionCoordinadorFase2Dto.getLinkFormatoG());

                        respuestaComiteSustentacionRepository.save(actaFechaRespuestaComite);
                }
        }

        @Override
        @Transactional
        public STICoordinadorFase3ResponseDto actualizarInformacionCoordinadoFase3(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionDto,
                        BindingResult result) {

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                if (sustentacionDto.getIdJuradoInterno().equals("Sin cambios")
                                && sustentacionDto.getIdJuradoExterno().equals("Sin cambios")) {
                        throw new InformationException("Atributos incorrectos");
                }

                if (sustentacionDto.getFechaActaConsejo() != null
                                && sustentacionDto.getFechaActaConsejo().isAfter(LocalDate.now())) {
                        throw new InformationException(
                                        "La fecha del consejo no puede ser mayor a la fecha actual.");
                }

                if (sustentacionDto.getFechaSustentacion() != null
                                && sustentacionDto.getFechaSustentacion().isBefore(LocalDate.now())) {
                        throw new InformationException(
                                        "La fecha máxima de la sustentacion no puede ser mayor a la fecha actual.");
                }

                archivoClient.obtenerDocentePorId(Long.parseLong(sustentacionDto.getIdJuradoInterno()));
                archivoClient.obtenerExpertoPorId(Long.parseLong(sustentacionDto.getIdJuradoExterno()));

                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 28 && trabajoGrado.getNumeroEstado() != 29
                                && trabajoGrado.getNumeroEstado() != 36) {
                        throw new InformationException("No es permitido registrar la información");
                }

                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: "
                                                                + trabajoGrado.getSustentacionProyectoInvestigacion()
                                                                                .getId()
                                                                + " no encontrado"));

                if (sustentacionProyectoInvestigacionTmp.getJuradosAceptados() == null
                                && sustentacionProyectoInvestigacionTmp.getNumeroActaConsejo() == null
                                && sustentacionProyectoInvestigacionTmp.getFechaActaConsejo() == null
                                && sustentacionProyectoInvestigacionTmp.getLinkOficioConsejo() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                sustentacionProyectoInvestigacionTmp
                                .setIdJuradoInterno(Long.parseLong(sustentacionDto.getIdJuradoInterno()));
                sustentacionProyectoInvestigacionTmp
                                .setIdJuradoExterno(Long.parseLong(sustentacionDto.getIdJuradoExterno()));

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                if (!sustentacionDto.getLinkOficioConsejo()
                                .equals(sustentacionProyectoInvestigacionTmp.getLinkOficioConsejo())) {
                        validarLink(sustentacionDto.getLinkOficioConsejo());
                        sustentacionDto.setLinkOficioConsejo(FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                        sustentacionDto.getLinkOficioConsejo()));
                        FilesUtilities.deleteFileExample(sustentacionProyectoInvestigacionTmp.getLinkOficioConsejo());
                }

                sustentacionProyectoInvestigacionTmp.setJuradosAceptados(sustentacionDto.getJuradosAceptados());
                sustentacionProyectoInvestigacionTmp.setFechaSustentacion(sustentacionDto.getFechaSustentacion());
                sustentacionProyectoInvestigacionTmp.setNumeroActaConsejo(sustentacionDto.getNumeroActaConsejo());
                sustentacionProyectoInvestigacionTmp.setFechaActaConsejo(sustentacionDto.getFechaActaConsejo());
                sustentacionProyectoInvestigacionTmp.setLinkOficioConsejo(sustentacionDto.getLinkOficioConsejo());

                if (sustentacionProyectoInvestigacionTmp.getFechaSustentacion() == null
                                && sustentacionDto.getFechaSustentacion() == null) {
                        trabajoGrado.setNumeroEstado(36);
                } else {
                        trabajoGrado.setNumeroEstado(29);
                }

                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacionTmp);

                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase3Dto(sustentacionProyectoInvestigacionRes);
        }

        @Override
        @Transactional
        public SustentacionTrabajoInvestigacionEstudianteResponseDto actualizarInformacionEstudiante(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionEstudianteDto sustentacionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                validarLink(sustentacionDto.getLinkEstudioHojaVidaAcademicaGrado());
                validarLink(sustentacionDto.getLinkFormatoH());
                validarLink(sustentacionDto.getLinkFormatoI());

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 30) {
                        throw new InformationException("No es permitido registrar la información");
                }

                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion()
                                                .getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Sustentacion con id: "
                                                + trabajoGrado.getSustentacionProyectoInvestigacion()
                                                                .getId()
                                                + " no encontrado"));

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                if (!sustentacionDto.getLinkFormatoH().equals(sustentacionProyectoInvestigacionTmp.getLinkFormatoH())) {
                        validarLink(sustentacionDto.getLinkFormatoH());
                        sustentacionDto.setLinkFormatoH(FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                        sustentacionDto.getLinkFormatoH()));
                        FilesUtilities.deleteFileExample(sustentacionProyectoInvestigacionTmp.getLinkFormatoH());
                }
                if (!sustentacionDto.getLinkFormatoI().equals(sustentacionProyectoInvestigacionTmp.getLinkFormatoI())) {
                        validarLink(sustentacionDto.getLinkFormatoI());
                        sustentacionDto.setLinkFormatoI(FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                        sustentacionDto.getLinkFormatoI()));
                        FilesUtilities.deleteFileExample(sustentacionProyectoInvestigacionTmp.getLinkFormatoI());
                }
                if (!sustentacionDto.getLinkEstudioHojaVidaAcademicaGrado().equals(
                                sustentacionProyectoInvestigacionTmp.getLinkEstudioHojaVidaAcademicaGrado())) {
                        validarLink(sustentacionDto.getLinkEstudioHojaVidaAcademicaGrado());
                        sustentacionDto.setLinkEstudioHojaVidaAcademicaGrado(
                                        FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                        sustentacionDto.getLinkEstudioHojaVidaAcademicaGrado()));
                        FilesUtilities.deleteFileExample(sustentacionProyectoInvestigacionTmp
                                        .getLinkEstudioHojaVidaAcademicaGrado());
                }

                sustentacionProyectoInvestigacionTmp.setLinkFormatoH(sustentacionDto.getLinkFormatoH());
                sustentacionProyectoInvestigacionTmp.setLinkFormatoI(sustentacionDto.getLinkFormatoI());
                sustentacionProyectoInvestigacionTmp.setLinkEstudioHojaVidaAcademicaGrado(
                                sustentacionDto.getLinkEstudioHojaVidaAcademicaGrado());

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacionTmp);

                trabajoGrado.setNumeroEstado(30);

                return sustentacionProyectoInvestigacionResponseMapper
                                .toEstudianteDto(sustentacionTrabajoInvestigacion);
        }

        @Override
        @Transactional
        public STICoordinadorFase4ResponseDto actualizarInformacionCoordinadoFase4(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionDto,
                        BindingResult result) {

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                if (sustentacionDto.getRespuestaSustentacion().equals(ConceptosVarios.APROBADO)
                                && (sustentacionDto.getLinkActaSustentacionPublica() == null
                                                || sustentacionDto.getNumeroActaFinal() == null
                                                || sustentacionDto.getFechaActaFinal() == null)) {
                        throw new InformationException("Atributos incorrectos");
                }

                if ((sustentacionDto.getRespuestaSustentacion().equals(ConceptosVarios.NO_APROBADO) ||
                                sustentacionDto.getRespuestaSustentacion().equals(ConceptosVarios.APLAZADO))
                                && (sustentacionDto.getLinkActaSustentacionPublica() != null
                                                || sustentacionDto.getNumeroActaFinal() != null
                                                || sustentacionDto.getFechaActaFinal() != null)) {
                        throw new InformationException("Envio de atributos no permitido");
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 30 && trabajoGrado.getNumeroEstado() != 31
                                && trabajoGrado.getNumeroEstado() != 32 && trabajoGrado.getNumeroEstado() != 33) {
                        throw new InformationException("No es permitido registrar la información");
                }

                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion()
                                                .getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: "
                                                                + trabajoGrado.getSustentacionProyectoInvestigacion()
                                                                                .getId()
                                                                + " no encontrado"));
                String rutaArchivo = identificacionArchivo(trabajoGrado);

                if (!sustentacionProyectoInvestigacionTmp.getRespuestaSustentacion()
                                .equals(sustentacionDto.getRespuestaSustentacion())) {
                        if (sustentacionDto.getRespuestaSustentacion().equals(ConceptosVarios.APROBADO)) {
                                validarLink(sustentacionDto.getLinkActaSustentacionPublica());

                                sustentacionDto.setLinkActaSustentacionPublica(
                                                FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                                sustentacionDto.getLinkActaSustentacionPublica()));

                                trabajoGrado.setNumeroEstado(31);
                        } else if (sustentacionDto.getRespuestaSustentacion().equals(ConceptosVarios.NO_APROBADO)) {

                                if (sustentacionProyectoInvestigacionTmp.getRespuestaSustentacion()
                                                .equals(ConceptosVarios.APROBADO)) {
                                        FilesUtilities.deleteFileExample(
                                                        sustentacionProyectoInvestigacionTmp
                                                                        .getLinkActaSustentacionPublica());
                                        FilesUtilities.deleteFileExample(
                                                        sustentacionProyectoInvestigacionTmp
                                                                        .getLinkEstudioHojaVidaAcademicaGrado());
                                }
                                trabajoGrado.setNumeroEstado(32);
                        } else {
                                if (sustentacionProyectoInvestigacionTmp.getRespuestaSustentacion()
                                                .equals(ConceptosVarios.APROBADO)) {
                                        FilesUtilities.deleteFileExample(
                                                        sustentacionProyectoInvestigacionTmp
                                                                        .getLinkActaSustentacionPublica());
                                        FilesUtilities.deleteFileExample(
                                                        sustentacionProyectoInvestigacionTmp
                                                                        .getLinkEstudioHojaVidaAcademicaGrado());
                                }
                                trabajoGrado.setNumeroEstado(33);
                        }
                } else {
                        if (sustentacionDto.getLinkActaSustentacionPublica() != null
                                        && sustentacionDto.getLinkActaSustentacionPublica().equals(
                                                        sustentacionProyectoInvestigacionTmp
                                                                        .getLinkActaSustentacionPublica())) {
                                validarLink(sustentacionDto.getLinkActaSustentacionPublica());
                                sustentacionDto.setLinkActaSustentacionPublica(
                                                FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                                sustentacionDto.getLinkActaSustentacionPublica()));
                                FilesUtilities.deleteFileExample(
                                                sustentacionProyectoInvestigacionTmp.getLinkActaSustentacionPublica());
                        }
                }

                Optional<TiemposPendientes> tiemposPendientes = tiemposPendientesRepository
                                .findByTrabajoGradoId(idTrabajoGrado);

                LocalDate fechaActual = LocalDate.now();

                if (tiemposPendientes.isPresent()) {
                        tiemposPendientes.get().setFechaLimite(fechaActual.plusDays(60));
                }

                actualizarInformacionCoordinadorFase4(sustentacionProyectoInvestigacionTmp,
                                sustentacionDto, trabajoGrado);
                sustentacionProyectoInvestigacionTmp
                                .setRespuestaSustentacion(sustentacionDto.getRespuestaSustentacion());
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacionTmp);

                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase4Dto(sustentacionProyectoInvestigacionRes);
        }

        private void actualizarInformacionCoordinadorFase4(
                        SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionTmp,
                        SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionDto,
                        TrabajoGrado trabajoGrado) {

                sustentacionProyectoInvestigacionTmp
                                .setRespuestaSustentacion(sustentacionDto.getRespuestaSustentacion());
                sustentacionProyectoInvestigacionTmp
                                .setLinkActaSustentacionPublica(sustentacionDto.getLinkActaSustentacionPublica());
                sustentacionProyectoInvestigacionTmp.setNumeroActaFinal(sustentacionDto.getNumeroActaFinal());
                sustentacionProyectoInvestigacionTmp.setFechaActaFinal(sustentacionDto.getFechaActaFinal());

        }

        private void validarLink(String link) {
                ValidationUtils.validarFormatoLink(link);
                String base64 = link.substring(link.indexOf('-') + 1);
                ValidationUtils.validarBase64(base64);
        }

}
