package com.unicauca.maestria.api.gestiontrabajosgrado.SustentacionProyectoInvestigacion.Coordinador.Fase1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.STICoordinadorFase1ResponseDto;
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
public class ListarInformacionCoordinadorFase1STest {

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
    void ListarInformacionCoordinadorFase1STest_Exito() {
        Long idTrabajoGrado = 1L;

        SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
        sustentacionTrabajoInvestigacionOld.setId(1L);
        sustentacionTrabajoInvestigacionOld.setConceptoCoordinador(ConceptoVerificacion.ACEPTADO);

        when(sustentacionProyectoInvestigacionRepository.findByTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.of(sustentacionTrabajoInvestigacionOld));

        STICoordinadorFase1ResponseDto stiCoordinadorFase1ResponseDto = new STICoordinadorFase1ResponseDto();
        stiCoordinadorFase1ResponseDto.setId(sustentacionTrabajoInvestigacionOld.getId());
        stiCoordinadorFase1ResponseDto
                .setConceptoCoordinador(sustentacionTrabajoInvestigacionOld.getConceptoCoordinador());

        when(sustentacionProyectoInvestigacionResponseMapper.toCoordinadorFase1Dto(sustentacionTrabajoInvestigacionOld))
                .thenReturn(stiCoordinadorFase1ResponseDto);

        STICoordinadorFase1ResponseDto resultado = sustentacionProyectoInvestigacionServiceImpl
                .listarInformacionCoordinadorFase1(idTrabajoGrado);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(ConceptoVerificacion.ACEPTADO, resultado.getConceptoCoordinador());

    }

    @Test
    void ListarInformacionCoordinadorFase1STest_NoHayRegistro() {
        Long idTrabajoGrado = 1L;

        SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
        sustentacionTrabajoInvestigacionOld.setId(1L);

        when(sustentacionProyectoInvestigacionRepository.findByTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.of(sustentacionTrabajoInvestigacionOld));

        InformationException exception = assertThrows(InformationException.class, () -> {
            sustentacionProyectoInvestigacionServiceImpl.listarInformacionCoordinadorFase1(idTrabajoGrado);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "No se han registrado datos";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void ListarInformacionCoordinadorFase1STest_TrabajoGradoNoExiste() {
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
