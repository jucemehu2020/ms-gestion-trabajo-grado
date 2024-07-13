package com.unicauca.maestria.api.gestiontrabajosgrado.SustentacionProyectoInvestigacion.Coordinador.Fase3;

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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientExpertos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.SustentacionTrabajoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3.STICoordinadorFase3ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaComiteSustentacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SustentacionProyectoInvestigacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TiemposPendientesRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ListarInformacionCoordinadorFase3STest {

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
    private TiemposPendientesRepository tiemposPendientesRepository;
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
                tiemposPendientesRepository,
                archivoClient,
                archivoClientExpertos,
                archivoClientEgresados);
        ReflectionTestUtils.setField(sustentacionProyectoInvestigacionServiceImpl, "envioCorreos",
                envioCorreos);
    }

    @Test
    void ListarInformacionCoordinadorFase2STest_Exito() {

        Long idTrabajoGrado = 1L;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionTrabajoInvestigacion();
        sustentacionTrabajoInvestigacionOld.setId(1L);
        sustentacionTrabajoInvestigacionOld.setJuradosAceptados(ConceptoVerificacion.RECHAZADO);
        sustentacionTrabajoInvestigacionOld.setNumeroActaConsejo("a-abc");
        sustentacionTrabajoInvestigacionOld.setFechaActaConsejo(LocalDate.parse("2024-05-24", formatter));
        sustentacionTrabajoInvestigacionOld.setIdJuradoInterno(1L);
        sustentacionTrabajoInvestigacionOld.setIdJuradoExterno(3L);

        when(sustentacionProyectoInvestigacionRepository.findByTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.of(sustentacionTrabajoInvestigacionOld));

        STICoordinadorFase3ResponseDto coordinadorFase3ResponseDto = new STICoordinadorFase3ResponseDto();
        coordinadorFase3ResponseDto.setId(sustentacionTrabajoInvestigacionOld.getId());
        coordinadorFase3ResponseDto.setJuradosAceptados(sustentacionTrabajoInvestigacionOld.getJuradosAceptados());
        coordinadorFase3ResponseDto.setNumeroActaConsejo(sustentacionTrabajoInvestigacionOld.getNumeroActaConsejo());
        coordinadorFase3ResponseDto.setFechaActaConsejo(sustentacionTrabajoInvestigacionOld.getFechaActaConsejo());
        coordinadorFase3ResponseDto
                .setIdJuradoInterno(String.valueOf(sustentacionTrabajoInvestigacionOld.getIdJuradoInterno()));
        coordinadorFase3ResponseDto
                .setIdJuradoExterno(String.valueOf(sustentacionTrabajoInvestigacionOld.getIdJuradoExterno()));

        when(sustentacionProyectoInvestigacionResponseMapper.toCoordinadorFase3Dto(sustentacionTrabajoInvestigacionOld))
                .thenReturn(coordinadorFase3ResponseDto);

        STICoordinadorFase3ResponseDto resultado = sustentacionProyectoInvestigacionServiceImpl
                .listarInformacionCoordinadorFase3(idTrabajoGrado);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(ConceptoVerificacion.RECHAZADO, resultado.getJuradosAceptados());
        assertEquals("a-abc", resultado.getNumeroActaConsejo());
        assertEquals(LocalDate.parse("2024-05-24", formatter), resultado.getFechaActaConsejo());
        assertEquals("1", resultado.getIdJuradoInterno());
        assertEquals("3", resultado.getIdJuradoExterno());

    }

    @Test
    void ListarInformacionCoordinadorFase2STest_NoHayRegistro() {
        Long idTrabajoGrado = 1L;

        SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionTrabajoInvestigacion();
        sustentacionTrabajoInvestigacionOld.setId(1L);

        when(sustentacionProyectoInvestigacionRepository.findByTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.of(sustentacionTrabajoInvestigacionOld));

        InformationException exception = assertThrows(InformationException.class, () -> {
            sustentacionProyectoInvestigacionServiceImpl.listarInformacionCoordinadorFase3(idTrabajoGrado);
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
            sustentacionProyectoInvestigacionServiceImpl.listarInformacionCoordinadorFase3(idTrabajoGrado);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Trabajo de grado con id 2 no encontrado";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

}
