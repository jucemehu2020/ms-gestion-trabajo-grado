package com.unicauca.maestria.api.gestiontrabajosgrado.services.generacion_resolucion;

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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientExpertos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.EstadoTrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.Constants;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.RespuestaComiteGeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.DirectorAndCodirectorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.ObtenerDocumentosParaEnvioDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_3.GeneracionResolucionCoordinadorFase3Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_3.GeneracionResolucionCoordinadorFase3ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.GeneracionResolucionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.GeneracionResolucionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.GeneracionResolucionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaComiteGeneracionResolucionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeneracionResolucionServiceImpl implements GeneracionResolucionService {

        private final GeneracionResolucionRepository generacionResolucionRepository;
        private final GeneracionResolucionMapper generacionResolucionMapper;
        private final GeneracionResolucionResponseMapper generacionResolucionResponseMapper;
        private final TrabajoGradoRepository trabajoGradoRepository;
        private final RespuestaComiteGeneracionResolucionRepository respuestaComiteGeneracionResolucionRepository;
        private final ArchivoClient archivoClient;
        private final ArchivoClientExpertos archivoClientExpertos;

        @Autowired
        private EnvioCorreos envioCorreos;

        @Override
        @Transactional(readOnly = true)
        public List<DirectorAndCodirectorResponseDto> listarDirectorAndCodirector() {
                List<DirectorAndCodirectorResponseDto> docentesYExpertos = new ArrayList<>();

                List<DocenteResponseDto> listadoDocentes = archivoClient.listarDocentesRes();
                List<ExpertoResponseDto> listadoExpertos = archivoClientExpertos.listar();
                List<DirectorAndCodirectorResponseDto> docentes = listadoDocentes.stream()
                                .map(docente -> new DirectorAndCodirectorResponseDto(
                                                docente.getPersona().getTipoIdentificacion(),
                                                docente.getPersona().getIdentificacion(),
                                                docente.getPersona().getNombre(),
                                                docente.getPersona().getApellido()))
                                .collect(Collectors.toList());
                List<DirectorAndCodirectorResponseDto> expertos = listadoExpertos.stream()
                                .map(experto -> new DirectorAndCodirectorResponseDto(
                                                experto.getPersona().getTipoIdentificacion(),
                                                experto.getPersona().getIdentificacion(),
                                                experto.getPersona().getNombre(),
                                                experto.getPersona().getApellido()))
                                .collect(Collectors.toList());

                docentesYExpertos.addAll(docentes);
                docentesYExpertos.addAll(expertos);

                return docentesYExpertos;
        }

        @Override
        @Transactional
        public GeneracionResolucionDocenteResponseDto insertarInformacionDocente(Long idTrabajoGrado,
                        GeneracionResolucionDocenteDto generacionResolucionDto,
                        BindingResult result) {

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + idTrabajoGrado
                                                                + " No encontrado"));

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                // Mapear DTO a entidad
                GeneracionResolucion generarResolucion = generacionResolucionMapper.toEntity(generacionResolucionDto);

                // Establecer la relación uno a uno
                generarResolucion.setIdTrabajoGrado(trabajoGrado);
                trabajoGrado.setIdGeneracionResolucion(generarResolucion);

                // Se cambia el numero de estado
                trabajoGrado.setNumeroEstado(13);

                // Guardar la entidad ExamenValoracion
                generarResolucion.setLinkAnteproyectoFinal(FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                generarResolucion.getLinkAnteproyectoFinal()));
                generarResolucion.setLinkSolicitudComite(FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                generarResolucion.getLinkSolicitudComite()));

                GeneracionResolucion generarResolucionRes = generacionResolucionRepository.save(generarResolucion);

                GeneracionResolucionDocenteResponseDto generacionResolucionDocenteResponseDto = generacionResolucionResponseMapper
                                .toDocenteDto(generarResolucionRes);
                generacionResolucionDocenteResponseDto.setTitulo(trabajoGrado.getTitulo());

                return generacionResolucionDocenteResponseDto;
        }

        @Override
        @Transactional
        public GeneracionResolucionCoordinadorFase1ResponseDto insertarInformacionCoordinadorFase1(
                        Long idTrabajoGrado,
                        GeneracionResolucionCoordinadorFase1Dto generacionResolucionDto,
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

                GeneracionResolucion generacionResolucionTmp = generacionResolucionRepository
                                .findById(trabajoGrado.getIdGeneracionResolucion().getIdGeneracionResolucion())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Generacion de resolucion con id: "
                                                                + trabajoGrado.getIdGeneracionResolucion()
                                                                                .getIdGeneracionResolucion()
                                                                + " no encontrado"));

                if (generacionResolucionDto.getConceptoDocumentosCoordinador()) {
                        correos.add(Constants.correoComite);
                        Map<String, Object> documentosEnvioComiteDto = generacionResolucionDto
                                        .getObtenerDocumentosParaEnvioDto()
                                        .getDocumentos();
                        envioCorreos.enviarCorreoConAnexos(correos,
                                        generacionResolucionDto.getEnvioEmailDto().getAsunto(),
                                        generacionResolucionDto.getEnvioEmailDto().getMensaje(),
                                        documentosEnvioComiteDto);
                        trabajoGrado.setNumeroEstado(15);

                } else {
                        EstudianteResponseDtoAll estudiante = archivoClient
                                        .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                        correos.add(estudiante.getPersona().getCorreoElectronico());
                        correos.add(trabajoGrado.getCorreoElectronicoTutor());
                        envioCorreos.enviarCorreosCorrecion(correos,
                                        generacionResolucionDto.getEnvioEmailDto().getAsunto(),
                                        generacionResolucionDto.getEnvioEmailDto().getMensaje());
                        trabajoGrado.setNumeroEstado(14);
                }

                generacionResolucionTmp.setConceptoDocumentosCoordinador(
                                generacionResolucionDto.getConceptoDocumentosCoordinador());

                GeneracionResolucion generarResolucionRes = generacionResolucionRepository
                                .save(generacionResolucionTmp);

                return generacionResolucionResponseMapper.toCoordinadorFase1Dto(generarResolucionRes);
        }

        @Override
        @Transactional(readOnly = true)
        public ObtenerDocumentosParaEnvioDto obtenerDocumentosParaEnviarAlComite(Long idTrabajoGrado) {

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: "
                                                                + idTrabajoGrado
                                                                + " No encontrado"));

                GeneracionResolucion generacionResolucion = generacionResolucionRepository
                                .findByTrabajoGradoId(
                                                trabajoGrado.getIdGeneracionResolucion().getIdGeneracionResolucion());

                ObtenerDocumentosParaEnvioDto obtenerDocumentosParaEnviar = new ObtenerDocumentosParaEnvioDto(
                                FilesUtilities.recuperarArchivo(generacionResolucion.getLinkAnteproyectoFinal()),
                                FilesUtilities.recuperarArchivo(generacionResolucion.getLinkSolicitudComite()));

                return obtenerDocumentosParaEnviar;
        }

        @Override
        @Transactional
        public GeneracionResolucionCoordinadorFase2ResponseDto insertarInformacionCoordinadorFase2(
                        Long idTrabajoGrado,
                        GeneracionResolucionCoordinadorFase2Dto generacionResolucionDto,
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

                GeneracionResolucion generacionResolucionTmp = generacionResolucionRepository
                                .findById(trabajoGrado.getIdGeneracionResolucion().getIdGeneracionResolucion())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Generacion de resolucion con id: "
                                                                + trabajoGrado.getIdGeneracionResolucion()
                                                                                .getIdGeneracionResolucion()
                                                                + " no encontrado"));

                // Mapear DTO a entidad
                GeneracionResolucion generarResolucion = generacionResolucionMapper.toEntity(generacionResolucionDto);

                if (generacionResolucionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()) {
                        correos.add(Constants.correoConsejo);
                        Map<String, Object> documentosParaConsejo = new HashMap<>();
                        String[] solicitudConsejoFacultad = generacionResolucionDto
                                        .getLinkSolicitudConsejoFacultad()
                                        .split("-");
                        documentosParaConsejo.put("Solicitud_Consejo_Facultad", solicitudConsejoFacultad[1]);
                        envioCorreos.enviarCorreoConAnexos(correos,
                                        generacionResolucionDto.getEnvioEmail().getAsunto(),
                                        generacionResolucionDto.getEnvioEmail().getMensaje(),
                                        documentosParaConsejo);
                        String rutaArchivo = identificacionArchivo(trabajoGrado);
                        generarResolucion.setLinkSolicitudConsejoFacultad(
                                        FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                        generarResolucion.getLinkSolicitudConsejoFacultad()));
                        trabajoGrado.setNumeroEstado(17);
                } else {
                        EstudianteResponseDtoAll estudiante = archivoClient
                                        .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                        correos.add(estudiante.getPersona().getCorreoElectronico());
                        correos.add(trabajoGrado.getCorreoElectronicoTutor());
                        envioCorreos.enviarCorreosCorrecion(correos,
                                        generacionResolucionDto.getEnvioEmail().getAsunto(),
                                        generacionResolucionDto.getEnvioEmail().getMensaje());
                        trabajoGrado.setNumeroEstado(16);
                }

                agregarInformacionCoordinadorFase2(generacionResolucionTmp, generacionResolucionDto);

                GeneracionResolucion generarResolucionRes = generacionResolucionRepository
                                .save(generacionResolucionTmp);

                return generacionResolucionResponseMapper.toCoordinadorFase2Dto(generarResolucionRes);
        }

        private void agregarInformacionCoordinadorFase2(GeneracionResolucion generacionResolucion,
                        GeneracionResolucionCoordinadorFase2Dto generacionResolucionDto) {
                // Crear una nueva instancia de RespuestaComite
                RespuestaComiteGeneracionResolucion respuestaComite = RespuestaComiteGeneracionResolucion.builder()
                                .conceptoComite(generacionResolucionDto.getActaFechaRespuestaComite().get(0)
                                                .getConceptoComite())
                                .numeroActa(generacionResolucionDto.getActaFechaRespuestaComite().get(0)
                                                .getNumeroActa())
                                .fechaActa(generacionResolucionDto.getActaFechaRespuestaComite().get(0).getFechaActa()
                                                .toString())
                                .generacionResolucion(generacionResolucion)
                                .build();

                // Si la colección está vacía, inicializarla
                if (generacionResolucion.getActaFechaRespuestaComite() == null) {
                        generacionResolucion.setActaFechaRespuestaComite(new ArrayList<>());
                }

                // Agregar la nueva respuesta a la lista existente
                generacionResolucion.getActaFechaRespuestaComite().add(respuestaComite);
                generacionResolucion.setLinkSolicitudConsejoFacultad(
                                generacionResolucionDto.getLinkSolicitudConsejoFacultad());
        }

        @Override
        @Transactional
        public GeneracionResolucionCoordinadorFase3ResponseDto insertarInformacionCoordinadorFase3(
                        Long idTrabajoGrado,
                        GeneracionResolucionCoordinadorFase3Dto generacionResolucionDto,
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

                GeneracionResolucion generacionResolucionTmp = generacionResolucionRepository
                                .findById(trabajoGrado.getIdGeneracionResolucion().getIdGeneracionResolucion())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Generacion de resolucion con id: "
                                                                + trabajoGrado.getIdGeneracionResolucion()
                                                                                .getIdGeneracionResolucion()
                                                                + " no encontrado"));

                trabajoGrado.setNumeroEstado(18);

                agregarInformacionCoordinadorFase3(generacionResolucionTmp, generacionResolucionDto);

                GeneracionResolucion generarResolucionRes = generacionResolucionRepository
                                .save(generacionResolucionTmp);

                return generacionResolucionResponseMapper.toCoordinadorFase3Dto(generarResolucionRes);
        }

        // Funciones privadas
        private void agregarInformacionCoordinadorFase3(GeneracionResolucion generacionResolucion,
                        GeneracionResolucionCoordinadorFase3Dto generacionResolucionDto) {
                generacionResolucion.setNumeroActaConsejoFacultad(
                                generacionResolucionDto.getNumeroActaConsejoFacultad());
                generacionResolucion.setFechaActaConsejoFacultad(generacionResolucionDto.getFechaActaConsejoFacultad());
        }

        @Override
        @Transactional(readOnly = true)
        public GeneracionResolucionDocenteResponseDto listarInformacionDocente(Long idTrabajoGrado) {
                GeneracionResolucion responseDto = generacionResolucionRepository
                                .findByIdTrabajoGradoId(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Generacion de resolucion con id: "
                                                                + idTrabajoGrado
                                                                + " no encontrado"));

                Optional<TrabajoGrado> trabajoGrado = trabajoGradoRepository
                                .findById(responseDto.getIdTrabajoGrado().getId());

                GeneracionResolucionDocenteResponseDto generacionResolucionDocenteResponseDto = generacionResolucionResponseMapper
                                .toDocenteDto(responseDto);

                generacionResolucionDocenteResponseDto.setTitulo(trabajoGrado.get().getTitulo());
                return generacionResolucionDocenteResponseDto;
        }

        @Override
        @Transactional(readOnly = true)
        public GeneracionResolucionCoordinadorFase1ResponseDto listarInformacionCoordinadorFase1(
                        Long idTrabajoGrado) {
                GeneracionResolucion generacionResolucionTmp = generacionResolucionRepository
                                .findByIdTrabajoGradoId(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id: " + idTrabajoGrado
                                                                + " no encontrado"));

                return generacionResolucionResponseMapper.toCoordinadorFase1Dto(generacionResolucionTmp);
        }

        @Transactional(readOnly = true)
        public GeneracionResolucionCoordinadorFase2ResponseDto listarInformacionCoordinadorFase2(
                        Long idTrabajoGrado) {
                GeneracionResolucion generacionResolucionTmp = generacionResolucionRepository
                                .findByIdTrabajoGradoId(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id: " + idTrabajoGrado
                                                                + " no encontrado"));

                return generacionResolucionResponseMapper.toCoordinadorFase2Dto(generacionResolucionTmp);
        }

        @Override
        @Transactional(readOnly = true)
        public GeneracionResolucionCoordinadorFase3ResponseDto listarInformacionCoordinadorFase3(
                        Long idTrabajoGrado) {
                GeneracionResolucion generacionResolucionTmp = generacionResolucionRepository
                                .findByIdTrabajoGradoId(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Generacion de resolucion con id: " + idTrabajoGrado
                                                                + " no encontrado"));

                return generacionResolucionResponseMapper.toCoordinadorFase3Dto(generacionResolucionTmp);
        }

        @Override
        public GeneracionResolucionDocenteResponseDto actualizarInformacionDocente(Long idTrabajoGrado,
                        GeneracionResolucionDocenteDto generacionResolucionDocenteDto,
                        BindingResult result) {

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                // Busca el trabajo de grado
                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException("Trabajo de grado con id: "
                                                + idTrabajoGrado
                                                + " no encontrado"));

                GeneracionResolucion generacionResolucionOld = generacionResolucionRepository
                                .findById(trabajoGrado.getIdGeneracionResolucion().getIdGeneracionResolucion())
                                .orElseThrow(() -> new ResourceNotFoundException("Examen de valoracion con id: "
                                                + trabajoGrado.getIdGeneracionResolucion().getIdGeneracionResolucion()
                                                + " no encontrado"));

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                GeneracionResolucion generacionResolucion = null;
                if (generacionResolucionOld != null) {
                        if (!generacionResolucionDocenteDto.getLinkAnteproyectoFinal()
                                        .equals(generacionResolucionOld.getLinkAnteproyectoFinal())) {
                                generacionResolucionDocenteDto.setLinkAnteproyectoFinal(
                                                FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                                generacionResolucionDocenteDto
                                                                                .getLinkAnteproyectoFinal()));
                                FilesUtilities.deleteFileExample(generacionResolucionOld.getLinkAnteproyectoFinal());
                        }
                        if (!generacionResolucionDocenteDto.getLinkSolicitudComite()
                                        .equals(generacionResolucionOld.getLinkSolicitudComite())) {
                                generacionResolucionDocenteDto.setLinkSolicitudComite(
                                                FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                                generacionResolucionDocenteDto
                                                                                .getLinkSolicitudComite()));
                                FilesUtilities.deleteFileExample(generacionResolucionOld.getLinkSolicitudComite());
                        }

                        updateExamenValoracionDocenteValues(generacionResolucionOld, generacionResolucionDocenteDto,
                                        trabajoGrado);
                        generacionResolucion = generacionResolucionRepository.save(generacionResolucionOld);
                }

                GeneracionResolucionDocenteResponseDto generacionResolucionDocenteResponseDto = generacionResolucionResponseMapper
                                .toDocenteDto(generacionResolucion);
                generacionResolucionDocenteResponseDto.setTitulo(trabajoGrado.getTitulo());

                return generacionResolucionDocenteResponseDto;
        }

        // Funciones privadas
        private void updateExamenValoracionDocenteValues(GeneracionResolucion generacionResolucion,
                        GeneracionResolucionDocenteDto generacionResolucionDocenteDto, TrabajoGrado trabajoGrado) {

                generacionResolucion.setDirector(generacionResolucionDocenteDto.getDirector());
                generacionResolucion.setCodirector(generacionResolucionDocenteDto.getCodirector());
                // Update archivos
                generacionResolucion
                                .setLinkAnteproyectoFinal(generacionResolucionDocenteDto.getLinkAnteproyectoFinal());
                generacionResolucion.setLinkSolicitudComite(generacionResolucionDocenteDto.getLinkSolicitudComite());
        }

        @Override
        public GeneracionResolucionCoordinadorFase1ResponseDto actualizarInformacionCoordinadorFase1(
                        Long idTrabajoGrado,
                        GeneracionResolucionCoordinadorFase1Dto generacionResolucionDocenteDto,
                        BindingResult result) {

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException("Trabajo de grado con id: "
                                                + idTrabajoGrado
                                                + " no encontrado"));

                GeneracionResolucion generacionResolucionOld = generacionResolucionRepository
                                .findById(trabajoGrado.getIdGeneracionResolucion().getIdGeneracionResolucion())
                                .orElseThrow(() -> new ResourceNotFoundException("Examen de valoracion con id: "
                                                + trabajoGrado.getIdGeneracionResolucion().getIdGeneracionResolucion()
                                                + " no encontrado"));

                GeneracionResolucion generacionResolucion = new GeneracionResolucion();

                ArrayList<String> correos = new ArrayList<>();

                if (generacionResolucionDocenteDto.getConceptoDocumentosCoordinador() != generacionResolucionOld
                                .getConceptoDocumentosCoordinador()) {
                        // Si pasa de aprobado a no aprobado
                        if (!generacionResolucionDocenteDto.getConceptoDocumentosCoordinador()) {
                                EstudianteResponseDtoAll estudiante = archivoClient
                                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                                correos.add(estudiante.getPersona().getCorreoElectronico());
                                correos.add(trabajoGrado.getCorreoElectronicoTutor());
                                envioCorreos.enviarCorreosCorrecion(correos,
                                                generacionResolucionDocenteDto.getEnvioEmailDto().getAsunto(),
                                                generacionResolucionDocenteDto.getEnvioEmailDto().getMensaje());
                                trabajoGrado.setNumeroEstado(22);
                        } else {
                                correos.add(Constants.correoComite);
                                Map<String, Object> documentosEnvioComiteDto = generacionResolucionDocenteDto
                                                .getObtenerDocumentosParaEnvioDto()
                                                .getDocumentos();
                                envioCorreos.enviarCorreoConAnexos(correos,
                                                generacionResolucionDocenteDto.getEnvioEmailDto().getAsunto(),
                                                generacionResolucionDocenteDto.getEnvioEmailDto().getMensaje(),
                                                documentosEnvioComiteDto);
                                trabajoGrado.setNumeroEstado(21);
                        }
                }

                generacionResolucionOld.setConceptoDocumentosCoordinador(
                                generacionResolucionDocenteDto.getConceptoDocumentosCoordinador());

                generacionResolucion = generacionResolucionRepository.save(generacionResolucionOld);

                return generacionResolucionResponseMapper.toCoordinadorFase1Dto(generacionResolucion);
        }

        @Override
        public GeneracionResolucionCoordinadorFase2ResponseDto actualizarInformacionCoordinadorFase2(
                        Long idTrabajoGrado,
                        GeneracionResolucionCoordinadorFase2Dto generacionResolucionCoordinadorFase1Dto,
                        BindingResult result) {

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException("Trabajo de grado con id: "
                                                + idTrabajoGrado
                                                + " no encontrado"));

                GeneracionResolucion generacionResolucionOld = generacionResolucionRepository
                                .findById(trabajoGrado.getIdGeneracionResolucion().getIdGeneracionResolucion())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Examen de valoracion con id: "
                                                                + trabajoGrado.getIdGeneracionResolucion()
                                                                                .getIdGeneracionResolucion()
                                                                + " no encontrado"));

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                GeneracionResolucion responseExamenValoracion = null;
                List<RespuestaComiteGeneracionResolucion> respuestaComiteList = generacionResolucionRepository
                                .findRespuestaComiteByGeneracionResolucionId(
                                                generacionResolucionOld.getIdGeneracionResolucion());
                RespuestaComiteGeneracionResolucion ultimoRegistro = respuestaComiteList.isEmpty() ? null
                                : respuestaComiteList.get(0);

                if (ultimoRegistro != null
                                && ultimoRegistro.getConceptoComite() != generacionResolucionCoordinadorFase1Dto
                                                .getActaFechaRespuestaComite().get(0)
                                                .getConceptoComite()) {
                        ArrayList<String> correos = new ArrayList<>();
                        // Si pasa de aprobado a no aprobado
                        if (!generacionResolucionCoordinadorFase1Dto.getActaFechaRespuestaComite().get(0)
                                        .getConceptoComite()) {
                                EstudianteResponseDtoAll estudiante = archivoClient
                                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                                correos.add(estudiante.getPersona().getCorreoElectronico());
                                correos.add(trabajoGrado.getCorreoElectronicoTutor());
                                envioCorreos.enviarCorreosCorrecion(correos,
                                                generacionResolucionCoordinadorFase1Dto.getEnvioEmail().getAsunto(),
                                                generacionResolucionCoordinadorFase1Dto.getEnvioEmail()
                                                                .getMensaje());
                                FilesUtilities.deleteFileExample(
                                                generacionResolucionOld.getLinkSolicitudConsejoFacultad());
                                trabajoGrado.setNumeroEstado(4);
                        } else {
                                correos.add(Constants.correoConsejo);
                                Map<String, Object> documentosParaConsejo = new HashMap<>();
                                String[] solicitudConsejoFacultad = generacionResolucionCoordinadorFase1Dto
                                                .getLinkSolicitudConsejoFacultad()
                                                .split("-");
                                documentosParaConsejo.put("Solicitud_Consejo_Facultad", solicitudConsejoFacultad[1]);
                                envioCorreos.enviarCorreoConAnexos(correos,
                                                generacionResolucionCoordinadorFase1Dto.getEnvioEmail().getAsunto(),
                                                generacionResolucionCoordinadorFase1Dto.getEnvioEmail().getMensaje(),
                                                documentosParaConsejo);
                                generacionResolucionCoordinadorFase1Dto
                                                .setLinkSolicitudConsejoFacultad(FilesUtilities.guardarArchivoNew2(
                                                                rutaArchivo,
                                                                generacionResolucionCoordinadorFase1Dto
                                                                                .getLinkSolicitudConsejoFacultad()));
                                trabajoGrado.setNumeroEstado(5);
                        }
                } else {
                        if (generacionResolucionOld != null) {
                                if (generacionResolucionCoordinadorFase1Dto.getLinkSolicitudConsejoFacultad()
                                                .compareTo(generacionResolucionOld
                                                                .getLinkSolicitudConsejoFacultad()) != 0) {
                                        generacionResolucionCoordinadorFase1Dto
                                                        .setLinkSolicitudConsejoFacultad(FilesUtilities
                                                                        .guardarArchivoNew2(rutaArchivo,
                                                                                        generacionResolucionCoordinadorFase1Dto
                                                                                                        .getLinkSolicitudConsejoFacultad()));
                                        FilesUtilities.deleteFileExample(
                                                        generacionResolucionOld.getLinkSolicitudConsejoFacultad());
                                }
                        }
                }
                updateExamenValoracionCoordinadorValues(generacionResolucionOld,
                                generacionResolucionCoordinadorFase1Dto, trabajoGrado);
                responseExamenValoracion = generacionResolucionRepository.save(generacionResolucionOld);
                return generacionResolucionResponseMapper.toCoordinadorFase2Dto(responseExamenValoracion);
        }

        private void updateExamenValoracionCoordinadorValues(GeneracionResolucion generacionResolucion,
                        GeneracionResolucionCoordinadorFase2Dto generacionResolucionCoordinadorFase2Dto,
                        TrabajoGrado trabajoGrado) {

                List<RespuestaComiteGeneracionResolucion> respuestaComiteList = generacionResolucionRepository
                                .findRespuestaComiteByGeneracionResolucionId(
                                                generacionResolucion.getIdGeneracionResolucion());
                RespuestaComiteGeneracionResolucion ultimoRegistro = respuestaComiteList.isEmpty() ? null
                                : respuestaComiteList.get(0);

                if (ultimoRegistro != null) {
                        // Actualizar los valores de ultimoRegistro
                        ultimoRegistro.setNumeroActa(
                                        generacionResolucionCoordinadorFase2Dto.getActaFechaRespuestaComite().get(0)
                                                        .getNumeroActa());
                        ultimoRegistro.setFechaActa(
                                        generacionResolucionCoordinadorFase2Dto.getActaFechaRespuestaComite().get(0)
                                                        .getFechaActa());

                        // Actualizar la lista actaFechaRespuestaComite de examenValoracion
                        RespuestaComiteGeneracionResolucion actaFechaRespuestaComite = respuestaComiteGeneracionResolucionRepository
                                        .findFirstByOrderByIdRespuestaComiteGeneracionResolucionDesc();

                        actaFechaRespuestaComite
                                        .setConceptoComite(generacionResolucionCoordinadorFase2Dto
                                                        .getActaFechaRespuestaComite().get(0)
                                                        .getConceptoComite());
                        actaFechaRespuestaComite
                                        .setNumeroActa(generacionResolucionCoordinadorFase2Dto
                                                        .getActaFechaRespuestaComite().get(0)
                                                        .getNumeroActa());
                        actaFechaRespuestaComite
                                        .setFechaActa(generacionResolucionCoordinadorFase2Dto
                                                        .getActaFechaRespuestaComite().get(0)
                                                        .getFechaActa());

                        respuestaComiteGeneracionResolucionRepository.save(actaFechaRespuestaComite);
                }
        }

        @Override
        public GeneracionResolucionCoordinadorFase3ResponseDto actualizarInformacionCoordinadorFase3(
                        Long idTrabajoGrado,
                        GeneracionResolucionCoordinadorFase3Dto generacionResolucionCoordinadorFase3Dto,
                        BindingResult result) {

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException("Trabajo de grado con id: "
                                                + idTrabajoGrado
                                                + " no encontrado"));

                GeneracionResolucion generacionResolucionOld = generacionResolucionRepository
                                .findById(trabajoGrado.getIdGeneracionResolucion().getIdGeneracionResolucion())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Examen de valoracion con id: "
                                                                + trabajoGrado.getIdGeneracionResolucion()
                                                                                .getIdGeneracionResolucion()
                                                                + " no encontrado"));

                GeneracionResolucion generacionResolucion = new GeneracionResolucion();

                generacionResolucion.setNumeroActaConsejoFacultad(
                                generacionResolucionCoordinadorFase3Dto.getNumeroActaConsejoFacultad());
                generacionResolucion.setFechaActaConsejoFacultad(
                                generacionResolucionCoordinadorFase3Dto.getFechaActaConsejoFacultad());

                generacionResolucion = generacionResolucionRepository.save(generacionResolucionOld);

                return generacionResolucionResponseMapper.toCoordinadorFase3Dto(generacionResolucion);
        }

        @Override
        @Transactional(readOnly = true)
        public String descargarArchivo(RutaArchivoDto rutaArchivo) {
                return FilesUtilities.recuperarArchivo(rutaArchivo.getRutaArchivo());
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

                String procesoVa = "Generacion_Resolucion";

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

}
