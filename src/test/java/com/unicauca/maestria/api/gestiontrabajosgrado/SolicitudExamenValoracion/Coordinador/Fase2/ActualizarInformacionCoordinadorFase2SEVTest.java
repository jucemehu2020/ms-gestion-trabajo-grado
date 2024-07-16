package com.unicauca.maestria.api.gestiontrabajosgrado.SolicitudExamenValoracion.Coordinador.Fase2;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientExpertos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.Concepto;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.TiemposPendientes;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.RespuestaComiteExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.EnvioEmailDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.InformacionEnvioEvaluadorDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.RespuestaComiteExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.SolicitudExamenValoracionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.SolicitudExamenValoracionCoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.AnexoSolicitudExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaComiteSolicitudRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TiemposPendientesRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion.SolicitudExamenValoracionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ActualizarInformacionCoordinadorFase2SEVTest {

        @Mock
        private SolicitudExamenValoracionRepository solicitudExamenValoracionRepository;
        @Mock
        private RespuestaComiteSolicitudRepository respuestaComiteSolicitudRepository;
        @Mock
        private TrabajoGradoRepository trabajoGradoRepository;
        @Mock
        private TiemposPendientesRepository tiemposPendientesRepository;
        @Mock
        private ArchivoClient archivoClient;
        @Mock
        private ArchivoClientExpertos archivoClientExpertos;
        @Mock
        private SolicitudExamenValoracionMapper examenValoracionMapper;
        @Mock
        private SolicitudExamenValoracionResponseMapper examenValoracionResponseMapper;
        @Mock
        private AnexoSolicitudExamenValoracionMapper anexoSolicitudExamenValoracionMapper;
        @Mock
        private BindingResult result;

        @Mock
        private EnvioCorreos envioCorreos;

        @InjectMocks
        private SolicitudExamenValoracionServiceImpl solicitudExamenValoracionService;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                solicitudExamenValoracionService = new SolicitudExamenValoracionServiceImpl(
                                solicitudExamenValoracionRepository,
                                respuestaComiteSolicitudRepository,
                                null,
                                trabajoGradoRepository,
                                tiemposPendientesRepository,
                                examenValoracionMapper,
                                examenValoracionResponseMapper,
                                anexoSolicitudExamenValoracionMapper,
                                archivoClient,
                                archivoClientExpertos);
                ReflectionTestUtils.setField(solicitudExamenValoracionService, "envioCorreos", envioCorreos);
        }

        @Test
        void ActualizarInformacionCoordinadorFase2SEVTest_ActualizacionExitosaModificacionTrue() {

                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                RespuestaComiteExamenValoracionDto respuestaComiteExamenValoracionDto = new RespuestaComiteExamenValoracionDto();
                respuestaComiteExamenValoracionDto.setConceptoComite(Concepto.APROBADO);
                respuestaComiteExamenValoracionDto.setNumeroActa("AX1-3445");
                respuestaComiteExamenValoracionDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

                List<RespuestaComiteExamenValoracionDto> listaRespuestaComite = new ArrayList();
                listaRespuestaComite.add(respuestaComiteExamenValoracionDto);

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Envio evaluadores");
                envioEmailDto.setMensaje("Envio documentos para que por favor los revisen y den respuesta oportuna");

                ArrayList<String> anexos = new ArrayList<>();
                anexos.add("cHJ1ZWJhIGRlIHRleHR");
                anexos.add("cHJ1ZWJhIGRlIHRleHR");

                InformacionEnvioEvaluadorDto informacionEnvioEvaluadorDto = new InformacionEnvioEvaluadorDto();
                informacionEnvioEvaluadorDto.setB64FormatoD("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioEvaluadorDto.setB64FormatoE("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioEvaluadorDto.setB64Anexos(anexos);
                informacionEnvioEvaluadorDto.setB64Oficio("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioEvaluadorDto.setB64FormatoB("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioEvaluadorDto.setB64FormatoC("cHJ1ZWJhIGRlIHRleHR");

                SolicitudExamenValoracionCoordinadorFase2Dto solicitudExamenValoracionCoordinadorFase2Dto = new SolicitudExamenValoracionCoordinadorFase2Dto();
                solicitudExamenValoracionCoordinadorFase2Dto.setActaFechaRespuestaComite(listaRespuestaComite);
                solicitudExamenValoracionCoordinadorFase2Dto
                                .setLinkOficioDirigidoEvaluadores("oficio.txt-cHJ1ZWJhIGRlIHRleHR");
                solicitudExamenValoracionCoordinadorFase2Dto
                                .setFechaMaximaEvaluacion(LocalDate.parse("2023-05-29", formatter));
                solicitudExamenValoracionCoordinadorFase2Dto.setEnvioEmailDto(envioEmailDto);
                solicitudExamenValoracionCoordinadorFase2Dto.setInformacionEnvioEvaluador(informacionEnvioEvaluadorDto);

                when(result.hasErrors()).thenReturn(false);

                RespuestaComiteExamenValoracion respuestaComiteExamenValoracionOld = new RespuestaComiteExamenValoracion();
                respuestaComiteExamenValoracionOld.setId(idTrabajoGrado);
                respuestaComiteExamenValoracionOld.setConceptoComite(Concepto.NO_APROBADO);
                respuestaComiteExamenValoracionOld.setNumeroActa("AX1-3445");
                respuestaComiteExamenValoracionOld.setFechaActa(LocalDate.parse("2023-05-24", formatter));

                List<RespuestaComiteExamenValoracion> listaRespuestaComiteExamenValoracionOld = new ArrayList<>();
                listaRespuestaComiteExamenValoracionOld.add(respuestaComiteExamenValoracionOld);

                SolicitudExamenValoracion solicitudExamenValoracionOld = new SolicitudExamenValoracion();
                solicitudExamenValoracionOld.setId(1L);
                solicitudExamenValoracionOld.setIdEvaluadorInterno(idTrabajoGrado);
                solicitudExamenValoracionOld.setIdEvaluadorExterno(idTrabajoGrado);
                solicitudExamenValoracionOld.setActaFechaRespuestaComite(listaRespuestaComiteExamenValoracionOld);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(4);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setSolicitudExamenValoracion(solicitudExamenValoracionOld);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                when(solicitudExamenValoracionRepository
                                .findById(trabajoGrado.getSolicitudExamenValoracion().getId()))
                                .thenReturn(Optional.of(solicitudExamenValoracionOld));

                when(solicitudExamenValoracionRepository
                                .findRespuestaComiteBySolicitudExamenValoracionId(
                                                solicitudExamenValoracionOld.getId()))
                                .thenReturn(listaRespuestaComiteExamenValoracionOld);

                PersonaDto personaDocenteDto = new PersonaDto();
                personaDocenteDto.getCorreoElectronico();

                DocenteResponseDto docenteResponseDto = new DocenteResponseDto();
                docenteResponseDto.setPersona(personaDocenteDto);

                when(archivoClient.obtenerDocentePorId(solicitudExamenValoracionOld.getIdEvaluadorInterno()))
                                .thenReturn(docenteResponseDto);

                PersonaDto personaExpertoDto = new PersonaDto();
                personaExpertoDto.getCorreoElectronico();

                ExpertoResponseDto expertoResponseDto = new ExpertoResponseDto();
                expertoResponseDto.setPersona(personaExpertoDto);

                when(archivoClientExpertos.obtenerExpertoPorId(solicitudExamenValoracionOld.getIdEvaluadorExterno()))
                                .thenReturn(expertoResponseDto);

                when(envioCorreos.enviarCorreoConAnexos(any(ArrayList.class), anyString(), anyString(), anyMap()))
                                .thenReturn(true);

                PersonaDto PersonaEstudianteDto = new PersonaDto();
                PersonaEstudianteDto.setIdentificacion(123L);
                PersonaEstudianteDto.setNombre("Juan");
                PersonaEstudianteDto.setApellido("Meneses");

                EstudianteResponseDtoAll estudianteResponseDtoAll = new EstudianteResponseDtoAll();
                estudianteResponseDtoAll.setPersona(PersonaEstudianteDto);

                when(archivoClient.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante()))
                                .thenReturn(estudianteResponseDtoAll);

                try (MockedStatic<FilesUtilities> utilities = mockStatic(FilesUtilities.class)) {
                        utilities.when(() -> FilesUtilities.guardarArchivoNew2(anyString(), anyString()))
                                        .thenReturn("path/to/new/file");
                }

                RespuestaComiteExamenValoracion respuestaComite = RespuestaComiteExamenValoracion.builder()
                                .conceptoComite(solicitudExamenValoracionCoordinadorFase2Dto
                                                .getActaFechaRespuestaComite().get(0)
                                                .getConceptoComite())
                                .numeroActa(solicitudExamenValoracionCoordinadorFase2Dto.getActaFechaRespuestaComite()
                                                .get(0)
                                                .getNumeroActa())
                                .fechaActa(solicitudExamenValoracionCoordinadorFase2Dto.getActaFechaRespuestaComite()
                                                .get(0)
                                                .getFechaActa())
                                .solicitudExamenValoracion(solicitudExamenValoracionOld)
                                .build();

                List<RespuestaComiteExamenValoracion> listaRespuestaComiteExamenValoracion = new ArrayList<>();
                listaRespuestaComiteExamenValoracion.add(respuestaComite);

                SolicitudExamenValoracion solicitudExamenValoracion = new SolicitudExamenValoracion();
                solicitudExamenValoracion.setActaFechaRespuestaComite(listaRespuestaComiteExamenValoracion);
                solicitudExamenValoracion.setLinkOficioDirigidoEvaluadores(
                                solicitudExamenValoracionCoordinadorFase2Dto.getLinkOficioDirigidoEvaluadores());
                solicitudExamenValoracion
                                .setFechaMaximaEvaluacion(solicitudExamenValoracionCoordinadorFase2Dto
                                                .getFechaMaximaEvaluacion());

                when(respuestaComiteSolicitudRepository.findFirstByOrderByIdDesc())
                                .thenReturn(respuestaComite);

                when(tiemposPendientesRepository.save(any(TiemposPendientes.class)))
                                .thenReturn(new TiemposPendientes());

                when(solicitudExamenValoracionRepository.save(any(SolicitudExamenValoracion.class)))
                                .thenReturn(solicitudExamenValoracion);

                SolicitudExamenValoracionCoordinadorFase2ResponseDto solicitudExamenValoracionCoordinadorResponseDto = new SolicitudExamenValoracionCoordinadorFase2ResponseDto();
                solicitudExamenValoracionCoordinadorResponseDto
                                .setId(solicitudExamenValoracionOld.getId());
                solicitudExamenValoracionCoordinadorResponseDto.setActaFechaRespuestaComite(listaRespuestaComite);
                solicitudExamenValoracionCoordinadorResponseDto.setLinkOficioDirigidoEvaluadores(
                                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/30-06-24/20240630181114-oficio.txt");
                solicitudExamenValoracionCoordinadorResponseDto
                                .setFechaMaximaEvaluacion(solicitudExamenValoracionCoordinadorFase2Dto
                                                .getFechaMaximaEvaluacion());

                when(examenValoracionResponseMapper.toCoordinadorFase2Dto(solicitudExamenValoracion))
                                .thenReturn(solicitudExamenValoracionCoordinadorResponseDto);

                SolicitudExamenValoracionCoordinadorFase2ResponseDto resultado = solicitudExamenValoracionService
                                .actualizarInformacionCoordinadorFase2(idTrabajoGrado,
                                                solicitudExamenValoracionCoordinadorFase2Dto,
                                                result);

                assertNotNull(resultado);
                assertEquals(1L, resultado.getId());
                assertEquals(Concepto.APROBADO, resultado.getActaFechaRespuestaComite().get(0).getConceptoComite());
                assertEquals("AX1-3445", resultado.getActaFechaRespuestaComite().get(0).getNumeroActa());
                assertEquals(LocalDate.parse("2023-05-24", formatter),
                                resultado.getActaFechaRespuestaComite().get(0).getFechaActa());
                assertEquals("./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/30-06-24/20240630181114-oficio.txt",
                                resultado.getLinkOficioDirigidoEvaluadores());
                assertEquals(LocalDate.parse("2023-05-29", formatter), resultado.getFechaMaximaEvaluacion());

        }

        @Test
        void ActualizarInformacionCoordinadorFase2SEVTest_ActualizacionExitosaModificacionFalse() {

                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                RespuestaComiteExamenValoracionDto respuestaComiteExamenValoracionDto = new RespuestaComiteExamenValoracionDto();
                respuestaComiteExamenValoracionDto.setConceptoComite(Concepto.NO_APROBADO);
                respuestaComiteExamenValoracionDto.setNumeroActa("AX1-3445");
                respuestaComiteExamenValoracionDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

                List<RespuestaComiteExamenValoracionDto> listaRespuestaComite = new ArrayList();
                listaRespuestaComite.add(respuestaComiteExamenValoracionDto);

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Envio evaluadores");
                envioEmailDto.setMensaje("Envio documentos para que por favor los revisen y den respuesta oportuna");

                SolicitudExamenValoracionCoordinadorFase2Dto solicitudExamenValoracionCoordinadorFase2Dto = new SolicitudExamenValoracionCoordinadorFase2Dto();
                solicitudExamenValoracionCoordinadorFase2Dto.setActaFechaRespuestaComite(listaRespuestaComite);
                solicitudExamenValoracionCoordinadorFase2Dto.setEnvioEmailDto(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                RespuestaComiteExamenValoracion respuestaComiteExamenValoracionOld = new RespuestaComiteExamenValoracion();
                respuestaComiteExamenValoracionOld.setId(idTrabajoGrado);
                respuestaComiteExamenValoracionOld.setConceptoComite(Concepto.APROBADO);
                respuestaComiteExamenValoracionOld.setNumeroActa("AX1-3445");
                respuestaComiteExamenValoracionOld.setFechaActa(LocalDate.parse("2023-05-24", formatter));

                List<RespuestaComiteExamenValoracion> listaRespuestaComiteExamenValoracionOld = new ArrayList<>();
                listaRespuestaComiteExamenValoracionOld.add(respuestaComiteExamenValoracionOld);

                SolicitudExamenValoracion solicitudExamenValoracionOld = new SolicitudExamenValoracion();
                solicitudExamenValoracionOld.setId(1L);
                solicitudExamenValoracionOld.setIdEvaluadorInterno(idTrabajoGrado);
                solicitudExamenValoracionOld.setIdEvaluadorExterno(idTrabajoGrado);
                solicitudExamenValoracionOld.setActaFechaRespuestaComite(listaRespuestaComiteExamenValoracionOld);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(4);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setSolicitudExamenValoracion(solicitudExamenValoracionOld);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                when(solicitudExamenValoracionRepository
                                .findById(trabajoGrado.getSolicitudExamenValoracion().getId()))
                                .thenReturn(Optional.of(solicitudExamenValoracionOld));

                when(solicitudExamenValoracionRepository
                                .findRespuestaComiteBySolicitudExamenValoracionId(
                                                solicitudExamenValoracionOld.getId()))
                                .thenReturn(listaRespuestaComiteExamenValoracionOld);

                PersonaDto PersonaEstudianteDto = new PersonaDto();
                PersonaEstudianteDto.setIdentificacion(123L);
                PersonaEstudianteDto.setNombre("Juan");
                PersonaEstudianteDto.setApellido("Meneses");

                EstudianteResponseDtoAll estudianteResponseDtoAll = new EstudianteResponseDtoAll();
                estudianteResponseDtoAll.setPersona(PersonaEstudianteDto);

                when(archivoClient.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante()))
                                .thenReturn(estudianteResponseDtoAll);

                when(envioCorreos.enviarCorreosCorrecion(any(ArrayList.class), anyString(), anyString()))
                                .thenReturn(true);

                RespuestaComiteExamenValoracion respuestaComite = RespuestaComiteExamenValoracion.builder()
                                .conceptoComite(solicitudExamenValoracionCoordinadorFase2Dto
                                                .getActaFechaRespuestaComite().get(0)
                                                .getConceptoComite())
                                .numeroActa(solicitudExamenValoracionCoordinadorFase2Dto.getActaFechaRespuestaComite()
                                                .get(0)
                                                .getNumeroActa())
                                .fechaActa(solicitudExamenValoracionCoordinadorFase2Dto.getActaFechaRespuestaComite()
                                                .get(0)
                                                .getFechaActa())
                                .solicitudExamenValoracion(solicitudExamenValoracionOld)
                                .build();

                List<RespuestaComiteExamenValoracion> listaRespuestaComiteExamenValoracion = new ArrayList<>();
                listaRespuestaComiteExamenValoracion.add(respuestaComite);

                SolicitudExamenValoracion solicitudExamenValoracion = new SolicitudExamenValoracion();
                solicitudExamenValoracion.setActaFechaRespuestaComite(listaRespuestaComiteExamenValoracion);

                when(respuestaComiteSolicitudRepository.findFirstByOrderByIdDesc())
                                .thenReturn(respuestaComite);

                when(solicitudExamenValoracionRepository.save(any(SolicitudExamenValoracion.class)))
                                .thenReturn(solicitudExamenValoracion);

                SolicitudExamenValoracionCoordinadorFase2ResponseDto solicitudExamenValoracionCoordinadorResponseDto = new SolicitudExamenValoracionCoordinadorFase2ResponseDto();
                solicitudExamenValoracionCoordinadorResponseDto
                                .setId(solicitudExamenValoracionOld.getId());
                solicitudExamenValoracionCoordinadorResponseDto.setActaFechaRespuestaComite(listaRespuestaComite);

                when(examenValoracionResponseMapper.toCoordinadorFase2Dto(solicitudExamenValoracion))
                                .thenReturn(solicitudExamenValoracionCoordinadorResponseDto);

                try (MockedStatic<FilesUtilities> utilities = mockStatic(FilesUtilities.class)) {
                        utilities.when(() -> FilesUtilities.deleteFileExample(anyString()))
                                        .thenReturn(true);

                        SolicitudExamenValoracionCoordinadorFase2ResponseDto resultado = solicitudExamenValoracionService
                                        .actualizarInformacionCoordinadorFase2(idTrabajoGrado,
                                                        solicitudExamenValoracionCoordinadorFase2Dto,
                                                        result);

                        assertNotNull(resultado);
                        assertEquals(1L, resultado.getId());
                        assertEquals(Concepto.NO_APROBADO, resultado.getActaFechaRespuestaComite().get(0).getConceptoComite());
                        assertEquals("AX1-3445", resultado.getActaFechaRespuestaComite().get(0).getNumeroActa());
                        assertEquals(LocalDate.parse("2023-05-24", formatter),
                                        resultado.getActaFechaRespuestaComite().get(0).getFechaActa());
                        assertEquals(null, resultado.getLinkOficioDirigidoEvaluadores());
                        assertEquals(null, resultado.getFechaMaximaEvaluacion());
                }

        }

        @Test
        void ActualizarInformacionCoordinadorFase2SEVTest_ActualizacionFalloPorFalse() {

                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                RespuestaComiteExamenValoracionDto respuestaComiteExamenValoracionDto = new RespuestaComiteExamenValoracionDto();
                respuestaComiteExamenValoracionDto.setConceptoComite(Concepto.NO_APROBADO);
                respuestaComiteExamenValoracionDto.setNumeroActa("AX1-3445");
                respuestaComiteExamenValoracionDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

                List<RespuestaComiteExamenValoracionDto> listaRespuestaComite = new ArrayList();
                listaRespuestaComite.add(respuestaComiteExamenValoracionDto);

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Envio evaluadores");
                envioEmailDto.setMensaje("Envio documentos para que por favor los revisen y den respuesta oportuna");

                ArrayList<String> anexos = new ArrayList<>();
                anexos.add("cHJ1ZWJhIGRlIHRleHR");
                anexos.add("cHJ1ZWJhIGRlIHRleHR");

                InformacionEnvioEvaluadorDto informacionEnvioEvaluadorDto = new InformacionEnvioEvaluadorDto();
                informacionEnvioEvaluadorDto.setB64FormatoD("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioEvaluadorDto.setB64FormatoE("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioEvaluadorDto.setB64Anexos(anexos);
                informacionEnvioEvaluadorDto.setB64Oficio("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioEvaluadorDto.setB64FormatoB("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioEvaluadorDto.setB64FormatoC("cHJ1ZWJhIGRlIHRleHR");

                SolicitudExamenValoracionCoordinadorFase2Dto solicitudExamenValoracionCoordinadorFase2Dto = new SolicitudExamenValoracionCoordinadorFase2Dto();
                solicitudExamenValoracionCoordinadorFase2Dto.setActaFechaRespuestaComite(listaRespuestaComite);
                solicitudExamenValoracionCoordinadorFase2Dto
                                .setLinkOficioDirigidoEvaluadores("oficio.txt-cHJ1ZWJhIGRlIHRleHR");
                solicitudExamenValoracionCoordinadorFase2Dto
                                .setFechaMaximaEvaluacion(LocalDate.parse("2023-05-29", formatter));
                solicitudExamenValoracionCoordinadorFase2Dto.setEnvioEmailDto(envioEmailDto);
                solicitudExamenValoracionCoordinadorFase2Dto.setInformacionEnvioEvaluador(informacionEnvioEvaluadorDto);

                when(result.hasErrors()).thenReturn(false);

                InformationException exception = assertThrows(InformationException.class, () -> {
                        solicitudExamenValoracionService.actualizarInformacionCoordinadorFase2(idTrabajoGrado,
                                        solicitudExamenValoracionCoordinadorFase2Dto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Envio de atributos no permitido";

                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void ActualizarInformacionCoordinadorFase2SEVTest_FaltanAtributos() {

                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                RespuestaComiteExamenValoracionDto respuestaComiteExamenValoracionDto = new RespuestaComiteExamenValoracionDto();
                respuestaComiteExamenValoracionDto.setConceptoComite(Concepto.APROBADO);
                respuestaComiteExamenValoracionDto.setNumeroActa("AX1-3445");
                respuestaComiteExamenValoracionDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

                List<RespuestaComiteExamenValoracionDto> listaRespuestaComite = new ArrayList();
                listaRespuestaComite.add(respuestaComiteExamenValoracionDto);

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Envio evaluadores");
                envioEmailDto.setMensaje("Envio documentos para que por favor los revisen y den respuesta oportuna");

                ArrayList<String> anexos = new ArrayList<>();
                anexos.add("cHJ1ZWJhIGRlIHRleHR");
                anexos.add("cHJ1ZWJhIGRlIHRleHR");

                InformacionEnvioEvaluadorDto informacionEnvioEvaluadorDto = new InformacionEnvioEvaluadorDto();
                informacionEnvioEvaluadorDto.setB64FormatoD("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioEvaluadorDto.setB64FormatoE("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioEvaluadorDto.setB64Anexos(anexos);
                informacionEnvioEvaluadorDto.setB64Oficio("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioEvaluadorDto.setB64FormatoB("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioEvaluadorDto.setB64FormatoC("cHJ1ZWJhIGRlIHRleHR");

                SolicitudExamenValoracionCoordinadorFase2Dto solicitudExamenValoracionCoordinadorFase2Dto = new SolicitudExamenValoracionCoordinadorFase2Dto();
                solicitudExamenValoracionCoordinadorFase2Dto.setActaFechaRespuestaComite(listaRespuestaComite);
                solicitudExamenValoracionCoordinadorFase2Dto
                                .setLinkOficioDirigidoEvaluadores("oficio.txt-cHJ1ZWJhIGRlIHRleHR");
                solicitudExamenValoracionCoordinadorFase2Dto
                                .setFechaMaximaEvaluacion(LocalDate.parse("2023-05-29", formatter));
                solicitudExamenValoracionCoordinadorFase2Dto.setEnvioEmailDto(envioEmailDto);
                solicitudExamenValoracionCoordinadorFase2Dto.setInformacionEnvioEvaluador(informacionEnvioEvaluadorDto);

                FieldError fieldError = new FieldError("SolicitudExamenValoracionCoordinadorResponseDto",
                                "actaFechaRespuestaComite[0].numeroActa",
                                "no debe ser nulo");
                when(result.hasErrors()).thenReturn(true);
                when(result.getFieldErrors()).thenReturn(List.of(fieldError));

                FieldErrorException exception = assertThrows(FieldErrorException.class, () -> {
                        solicitudExamenValoracionService.actualizarInformacionCoordinadorFase2(idTrabajoGrado,
                                        solicitudExamenValoracionCoordinadorFase2Dto,
                                        result);
                });

                assertNotNull(exception.getResult());
                List<FieldError> fieldErrors = exception.getResult().getFieldErrors();
                assertFalse(fieldErrors.isEmpty());
                String actualMessage = "El campo " + fieldErrors.get(0).getField() + ", "
                                + fieldError.getDefaultMessage();
                String expectedMessage = "El campo actaFechaRespuestaComite[0].numeroActa, no debe ser nulo";
                assertTrue(actualMessage.contains(expectedMessage));
        }

        @Test
        void ActualizarInformacionCoordinadorFase2SEVTest_EstadoNoValido() {

                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                RespuestaComiteExamenValoracionDto respuestaComiteExamenValoracionDto = new RespuestaComiteExamenValoracionDto();
                respuestaComiteExamenValoracionDto.setConceptoComite(Concepto.APROBADO);
                respuestaComiteExamenValoracionDto.setNumeroActa("AX1-3445");
                respuestaComiteExamenValoracionDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

                List<RespuestaComiteExamenValoracionDto> listaRespuestaComite = new ArrayList();
                listaRespuestaComite.add(respuestaComiteExamenValoracionDto);

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Envio evaluadores");
                envioEmailDto.setMensaje("Envio documentos para que por favor los revisen y den respuesta oportuna");

                ArrayList<String> anexos = new ArrayList<>();
                anexos.add("cHJ1ZWJhIGRlIHRleHR");
                anexos.add("cHJ1ZWJhIGRlIHRleHR");

                InformacionEnvioEvaluadorDto informacionEnvioEvaluadorDto = new InformacionEnvioEvaluadorDto();
                informacionEnvioEvaluadorDto.setB64FormatoD("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioEvaluadorDto.setB64FormatoE("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioEvaluadorDto.setB64Anexos(anexos);
                informacionEnvioEvaluadorDto.setB64Oficio("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioEvaluadorDto.setB64FormatoB("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioEvaluadorDto.setB64FormatoC("cHJ1ZWJhIGRlIHRleHR");

                SolicitudExamenValoracionCoordinadorFase2Dto solicitudExamenValoracionCoordinadorFase2Dto = new SolicitudExamenValoracionCoordinadorFase2Dto();
                solicitudExamenValoracionCoordinadorFase2Dto.setActaFechaRespuestaComite(listaRespuestaComite);
                solicitudExamenValoracionCoordinadorFase2Dto
                                .setLinkOficioDirigidoEvaluadores("oficio.txt-cHJ1ZWJhIGRlIHRleHR");
                solicitudExamenValoracionCoordinadorFase2Dto
                                .setFechaMaximaEvaluacion(LocalDate.parse("2023-05-29", formatter));
                solicitudExamenValoracionCoordinadorFase2Dto.setEnvioEmailDto(envioEmailDto);
                solicitudExamenValoracionCoordinadorFase2Dto.setInformacionEnvioEvaluador(informacionEnvioEvaluadorDto);

                when(result.hasErrors()).thenReturn(false);

                SolicitudExamenValoracion solicitudExamenValoracionOld = new SolicitudExamenValoracion();
                solicitudExamenValoracionOld.setId(1L);
                solicitudExamenValoracionOld.setIdEvaluadorInterno(idTrabajoGrado);
                solicitudExamenValoracionOld.setIdEvaluadorExterno(idTrabajoGrado);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(2);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setSolicitudExamenValoracion(solicitudExamenValoracionOld);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                InformationException exception = assertThrows(InformationException.class, () -> {
                        solicitudExamenValoracionService.actualizarInformacionCoordinadorFase2(idTrabajoGrado,
                                        solicitudExamenValoracionCoordinadorFase2Dto,
                                        result);
                });

                String expectedMessage = "No es permitido registrar la informacion";
                assertNotNull(exception.getMessage());
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void ActualizarInformacionCoordinadorFase2SEVTest_TrabajoGradoNoExiste() {

                Long idTrabajoGrado = 2L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                RespuestaComiteExamenValoracionDto respuestaComiteExamenValoracionDto = new RespuestaComiteExamenValoracionDto();
                respuestaComiteExamenValoracionDto.setConceptoComite(Concepto.APROBADO);
                respuestaComiteExamenValoracionDto.setNumeroActa("AX1-3445");
                respuestaComiteExamenValoracionDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

                List<RespuestaComiteExamenValoracionDto> listaRespuestaComite = new ArrayList();
                listaRespuestaComite.add(respuestaComiteExamenValoracionDto);

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Envio evaluadores");
                envioEmailDto.setMensaje("Envio documentos para que por favor los revisen y den respuesta oportuna");

                ArrayList<String> anexos = new ArrayList<>();
                anexos.add("cHJ1ZWJhIGRlIHRleHR");
                anexos.add("cHJ1ZWJhIGRlIHRleHR");

                InformacionEnvioEvaluadorDto informacionEnvioEvaluadorDto = new InformacionEnvioEvaluadorDto();
                informacionEnvioEvaluadorDto.setB64FormatoD("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioEvaluadorDto.setB64FormatoE("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioEvaluadorDto.setB64Anexos(anexos);
                informacionEnvioEvaluadorDto.setB64Oficio("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioEvaluadorDto.setB64FormatoB("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioEvaluadorDto.setB64FormatoC("cHJ1ZWJhIGRlIHRleHR");

                SolicitudExamenValoracionCoordinadorFase2Dto solicitudExamenValoracionCoordinadorFase2Dto = new SolicitudExamenValoracionCoordinadorFase2Dto();
                solicitudExamenValoracionCoordinadorFase2Dto.setActaFechaRespuestaComite(listaRespuestaComite);
                solicitudExamenValoracionCoordinadorFase2Dto
                                .setLinkOficioDirigidoEvaluadores("oficio.txt-cHJ1ZWJhIGRlIHRleHR");
                solicitudExamenValoracionCoordinadorFase2Dto
                                .setFechaMaximaEvaluacion(LocalDate.parse("2023-05-29", formatter));
                solicitudExamenValoracionCoordinadorFase2Dto.setEnvioEmailDto(envioEmailDto);
                solicitudExamenValoracionCoordinadorFase2Dto.setInformacionEnvioEvaluador(informacionEnvioEvaluadorDto);

                when(result.hasErrors()).thenReturn(false);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        solicitudExamenValoracionService.actualizarInformacionCoordinadorFase2(idTrabajoGrado,
                                        solicitudExamenValoracionCoordinadorFase2Dto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Trabajo de grado con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }
}
