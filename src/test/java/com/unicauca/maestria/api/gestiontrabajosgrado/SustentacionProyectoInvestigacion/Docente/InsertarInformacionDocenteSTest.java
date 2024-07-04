package com.unicauca.maestria.api.gestiontrabajosgrado.SustentacionProyectoInvestigacion.Docente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientEgresados;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientExpertos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.TipoIdentificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.SustentacionTrabajoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.GeneracionResolucionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.GeneracionResolucionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.GeneracionResolucionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaComiteGeneracionResolucionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaComiteSustentacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SustentacionProyectoInvestigacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.generacion_resolucion.GeneracionResolucionServiceImpl;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class InsertarInformacionDocenteSTest {

        @Mock
        private SustentacionProyectoInvestigacionRepository sustentacionProyectoInvestigacionRepository;
        @Mock
        private SustentacionProyectoInvestigacionMapper sustentacionProyectoIngestigacionMapper;
        @Mock
        private SustentacionProyectoInvestigacionResponseMapper sustentacionProyectoInvestigacionResponseMapper;
        @Mock
        private RespuestaComiteSustentacionRepository respuestaComiteSustentacionRepository;
        @Mock
        private TrabajoGradoRepository trabajoGradoRepository;
        @Mock
        private ArchivoClient archivoClient;
        @Mock
        private ArchivoClientExpertos archivoClientExpertos;
        @Mock
        private ArchivoClientEgresados archivoClientEgresados;
        @Mock
        private BindingResult result;
        @Mock
        private EnvioCorreos envioCorreos;
        @InjectMocks
        private SustentacionProyectoInvestigacionServiceImpl sustentacionProyectoInvestigacionServiceImpl;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                sustentacionProyectoInvestigacionServiceImpl = new SustentacionProyectoInvestigacionServiceImpl(
                                sustentacionProyectoInvestigacionRepository,
                                sustentacionProyectoIngestigacionMapper,
                                sustentacionProyectoInvestigacionResponseMapper,
                                respuestaComiteSustentacionRepository,
                                trabajoGradoRepository,
                                archivoClient,
                                archivoClientExpertos,
                                archivoClientEgresados);
                ReflectionTestUtils.setField(sustentacionProyectoInvestigacionServiceImpl, "envioCorreos",
                                envioCorreos);
        }

        @Test
        void SustentacionProyectoInvestigacionServiceImpltTest_RegistroExitoso() {

                Long idTrabajoGrado = 1L;

                SustentacionTrabajoInvestigacionDocenteDto sustentacionTrabajoInvestigacionDocenteDto = new SustentacionTrabajoInvestigacionDocenteDto();
                sustentacionTrabajoInvestigacionDocenteDto.setLinkFormatoF("linkFormatoF.txt-cHJ1ZWJhIGRlIHRleHR");
                sustentacionTrabajoInvestigacionDocenteDto.setUrlDocumentacion("www.google.com");
                sustentacionTrabajoInvestigacionDocenteDto.setIdJuradoInterno(1L);
                sustentacionTrabajoInvestigacionDocenteDto.setIdJuradoExterno(1L);

                when(result.hasErrors()).thenReturn(false);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(7);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacion = new SustentacionTrabajoInvestigacion();
                sustentacionTrabajoInvestigacion.setIdSustentacionTrabajoInvestigacion(1L);
                sustentacionTrabajoInvestigacion
                                .setLinkFormatoF(sustentacionTrabajoInvestigacionDocenteDto.getLinkFormatoF());
                sustentacionTrabajoInvestigacion
                                .setUrlDocumentacion(sustentacionTrabajoInvestigacionDocenteDto.getUrlDocumentacion());
                sustentacionTrabajoInvestigacion
                                .setIdJuradoInterno(sustentacionTrabajoInvestigacionDocenteDto.getIdJuradoInterno());
                sustentacionTrabajoInvestigacion
                                .setIdJuradoExterno(sustentacionTrabajoInvestigacionDocenteDto.getIdJuradoExterno());

                when(sustentacionProyectoIngestigacionMapper.toEntity(sustentacionTrabajoInvestigacionDocenteDto))
                                .thenReturn(sustentacionTrabajoInvestigacion);

                PersonaDto PersonaEstudianteDto = new PersonaDto();
                PersonaEstudianteDto.setIdentificacion(123L);
                PersonaEstudianteDto.setNombre("Juan");
                PersonaEstudianteDto.setApellido("Meneses");

                EstudianteResponseDtoAll estudianteResponseDtoAll = new EstudianteResponseDtoAll();
                estudianteResponseDtoAll.setPersona(PersonaEstudianteDto);

                when(archivoClient.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante()))
                                .thenReturn(estudianteResponseDtoAll);

                when(sustentacionProyectoInvestigacionRepository.save(any(SustentacionTrabajoInvestigacion.class)))
                                .thenReturn(sustentacionTrabajoInvestigacion);

                SustentacionTrabajoInvestigacionDocenteResponseDto sustentacionTrabajoInvestigacionDocenteResponseDto = new SustentacionTrabajoInvestigacionDocenteResponseDto();
                sustentacionTrabajoInvestigacionDocenteResponseDto.setIdSustentacionTrabajoInvestigacion(1L);
                sustentacionTrabajoInvestigacionDocenteResponseDto
                                .setLinkFormatoF("./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-linkFormatoF.txt");
                sustentacionTrabajoInvestigacionDocenteResponseDto
                                .setUrlDocumentacion(sustentacionTrabajoInvestigacionDocenteDto.getUrlDocumentacion());
                sustentacionTrabajoInvestigacionDocenteResponseDto
                                .setIdJuradoInterno(sustentacionTrabajoInvestigacionDocenteDto.getIdJuradoInterno());
                sustentacionTrabajoInvestigacionDocenteResponseDto
                                .setIdJuradoExterno(sustentacionTrabajoInvestigacionDocenteDto.getIdJuradoExterno());

                when(sustentacionProyectoInvestigacionResponseMapper.toDocenteDto(sustentacionTrabajoInvestigacion))
                                .thenReturn(sustentacionTrabajoInvestigacionDocenteResponseDto);

                try (MockedStatic<FilesUtilities> utilities = mockStatic(FilesUtilities.class)) {
                        utilities.when(() -> FilesUtilities.guardarArchivoNew2(anyString(), anyString()))
                                        .thenReturn("path/to/new/file");

                        SustentacionTrabajoInvestigacionDocenteResponseDto resultado = sustentacionProyectoInvestigacionServiceImpl
                                        .insertarInformacionDocente(idTrabajoGrado,
                                                        sustentacionTrabajoInvestigacionDocenteDto, result);

                        assertNotNull(resultado);
                        assertEquals(1L, resultado.getIdSustentacionTrabajoInvestigacion());
                        assertEquals("./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-linkFormatoF.txt",
                                        resultado.getLinkFormatoF());
                        assertEquals("www.google.com", resultado.getUrlDocumentacion());
                        assertEquals(1, resultado.getIdJuradoInterno());
                        assertEquals(1, resultado.getIdJuradoExterno());
                }

        }

}
