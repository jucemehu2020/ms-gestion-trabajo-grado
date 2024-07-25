package com.unicauca.maestria.api.gestiontrabajosgrado.SustentacionProyectoInvestigacion.Coordinador.Fase2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientEgresados;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.Concepto;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_proyecto_investigacion.RespuestaComiteSustentacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.RespuestaComiteSustentacionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.STICoordinadorFase2ResponseDto;
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
public class ListarInformacionCoordinadorFase2STest {

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
        void ListarInformacionCoordinadorFase2STest_Exito() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                RespuestaComiteSustentacion respuestaComiteSustentacion = new RespuestaComiteSustentacion();
                respuestaComiteSustentacion.setConceptoComite(Concepto.APROBADO);
                respuestaComiteSustentacion.setNumeroActa("AX1-3445");
                respuestaComiteSustentacion.setFechaActa(LocalDate.parse("2023-05-24", formatter));

                List<RespuestaComiteSustentacion> listaRespuestaComiteSustentacion = new ArrayList<>();
                listaRespuestaComiteSustentacion.add(respuestaComiteSustentacion);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);
                sustentacionTrabajoInvestigacionOld.setActaFechaRespuestaComite(listaRespuestaComiteSustentacion);
                sustentacionTrabajoInvestigacionOld.setLinkEstudioHojaVidaAcademica(
                                "./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkEstudioHojaVidaAcademica.txt");
                sustentacionTrabajoInvestigacionOld.setLinkFormatoG(
                                "./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkFormatoG.txt");

                when(sustentacionProyectoInvestigacionRepository.findByTrabajoGradoId(idTrabajoGrado))
                                .thenReturn(Optional.of(sustentacionTrabajoInvestigacionOld));

                RespuestaComiteSustentacionDto respuestaComiteSustentacionDto = new RespuestaComiteSustentacionDto();
                respuestaComiteSustentacionDto.setConceptoComite(respuestaComiteSustentacion.getConceptoComite());
                respuestaComiteSustentacionDto.setNumeroActa(respuestaComiteSustentacion.getNumeroActa());
                respuestaComiteSustentacionDto.setFechaActa(respuestaComiteSustentacion.getFechaActa());

                List<RespuestaComiteSustentacionDto> listaRespuestaComiteDto = new ArrayList<>();
                listaRespuestaComiteDto.add(respuestaComiteSustentacionDto);

                STICoordinadorFase2ResponseDto resCoordinadorFase2ResponseDto = new STICoordinadorFase2ResponseDto();
                resCoordinadorFase2ResponseDto.setId(sustentacionTrabajoInvestigacionOld.getId());
                resCoordinadorFase2ResponseDto.setActaFechaRespuestaComite(listaRespuestaComiteDto);
                resCoordinadorFase2ResponseDto
                                .setLinkEstudioHojaVidaAcademica(
                                                sustentacionTrabajoInvestigacionOld.getLinkEstudioHojaVidaAcademica());
                resCoordinadorFase2ResponseDto.setLinkFormatoG(sustentacionTrabajoInvestigacionOld.getLinkFormatoG());

                when(sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase2Dto(sustentacionTrabajoInvestigacionOld))
                                .thenReturn(resCoordinadorFase2ResponseDto);

                STICoordinadorFase2ResponseDto resultado = sustentacionProyectoInvestigacionServiceImpl
                                .listarInformacionCoordinadorFase2(idTrabajoGrado);

                assertNotNull(resultado);
                assertEquals(1L, resultado.getId());
                assertEquals(Concepto.APROBADO, resultado.getActaFechaRespuestaComite().get(0).getConceptoComite());
                assertEquals("AX1-3445", resultado.getActaFechaRespuestaComite().get(0).getNumeroActa());
                assertEquals(LocalDate.parse("2023-05-24", formatter),
                                resultado.getActaFechaRespuestaComite().get(0).getFechaActa());
                assertEquals(
                                "./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkEstudioHojaVidaAcademica.txt",
                                resultado.getLinkEstudioHojaVidaAcademica());
                assertEquals(
                                "./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkFormatoG.txt",
                                resultado.getLinkFormatoG());

        }

        @Test
        void ListarInformacionCoordinadorFase2STest_NoHayRegistro() {
                Long idTrabajoGrado = 1L;

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);

                when(sustentacionProyectoInvestigacionRepository.findByTrabajoGradoId(idTrabajoGrado))
                                .thenReturn(Optional.of(sustentacionTrabajoInvestigacionOld));

                InformationException exception = assertThrows(InformationException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.listarInformacionCoordinadorFase2(idTrabajoGrado);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "No se han registrado datos";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void ListarInformacionCoordinadorFase2STest_TrabajoGradoNoExiste() {
                Long idTrabajoGrado = 2L;

                when(sustentacionProyectoInvestigacionRepository.findByTrabajoGradoId(idTrabajoGrado))
                                .thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.listarInformacionCoordinadorFase1(idTrabajoGrado);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Trabajo de grado con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

}
