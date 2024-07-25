package com.unicauca.maestria.api.gestiontrabajosgrado.SustentacionProyectoInvestigacion.Coordinador.Fase4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptosVarios;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_4.STICoordinadorFase4ResponseDto;
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
import com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ListarInformacionCoordinadorFase4STest {

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
        private TiemposPendientesRepository tiemposPendientesRepository;
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
                                tiemposPendientesRepository,
                                archivoClient,
                                archivoClientEgresados);
                ReflectionTestUtils.setField(sustentacionProyectoInvestigacionServiceImpl, "envioCorreos",
                                envioCorreos);
        }

        @Test
        void ListarInformacionCoordinadorFase4STest_Exito() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);
                sustentacionTrabajoInvestigacionOld.setRespuestaSustentacion(ConceptosVarios.APROBADO);
                sustentacionTrabajoInvestigacionOld
                                .setLinkActaSustentacionPublica(
                                                "./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkActaSustentacionPublica.txt");
                sustentacionTrabajoInvestigacionOld
                                .setLinkEstudioHojaVidaAcademicaGrado(
                                                "./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkEstudioHojaVidaAcademicaGrado.txt");
                sustentacionTrabajoInvestigacionOld.setNumeroActaFinal("a-abc");
                sustentacionTrabajoInvestigacionOld.setFechaActaFinal(LocalDate.parse("2024-05-29", formatter));

                when(sustentacionProyectoInvestigacionRepository.findByTrabajoGradoId(idTrabajoGrado))
                                .thenReturn(Optional.of(sustentacionTrabajoInvestigacionOld));

                STICoordinadorFase4ResponseDto coordinadorFase4ResponseDto = new STICoordinadorFase4ResponseDto();
                coordinadorFase4ResponseDto.setId(sustentacionTrabajoInvestigacionOld.getId());
                coordinadorFase4ResponseDto.setRespuestaSustentacion(
                                sustentacionTrabajoInvestigacionOld.getRespuestaSustentacion());
                coordinadorFase4ResponseDto.setLinkActaSustentacionPublica(
                                sustentacionTrabajoInvestigacionOld.getLinkActaSustentacionPublica());
                coordinadorFase4ResponseDto
                                .setNumeroActaFinal(sustentacionTrabajoInvestigacionOld.getNumeroActaFinal());
                coordinadorFase4ResponseDto.setFechaActaFinal(sustentacionTrabajoInvestigacionOld.getFechaActaFinal());

                when(sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase4Dto(sustentacionTrabajoInvestigacionOld))
                                .thenReturn(coordinadorFase4ResponseDto);

                STICoordinadorFase4ResponseDto resultado = sustentacionProyectoInvestigacionServiceImpl
                                .listarInformacionCoordinadorFase4(idTrabajoGrado);

                assertNotNull(resultado);
                assertEquals(1L, resultado.getId());
                assertEquals(ConceptosVarios.APROBADO, resultado.getRespuestaSustentacion());
                assertEquals("./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkActaSustentacionPublica.txt",
                                resultado.getLinkActaSustentacionPublica());
                assertEquals("a-abc", resultado.getNumeroActaFinal());
                assertEquals(LocalDate.parse("2024-05-29", formatter), resultado.getFechaActaFinal());

        }

        @Test
        void ListarInformacionCoordinadorFase4STest_NoHayRegistro() {
                Long idTrabajoGrado = 1L;

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);

                when(sustentacionProyectoInvestigacionRepository.findByTrabajoGradoId(idTrabajoGrado))
                                .thenReturn(Optional.of(sustentacionTrabajoInvestigacionOld));

                InformationException exception = assertThrows(InformationException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.listarInformacionCoordinadorFase4(idTrabajoGrado);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "No se han registrado datos";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void ListarInformacionCoordinadorFase4STest_TrabajoGradoNoExiste() {
                Long idTrabajoGrado = 2L;

                when(sustentacionProyectoInvestigacionRepository.findByTrabajoGradoId(idTrabajoGrado))
                                .thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.listarInformacionCoordinadorFase4(idTrabajoGrado);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Trabajo de grado con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

}
