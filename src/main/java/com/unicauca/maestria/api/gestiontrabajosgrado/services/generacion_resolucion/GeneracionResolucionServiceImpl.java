package com.unicauca.maestria.api.gestiontrabajosgrado.services.generacion_resolucion;

import java.lang.reflect.Field;
import java.util.ArrayList;
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

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientExpertos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.ConvertString;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.CamposUnicosGenerarResolucionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.DirectorAndCodirectorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.GeneracionResolucionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.GeneracionResolucionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.GeneracionResolucionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.GeneracionResolucionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeneracionResolucionServiceImpl implements GeneracionResolucionService {

        private final GeneracionResolucionRepository generacionResolucionRepository;
        private final GeneracionResolucionMapper generacionResolucionMapper;
        private final GeneracionResolucionResponseMapper generacionResolucionResponseMapper;
        private final TrabajoGradoRepository trabajoGradoRepository;
        private final ArchivoClient archivoClient;
        private final ArchivoClientExpertos archivoClientExpertos;
        private final InformacionUnicaGeneracionResolucion informacionUnicaGeneracionResolucion;

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

        // @Override
        // @Transactional
        // public GeneracionResolucionDto crear(GeneracionResolucionDto
        // generacionResolucionDto, BindingResult result) {
        // if (result.hasErrors()) {
        // throw new FieldErrorException(result);
        // }

        // TrabajoGrado trabajoGrado = trabajoGradoRepository
        // .findById(generacionResolucionDto.getIdTrabajoGrados())
        // .orElseThrow(() -> new ResourceNotFoundException(
        // "TrabajoGrado con id: " + generacionResolucionDto.getIdTrabajoGrados()
        // + " No encontrado"));

        // Map<String, String> validacionCamposUnicos = validacionCampoUnicos(
        // obtenerCamposUnicos(generacionResolucionDto),
        // null);
        // if (!validacionCamposUnicos.isEmpty()) {
        // throw new FieldUniqueException(validacionCamposUnicos);
        // }

        // // Obtener iniciales del trabajo de grado
        // String procesoVa = "Generacion_Resolucion";
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
        // GeneracionResolucion generarResolucion =
        // generacionResolucionMapper.toEntity(generacionResolucionDto);

        // // Establecer la relación uno a uno
        // generarResolucion.setIdTrabajoGrado(trabajoGrado);
        // trabajoGrado.setIdGeneracionResolucion(generarResolucion);

        // // Se cambia el numero de estado
        // trabajoGrado.setNumeroEstado(6);

        // // Guardar la entidad ExamenValoracion
        // generarResolucion.setLinkAnteproyectoAprobado(
        // FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
        // generarResolucion.getLinkAnteproyectoAprobado(), nombreCarpeta));
        // generarResolucion
        // .setLinkSolicitudComite(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado,
        // procesoVa,
        // generarResolucion.getLinkSolicitudComite(), nombreCarpeta));
        // generarResolucion.setLinkSolicitudConcejoFacultad(
        // FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
        // generarResolucion.getLinkSolicitudConcejoFacultad(), nombreCarpeta));
        // generarResolucion.setLinkResolucionGeneradaCF(
        // FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
        // generarResolucion.getLinkResolucionGeneradaCF(), nombreCarpeta));

        // GeneracionResolucion generarResolucionRes =
        // generacionResolucionRepository.save(generarResolucion);

        // return generacionResolucionMapper.toDto(generarResolucionRes);
        // }

        @Override
        @Transactional
        public GeneracionResolucionDocenteResponseDto insertarInformacionDocente(
                        GeneracionResolucionDocenteDto generacionResolucionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(generacionResolucionDto.getIdTrabajoGrados())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + generacionResolucionDto.getIdTrabajoGrados()
                                                                + " No encontrado"));

                // Map<String, String> validacionCamposUnicos = validacionCampoUnicos(
                // obtenerCamposUnicos(generacionResolucionDto),
                // null);
                // if (!validacionCamposUnicos.isEmpty()) {
                // throw new FieldUniqueException(validacionCamposUnicos);
                // }

                // Obtener iniciales del trabajo de grado
                String procesoVa = "Generacion_Resolucion";
                String tituloTrabajoGrado = ConvertString.obtenerIniciales(trabajoGrado.getTitulo());

                Long idenficiacionEstudiante = trabajoGrado.getEstudiante().getPersona().getIdentificacion();
                String nombreEstudiante = trabajoGrado.getEstudiante().getPersona().getNombre();
                String apellidoEstudiante = trabajoGrado.getEstudiante().getPersona().getApellido();
                String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

                // Mapear DTO a entidad
                GeneracionResolucion generarResolucion = generacionResolucionMapper.toEntity(generacionResolucionDto);

                // Establecer la relación uno a uno
                generarResolucion.setIdTrabajoGrado(trabajoGrado);
                trabajoGrado.setIdGeneracionResolucion(generarResolucion);

                // Se cambia el numero de estado
                trabajoGrado.setNumeroEstado(8);

                // Guardar la entidad ExamenValoracion
                generarResolucion.setLinkAnteproyectoFinal(
                                FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                generarResolucion.getLinkAnteproyectoFinal(), nombreCarpeta));
                generarResolucion
                                .setLinkSolicitudComite(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                generarResolucion.getLinkSolicitudComite(), nombreCarpeta));

                GeneracionResolucion generarResolucionRes = generacionResolucionRepository.save(generarResolucion);

                return generacionResolucionResponseMapper.toDocenteDto(generarResolucionRes);
        }

        @Override
        @Transactional
        public GeneracionResolucionCoordinadorFase1ResponseDto insertarInformacionCoordinadorFase1(
                        GeneracionResolucionCoordinadorFase1Dto generacionResolucionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(generacionResolucionDto.getIdTrabajoGrados())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + generacionResolucionDto.getIdTrabajoGrados()
                                                                + " No encontrado"));

                GeneracionResolucion generacionResolucionTmp = generacionResolucionRepository
                                .findByTrabajoGradoId(generacionResolucionDto.getIdTrabajoGrados());

                // Map<String, String> validacionCamposUnicos = validacionCampoUnicos(
                // obtenerCamposUnicos(generacionResolucionDto),
                // null);
                // if (!validacionCamposUnicos.isEmpty()) {
                // throw new FieldUniqueException(validacionCamposUnicos);
                // }

                // Obtener iniciales del trabajo de grado
                String procesoVa = "Generacion_Resolucion";
                String tituloTrabajoGrado = ConvertString.obtenerIniciales(trabajoGrado.getTitulo());

                Long idenficiacionEstudiante = trabajoGrado.getEstudiante().getPersona().getIdentificacion();
                String nombreEstudiante = trabajoGrado.getEstudiante().getPersona().getNombre();
                String apellidoEstudiante = trabajoGrado.getEstudiante().getPersona().getApellido();
                String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

                // Mapear DTO a entidad
                GeneracionResolucion generarResolucion = generacionResolucionMapper.toEntity(generacionResolucionDto);

                // Establecer la relación uno a uno
                // generarResolucion.setIdTrabajoGrado(trabajoGrado);
                // trabajoGrado.setIdGeneracionResolucion(generarResolucion);

                // Se cambia el numero de estado
                trabajoGrado.setNumeroEstado(9);

                // Guardar la entidad ExamenValoracion
                generarResolucion.setLinkSolicitudConsejoFacultad(
                                FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                generarResolucion.getLinkSolicitudConsejoFacultad(), nombreCarpeta));

                agregarInformacionCoordinadorFase1(generacionResolucionTmp, generacionResolucionDto);

                GeneracionResolucion generarResolucionRes = generacionResolucionRepository
                                .save(generacionResolucionTmp);

                return generacionResolucionResponseMapper.toCoordinadorFase1Dto(generarResolucionRes);
        }

        // Funciones privadas
        private void agregarInformacionCoordinadorFase1(GeneracionResolucion generacionResolucion,
                        GeneracionResolucionCoordinadorFase1Dto generacionResolucionDto) {
                generacionResolucion.setNumeroActaSolicitudComite(
                                generacionResolucionDto.getNumeroActaSolicitudComite());
                generacionResolucion.setFechaActaSolicitudComite(generacionResolucionDto.getFechaActaSolicitudComite());
                generacionResolucion.setLinkSolicitudConsejoFacultad(
                                generacionResolucionDto.getLinkSolicitudConsejoFacultad());
        }

        @Override
        @Transactional
        public GeneracionResolucionCoordinadorFase2ResponseDto insertarInformacionCoordinadorFase2(
                        GeneracionResolucionCoordinadorFase2Dto generacionResolucionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(generacionResolucionDto.getIdTrabajoGrados())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + generacionResolucionDto.getIdTrabajoGrados()
                                                                + " No encontrado"));

                GeneracionResolucion generacionResolucionTmp = generacionResolucionRepository
                                .findByTrabajoGradoId(generacionResolucionDto.getIdTrabajoGrados());

                // Map<String, String> validacionCamposUnicos = validacionCampoUnicos(
                // obtenerCamposUnicos(generacionResolucionDto),
                // null);
                // if (!validacionCamposUnicos.isEmpty()) {
                // throw new FieldUniqueException(validacionCamposUnicos);
                // }

                // Obtener iniciales del trabajo de grado
                String procesoVa = "Generacion_Resolucion";
                String tituloTrabajoGrado = ConvertString.obtenerIniciales(trabajoGrado.getTitulo());

                Long idenficiacionEstudiante = trabajoGrado.getEstudiante().getPersona().getIdentificacion();
                String nombreEstudiante = trabajoGrado.getEstudiante().getPersona().getNombre();
                String apellidoEstudiante = trabajoGrado.getEstudiante().getPersona().getApellido();
                String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

                // Mapear DTO a entidad
                //GeneracionResolucion generarResolucion = generacionResolucionMapper.toEntity(generacionResolucionDto);

                // Establecer la relación uno a uno
                // generarResolucion.setIdTrabajoGrado(trabajoGrado);
                // trabajoGrado.setIdGeneracionResolucion(generarResolucion);

                // Se cambia el numero de estado
                trabajoGrado.setNumeroEstado(10);

                agregarInformacionCoordinadorFase2(generacionResolucionTmp, generacionResolucionDto);

                GeneracionResolucion generarResolucionRes = generacionResolucionRepository
                                .save(generacionResolucionTmp);

                return generacionResolucionResponseMapper.toCoordinadorFase2Dto(generarResolucionRes);
        }

        // Funciones privadas
        private void agregarInformacionCoordinadorFase2(GeneracionResolucion generacionResolucion,
                        GeneracionResolucionCoordinadorFase2Dto generacionResolucionDto) {
                generacionResolucion.setNumeroActaConsejoFacultad(
                                generacionResolucionDto.getNumeroActaConsejoFacultad());
                generacionResolucion.setFechaActaConsejoFacultad(generacionResolucionDto.getFechaActaConsejoFacultad());
        }

        // @Override
        // @Transactional(readOnly = true)
        // public GeneracionResolucionDto buscarPorId(Long idTrabajoGrado) {
        // return generacionResolucionRepository.findByIdTrabajoGradoId(idTrabajoGrado)
        // .stream()
        // .map(generacionResolucionMapper::toDto)
        // .findFirst()
        // .orElse(null);
        // }

        @Override
        @Transactional(readOnly = true)
        public GeneracionResolucionDocenteResponseDto listarInformacionDocente(Long idTrabajoGrado) {
                Optional<GeneracionResolucion> responseDto = generacionResolucionRepository
                                .findByIdTrabajoGradoId(idTrabajoGrado);
                if (responseDto.isPresent()) {
                        return generacionResolucionResponseMapper.toDocenteDto(responseDto.get());
                } else {
                        return null;
                }
        }

        @Override
        @Transactional(readOnly = true)
        public GeneracionResolucionCoordinadorFase1ResponseDto listarInformacionCoordinadorFase1(Long idTrabajoGrado) {
                return generacionResolucionRepository.findByIdTrabajoGradoId(idTrabajoGrado)
                                .stream()
                                .map(generacionResolucionResponseMapper::toCoordinadorFase1Dto)
                                .findFirst()
                                .orElse(null);
        }

        @Override
        @Transactional(readOnly = true)
        public GeneracionResolucionCoordinadorFase2ResponseDto listarInformacionCoordinadorFase2(Long idTrabajoGrado) {
                return generacionResolucionRepository.findByIdTrabajoGradoId(idTrabajoGrado)
                                .stream()
                                .map(generacionResolucionResponseMapper::toCoordinadorFase2Dto)
                                .findFirst()
                                .orElse(null);
        }

        // @Override
        // public GeneracionResolucionDto actualizar(Long id, GeneracionResolucionDto generacionResolucionDto,
        //                 BindingResult result) {

        //         if (result.hasErrors()) {
        //                 throw new FieldErrorException(result);
        //         }

        //         GeneracionResolucion generacionResolucionTmp = generacionResolucionRepository.findById(id).orElseThrow(
        //                         () -> new ResourceNotFoundException(
        //                                         "Generacion de resolucion con id: " + id + " no encontrado"));

        //         // Busca el trabajo de grado
        //         TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(id).orElseThrow(
        //                         () -> new ResourceNotFoundException(
        //                                         "Trabajo de grado con id: " + id + " no encontrado"));

        //         String procesoVa = "Generacion_Resolucion";
        //         String tituloTrabajoGrado = ConvertString.obtenerIniciales(trabajoGrado.getTitulo());
        //         Long idenficiacionEstudiante = trabajoGrado.getEstudiante().getPersona().getIdentificacion();
        //         String nombreEstudiante = trabajoGrado.getEstudiante().getPersona().getNombre();
        //         String apellidoEstudiante = trabajoGrado.getEstudiante().getPersona().getApellido();
        //         String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

        //         GeneracionResolucion responseExamenValoracion = null;
        //         if (generacionResolucionTmp != null) {
        //                 if (generacionResolucionDto.getLinkAnteproyectoAprobado()
        //                                 .compareTo(generacionResolucionTmp.getLinkAnteproyectoAprobado()) != 0) {
        //                         generacionResolucionDto
        //                                         .setLinkAnteproyectoAprobado(FilesUtilities
        //                                                         .guardarArchivoNew(tituloTrabajoGrado, procesoVa,
        //                                                                         generacionResolucionDto
        //                                                                                         .getLinkAnteproyectoAprobado(),
        //                                                                         nombreCarpeta));
        //                         FilesUtilities.deleteFileExample(generacionResolucionTmp.getLinkAnteproyectoAprobado());
        //                 }
        //                 if (generacionResolucionDto.getLinkSolicitudComite()
        //                                 .compareTo(generacionResolucionTmp.getLinkSolicitudComite()) != 0) {
        //                         generacionResolucionDto
        //                                         .setLinkSolicitudComite(FilesUtilities
        //                                                         .guardarArchivoNew(tituloTrabajoGrado, procesoVa,
        //                                                                         generacionResolucionDto
        //                                                                                         .getLinkSolicitudComite(),
        //                                                                         nombreCarpeta));
        //                         FilesUtilities.deleteFileExample(generacionResolucionTmp.getLinkSolicitudComite());
        //                 }
        //                 if (generacionResolucionDto.getLinkSolicitudConcejoFacultad()
        //                                 .compareTo(generacionResolucionTmp.getLinkSolicitudConcejoFacultad()) != 0) {
        //                         generacionResolucionDto
        //                                         .setLinkSolicitudConcejoFacultad(FilesUtilities
        //                                                         .guardarArchivoNew(tituloTrabajoGrado, procesoVa,
        //                                                                         generacionResolucionDto
        //                                                                                         .getLinkSolicitudConcejoFacultad(),
        //                                                                         nombreCarpeta));
        //                         FilesUtilities.deleteFileExample(
        //                                         generacionResolucionTmp.getLinkSolicitudConcejoFacultad());
        //                 }
        //                 if (generacionResolucionDto.getLinkResolucionGeneradaCF()
        //                                 .compareTo(generacionResolucionTmp.getLinkResolucionGeneradaCF()) != 0) {
        //                         generacionResolucionDto
        //                                         .setLinkResolucionGeneradaCF(FilesUtilities
        //                                                         .guardarArchivoNew(tituloTrabajoGrado, procesoVa,
        //                                                                         generacionResolucionDto
        //                                                                                         .getLinkResolucionGeneradaCF(),
        //                                                                         nombreCarpeta));
        //                         FilesUtilities.deleteFileExample(generacionResolucionTmp.getLinkResolucionGeneradaCF());
        //                 }
        //                 // Repetir esto
        //                 updateRtaExamenValoracionValues(generacionResolucionTmp, generacionResolucionDto);
        //                 responseExamenValoracion = generacionResolucionRepository.save(generacionResolucionTmp);
        //         }
        //         return generacionResolucionMapper.toDto(responseExamenValoracion);
        // }



        // // Funciones privadas
        // private void updateRtaExamenValoracionValues(GeneracionResolucion generacionResolucion,
        //                 GeneracionResolucionDto generacionResolucionDto) {
        //         generacionResolucion.setDirector(generacionResolucionDto.getDirector());
        //         generacionResolucion.setCodirector(generacionResolucionDto.getCodirector());
        //         generacionResolucion.setNumeroActaRevision(generacionResolucionDto.getNumeroActaRevision());
        //         generacionResolucion.setFechaActa(generacionResolucionDto.getFechaActa());
        //         generacionResolucion
        //                         .setNumeroResolucionGeneradaCF(
        //                                         generacionResolucionDto.getNumeroResolucionGeneradaCF());
        //         generacionResolucion.setFechaResolucion(generacionResolucionDto.getFechaResolucion());
        //         // Update archivos
        //         generacionResolucion.setLinkAnteproyectoAprobado(generacionResolucionDto.getLinkAnteproyectoAprobado());
        //         generacionResolucion.setLinkSolicitudComite(generacionResolucionDto.getLinkAnteproyectoAprobado());
        //         generacionResolucion.setLinkSolicitudConcejoFacultad(
        //                         generacionResolucionDto.getLinkSolicitudConcejoFacultad());
        //         generacionResolucion.setLinkResolucionGeneradaCF(generacionResolucionDto.getLinkResolucionGeneradaCF());
        // }

        @Override
        @Transactional(readOnly = true)
        public String descargarArchivo(RutaArchivoDto rutaArchivo) {
                return FilesUtilities.recuperarArchivo(rutaArchivo.getRutaArchivo());
        }

        private CamposUnicosGenerarResolucionDto obtenerCamposUnicos(
                        GeneracionResolucionDto respuesta) {
                return informacionUnicaGeneracionResolucion.apply(respuesta);
        }

        private Map<String, String> validacionCampoUnicos(CamposUnicosGenerarResolucionDto camposUnicos,
                        CamposUnicosGenerarResolucionDto camposUnicosBD) {

                Map<String, Function<CamposUnicosGenerarResolucionDto, Boolean>> mapCamposUnicos = new HashMap<>();

                mapCamposUnicos.put("idTrabajoGrados",
                                dto -> (camposUnicosBD == null || !dto.getIdTrabajoGrados()
                                                .equals(camposUnicosBD.getIdTrabajoGrados()))
                                                && generacionResolucionRepository
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
                return "Campo único, ya se ha registrado una GENERACION DE RESOLUCION al trabajo de grado: "
                                + valorCampo;
        }

}
