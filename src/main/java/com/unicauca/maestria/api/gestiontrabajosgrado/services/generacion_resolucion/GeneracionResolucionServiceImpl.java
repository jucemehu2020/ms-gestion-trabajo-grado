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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.Concepto;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.Constants;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.ValidationUtils;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.RespuestaComiteGeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.DirectorAndCodirectorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_3.GeneracionResolucionCoordinadorFase3Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_3.GeneracionResolucionCoordinadorFase3ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteListDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
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
        // private final ArchivoClientExpertos archivoClientExpertos;

        @Autowired
        private EnvioCorreos envioCorreos;

        @Override
        @Transactional(readOnly = true)
        public List<DirectorAndCodirectorResponseDto> listarDirectorAndCodirector() {
                List<DirectorAndCodirectorResponseDto> docentesYExpertos = new ArrayList<>();

                List<DocenteResponseDto> listadoDocentesDirector = archivoClient.listarDocentesRes();
                if (listadoDocentesDirector.size() == 0) {
                        throw new InformationException("No hay docentes registrados");
                }

                // List<ExpertoResponseDto> listadoExpertos = archivoClientExpertos.listar();
                List<DirectorAndCodirectorResponseDto> docentes = listadoDocentesDirector.stream()
                                .map(docente -> new DirectorAndCodirectorResponseDto(
                                                docente.getId(),
                                                docente.getPersona().getTipoIdentificacion(),
                                                docente.getPersona().getIdentificacion(),
                                                docente.getPersona().getNombre(),
                                                docente.getPersona().getApellido()))
                                .collect(Collectors.toList());
                // List<DirectorAndCodirectorResponseDto> expertos =
                // listadoDocentesCoDirector.stream()
                // .map(experto -> new DirectorAndCodirectorResponseDto(
                // experto.getPersona().getTipoIdentificacion(),
                // experto.getPersona().getIdentificacion(),
                // experto.getPersona().getNombre(),
                // experto.getPersona().getApellido()))
                // .collect(Collectors.toList());

                docentesYExpertos.addAll(docentes);
                // docentesYExpertos.addAll(expertos);

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

                validarLink(generacionResolucionDto.getLinkAnteproyectoFinal());
                validarLink(generacionResolucionDto.getLinkSolicitudComite());

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado
                                                                + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 7 && trabajoGrado.getNumeroEstado() != 17) {
                        throw new InformationException("No es permitido registrar la informacion");
                }

                if (generacionResolucionDto.getIdDirector() == generacionResolucionDto.getIdCodirector()) {
                        throw new InformationException(
                                        "No se permite registrar al mismo docente como director y codirector");
                }

                archivoClient.obtenerDocentePorId(generacionResolucionDto.getIdDirector());

                archivoClient.obtenerDocentePorId(generacionResolucionDto.getIdCodirector());

                // Mapear DTO a entidad
                GeneracionResolucion generarResolucion = generacionResolucionMapper.toEntity(generacionResolucionDto);
                generarResolucion.setDirector(generacionResolucionDto.getIdDirector());
                generarResolucion.setCodirector(generacionResolucionDto.getIdCodirector());

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                // Establecer la relación uno a uno
                generarResolucion.setTrabajoGrado(trabajoGrado);
                trabajoGrado.setGeneracionResolucion(generarResolucion);

                // Se cambia el numero de estado
                trabajoGrado.setNumeroEstado(18);

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

                if (generacionResolucionDto.getConceptoDocumentosCoordinador().equals(ConceptoVerificacion.RECHAZADO)
                                && generacionResolucionDto.getObtenerDocumentosParaEnvio() != null) {
                        throw new InformationException("Envio de atributos no permitido");
                }

                if (generacionResolucionDto.getConceptoDocumentosCoordinador().equals(ConceptoVerificacion.ACEPTADO)
                                && generacionResolucionDto.getObtenerDocumentosParaEnvio() == null) {
                        throw new InformationException("Atributos incorrectos");
                }

                if (generacionResolucionDto.getConceptoDocumentosCoordinador().equals(ConceptoVerificacion.ACEPTADO)) {
                        ValidationUtils.validarBase64(generacionResolucionDto.getObtenerDocumentosParaEnvio()
                                        .getBase64AnteproyectoFinal());
                        ValidationUtils.validarBase64(generacionResolucionDto.getObtenerDocumentosParaEnvio()
                                        .getBase64SolicitudComite());
                }

                ArrayList<String> correos = new ArrayList<>();

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id "
                                                                + idTrabajoGrado
                                                                + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 18) {
                        throw new InformationException("No es permitido registrar la informacion");
                }

                GeneracionResolucion generacionResolucionTmp = generacionResolucionRepository
                                .findById(trabajoGrado.getGeneracionResolucion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Generacion de resolucion con id: "
                                                                + trabajoGrado.getGeneracionResolucion()
                                                                                .getId()
                                                                + " no encontrado"));

                if (generacionResolucionDto.getConceptoDocumentosCoordinador().equals(ConceptoVerificacion.ACEPTADO)) {
                        correos.add(Constants.correoComite);
                        Map<String, Object> documentosEnvioComiteDto = generacionResolucionDto
                                        .getObtenerDocumentosParaEnvio()
                                        .getDocumentos();
                        envioCorreos.enviarCorreoConAnexos(correos,
                                        generacionResolucionDto.getEnvioEmail().getAsunto(),
                                        generacionResolucionDto.getEnvioEmail().getMensaje(),
                                        documentosEnvioComiteDto);
                        trabajoGrado.setNumeroEstado(20);

                } else {
                        EstudianteResponseDtoAll estudiante = archivoClient
                                        .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                        correos.add(estudiante.getCorreoUniversidad());
                        correos.add(trabajoGrado.getCorreoElectronicoTutor());
                        envioCorreos.enviarCorreosCorrecion(correos,
                                        generacionResolucionDto.getEnvioEmail().getAsunto(),
                                        generacionResolucionDto.getEnvioEmail().getMensaje());
                        trabajoGrado.setNumeroEstado(19);
                }

                generacionResolucionTmp.setConceptoDocumentosCoordinador(
                                generacionResolucionDto.getConceptoDocumentosCoordinador());

                GeneracionResolucion generarResolucionRes = generacionResolucionRepository
                                .save(generacionResolucionTmp);

                return generacionResolucionResponseMapper.toCoordinadorFase1Dto(generarResolucionRes);
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

                if (generacionResolucionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                .equals(Concepto.NO_APROBADO)
                                && generacionResolucionDto.getLinkSolicitudConsejoFacultad() != null) {
                        throw new InformationException("Envio de atributos no permitido");
                }

                if (generacionResolucionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                .equals(Concepto.APROBADO)
                                && generacionResolucionDto.getLinkSolicitudConsejoFacultad() == null) {
                        throw new InformationException("Atributos incorrectos");
                }

                if (generacionResolucionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                .equals(Concepto.APROBADO)) {
                        validarLink(generacionResolucionDto.getLinkSolicitudConsejoFacultad());
                }

                ArrayList<String> correos = new ArrayList<>();

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id "
                                                                + idTrabajoGrado
                                                                + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 20) {
                        throw new InformationException("No es permitido registrar la informacion");
                }

                GeneracionResolucion generacionResolucionTmp = generacionResolucionRepository
                                .findById(trabajoGrado.getGeneracionResolucion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Generacion de resolucion con id: "
                                                                + trabajoGrado.getGeneracionResolucion()
                                                                                .getId()
                                                                + " no encontrado"));

                for (RespuestaComiteGeneracionResolucion respuesta : generacionResolucionTmp
                                .getActaFechaRespuestaComite()) {
                        if (respuesta.getConceptoComite().equals(Concepto.APROBADO)) {
                                throw new InformationException("El concepto ya es APROBADO");
                        }
                }

                if (generacionResolucionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                .equals(Concepto.APROBADO)) {
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
                        generacionResolucionDto.setLinkSolicitudConsejoFacultad(
                                        FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                        generacionResolucionDto.getLinkSolicitudConsejoFacultad()));
                        trabajoGrado.setNumeroEstado(22);
                } else {
                        EstudianteResponseDtoAll estudiante = archivoClient
                                        .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                        correos.add(estudiante.getCorreoUniversidad());
                        correos.add(trabajoGrado.getCorreoElectronicoTutor());
                        envioCorreos.enviarCorreosCorrecion(correos,
                                        generacionResolucionDto.getEnvioEmail().getAsunto(),
                                        generacionResolucionDto.getEnvioEmail().getMensaje());
                        trabajoGrado.setNumeroEstado(21);
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
                                .fechaActa(generacionResolucionDto.getActaFechaRespuestaComite().get(0).getFechaActa())
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
                                                "Trabajo de grado con id "
                                                                + idTrabajoGrado
                                                                + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 22) {
                        throw new InformationException("No es permitido registrar la informacion");
                }

                GeneracionResolucion generacionResolucionTmp = generacionResolucionRepository
                                .findById(trabajoGrado.getGeneracionResolucion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Generacion de resolucion con id: "
                                                                + trabajoGrado.getGeneracionResolucion()
                                                                                .getId()
                                                                + " no encontrado"));

                trabajoGrado.setNumeroEstado(23);

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
        public GeneracionResolucionDocenteListDto listarInformacionDocente(Long idTrabajoGrado) {
                GeneracionResolucion generacionResolucion = generacionResolucionRepository
                                .findByTrabajoGradoId(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id "
                                                                + idTrabajoGrado
                                                                + " no encontrado"));

                if (generacionResolucion.getDirector() == null && generacionResolucion.getCodirector() == null
                                && generacionResolucion.getLinkAnteproyectoFinal() == null
                                && generacionResolucion.getLinkSolicitudComite() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                Optional<TrabajoGrado> trabajoGrado = trabajoGradoRepository
                                .findById(generacionResolucion.getTrabajoGrado().getId());

                DocenteResponseDto docente1 = archivoClient.obtenerDocentePorId(generacionResolucion.getDirector());
                String nombre_docente1 = docente1.getPersona().getNombre() + " " + docente1.getPersona().getApellido();
                Map<String, String> evaluadorInternoMap1 = new HashMap<>();
                evaluadorInternoMap1.put("id", docente1.getId().toString());
                evaluadorInternoMap1.put("nombres", nombre_docente1);

                // Obtener y construir información del evaluador externo
                DocenteResponseDto docente2 = archivoClient.obtenerDocentePorId(generacionResolucion.getCodirector());
                String nombre_docente2 = docente2.getPersona().getNombre() + " " + docente2.getPersona().getApellido();
                Map<String, String> evaluadorInternoMap2 = new HashMap<>();
                evaluadorInternoMap2.put("id", docente2.getId().toString());
                evaluadorInternoMap2.put("nombres", nombre_docente2);

                GeneracionResolucionDocenteListDto generacionResolucionDocenteListDto = new GeneracionResolucionDocenteListDto();

                generacionResolucionDocenteListDto.setId(generacionResolucion.getId());
                generacionResolucionDocenteListDto.setTitulo(trabajoGrado.get().getTitulo());
                generacionResolucionDocenteListDto.setDirector(evaluadorInternoMap1);
                generacionResolucionDocenteListDto.setCodirector(evaluadorInternoMap2);
                generacionResolucionDocenteListDto
                                .setLinkAnteproyectoFinal(generacionResolucion.getLinkAnteproyectoFinal());
                generacionResolucionDocenteListDto
                                .setLinkSolicitudComite(generacionResolucion.getLinkSolicitudComite());

                return generacionResolucionDocenteListDto;
        }

        @Override
        @Transactional(readOnly = true)
        public GeneracionResolucionCoordinadorFase1ResponseDto listarInformacionCoordinadorFase1(
                        Long idTrabajoGrado) {
                GeneracionResolucion generacionResolucionTmp = generacionResolucionRepository
                                .findByTrabajoGradoId(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado
                                                                + " no encontrado"));

                if (generacionResolucionTmp.getConceptoDocumentosCoordinador() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                return generacionResolucionResponseMapper.toCoordinadorFase1Dto(generacionResolucionTmp);
        }

        @Transactional(readOnly = true)
        public GeneracionResolucionCoordinadorFase2ResponseDto listarInformacionCoordinadorFase2(
                        Long idTrabajoGrado) {
                GeneracionResolucion generacionResolucionTmp = generacionResolucionRepository
                                .findByTrabajoGradoId(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado
                                                                + " no encontrado"));

                boolean actaFechaRespuestaComiteEmpty = generacionResolucionTmp
                                .getActaFechaRespuestaComite() == null ||
                                generacionResolucionTmp.getActaFechaRespuestaComite().isEmpty();
                if (actaFechaRespuestaComiteEmpty
                                && generacionResolucionTmp.getLinkSolicitudConsejoFacultad() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                return generacionResolucionResponseMapper.toCoordinadorFase2Dto(generacionResolucionTmp);
        }

        @Override
        @Transactional(readOnly = true)
        public GeneracionResolucionCoordinadorFase3ResponseDto listarInformacionCoordinadorFase3(
                        Long idTrabajoGrado) {
                GeneracionResolucion generacionResolucionTmp = generacionResolucionRepository
                                .findByTrabajoGradoId(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado
                                                                + " no encontrado"));

                if (generacionResolucionTmp.getNumeroActaConsejoFacultad() == null
                                && generacionResolucionTmp.getFechaActaConsejoFacultad() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                return generacionResolucionResponseMapper.toCoordinadorFase3Dto(generacionResolucionTmp);
        }

        @Override
        public GeneracionResolucionDocenteResponseDto actualizarInformacionDocente(Long idTrabajoGrado,
                        GeneracionResolucionDocenteDto generacionResolucionDocenteDto,
                        BindingResult result) {

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                validarLink(generacionResolucionDocenteDto.getLinkAnteproyectoFinal());
                validarLink(generacionResolucionDocenteDto.getLinkSolicitudComite());

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException("Trabajo de grado con id "
                                                + idTrabajoGrado
                                                + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 18 && trabajoGrado.getNumeroEstado() != 19
                                && trabajoGrado.getNumeroEstado() != 21) {
                        throw new InformationException("No es permitido registrar la informacion");
                }

                if (generacionResolucionDocenteDto.getIdDirector() == generacionResolucionDocenteDto
                                .getIdCodirector()) {
                        throw new InformationException(
                                        "No se permite registrar al mismo docente como director y codirector");
                }

                GeneracionResolucion generacionResolucionOld = generacionResolucionRepository
                                .findById(trabajoGrado.getGeneracionResolucion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Examen de valoracion con id: "
                                                + trabajoGrado.getGeneracionResolucion().getId()
                                                + " no encontrado"));

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                trabajoGrado.setNumeroEstado(18);

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
                GeneracionResolucion generacionResolucion = generacionResolucionRepository
                                .save(generacionResolucionOld);

                GeneracionResolucionDocenteResponseDto generacionResolucionDocenteResponseDto = generacionResolucionResponseMapper
                                .toDocenteDto(generacionResolucion);
                generacionResolucionDocenteResponseDto.setTitulo(trabajoGrado.getTitulo());

                return generacionResolucionDocenteResponseDto;
        }

        private void updateExamenValoracionDocenteValues(GeneracionResolucion generacionResolucion,
                        GeneracionResolucionDocenteDto generacionResolucionDocenteDto, TrabajoGrado trabajoGrado) {

                archivoClient.obtenerDocentePorId(generacionResolucionDocenteDto.getIdDirector());

                archivoClient.obtenerDocentePorId(generacionResolucionDocenteDto.getIdCodirector());

                generacionResolucion.setDirector(generacionResolucionDocenteDto.getIdDirector());
                generacionResolucion.setCodirector(generacionResolucionDocenteDto.getIdCodirector());
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

                if (generacionResolucionDocenteDto.getConceptoDocumentosCoordinador()
                                .equals(ConceptoVerificacion.RECHAZADO)
                                && generacionResolucionDocenteDto.getObtenerDocumentosParaEnvio() != null) {
                        throw new InformationException("Envio de atributos no permitido");
                }

                if (generacionResolucionDocenteDto.getConceptoDocumentosCoordinador()
                                .equals(ConceptoVerificacion.ACEPTADO)
                                && generacionResolucionDocenteDto.getObtenerDocumentosParaEnvio() == null) {
                        throw new InformationException("Atributos incorrectos");
                }

                if (generacionResolucionDocenteDto.getConceptoDocumentosCoordinador()
                                .equals(ConceptoVerificacion.ACEPTADO)) {
                        ValidationUtils.validarBase64(generacionResolucionDocenteDto.getObtenerDocumentosParaEnvio()
                                        .getBase64AnteproyectoFinal());
                        ValidationUtils.validarBase64(generacionResolucionDocenteDto.getObtenerDocumentosParaEnvio()
                                        .getBase64SolicitudComite());
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException("Trabajo de grado con id "
                                                + idTrabajoGrado
                                                + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 20 && trabajoGrado.getNumeroEstado() != 19) {
                        throw new InformationException("No es permitido registrar la informacion");
                }

                GeneracionResolucion generacionResolucionOld = generacionResolucionRepository
                                .findById(trabajoGrado.getGeneracionResolucion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Examen de valoracion con id: "
                                                + trabajoGrado.getGeneracionResolucion().getId()
                                                + " no encontrado"));

                GeneracionResolucion generacionResolucion = new GeneracionResolucion();

                ArrayList<String> correos = new ArrayList<>();

                if (generacionResolucionDocenteDto.getConceptoDocumentosCoordinador() != generacionResolucionOld
                                .getConceptoDocumentosCoordinador()) {
                        // Si pasa de aprobado a no aprobado
                        if (!generacionResolucionDocenteDto.getConceptoDocumentosCoordinador()
                                        .equals(ConceptoVerificacion.ACEPTADO)) {
                                EstudianteResponseDtoAll estudiante = archivoClient
                                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                                correos.add(estudiante.getCorreoUniversidad());
                                correos.add(trabajoGrado.getCorreoElectronicoTutor());
                                envioCorreos.enviarCorreosCorrecion(correos,
                                                generacionResolucionDocenteDto.getEnvioEmail().getAsunto(),
                                                generacionResolucionDocenteDto.getEnvioEmail().getMensaje());
                                trabajoGrado.setNumeroEstado(19);
                        } else {
                                correos.add(Constants.correoComite);
                                Map<String, Object> documentosEnvioComiteDto = generacionResolucionDocenteDto
                                                .getObtenerDocumentosParaEnvio()
                                                .getDocumentos();
                                envioCorreos.enviarCorreoConAnexos(correos,
                                                generacionResolucionDocenteDto.getEnvioEmail().getAsunto(),
                                                generacionResolucionDocenteDto.getEnvioEmail().getMensaje(),
                                                documentosEnvioComiteDto);
                                trabajoGrado.setNumeroEstado(20);
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

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                if (generacionResolucionCoordinadorFase1Dto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                .equals(Concepto.NO_APROBADO)
                                && generacionResolucionCoordinadorFase1Dto.getLinkSolicitudConsejoFacultad() != null) {
                        throw new InformationException("Envio de atributos no permitido");
                }

                if (generacionResolucionCoordinadorFase1Dto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                .equals(Concepto.APROBADO)
                                && generacionResolucionCoordinadorFase1Dto.getLinkSolicitudConsejoFacultad() == null) {
                        throw new InformationException("Atributos incorrectos");
                }

                if (generacionResolucionCoordinadorFase1Dto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                .equals(Concepto.APROBADO)) {
                        validarLink(generacionResolucionCoordinadorFase1Dto.getLinkSolicitudConsejoFacultad());
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException("Trabajo de grado con id "
                                                + idTrabajoGrado
                                                + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 21 && trabajoGrado.getNumeroEstado() != 22) {
                        throw new InformationException("No es permitido registrar la informacion");
                }

                GeneracionResolucion generacionResolucionOld = generacionResolucionRepository
                                .findById(trabajoGrado.getGeneracionResolucion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Examen de valoracion con id: "
                                                                + trabajoGrado.getGeneracionResolucion()
                                                                                .getId()
                                                                + " no encontrado"));

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                GeneracionResolucion responseExamenValoracion = null;
                List<RespuestaComiteGeneracionResolucion> respuestaComiteList = generacionResolucionRepository
                                .findRespuestaComiteByGeneracionResolucionId(
                                                generacionResolucionOld.getId());
                RespuestaComiteGeneracionResolucion ultimoRegistro = respuestaComiteList.isEmpty() ? null
                                : respuestaComiteList.get(0);

                if (ultimoRegistro != null
                                && ultimoRegistro.getConceptoComite() != generacionResolucionCoordinadorFase1Dto
                                                .getActaFechaRespuestaComite().get(0)
                                                .getConceptoComite()) {
                        ArrayList<String> correos = new ArrayList<>();
                        // Si pasa de aprobado a no aprobado
                        if (!generacionResolucionCoordinadorFase1Dto.getActaFechaRespuestaComite().get(0)
                                        .getConceptoComite().equals(Concepto.APROBADO)) {
                                EstudianteResponseDtoAll estudiante = archivoClient
                                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                                correos.add(estudiante.getCorreoUniversidad());
                                correos.add(trabajoGrado.getCorreoElectronicoTutor());
                                envioCorreos.enviarCorreosCorrecion(correos,
                                                generacionResolucionCoordinadorFase1Dto.getEnvioEmail().getAsunto(),
                                                generacionResolucionCoordinadorFase1Dto.getEnvioEmail()
                                                                .getMensaje());
                                FilesUtilities.deleteFileExample(
                                                generacionResolucionOld.getLinkSolicitudConsejoFacultad());
                                trabajoGrado.setNumeroEstado(21);
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
                                trabajoGrado.setNumeroEstado(22);
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
                                                generacionResolucion.getId());
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
                                        .findFirstByOrderByIdDesc();

                        actaFechaRespuestaComite.setConceptoComite(generacionResolucionCoordinadorFase2Dto
                                        .getActaFechaRespuestaComite().get(0)
                                        .getConceptoComite());
                        actaFechaRespuestaComite.setNumeroActa(generacionResolucionCoordinadorFase2Dto
                                        .getActaFechaRespuestaComite().get(0)
                                        .getNumeroActa());
                        actaFechaRespuestaComite.setFechaActa(generacionResolucionCoordinadorFase2Dto
                                        .getActaFechaRespuestaComite().get(0)
                                        .getFechaActa());

                        respuestaComiteGeneracionResolucionRepository.save(actaFechaRespuestaComite);
                }

                generacionResolucion.setLinkSolicitudConsejoFacultad(
                                generacionResolucionCoordinadorFase2Dto.getLinkSolicitudConsejoFacultad());
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
                                .orElseThrow(() -> new ResourceNotFoundException("Trabajo de grado con id "
                                                + idTrabajoGrado
                                                + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 21) {
                        throw new InformationException("No es permitido registrar la informacion");
                }

                GeneracionResolucion generacionResolucionOld = generacionResolucionRepository
                                .findById(trabajoGrado.getGeneracionResolucion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Examen de valoracion con id: "
                                                                + trabajoGrado.getGeneracionResolucion()
                                                                                .getId()
                                                                + " no encontrado"));

                trabajoGrado.setNumeroEstado(23);

                GeneracionResolucion generacionResolucion = new GeneracionResolucion();

                generacionResolucion.setNumeroActaConsejoFacultad(
                                generacionResolucionCoordinadorFase3Dto.getNumeroActaConsejoFacultad());
                generacionResolucion.setFechaActaConsejoFacultad(
                                generacionResolucionCoordinadorFase3Dto.getFechaActaConsejoFacultad());

                generacionResolucion = generacionResolucionRepository.save(generacionResolucionOld);

                return generacionResolucionResponseMapper.toCoordinadorFase3Dto(generacionResolucion);
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

        private void validarLink(String link) {
                ValidationUtils.validarFormatoLink(link);
                String base64 = link.substring(link.indexOf('-') + 1);
                ValidationUtils.validarBase64(base64);
        }

}
