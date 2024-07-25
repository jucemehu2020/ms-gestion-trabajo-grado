package com.unicauca.maestria.api.gestiontrabajosgrado.SolicitudExamenValoracion.Docente;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.AnexoSolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteResponseListDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ServiceUnavailableException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.AnexoSolicitudExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.AnexosSolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion.SolicitudExamenValoracionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ListarInformacionDocenteSEVTest {

        @Mock
        private SolicitudExamenValoracionRepository solicitudExamenValoracionRepository;
        @Mock
        private AnexosSolicitudExamenValoracionRepository anexosSolicitudExamenValoracionRepository;
        @Mock
        private TrabajoGradoRepository trabajoGradoRepository;
        @Mock
        private ArchivoClient archivoClient;
        @Mock
        private SolicitudExamenValoracionMapper examenValoracionMapper;
        @Mock
        private SolicitudExamenValoracionResponseMapper examenValoracionResponseMapper;
        @Mock
        private AnexoSolicitudExamenValoracionMapper anexoSolicitudExamenValoracionMapper;
        @Mock
        private BindingResult result;

        @InjectMocks
        private SolicitudExamenValoracionServiceImpl solicitudExamenValoracionService;

        @BeforeEach
        void setUp() {
                solicitudExamenValoracionService = new SolicitudExamenValoracionServiceImpl(
                                solicitudExamenValoracionRepository,
                                null,
                                anexosSolicitudExamenValoracionRepository,
                                trabajoGradoRepository,
                                null,
                                examenValoracionMapper,
                                examenValoracionResponseMapper,
                                anexoSolicitudExamenValoracionMapper,
                                archivoClient);
        }

        @Test
        void ListarInformacionDocenteSEVTest_Exito() {
                Long idTrabajoGrado = 1L;
                SolicitudExamenValoracion solicitudExamenValoracion = new SolicitudExamenValoracion();
                solicitudExamenValoracion.setId(1L);
                solicitudExamenValoracion.setTitulo("Prueba");
                solicitudExamenValoracion.setLinkFormatoA(
                                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-formatoA.txt");
                solicitudExamenValoracion.setLinkFormatoD(
                                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-formatoD.txt");
                solicitudExamenValoracion.setLinkFormatoE(
                                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-formatoE.txt");
                solicitudExamenValoracion.setActaFechaRespuestaComite(new ArrayList<>());
                solicitudExamenValoracion.setIdEvaluadorInterno(1L);
                solicitudExamenValoracion.setIdEvaluadorExterno(1L);

                when(solicitudExamenValoracionRepository.findByTrabajoGradoId(idTrabajoGrado))
                                .thenReturn(Optional.of(solicitudExamenValoracion));

                PersonaDto personaDto1 = new PersonaDto();
                personaDto1.setNombre("Danny");
                personaDto1.setApellido("Mage");
                personaDto1.setCorreoElectronico("dannyad@unicauca.edu.co");

                PersonaDto personaDto2 = new PersonaDto();
                personaDto2.setNombre("Julio");
                personaDto2.setApellido("Mellizo");
                personaDto2.setCorreoElectronico("mellizohurt@unicauca.edu.co");

                DocenteResponseDto docenteResponseDto = new DocenteResponseDto();
                docenteResponseDto.setId(1L);
                docenteResponseDto.setPersona(personaDto1);

                ExpertoResponseDto expertoResponseDto = new ExpertoResponseDto();
                expertoResponseDto.setId(1L);
                expertoResponseDto.setPersona(personaDto2);
                expertoResponseDto.setUniversidadtitexp("Universidad de Mexico");

                when(archivoClient.obtenerDocentePorId(solicitudExamenValoracion.getIdEvaluadorInterno()))
                                .thenReturn(docenteResponseDto);
                when(archivoClient.obtenerExpertoPorId(solicitudExamenValoracion.getIdEvaluadorExterno()))
                                .thenReturn(expertoResponseDto);

                List<AnexoSolicitudExamenValoracion> listaAnexos = new ArrayList<>();

                when(anexosSolicitudExamenValoracionRepository
                                .obtenerAnexosPorId(solicitudExamenValoracion.getId()))
                                .thenReturn(listaAnexos);

                SolicitudExamenValoracionDocenteResponseListDto solicitudExamenValoracionDocenteResponseListDto = solicitudExamenValoracionService
                                .listarInformacionDocente(idTrabajoGrado);

                assertNotNull(solicitudExamenValoracionDocenteResponseListDto);
                assertEquals(1, solicitudExamenValoracionDocenteResponseListDto.getId());
                assertEquals("Prueba", solicitudExamenValoracionDocenteResponseListDto.getTitulo());
                assertEquals("./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-formatoA.txt",
                                solicitudExamenValoracionDocenteResponseListDto.getLinkFormatoA());
                assertEquals("./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-formatoD.txt",
                                solicitudExamenValoracionDocenteResponseListDto.getLinkFormatoD());
                assertEquals("./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-formatoE.txt",
                                solicitudExamenValoracionDocenteResponseListDto.getLinkFormatoE());

                Map<String, String> docenteEsperado = new HashMap<>();
                docenteEsperado.put("id", "1");
                docenteEsperado.put("correo", "dannyad@unicauca.edu.co");
                docenteEsperado.put("universidad", "Universidad del Cauca");
                docenteEsperado.put("nombres", "Danny Mage");

                assertEquals(docenteEsperado, solicitudExamenValoracionDocenteResponseListDto.getEvaluadorInterno());

                Map<String, String> expertoEsperado = new HashMap<>();
                expertoEsperado.put("id", "1");
                expertoEsperado.put("correo", "mellizohurt@unicauca.edu.co");
                expertoEsperado.put("universidad", "Universidad de Mexico");
                expertoEsperado.put("nombres", "Julio Mellizo");

                assertEquals(expertoEsperado, solicitudExamenValoracionDocenteResponseListDto.getEvaluadorExterno());
        }

        @Test
        void ListarInformacionDocenteSEVTest_NoHayRegistros() {
                Long idTrabajoGrado = 1L;
                SolicitudExamenValoracion solicitudExamenValoracion = new SolicitudExamenValoracion();

                when(solicitudExamenValoracionRepository.findByTrabajoGradoId(idTrabajoGrado))
                                .thenReturn(Optional.of(solicitudExamenValoracion));

                InformationException exception = assertThrows(InformationException.class, () -> {
                        solicitudExamenValoracionService.listarInformacionDocente(idTrabajoGrado);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "No se han registrado datos";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void ListarInformacionDocenteSEVTest_ServidorDocenteCaido() {
                Long idTrabajoGrado = 1L;
                SolicitudExamenValoracion solicitudExamenValoracion = new SolicitudExamenValoracion();
                solicitudExamenValoracion.setId(1L);
                solicitudExamenValoracion.setTitulo("Prueba");
                solicitudExamenValoracion.setLinkFormatoA(
                                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-formatoA.txt");
                solicitudExamenValoracion.setLinkFormatoD(
                                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-formatoD.txt");
                solicitudExamenValoracion.setLinkFormatoE(
                                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-formatoE.txt");
                solicitudExamenValoracion.setActaFechaRespuestaComite(new ArrayList<>());
                solicitudExamenValoracion.setIdEvaluadorInterno(1L);
                solicitudExamenValoracion.setIdEvaluadorExterno(1L);

                when(solicitudExamenValoracionRepository.findByTrabajoGradoId(idTrabajoGrado))
                                .thenReturn(Optional.of(solicitudExamenValoracion));

                when(archivoClient.obtenerDocentePorId(solicitudExamenValoracion.getIdEvaluadorInterno()))
                                .thenThrow(new ServiceUnavailableException(
                                                "Servidor externo actualmente fuera de servicio"));

                ServiceUnavailableException thrown = assertThrows(
                                ServiceUnavailableException.class,
                                () -> solicitudExamenValoracionService.listarInformacionDocente(idTrabajoGrado),
                                "Servidor externo actualmente fuera de servicio");

                assertNotNull(thrown.getMessage());
                assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
        }

        @Test
        void ListarInformacionDocenteSEVTest_ServidorExpertoCaido() {
                Long idTrabajoGrado = 1L;
                SolicitudExamenValoracion solicitudExamenValoracion = new SolicitudExamenValoracion();
                solicitudExamenValoracion.setId(1L);
                solicitudExamenValoracion.setTitulo("Prueba");
                solicitudExamenValoracion.setLinkFormatoA(
                                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-formatoA.txt");
                solicitudExamenValoracion.setLinkFormatoD(
                                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-formatoD.txt");
                solicitudExamenValoracion.setLinkFormatoE(
                                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-formatoE.txt");
                solicitudExamenValoracion.setActaFechaRespuestaComite(new ArrayList<>());
                solicitudExamenValoracion.setIdEvaluadorInterno(1L);
                solicitudExamenValoracion.setIdEvaluadorExterno(1L);

                when(solicitudExamenValoracionRepository.findByTrabajoGradoId(idTrabajoGrado))
                                .thenReturn(Optional.of(solicitudExamenValoracion));

                PersonaDto personaDto1 = new PersonaDto();
                personaDto1.setNombre("Danny");
                personaDto1.setApellido("Mage");
                personaDto1.setCorreoElectronico("dannyad@unicauca.edu.co");

                DocenteResponseDto docenteResponseDto = new DocenteResponseDto();
                docenteResponseDto.setId(1L);
                docenteResponseDto.setPersona(personaDto1);

                when(archivoClient.obtenerDocentePorId(solicitudExamenValoracion.getIdEvaluadorInterno()))
                                .thenReturn(docenteResponseDto);

                when(archivoClient.obtenerExpertoPorId(solicitudExamenValoracion.getIdEvaluadorExterno()))
                                .thenThrow(new ServiceUnavailableException(
                                                "Servidor externo actualmente fuera de servicio"));

                ServiceUnavailableException thrown = assertThrows(
                                ServiceUnavailableException.class,
                                () -> solicitudExamenValoracionService.listarInformacionDocente(idTrabajoGrado),
                                "Servidor externo actualmente fuera de servicio");

                assertNotNull(thrown.getMessage());
                assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
        }

        @Test
        void ListarInformacionDocenteSEVTest_TrabajoGradoNoExiste() {
                Long idTrabajoGrado = 2L;

                when(solicitudExamenValoracionRepository.findByTrabajoGradoId(idTrabajoGrado))
                                .thenThrow(new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        solicitudExamenValoracionService.listarInformacionDocente(idTrabajoGrado);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Trabajo de grado con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

}
