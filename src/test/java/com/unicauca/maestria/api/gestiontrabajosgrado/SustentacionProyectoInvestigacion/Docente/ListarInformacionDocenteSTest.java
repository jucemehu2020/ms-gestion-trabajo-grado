package com.unicauca.maestria.api.gestiontrabajosgrado.SustentacionProyectoInvestigacion.Docente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientEgresados;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientExpertos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.SustentacionTrabajoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionListDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ServiceUnavailableException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaComiteSustentacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SustentacionProyectoInvestigacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ListarInformacionDocenteSTest {

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
                                null,
                                archivoClient,
                                archivoClientExpertos,
                                archivoClientEgresados);
                ReflectionTestUtils.setField(sustentacionProyectoInvestigacionServiceImpl, "envioCorreos",
                                envioCorreos);
        }

        @Test
        void ListarInformacionDocenteSTest_Exito() {

                Long idTrabajoGrado = 1L;

                SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionTrabajoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);
                sustentacionTrabajoInvestigacionOld.setLinkFormatoF(
                                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-linkFormatoF.txt");
                sustentacionTrabajoInvestigacionOld.setUrlDocumentacion("www.google.com");
                sustentacionTrabajoInvestigacionOld.setIdJuradoInterno(1L);
                sustentacionTrabajoInvestigacionOld.setIdJuradoExterno(1L);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(18);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                when(sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion()
                                                .getId()))
                                .thenReturn(Optional.of(sustentacionTrabajoInvestigacionOld));

                PersonaDto personaDto1 = new PersonaDto();
                personaDto1.setNombre("Danny");
                personaDto1.setApellido("Mage");
                personaDto1.setCorreoElectronico("dannyad@unicauca.edu.co");

                DocenteResponseDto docenteResponseDto = new DocenteResponseDto();
                docenteResponseDto.setId(1L);
                docenteResponseDto.setPersona(personaDto1);

                when(archivoClient.obtenerDocentePorId(sustentacionTrabajoInvestigacionOld.getIdJuradoInterno()))
                                .thenReturn(docenteResponseDto);

                Map<String, String> docenteEsperado = new HashMap<>();
                docenteEsperado.put("correo", docenteResponseDto.getPersona().getCorreoElectronico());
                docenteEsperado.put("universidad", "Universidad del Cauca");
                docenteEsperado.put("nombres",
                                docenteResponseDto.getPersona().getNombre() + " "
                                                + docenteResponseDto.getPersona().getApellido());

                PersonaDto personaDto2 = new PersonaDto();
                personaDto2.setNombre("Julio");
                personaDto2.setApellido("Mellizo");
                personaDto2.setCorreoElectronico("mellizohurt@unicauca.edu.co");

                ExpertoResponseDto expertoResponseDto = new ExpertoResponseDto();
                expertoResponseDto.setId(1L);
                expertoResponseDto.setPersona(personaDto2);
                expertoResponseDto.setUniversidadtitexp("Universidad de Mexico");

                when(archivoClientExpertos
                                .obtenerExpertoPorId(sustentacionTrabajoInvestigacionOld.getIdJuradoExterno()))
                                .thenReturn(expertoResponseDto);

                Map<String, String> expertoEsperado = new HashMap<>();
                expertoEsperado.put("correo", expertoResponseDto.getPersona().getCorreoElectronico());
                expertoEsperado.put("universidad", expertoResponseDto.getUniversidadtitexp());
                expertoEsperado.put("nombres",
                                expertoResponseDto.getPersona().getNombre() + " "
                                                + expertoResponseDto.getPersona().getApellido());

                SustentacionTrabajoInvestigacionListDocenteDto sustentacionTrabajoInvestigacionListDocenteDto = new SustentacionTrabajoInvestigacionListDocenteDto();
                sustentacionTrabajoInvestigacionListDocenteDto.setIdSustentacionTrabajoInvestigacion(
                                sustentacionTrabajoInvestigacionOld.getId());
                sustentacionTrabajoInvestigacionListDocenteDto
                                .setLinkFormatoF(sustentacionTrabajoInvestigacionOld.getLinkFormatoF());
                sustentacionTrabajoInvestigacionListDocenteDto
                                .setUrlDocumentacion(sustentacionTrabajoInvestigacionOld.getUrlDocumentacion());
                sustentacionTrabajoInvestigacionListDocenteDto.setJuradoInterno(docenteEsperado);
                sustentacionTrabajoInvestigacionListDocenteDto.setJuradoExterno(expertoEsperado);

                SustentacionTrabajoInvestigacionListDocenteDto resultado = sustentacionProyectoInvestigacionServiceImpl
                                .listarInformacionDocente(idTrabajoGrado);

                assertNotNull(resultado);
                assertEquals(1L, resultado.getIdSustentacionTrabajoInvestigacion());
                assertEquals(
                                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-linkFormatoF.txt",
                                resultado.getLinkFormatoF());
                assertEquals("www.google.com", resultado.getUrlDocumentacion());
                assertEquals(docenteEsperado, resultado.getJuradoInterno());
                assertEquals(expertoEsperado, resultado.getJuradoExterno());

        }

        @Test
        void ListarInformacionDocenteSTest_NoHayRegistro() {
                Long idTrabajoGrado = 1L;

                SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionTrabajoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(18);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                when(sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion()
                                                .getId()))
                                .thenReturn(Optional.of(new SustentacionTrabajoInvestigacion()));

                InformationException exception = assertThrows(InformationException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.listarInformacionDocente(idTrabajoGrado);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "No se han registrado datos";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void SustentacionProyectoInvestigacionServiceImplTest_TrabajoGradoNoExiste() {
                Long idTrabajoGrado = 2L;

                when(trabajoGradoRepository.findById(idTrabajoGrado))
                                .thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.listarInformacionDocente(idTrabajoGrado);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Trabajo de grado con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void SustentacionProyectoInvestigacionServiceImplTest_ServidorDocenteCaido() {

                Long idTrabajoGrado = 1L;

                SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionTrabajoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);
                sustentacionTrabajoInvestigacionOld.setLinkFormatoF(
                                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-linkFormatoF.txt");
                sustentacionTrabajoInvestigacionOld.setUrlDocumentacion("www.google.com");
                sustentacionTrabajoInvestigacionOld.setIdJuradoInterno(1L);
                sustentacionTrabajoInvestigacionOld.setIdJuradoExterno(1L);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(18);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                when(archivoClient.obtenerDocentePorId(sustentacionTrabajoInvestigacionOld.getIdJuradoInterno()))
                                .thenThrow(new ServiceUnavailableException(
                                                "Servidor externo actualmente fuera de servicio"));

                when(sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion()
                                                .getId()))
                                .thenReturn(Optional.of(sustentacionTrabajoInvestigacionOld));

                ServiceUnavailableException thrown = assertThrows(
                                ServiceUnavailableException.class,
                                () -> sustentacionProyectoInvestigacionServiceImpl
                                                .listarInformacionDocente(idTrabajoGrado),
                                "Servidor externo actualmente fuera de servicio");

                assertNotNull(thrown.getMessage());
                assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
        }

        @Test
        void SustentacionProyectoInvestigacionServiceImplTest_ServidorExpertoCaido() {

                Long idTrabajoGrado = 1L;

                SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionTrabajoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);
                sustentacionTrabajoInvestigacionOld.setLinkFormatoF(
                                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-linkFormatoF.txt");
                sustentacionTrabajoInvestigacionOld.setUrlDocumentacion("www.google.com");
                sustentacionTrabajoInvestigacionOld.setIdJuradoInterno(1L);
                sustentacionTrabajoInvestigacionOld.setIdJuradoExterno(1L);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(18);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                when(sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion()
                                                .getId()))
                                .thenReturn(Optional.of(sustentacionTrabajoInvestigacionOld));

                PersonaDto personaDto1 = new PersonaDto();
                personaDto1.setNombre("Danny");
                personaDto1.setApellido("Mage");
                personaDto1.setCorreoElectronico("dannyad@unicauca.edu.co");

                DocenteResponseDto docenteResponseDto = new DocenteResponseDto();
                docenteResponseDto.setId(1L);
                docenteResponseDto.setPersona(personaDto1);

                when(archivoClient.obtenerDocentePorId(sustentacionTrabajoInvestigacionOld.getIdJuradoInterno()))
                                .thenReturn(docenteResponseDto);

                when(archivoClientExpertos
                                .obtenerExpertoPorId(sustentacionTrabajoInvestigacionOld.getIdJuradoExterno()))
                                .thenThrow(new ServiceUnavailableException(
                                                "Servidor externo actualmente fuera de servicio"));

                ServiceUnavailableException thrown = assertThrows(
                                ServiceUnavailableException.class,
                                () -> sustentacionProyectoInvestigacionServiceImpl
                                                .listarInformacionDocente(idTrabajoGrado),
                                "Servidor externo actualmente fuera de servicio");

                assertNotNull(thrown.getMessage());
                assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
        }

}
