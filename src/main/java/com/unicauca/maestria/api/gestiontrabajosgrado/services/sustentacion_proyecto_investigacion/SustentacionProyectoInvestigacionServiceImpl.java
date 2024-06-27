package com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientEgresados;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientExpertos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.EstadoTrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.Constants;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.RespuestaComiteSustentacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.SustentacionTrabajoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.egresado.CursoSaveDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.egresado.EmpresaSaveDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.STICoordinadorFase1ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.SustentacionTrabajoInvestigacionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.STICoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.SustentacionTrabajoInvestigacionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3.STICoordinadorFase3ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3.SustentacionTrabajoInvestigacionCoordinadorFase3Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_4.STICoordinadorFase4ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_4.SustentacionTrabajoInvestigacionCoordinadorFase4Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.estudiante.SustentacionTrabajoInvestigacionEstudianteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.estudiante.SustentacionTrabajoInvestigacionEstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaComiteSustentacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SustentacionProyectoInvestigacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SustentacionProyectoInvestigacionServiceImpl implements SustentacionProyectoInvestigacionService {

        private final SustentacionProyectoInvestigacionRepository sustentacionProyectoInvestigacionRepository;
        private final SustentacionProyectoInvestigacionMapper sustentacionProyectoIngestigacionMapper;
        private final SustentacionProyectoInvestigacionResponseMapper sustentacionProyectoInvestigacionResponseMapper;
        private final RespuestaComiteSustentacionRepository respuestaComiteSustentacionRepository;
        private final TrabajoGradoRepository trabajoGradoRepository;
        private final ArchivoClient archivoClient;
        private final ArchivoClientExpertos archivoClientExpertos;
        private final ArchivoClientEgresados archivoClientEgresados;

        @Autowired
        private EnvioCorreos envioCorreos;

        @Override
        @Transactional
        public SustentacionTrabajoInvestigacionDocenteResponseDto insertarInformacionDocente(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionDocenteDto sustentacionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + idTrabajoGrado
                                                                + " No encontrado"));

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                // Mapear DTO a entidad
                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoIngestigacionMapper
                                .toEntity(sustentacionDto);

                // Establecer la relación uno a uno
                sustentacionProyectoInvestigacion.setIdTrabajoGrado(trabajoGrado);
                trabajoGrado.setIdSustentacionProyectoInvestigacion(sustentacionProyectoInvestigacion);

                trabajoGrado.setNumeroEstado(12);

                // Guardar la entidad SustentacionProyectoInvestigacion
                sustentacionProyectoInvestigacion.setLinkFormatoF(
                                FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                sustentacionProyectoInvestigacion.getLinkFormatoF()));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacion);

                // Obtener y construir información del evaluador interno
                DocenteResponseDto docente = archivoClient
                                .obtenerDocentePorId(sustentacionDto.getIdJuradoInterno());
                String nombre_docente = docente.getPersona().getNombre() + " " + docente.getPersona().getApellido();
                Map<String, String> evaluadorInternoMap = new HashMap<>();
                evaluadorInternoMap.put("nombres", nombre_docente);
                evaluadorInternoMap.put("universidad", "Universidad del Cauca");
                evaluadorInternoMap.put("correo", docente.getPersona().getCorreoElectronico());

                // Obtener y construir información del evaluador externo
                ExpertoResponseDto experto = archivoClientExpertos
                                .obtenerExpertoPorId(sustentacionDto.getIdJuradoExterno());
                String nombre_experto = experto.getPersona().getNombre() + " " + experto.getPersona().getApellido();
                Map<String, String> evaluadorExternoMap = new HashMap<>();
                evaluadorExternoMap.put("nombres", nombre_experto);
                evaluadorExternoMap.put("universidad", experto.getUniversidad());
                evaluadorExternoMap.put("correo", experto.getPersona().getCorreoElectronico());

                SustentacionTrabajoInvestigacionDocenteResponseDto sustentacionResponseDto = sustentacionProyectoInvestigacionResponseMapper
                                .toDocenteDto(sustentacionProyectoInvestigacionRes);

                sustentacionResponseDto.setJuradoInterno(evaluadorInternoMap);
                sustentacionResponseDto.setJuradoExterno(evaluadorExternoMap);

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

                ArrayList<String> correos = new ArrayList<>();

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + idTrabajoGrado + " no encontrado"));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                .getIdSustentacionTrabajoInvestigacion())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: "
                                                                + trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                                                .getIdSustentacionTrabajoInvestigacion()
                                                                + " no encontrado"));

                if (sustentacionDto.getConceptoCoordinador()) {

                        correos.add(Constants.correoComite);
                        Map<String, Object> documentosEnvioComiteDto = sustentacionDto
                                        .getObtenerDocumentosParaEnvioDto()
                                        .getDocumentos();
                        envioCorreos.enviarCorreoConAnexos(correos, sustentacionDto.getEnvioEmailDto().getAsunto(),
                                        sustentacionDto.getEnvioEmailDto().getMensaje(), documentosEnvioComiteDto);

                        trabajoGrado.setNumeroEstado(21);
                } else {
                        EstudianteResponseDtoAll estudiante = archivoClient
                                        .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                        correos.add(estudiante.getPersona().getCorreoElectronico());
                        correos.add(trabajoGrado.getCorreoElectronicoTutor());
                        envioCorreos.enviarCorreosCorrecion(correos,
                                        sustentacionDto.getEnvioEmailDto().getAsunto(),
                                        sustentacionDto.getEnvioEmailDto().getMensaje());
                        trabajoGrado.setNumeroEstado(19);
                }

                sustentacionProyectoInvestigacionTmp.setConceptoCoordinador(sustentacionDto.getConceptoCoordinador());

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
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

                ArrayList<String> correos = new ArrayList<>();

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: "
                                                                + idTrabajoGrado
                                                                + " No encontrado"));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                .getIdSustentacionTrabajoInvestigacion())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: "
                                                                + trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                                                .getIdSustentacionTrabajoInvestigacion()
                                                                + " no encontrado"));

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                if (sustentacionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()) {
                        Map<String, Object> documentosParaConsejo = new HashMap<>();
                        correos.add(Constants.correoComite);
                        String[] solicitudConsejoFacultad = sustentacionDto
                                        .getLinkEstudioHojaVidaAcademica()
                                        .split("-");
                        documentosParaConsejo.put("historiaAcademica", solicitudConsejoFacultad[1]);
                        String[] formatoG = sustentacionDto
                                        .getLinkFormatoG()
                                        .split("-");
                        documentosParaConsejo.put("formatoG", formatoG[1]);
                        envioCorreos.enviarCorreoConAnexos(correos, sustentacionDto.getEnvioEmailDto().getAsunto(),
                                        sustentacionDto.getEnvioEmailDto().getMensaje(), documentosParaConsejo);

                        sustentacionDto.setLinkEstudioHojaVidaAcademica(FilesUtilities.guardarArchivoNew2(
                                        rutaArchivo,
                                        sustentacionDto.getLinkEstudioHojaVidaAcademica()));

                        sustentacionDto.setLinkFormatoG(FilesUtilities.guardarArchivoNew2(
                                        rutaArchivo,
                                        sustentacionDto.getLinkFormatoG()));

                        trabajoGrado.setNumeroEstado(21);
                } else {
                        EstudianteResponseDtoAll estudiante = archivoClient
                                        .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                        correos.add(estudiante.getPersona().getCorreoElectronico());
                        correos.add(trabajoGrado.getCorreoElectronicoTutor());
                        envioCorreos.enviarCorreosCorrecion(correos,
                                        sustentacionDto.getEnvioEmailDto().getAsunto(),
                                        sustentacionDto.getEnvioEmailDto().getMensaje());
                        trabajoGrado.setNumeroEstado(23);
                }

                agregarInformacionCoordinadorFase2(sustentacionProyectoInvestigacion, sustentacionDto);

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacion);

                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase2Dto(sustentacionProyectoInvestigacionRes);
        }

        private void agregarInformacionCoordinadorFase2(
                        SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacion,
                        SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionTrabajoInvestigacionCoordinadorFase2Dto) {

                RespuestaComiteSustentacion respuestaComite = RespuestaComiteSustentacion.builder()
                                .conceptoComite(sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                                .getActaFechaRespuestaComite().get(0)
                                                .getConceptoComite())
                                .numeroActa(sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                                .getActaFechaRespuestaComite().get(0)
                                                .getNumeroActa())
                                .fechaActa(sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                                .getActaFechaRespuestaComite().get(0).getFechaActa()
                                                .toString())
                                .sustentacionTrabajoInvestigacion(sustentacionTrabajoInvestigacion)
                                .build();

                // Si la colección está vacía, inicializarla
                if (sustentacionTrabajoInvestigacion.getActaFechaRespuestaComite() == null) {
                        sustentacionTrabajoInvestigacion.setActaFechaRespuestaComite(new ArrayList<>());
                }

                // Agregar la nueva respuesta a la lista existente
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

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: "
                                                                + idTrabajoGrado
                                                                + " No encontrado"));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                .getIdSustentacionTrabajoInvestigacion())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: "
                                                                + trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                                                .getIdSustentacionTrabajoInvestigacion()
                                                                + " no encontrado"));

                agregarInformacionCoordinadorFase3(sustentacionProyectoInvestigacionTmp, sustentacionDto, trabajoGrado);

                trabajoGrado.setNumeroEstado(23);

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacionTmp);

                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase3Dto(sustentacionProyectoInvestigacionRes);
        }

        private void agregarInformacionCoordinadorFase3(
                        SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion,
                        SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionDto,
                        TrabajoGrado trabajoGrado) {

                ArrayList<String> correos = new ArrayList<>();

                if (!sustentacionDto.getJuradosAceptados()) {
                        sustentacionProyectoInvestigacion
                                        .setIdJuradoInterno(Long.parseLong(sustentacionDto.getIdJuradoInterno()));
                        sustentacionProyectoInvestigacion
                                        .setIdJuradoExterno(Long.parseLong(sustentacionDto.getIdJuradoExterno()));

                        EstudianteResponseDtoAll estudiante = archivoClient
                                        .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                        correos.add(estudiante.getPersona().getCorreoElectronico());
                        correos.add(trabajoGrado.getCorreoElectronicoTutor());
                        envioCorreos.enviarCorreosCorrecion(correos,
                                        sustentacionDto.getEnvioEmailDto().getAsunto(),
                                        sustentacionDto.getEnvioEmailDto().getMensaje());
                }
                sustentacionProyectoInvestigacion.setJuradosAceptados(sustentacionDto.getJuradosAceptados());
                sustentacionProyectoInvestigacion.setNumeroActaConsejo(sustentacionDto.getNumeroActaConsejo());
                sustentacionProyectoInvestigacion.setFechaActaConsejo(sustentacionDto.getFechaActaConsejo());

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

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: "
                                                                + idTrabajoGrado
                                                                + " No encontrado"));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                .getIdSustentacionTrabajoInvestigacion())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: "
                                                                + trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                                                .getIdSustentacionTrabajoInvestigacion()
                                                                + " no encontrado"));

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoIngestigacionMapper
                                .toEntity(sustentacionDto);

                trabajoGrado.setNumeroEstado(16);

                sustentacionProyectoInvestigacionTmp.setLinkFormatoH(
                                FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                sustentacionProyectoInvestigacion.getLinkFormatoH()));

                sustentacionProyectoInvestigacionTmp.setLinkFormatoI(
                                FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                sustentacionProyectoInvestigacion.getLinkFormatoI()));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
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

                // if (sustentacionDto.getRespuestaSustentacion().equals("No aprobado")
                // || sustentacionDto.getRespuestaSustentacion().equals("Aplazado")) {
                // if (!sustentacionDto.validarUnSoloAtributo()) {
                // throw new InformationException("No es permitido enviar mas atributos");
                // }
                // }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: "
                                                                + idTrabajoGrado
                                                                + " No encontrado"));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                .getIdSustentacionTrabajoInvestigacion())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: "
                                                                + trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                                                .getIdSustentacionTrabajoInvestigacion()
                                                                + " no encontrado"));

                // Mapear DTO a entidad
                // SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion =
                // sustentacionProyectoIngestigacionMapper
                // .toEntity(sustentacionDto);

                trabajoGrado.setNumeroEstado(17);

                // Guardar la entidad SustentacionProyectoInvestigacion

                if (sustentacionDto.getRespuestaSustentacion().equals("Aprobado")) {
                        trabajoGrado.setNumeroEstado(25);
                } else if (sustentacionDto.getRespuestaSustentacion().equals("No aprobado")) {
                        // Consultar aqui que se hace
                        trabajoGrado.setNumeroEstado(26);
                } else {
                        // aqui hacer lo de agregar a la tabla de tiempo
                        trabajoGrado.setNumeroEstado(27);
                }

                agregarInformacionCoordinadorFase4(sustentacionProyectoInvestigacionTmp, sustentacionDto, trabajoGrado);

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacionTmp);

                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase4Dto(sustentacionProyectoInvestigacionRes);
        }

        private void agregarInformacionCoordinadorFase4(
                        SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion,
                        SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionDto,
                        TrabajoGrado trabajoGrado) {

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                if (sustentacionDto.getRespuestaSustentacion().equals("Aprobado")) {
                        sustentacionProyectoInvestigacion.setLinkActaSustentacionPublica(
                                        FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                        sustentacionDto
                                                                        .getLinkActaSustentacionPublica()));

                        sustentacionProyectoInvestigacion.setLinkEstudioHojaVidaAcademicaGrado(
                                        FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                        sustentacionDto
                                                                        .getLinkEstudioHojaVidaAcademicaGrado()));
                } else {
                        sustentacionProyectoInvestigacion
                                        .setLinkActaSustentacionPublica(
                                                        sustentacionDto.getLinkActaSustentacionPublica());
                        sustentacionProyectoInvestigacion.setLinkEstudioHojaVidaAcademicaGrado(
                                        sustentacionDto.getLinkEstudioHojaVidaAcademicaGrado());
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
        public SustentacionTrabajoInvestigacionDocenteResponseDto listarInformacionDocente(Long idTrabajoGrado) {
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + idTrabajoGrado
                                                                + " No encontrado"));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                .getIdSustentacionTrabajoInvestigacion())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: "
                                                                + trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                                                .getIdSustentacionTrabajoInvestigacion()
                                                                + " no encontrado"));

                // Obtener y construir información del evaluador interno
                DocenteResponseDto docente = archivoClient
                                .obtenerDocentePorId(sustentacionProyectoInvestigacionTmp.getIdJuradoInterno());
                String nombre_docente = docente.getPersona().getNombre() + " " + docente.getPersona().getApellido();
                Map<String, String> evaluadorInternoMap = new HashMap<>();
                evaluadorInternoMap.put("nombres", nombre_docente);
                evaluadorInternoMap.put("universidad", "Universidad del Cauca");
                evaluadorInternoMap.put("correo", docente.getPersona().getCorreoElectronico());

                // Obtener y construir información del evaluador externo
                ExpertoResponseDto experto = archivoClientExpertos
                                .obtenerExpertoPorId(sustentacionProyectoInvestigacionTmp.getIdJuradoExterno());
                String nombre_experto = experto.getPersona().getNombre() + " " + experto.getPersona().getApellido();
                Map<String, String> evaluadorExternoMap = new HashMap<>();
                evaluadorExternoMap.put("nombres", nombre_experto);
                evaluadorExternoMap.put("universidad", experto.getUniversidad());
                evaluadorExternoMap.put("correo", experto.getPersona().getCorreoElectronico());

                SustentacionTrabajoInvestigacionDocenteResponseDto sustentacionTrabajoInvestigacionDocenteResponseDto = new SustentacionTrabajoInvestigacionDocenteResponseDto();
                sustentacionTrabajoInvestigacionDocenteResponseDto.setIdSustentacionTrabajoInvestigacion(
                                sustentacionProyectoInvestigacionTmp.getIdSustentacionTrabajoInvestigacion());
                sustentacionTrabajoInvestigacionDocenteResponseDto
                                .setLinkFormatoF(sustentacionProyectoInvestigacionTmp.getLinkFormatoF());
                sustentacionTrabajoInvestigacionDocenteResponseDto
                                .setUrlDocumentacion(sustentacionProyectoInvestigacionTmp.getUrlDocumentacion());
                sustentacionTrabajoInvestigacionDocenteResponseDto.setJuradoInterno(evaluadorInternoMap);
                sustentacionTrabajoInvestigacionDocenteResponseDto.setJuradoExterno(evaluadorExternoMap);

                return sustentacionTrabajoInvestigacionDocenteResponseDto;
        }

        @Override
        @Transactional(readOnly = true)
        public STICoordinadorFase1ResponseDto listarInformacionCoordinadorFase1(
                        Long idTrabajoGrado) {
                return sustentacionProyectoInvestigacionRepository.findByIdTrabajoGradoId(idTrabajoGrado)
                                .stream()
                                .map(sustentacionProyectoInvestigacionResponseMapper::toCoordinadorFase1Dto)
                                .findFirst()
                                .orElse(null);
        }

        @Override
        @Transactional(readOnly = true)
        public STICoordinadorFase2ResponseDto listarInformacionCoordinadorFase2(
                        Long idTrabajoGrado) {

                Optional<SustentacionTrabajoInvestigacion> entityOptional = sustentacionProyectoInvestigacionRepository
                                .findByIdTrabajoGradoId(idTrabajoGrado);

                // SolicitudExamenValoracion entity = entityOptional.get();
                STICoordinadorFase2ResponseDto responseDto = sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase2Dto(entityOptional.get());

                // Obtener y construir información del evaluador interno
                DocenteResponseDto docente = archivoClient
                                .obtenerDocentePorId(entityOptional.get().getIdJuradoInterno());
                String nombre_docente = docente.getPersona().getNombre() + " " + docente.getPersona().getApellido();
                Map<String, String> evaluadorInternoMap = new HashMap<>();
                evaluadorInternoMap.put("nombres", nombre_docente);
                evaluadorInternoMap.put("universidad", "Universidad del Cauca");
                evaluadorInternoMap.put("correo", docente.getPersona().getCorreoElectronico());

                // Obtener y construir información del evaluador externo
                ExpertoResponseDto experto = archivoClientExpertos
                                .obtenerExpertoPorId(entityOptional.get().getIdJuradoExterno());
                String nombre_experto = experto.getPersona().getNombre() + " " + experto.getPersona().getApellido();
                Map<String, String> evaluadorExternoMap = new HashMap<>();
                evaluadorExternoMap.put("nombres", nombre_experto);
                evaluadorExternoMap.put("universidad", experto.getUniversidad());
                evaluadorExternoMap.put("correo", experto.getPersona().getCorreoElectronico());

                // responseDto.setJuradoInterno(evaluadorExternoMap);
                // responseDto.setJuradoExterno(evaluadorExternoMap);

                return responseDto;
        }

        @Override
        @Transactional(readOnly = true)
        public STICoordinadorFase3ResponseDto listarInformacionCoordinadorFase3(
                        Long idTrabajoGrado) {
                return sustentacionProyectoInvestigacionRepository.findByIdTrabajoGradoId(idTrabajoGrado)
                                .stream()
                                .map(sustentacionProyectoInvestigacionResponseMapper::toCoordinadorFase3Dto)
                                .findFirst()
                                .orElse(null);
        }

        @Override
        @Transactional(readOnly = true)
        public SustentacionTrabajoInvestigacionEstudianteResponseDto listarInformacionEstudiante(Long idTrabajoGrado) {
                return sustentacionProyectoInvestigacionRepository.findByIdTrabajoGradoId(idTrabajoGrado)
                                .stream()
                                .map(sustentacionProyectoInvestigacionResponseMapper::toEstudianteDto)
                                .findFirst()
                                .orElse(null);
        }

        @Override
        @Transactional(readOnly = true)
        public STICoordinadorFase4ResponseDto listarInformacionCoordinadorFase4(
                        Long idTrabajoGrado) {
                return sustentacionProyectoInvestigacionRepository.findByIdTrabajoGradoId(idTrabajoGrado)
                                .stream()
                                .map(sustentacionProyectoInvestigacionResponseMapper::toCoordinadorFase4Dto)
                                .findFirst()
                                .orElse(null);
        }

        @Override
        @Transactional(readOnly = true)
        public String descargarArchivo(RutaArchivoDto rutaArchivo) {
                return FilesUtilities.recuperarArchivo(rutaArchivo.getRutaArchivo());
        }

        @Override
        @Transactional(readOnly = true)
        public Boolean verificarEgresado(Long idTrabajoGrado) {
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + idTrabajoGrado + " no encontrado"));

                List<CursoSaveDto> cursos = archivoClientEgresados
                                .obtenerCursosPorIdEstudiante(trabajoGrado.getIdEstudiante());
                List<EmpresaSaveDto> empresas = archivoClientEgresados
                                .obtenerEmpresasPorIdEstudiante(trabajoGrado.getIdEstudiante());

                if (cursos.size() == 0 || empresas.size() == 0) {
                        return false;
                }

                return true;
        }

        @Override
        @Transactional(readOnly = true)
        public List<TrabajoGradoResponseDto> listarEstadosExamenValoracion(Integer numeroEstado) {

                List<TrabajoGrado> listaTrabajoGrado = trabajoGradoRepository.findByNumeroEstado(numeroEstado);
                List<TrabajoGradoResponseDto> trabajosGradoDto = listaTrabajoGrado.stream().map(trabajo -> {
                        EstadoTrabajoGrado estadoEnum = EstadoTrabajoGrado.values()[trabajo.getNumeroEstado()];
                        return TrabajoGradoResponseDto.builder()
                                        .id(trabajo.getId())
                                        .estado(estadoEnum.getMensaje())
                                        .fechaCreacion(trabajo.getFechaCreacion())
                                        .titulo(trabajo.getTitulo() != null ? trabajo.getTitulo()
                                                        : "Título no disponible")
                                        .numeroEstado(trabajo.getNumeroEstado())
                                        .build();
                }).collect(Collectors.toList());
                return trabajosGradoDto;
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
                                                "TrabajoGrado con id: " + idTrabajoGrado + " no encontrado"));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                .getIdSustentacionTrabajoInvestigacion())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: "
                                                                + trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                                                .getIdSustentacionTrabajoInvestigacion()
                                                                + " no encontrado"));

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacion = null;
                if (sustentacionProyectoInvestigacionTmp != null) {
                        if (!sustentacionDto.getLinkFormatoF()
                                        .equals(sustentacionProyectoInvestigacionTmp.getLinkFormatoF())) {
                                sustentacionDto.setLinkFormatoF(
                                                FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                                sustentacionDto.getLinkFormatoF()));
                                FilesUtilities.deleteFileExample(
                                                sustentacionProyectoInvestigacionTmp.getLinkFormatoF());
                        }

                        updateExamenValoracionDocenteValues(sustentacionProyectoInvestigacionTmp, sustentacionDto,
                                        trabajoGrado);
                        sustentacionTrabajoInvestigacion = sustentacionProyectoInvestigacionRepository
                                        .save(sustentacionProyectoInvestigacionTmp);
                }

                // Obtener y construir información del evaluador interno
                DocenteResponseDto docente = archivoClient
                                .obtenerDocentePorId(sustentacionDto.getIdJuradoInterno());
                String nombre_docente = docente.getPersona().getNombre() + " " + docente.getPersona().getApellido();
                Map<String, String> evaluadorInternoMap = new HashMap<>();
                evaluadorInternoMap.put("nombres", nombre_docente);
                evaluadorInternoMap.put("universidad", "Universidad del Cauca");
                evaluadorInternoMap.put("correo", docente.getPersona().getCorreoElectronico());

                // Obtener y construir información del evaluador externo
                ExpertoResponseDto experto = archivoClientExpertos
                                .obtenerExpertoPorId(sustentacionDto.getIdJuradoExterno());
                String nombre_experto = experto.getPersona().getNombre() + " " + experto.getPersona().getApellido();
                Map<String, String> evaluadorExternoMap = new HashMap<>();
                evaluadorExternoMap.put("nombres", nombre_experto);
                evaluadorExternoMap.put("universidad", experto.getUniversidad());
                evaluadorExternoMap.put("correo", experto.getPersona().getCorreoElectronico());

                SustentacionTrabajoInvestigacionDocenteResponseDto sustentacionResponseDto = sustentacionProyectoInvestigacionResponseMapper
                                .toDocenteDto(sustentacionTrabajoInvestigacion);

                sustentacionResponseDto.setJuradoInterno(evaluadorInternoMap);
                sustentacionResponseDto.setJuradoExterno(evaluadorExternoMap);

                return sustentacionResponseDto;
        }

        private void updateExamenValoracionDocenteValues(
                        SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacion,
                        SustentacionTrabajoInvestigacionDocenteDto sustentacionTrabajoInvestigacionDocenteDto,
                        TrabajoGrado trabajoGrado) {

                sustentacionTrabajoInvestigacion
                                .setLinkFormatoF(sustentacionTrabajoInvestigacionDocenteDto.getLinkFormatoF());
                sustentacionTrabajoInvestigacion
                                .setIdJuradoInterno(sustentacionTrabajoInvestigacionDocenteDto.getIdJuradoInterno());
                sustentacionTrabajoInvestigacion
                                .setIdJuradoExterno(sustentacionTrabajoInvestigacionDocenteDto.getIdJuradoExterno());
                // Update archivos
                sustentacionTrabajoInvestigacion
                                .setLinkFormatoF(sustentacionTrabajoInvestigacionDocenteDto.getLinkFormatoF());
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

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + idTrabajoGrado + " no encontrado"));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                .getIdSustentacionTrabajoInvestigacion())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: "
                                                                + trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                                                .getIdSustentacionTrabajoInvestigacion()
                                                                + " no encontrado"));

                ArrayList<String> correos = new ArrayList<>();

                if (sustentacionDto.getConceptoCoordinador() != sustentacionProyectoInvestigacionTmp
                                .getConceptoCoordinador()) {
                        // Si pasa de aprobado a no aprobado
                        if (!sustentacionDto.getConceptoCoordinador()) {
                                EstudianteResponseDtoAll estudiante = archivoClient
                                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                                correos.add(estudiante.getPersona().getCorreoElectronico());
                                correos.add(trabajoGrado.getCorreoElectronicoTutor());
                                envioCorreos.enviarCorreosCorrecion(correos,
                                                sustentacionDto.getEnvioEmailDto().getAsunto(),
                                                sustentacionDto.getEnvioEmailDto().getMensaje());
                                trabajoGrado.setNumeroEstado(22);
                        } else {
                                correos.add(Constants.correoComite);
                                Map<String, Object> documentosEnvioComiteDto = sustentacionDto
                                                .getObtenerDocumentosParaEnvioDto()
                                                .getDocumentos();
                                envioCorreos.enviarCorreoConAnexos(correos,
                                                sustentacionDto.getEnvioEmailDto().getAsunto(),
                                                sustentacionDto.getEnvioEmailDto().getMensaje(),
                                                documentosEnvioComiteDto);
                                trabajoGrado.setNumeroEstado(21);
                        }
                }

                sustentacionProyectoInvestigacionTmp.setConceptoCoordinador(sustentacionDto.getConceptoCoordinador());
                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
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

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + idTrabajoGrado + " no encontrado"));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionOld = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                .getIdSustentacionTrabajoInvestigacion())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: "
                                                                + trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                                                .getIdSustentacionTrabajoInvestigacion()
                                                                + " no encontrado"));

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacion = null;
                List<RespuestaComiteSustentacion> respuestaComiteList = sustentacionProyectoInvestigacionRepository
                                .findRespuestaComiteBySustentacionId(
                                                sustentacionProyectoInvestigacionOld
                                                                .getIdSustentacionTrabajoInvestigacion());
                RespuestaComiteSustentacion ultimoRegistro = respuestaComiteList.isEmpty() ? null
                                : respuestaComiteList.get(0);

                if (ultimoRegistro != null
                                && ultimoRegistro.getConceptoComite() != sustentacionDto
                                                .getActaFechaRespuestaComite().get(0)
                                                .getConceptoComite()) {
                        ArrayList<String> correos = new ArrayList<>();
                        // Si pasa de aprobado a no aprobado
                        if (!sustentacionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()) {
                                EstudianteResponseDtoAll estudiante = archivoClient
                                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                                correos.add(estudiante.getPersona().getCorreoElectronico());
                                correos.add(trabajoGrado.getCorreoElectronicoTutor());
                                envioCorreos.enviarCorreosCorrecion(correos,
                                                sustentacionDto.getEnvioEmailDto().getAsunto(),
                                                sustentacionDto.getEnvioEmailDto().getMensaje());
                                FilesUtilities.deleteFileExample(
                                                sustentacionProyectoInvestigacionOld
                                                                .getLinkEstudioHojaVidaAcademica());
                                FilesUtilities.deleteFileExample(
                                                sustentacionProyectoInvestigacionOld
                                                                .getLinkFormatoG());

                                trabajoGrado.setNumeroEstado(23);
                        } else {
                                correos.add(Constants.correoComite);
                                Map<String, Object> documentosParaConsejo = new HashMap<>();
                                correos.add(Constants.correoComite);
                                String[] solicitudConsejoFacultad = sustentacionDto
                                                .getLinkEstudioHojaVidaAcademica()
                                                .split("-");
                                documentosParaConsejo.put("historiaAcademica", solicitudConsejoFacultad[1]);
                                String[] formatoG = sustentacionDto
                                                .getLinkFormatoG()
                                                .split("-");
                                documentosParaConsejo.put("formatoG", formatoG[1]);
                                envioCorreos.enviarCorreoConAnexos(correos,
                                                sustentacionDto.getEnvioEmailDto().getAsunto(),
                                                sustentacionDto.getEnvioEmailDto().getMensaje(),
                                                documentosParaConsejo);

                                sustentacionDto.setLinkEstudioHojaVidaAcademica(FilesUtilities.guardarArchivoNew2(
                                                rutaArchivo,
                                                sustentacionDto.getLinkEstudioHojaVidaAcademica()));

                                sustentacionDto.setLinkFormatoG(FilesUtilities.guardarArchivoNew2(
                                                rutaArchivo,
                                                sustentacionDto.getLinkFormatoG()));

                                trabajoGrado.setNumeroEstado(21);
                        }
                } else {
                        if (sustentacionProyectoInvestigacionOld != null) {
                                if (sustentacionDto.getLinkEstudioHojaVidaAcademica()
                                                .compareTo(sustentacionProyectoInvestigacionOld
                                                                .getLinkEstudioHojaVidaAcademica()) != 0) {
                                        sustentacionDto.setLinkEstudioHojaVidaAcademica(FilesUtilities
                                                        .guardarArchivoNew2(rutaArchivo, sustentacionDto
                                                                        .getLinkEstudioHojaVidaAcademica()));
                                        FilesUtilities.deleteFileExample(
                                                        sustentacionProyectoInvestigacionOld
                                                                        .getLinkEstudioHojaVidaAcademica());
                                }
                                if (sustentacionDto.getLinkFormatoG()
                                                .compareTo(sustentacionProyectoInvestigacionOld
                                                                .getLinkFormatoG()) != 0) {
                                        sustentacionDto.setLinkFormatoG(FilesUtilities
                                                        .guardarArchivoNew2(rutaArchivo, sustentacionDto
                                                                        .getLinkFormatoG()));
                                        FilesUtilities.deleteFileExample(
                                                        sustentacionProyectoInvestigacionOld
                                                                        .getLinkFormatoG());
                                }
                        }
                }
                updateExamenValoracionCoordinadorFase2Values(sustentacionProyectoInvestigacionOld, sustentacionDto,
                                trabajoGrado);
                sustentacionTrabajoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacionOld);
                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase2Dto(sustentacionTrabajoInvestigacion);
        }

        private void updateExamenValoracionCoordinadorFase2Values(
                        SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacion,
                        SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionTrabajoInvestigacionCoordinadorFase2Dto,
                        TrabajoGrado trabajoGrado) {

                List<RespuestaComiteSustentacion> respuestaComiteList = sustentacionProyectoInvestigacionRepository
                                .findRespuestaComiteBySustentacionId(
                                                sustentacionTrabajoInvestigacion
                                                                .getIdSustentacionTrabajoInvestigacion());
                RespuestaComiteSustentacion ultimoRegistro = respuestaComiteList.isEmpty() ? null
                                : respuestaComiteList.get(0);

                if (ultimoRegistro != null) {
                        // Actualizar los valores de ultimoRegistro
                        ultimoRegistro.setNumeroActa(
                                        sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                                        .getActaFechaRespuestaComite().get(0).getNumeroActa());
                        ultimoRegistro.setFechaActa(
                                        sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                                        .getActaFechaRespuestaComite().get(0).getFechaActa());

                        // Actualizar la lista actaFechaRespuestaComite de examenValoracion
                        RespuestaComiteSustentacion actaFechaRespuestaComite = respuestaComiteSustentacionRepository
                                        .findFirstByOrderByIdRespuestaComiteSustentacionDesc();

                        actaFechaRespuestaComite
                                        .setConceptoComite(sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                                        .getActaFechaRespuestaComite().get(0)
                                                        .getConceptoComite());
                        actaFechaRespuestaComite
                                        .setNumeroActa(sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                                        .getActaFechaRespuestaComite().get(0)
                                                        .getNumeroActa());
                        actaFechaRespuestaComite
                                        .setFechaActa(sustentacionTrabajoInvestigacionCoordinadorFase2Dto
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

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + idTrabajoGrado + " no encontrado"));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                .getIdSustentacionTrabajoInvestigacion())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: "
                                                                + trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                                                .getIdSustentacionTrabajoInvestigacion()
                                                                + " no encontrado"));

                ArrayList<String> correos = new ArrayList<>();

                if (sustentacionProyectoInvestigacionTmp.getJuradosAceptados() != sustentacionDto
                                .getJuradosAceptados()) {
                        // Si pasa de acepados a no aceptados
                        if (!sustentacionDto.getJuradosAceptados()) {
                                sustentacionProyectoInvestigacionTmp
                                                .setIdJuradoInterno(
                                                                Long.parseLong(sustentacionDto.getIdJuradoInterno()));
                                sustentacionProyectoInvestigacionTmp
                                                .setIdJuradoExterno(
                                                                Long.parseLong(sustentacionDto.getIdJuradoExterno()));

                                EstudianteResponseDtoAll estudiante = archivoClient
                                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                                correos.add(estudiante.getPersona().getCorreoElectronico());
                                correos.add(trabajoGrado.getCorreoElectronicoTutor());
                                envioCorreos.enviarCorreosCorrecion(correos,
                                                sustentacionDto.getEnvioEmailDto().getAsunto(),
                                                sustentacionDto.getEnvioEmailDto().getMensaje());
                        }
                }

                sustentacionProyectoInvestigacionTmp.setJuradosAceptados(sustentacionDto.getJuradosAceptados());
                sustentacionProyectoInvestigacionTmp.setNumeroActaConsejo(sustentacionDto.getNumeroActaConsejo());
                sustentacionProyectoInvestigacionTmp.setFechaActaConsejo(sustentacionDto.getFechaActaConsejo());

                trabajoGrado.setNumeroEstado(23);

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
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

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + idTrabajoGrado + " no encontrado"));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                .getIdSustentacionTrabajoInvestigacion())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: "
                                                                + trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                                                .getIdSustentacionTrabajoInvestigacion()
                                                                + " no encontrado"));

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacion = null;

                if (!sustentacionDto.getLinkFormatoH()
                                .equals(sustentacionProyectoInvestigacionTmp.getLinkFormatoH())) {
                        sustentacionDto.setLinkFormatoH(
                                        FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                        sustentacionDto.getLinkFormatoH()));
                        FilesUtilities.deleteFileExample(
                                        sustentacionProyectoInvestigacionTmp.getLinkFormatoH());
                }
                if (!sustentacionDto.getLinkFormatoI()
                                .equals(sustentacionProyectoInvestigacionTmp.getLinkFormatoI())) {
                        sustentacionDto.setLinkFormatoI(
                                        FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                        sustentacionDto.getLinkFormatoI()));
                        FilesUtilities.deleteFileExample(
                                        sustentacionProyectoInvestigacionTmp.getLinkFormatoI());
                }

                // sustentacionProyectoInvestigacionTmp
                // .setLinkFormatoH(sustentacionDto.getLinkFormatoH());
                // sustentacionProyectoInvestigacionTmp
                // .setLinkFormatoI(sustentacionDto.getLinkFormatoI());

                sustentacionTrabajoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacionTmp);

                trabajoGrado.setNumeroEstado(23);

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

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + idTrabajoGrado + " no encontrado"));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                .getIdSustentacionTrabajoInvestigacion())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: "
                                                                + trabajoGrado.getIdSustentacionProyectoInvestigacion()
                                                                                .getIdSustentacionTrabajoInvestigacion()
                                                                + " no encontrado"));
                String rutaArchivo = identificacionArchivo(trabajoGrado);

                if (!sustentacionProyectoInvestigacionTmp.getRespuestaSustentacion()
                                .equals(sustentacionDto.getRespuestaSustentacion())) {
                        if (sustentacionDto.equals("Aprobado")) {
                                // sustentacionProyectoInvestigacionTmp.setLinkActaSustentacionPublica(
                                // FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                // sustentacionDto
                                // .getLinkActaSustentacionPublica()));

                                // sustentacionProyectoInvestigacionTmp.setLinkEstudioHojaVidaAcademicaGrado(
                                // FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                // sustentacionDto
                                // .getLinkEstudioHojaVidaAcademicaGrado()));

                                trabajoGrado.setNumeroEstado(25);
                        } else if (sustentacionDto.equals("No aprobado")) {
                                // Consultar aqui que se hace
                                if (sustentacionProyectoInvestigacionTmp.getRespuestaSustentacion()
                                                .equals("Aprobado")) {
                                        FilesUtilities.deleteFileExample(
                                                        sustentacionProyectoInvestigacionTmp
                                                                        .getLinkActaSustentacionPublica());
                                        FilesUtilities.deleteFileExample(
                                                        sustentacionProyectoInvestigacionTmp
                                                                        .getLinkEstudioHojaVidaAcademicaGrado());
                                }
                                trabajoGrado.setNumeroEstado(26);
                        } else {
                                if (sustentacionProyectoInvestigacionTmp.getRespuestaSustentacion()
                                                .equals("Aprobado")) {
                                        FilesUtilities.deleteFileExample(
                                                        sustentacionProyectoInvestigacionTmp
                                                                        .getLinkActaSustentacionPublica());
                                        FilesUtilities.deleteFileExample(
                                                        sustentacionProyectoInvestigacionTmp
                                                                        .getLinkEstudioHojaVidaAcademicaGrado());
                                }
                                // aqui hacer lo de agregar a la tabla de tiempo
                                trabajoGrado.setNumeroEstado(27);
                        }
                } else {
                        if (!sustentacionDto.getLinkActaSustentacionPublica().equals(
                                        sustentacionProyectoInvestigacionTmp.getLinkActaSustentacionPublica())) {
                                sustentacionDto
                                                .setLinkActaSustentacionPublica(
                                                                FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                                                sustentacionProyectoInvestigacionTmp
                                                                                                .getLinkActaSustentacionPublica()));
                                FilesUtilities.deleteFileExample(
                                                sustentacionProyectoInvestigacionTmp.getLinkActaSustentacionPublica());
                        }
                        if (!sustentacionDto.getLinkEstudioHojaVidaAcademicaGrado().equals(
                                        sustentacionProyectoInvestigacionTmp.getLinkEstudioHojaVidaAcademicaGrado())) {
                                sustentacionDto
                                                .setLinkEstudioHojaVidaAcademicaGrado(
                                                                FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                                                sustentacionProyectoInvestigacionTmp
                                                                                                .getLinkEstudioHojaVidaAcademicaGrado()));
                                FilesUtilities.deleteFileExample(sustentacionProyectoInvestigacionTmp
                                                .getLinkEstudioHojaVidaAcademicaGrado());
                        }
                }

                agregarInformacionCoordinadorFase4(sustentacionProyectoInvestigacionTmp,
                                sustentacionDto, trabajoGrado);
                sustentacionProyectoInvestigacionTmp
                                .setRespuestaSustentacion(sustentacionDto.getRespuestaSustentacion());
                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacionTmp);

                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase4Dto(sustentacionProyectoInvestigacionRes);
        }

}
