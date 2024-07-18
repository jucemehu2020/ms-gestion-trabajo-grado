package com.unicauca.maestria.api.gestiontrabajosgrado.SustentacionProyectoInvestigacion.Estudiante;

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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.SustentacionTrabajoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.estudiante.SustentacionTrabajoInvestigacionEstudianteResponseDto;
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
public class ListarInformacionEstudianteSTest {

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
                archivoClientEgresados);
        ReflectionTestUtils.setField(sustentacionProyectoInvestigacionServiceImpl, "envioCorreos",
                envioCorreos);
    }

    @Test
    void ListarInformacionCoordinadorFase2STest_Exito() {
        Long idTrabajoGrado = 1L;

        SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionTrabajoInvestigacion();
        sustentacionTrabajoInvestigacionOld.setId(1L);
        sustentacionTrabajoInvestigacionOld.setLinkFormatoH(
                "./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkFormatoH.txt");
        sustentacionTrabajoInvestigacionOld.setLinkFormatoI(
                "./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkFormatoI.txt");

        when(sustentacionProyectoInvestigacionRepository.findByTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.of(sustentacionTrabajoInvestigacionOld));

        SustentacionTrabajoInvestigacionEstudianteResponseDto sustentacionTrabajoInvestigacionEstudianteResponseDto = new SustentacionTrabajoInvestigacionEstudianteResponseDto();
        sustentacionTrabajoInvestigacionEstudianteResponseDto.setId(sustentacionTrabajoInvestigacionOld.getId());
        sustentacionTrabajoInvestigacionEstudianteResponseDto
                .setLinkFormatoH(sustentacionTrabajoInvestigacionOld.getLinkFormatoH());
        sustentacionTrabajoInvestigacionEstudianteResponseDto
                .setLinkFormatoI(sustentacionTrabajoInvestigacionOld.getLinkFormatoI());

        when(sustentacionProyectoInvestigacionResponseMapper.toEstudianteDto(sustentacionTrabajoInvestigacionOld))
                .thenReturn(sustentacionTrabajoInvestigacionEstudianteResponseDto);

        SustentacionTrabajoInvestigacionEstudianteResponseDto resultado = sustentacionProyectoInvestigacionServiceImpl
                .listarInformacionEstudiante(idTrabajoGrado);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(
                "./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkFormatoH.txt",
                resultado.getLinkFormatoH());
        assertEquals(
                "./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkFormatoI.txt",
                resultado.getLinkFormatoI());
    }

    @Test
    void ListarInformacionCoordinadorFase2STest_NoHayRegistro() {
        Long idTrabajoGrado = 1L;

        SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionTrabajoInvestigacion();
        sustentacionTrabajoInvestigacionOld.setId(1L);

        when(sustentacionProyectoInvestigacionRepository.findByTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.of(sustentacionTrabajoInvestigacionOld));

        InformationException exception = assertThrows(InformationException.class, () -> {
            sustentacionProyectoInvestigacionServiceImpl.listarInformacionEstudiante(idTrabajoGrado);
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
