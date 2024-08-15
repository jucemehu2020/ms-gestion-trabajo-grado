package com.unicauca.maestria.api.gestiontrabajosgrado.SustentacionProyectoInvestigacion.Estudiante;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientEgresados;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.egresado.EmpresaSaveDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
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
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class InsertarInformacionEstudianteSTest {

        @Mock
        private SustentacionProyectoInvestigacionRepository sustentacionProyectoInvestigacionRepository;
        @Mock
        private SustentacionProyectoInvestigacionMapper sustentacionProyectoIngestigacionMapper;
        @Mock
        private SustentacionProyectoInvestigacionResponseMapper sustentacionProyectoInvestigacionResponseMapper;
        @Mock
        private RespuestaComiteSustentacionRepository respuestaComiteSustentacionRepository;
        @Mock
        private AnexosSustentacionRepository anexosSustentacionRepository;
        @Mock
        private AnexoSustentacionMapper anexoSustentacionMapper;
        @Mock
        private TrabajoGradoRepository trabajoGradoRepository;
        @Mock
        private ArchivoClient archivoClient;
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
                                anexosSustentacionRepository,
                                anexoSustentacionMapper,
                                trabajoGradoRepository,
                                null,
                                archivoClient,
                                archivoClientEgresados);
                ReflectionTestUtils.setField(sustentacionProyectoInvestigacionServiceImpl, "envioCorreos",
                                envioCorreos);
        }

        @Test
        void InsertarInformacionEstudianteSTest_RegistroExitoso() {
                Long idTrabajoGrado = 1L;

                SustentacionTrabajoInvestigacionEstudianteDto sustentacionTrabajoInvestigacionEstudianteDto = new SustentacionTrabajoInvestigacionEstudianteDto();
                sustentacionTrabajoInvestigacionEstudianteDto.setLinkFormatoH("linkFormatoH.txt-cHJ1ZWJhIGRlIHRleHR");
                sustentacionTrabajoInvestigacionEstudianteDto.setLinkFormatoI("linkFormatoI.txt-cHJ1ZWJhIGRlIHRleHR");
                sustentacionTrabajoInvestigacionEstudianteDto.setLinkEstudioHojaVidaAcademicaGrado(
                                "linkEstudioHojaVidaAcademicaGrado.txt-cHJ1ZWJhIGRlIHRleHR");

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(29);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                EmpresaSaveDto empresaSaveDto = new EmpresaSaveDto();
                empresaSaveDto.setId(1L);
                empresaSaveDto.setIdEstudiante(1L);
                empresaSaveDto.setNombre("GSE");
                empresaSaveDto.setUbicacion("Bogota");
                empresaSaveDto.setCargo("Desarrollador Junior");
                empresaSaveDto.setJefeDirecto("Juan M.");
                empresaSaveDto.setTelefono("31674832734");
                empresaSaveDto.setCorreo("julio@gse.com.co");
                empresaSaveDto.setEstado("Labora actualmente");

                ArrayList<EmpresaSaveDto> listaEmpresas = new ArrayList<>();
                listaEmpresas.add(empresaSaveDto);

                when(archivoClientEgresados.obtenerEmpresasPorIdEstudiante(trabajoGrado.getIdEstudiante()))
                                .thenReturn(listaEmpresas);

                when(sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion().getId()))
                                .thenReturn(Optional.of(sustentacionTrabajoInvestigacionOld));

                PersonaDto PersonaEstudianteDto = new PersonaDto();
                PersonaEstudianteDto.setIdentificacion(123L);
                PersonaEstudianteDto.setNombre("Juan");
                PersonaEstudianteDto.setApellido("Meneses");

                EstudianteResponseDtoAll estudianteResponseDtoAll = new EstudianteResponseDtoAll();
                estudianteResponseDtoAll.setPersona(PersonaEstudianteDto);

                when(archivoClient.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante()))
                                .thenReturn(estudianteResponseDtoAll);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionNew = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionNew.setId(sustentacionTrabajoInvestigacionOld.getId());
                sustentacionTrabajoInvestigacionNew.setLinkFormatoH(
                                "./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkFormatoH.txt");
                sustentacionTrabajoInvestigacionNew.setLinkFormatoI(
                                "./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkFormatoI.txt");
                sustentacionTrabajoInvestigacionNew.setLinkEstudioHojaVidaAcademicaGrado(
                                "./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkEstudioHojaVidaAcademicaGrado.txt");

                SustentacionTrabajoInvestigacionEstudianteResponseDto sustentacionTrabajoInvestigacionEstudianteResponseDto = new SustentacionTrabajoInvestigacionEstudianteResponseDto();
                sustentacionTrabajoInvestigacionEstudianteResponseDto
                                .setId(sustentacionTrabajoInvestigacionNew.getId());
                sustentacionTrabajoInvestigacionEstudianteResponseDto
                                .setLinkFormatoH(sustentacionTrabajoInvestigacionNew.getLinkFormatoH());
                sustentacionTrabajoInvestigacionEstudianteResponseDto
                                .setLinkFormatoI(sustentacionTrabajoInvestigacionNew.getLinkFormatoI());
                sustentacionTrabajoInvestigacionEstudianteResponseDto
                                .setLinkEstudioHojaVidaAcademicaGrado(sustentacionTrabajoInvestigacionNew
                                                .getLinkEstudioHojaVidaAcademicaGrado());

                when(sustentacionProyectoInvestigacionRepository.save(sustentacionTrabajoInvestigacionOld))
                                .thenReturn(sustentacionTrabajoInvestigacionNew);

                when(sustentacionProyectoInvestigacionResponseMapper
                                .toEstudianteDto(sustentacionTrabajoInvestigacionNew))
                                .thenReturn(sustentacionTrabajoInvestigacionEstudianteResponseDto);

                try (MockedStatic<FilesUtilities> utilities = mockStatic(FilesUtilities.class)) {
                        utilities.when(() -> FilesUtilities.guardarArchivoNew2(anyString(), anyString()))
                                        .thenReturn("path/to/new/file");

                        SustentacionTrabajoInvestigacionEstudianteResponseDto resultado = sustentacionProyectoInvestigacionServiceImpl
                                        .insertarInformacionEstudiante(idTrabajoGrado,
                                                        sustentacionTrabajoInvestigacionEstudianteDto,
                                                        result);

                        assertNotNull(resultado);
                        assertEquals(1L, resultado.getId());
                        assertEquals("./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkFormatoH.txt",
                                        resultado.getLinkFormatoH());
                        assertEquals("./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkFormatoI.txt",
                                        resultado.getLinkFormatoI());
                        assertEquals("./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkEstudioHojaVidaAcademicaGrado.txt",
                                        resultado.getLinkEstudioHojaVidaAcademicaGrado());
                }
        }

        @Test
        void InsertarInformacionEstudianteSTest_FaltanAtributos() {
                Long idTrabajoGrado = 1L;

                SustentacionTrabajoInvestigacionEstudianteDto sustentacionTrabajoInvestigacionEstudianteDto = new SustentacionTrabajoInvestigacionEstudianteDto();
                sustentacionTrabajoInvestigacionEstudianteDto.setLinkFormatoH("linkFormatoH.txt-cHJ1ZWJhIGRlIHRleHR");
                sustentacionTrabajoInvestigacionEstudianteDto.setLinkEstudioHojaVidaAcademicaGrado(
                                "linkEstudioHojaVidaAcademicaGrado.txt-cHJ1ZWJhIGRlIHRleHR");

                FieldError fieldError = new FieldError("SustentacionProyectoInvestigacionServiceImpl", "linkFormatoI",
                                "no debe ser nulo");
                when(result.hasErrors()).thenReturn(true);
                when(result.getFieldErrors()).thenReturn(List.of(fieldError));

                FieldErrorException exception = assertThrows(FieldErrorException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.insertarInformacionEstudiante(idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionEstudianteDto,
                                        result);
                });

                assertNotNull(exception.getResult());
                List<FieldError> fieldErrors = exception.getResult().getFieldErrors();
                assertFalse(fieldErrors.isEmpty());
                String actualMessage = "El campo: " + fieldErrors.get(0).getField() + ", "
                                + fieldError.getDefaultMessage();
                String expectedMessage = "El campo: linkFormatoI, no debe ser nulo";
                assertTrue(actualMessage.contains(expectedMessage));
        }

        @Test
        void InsertarInformacionEstudianteSTest_EstadoNoValido() {
                Long idTrabajoGrado = 1L;

                SustentacionTrabajoInvestigacionEstudianteDto sustentacionTrabajoInvestigacionEstudianteDto = new SustentacionTrabajoInvestigacionEstudianteDto();
                sustentacionTrabajoInvestigacionEstudianteDto.setLinkFormatoH("linkFormatoH.txt-cHJ1ZWJhIGRlIHRleHR");
                sustentacionTrabajoInvestigacionEstudianteDto.setLinkFormatoI("linkFormatoI.txt-cHJ1ZWJhIGRlIHRleHR");
                sustentacionTrabajoInvestigacionEstudianteDto.setLinkEstudioHojaVidaAcademicaGrado(
                                "linkEstudioHojaVidaAcademicaGrado.txt-cHJ1ZWJhIGRlIHRleHR");

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(20);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                InformationException exception = assertThrows(InformationException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.insertarInformacionEstudiante(idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionEstudianteDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "No es permitido registrar la información";

                assertTrue(exception.getMessage().contains(expectedMessage));

        }

        @Test
        void InsertarInformacionEstudianteSTest_FaltaRegistroEmpresa() {
                Long idTrabajoGrado = 1L;

                SustentacionTrabajoInvestigacionEstudianteDto sustentacionTrabajoInvestigacionEstudianteDto = new SustentacionTrabajoInvestigacionEstudianteDto();
                sustentacionTrabajoInvestigacionEstudianteDto.setLinkFormatoH("linkFormatoH.txt-cHJ1ZWJhIGRlIHRleHR");
                sustentacionTrabajoInvestigacionEstudianteDto.setLinkFormatoI("linkFormatoI.txt-cHJ1ZWJhIGRlIHRleHR");
                sustentacionTrabajoInvestigacionEstudianteDto.setLinkEstudioHojaVidaAcademicaGrado(
                                "linkEstudioHojaVidaAcademicaGrado.txt-cHJ1ZWJhIGRlIHRleHR");

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(29);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                when(archivoClientEgresados.obtenerEmpresasPorIdEstudiante(trabajoGrado.getIdEstudiante()))
                                .thenReturn(new ArrayList<>());

                InformationException exception = assertThrows(InformationException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.insertarInformacionEstudiante(idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionEstudianteDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "No es permitido registrar la información debido a que el estudiante no ha completado los datos de egresado";
                assertTrue(exception.getMessage().contains(expectedMessage));

        }

        @Test
        void InsertarInformacionEstudianteSTest_NoExisteTrabajoGrado() {
                Long idTrabajoGrado = 2L;

                SustentacionTrabajoInvestigacionEstudianteDto sustentacionTrabajoInvestigacionEstudianteDto = new SustentacionTrabajoInvestigacionEstudianteDto();
                sustentacionTrabajoInvestigacionEstudianteDto.setLinkFormatoH("linkFormatoH.txt-cHJ1ZWJhIGRlIHRleHR");
                sustentacionTrabajoInvestigacionEstudianteDto.setLinkFormatoI("linkFormatoI.txt-cHJ1ZWJhIGRlIHRleHR");
                sustentacionTrabajoInvestigacionEstudianteDto.setLinkEstudioHojaVidaAcademicaGrado(
                                "linkEstudioHojaVidaAcademicaGrado.txt-cHJ1ZWJhIGRlIHRleHR");

                when(result.hasErrors()).thenReturn(false);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.insertarInformacionEstudiante(idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionEstudianteDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Trabajo de grado con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

}
