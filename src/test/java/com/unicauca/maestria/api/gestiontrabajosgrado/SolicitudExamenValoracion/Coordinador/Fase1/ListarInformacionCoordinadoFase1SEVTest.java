package com.unicauca.maestria.api.gestiontrabajosgrado.SolicitudExamenValoracion.Coordinador.Fase1;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase1.SolicitudExamenValoracionResponseFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.AnexoSolicitudExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaComiteSolicitudRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion.SolicitudExamenValoracionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ListarInformacionCoordinadoFase1SEVTest {

    @Mock
    private SolicitudExamenValoracionRepository solicitudExamenValoracionRepository;
    @Mock
    private RespuestaComiteSolicitudRepository respuestaComiteSolicitudRepository;
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
                null,
                examenValoracionMapper,
                examenValoracionResponseMapper,
                anexoSolicitudExamenValoracionMapper,
                archivoClient);
        ReflectionTestUtils.setField(solicitudExamenValoracionService, "envioCorreos", envioCorreos);
    }

    @Test
    public void ListarInformacionCoordinadoFase1SEVTest_Exito() {

        Long idTrabajoGrado = 1L;
        SolicitudExamenValoracion solicitudExamenValoracion = new SolicitudExamenValoracion();
        solicitudExamenValoracion.setConceptoCoordinadorDocumentos(ConceptoVerificacion.ACEPTADO);

        when(solicitudExamenValoracionRepository.findByTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.of(solicitudExamenValoracion));

        SolicitudExamenValoracionResponseFase1Dto solicitudExamenValoracionResponseFase1Dto = new SolicitudExamenValoracionResponseFase1Dto();
        solicitudExamenValoracionResponseFase1Dto.setId(1L);
        solicitudExamenValoracionResponseFase1Dto.setConceptoCoordinadorDocumentos(ConceptoVerificacion.ACEPTADO);

        when(examenValoracionResponseMapper.toCoordinadorFase1Dto(solicitudExamenValoracion))
                .thenReturn(solicitudExamenValoracionResponseFase1Dto);

        SolicitudExamenValoracionResponseFase1Dto SolicitudExamenValoracionResponseFase1Dto = solicitudExamenValoracionService
                .listarInformacionCoordinadorFase1(idTrabajoGrado);

        assertNotNull(SolicitudExamenValoracionResponseFase1Dto);
        assertEquals(1, SolicitudExamenValoracionResponseFase1Dto.getId());
        assertEquals(ConceptoVerificacion.ACEPTADO, SolicitudExamenValoracionResponseFase1Dto.getConceptoCoordinadorDocumentos());

    }

    @Test
    void ListarInformacionCoordinadoFase1SEVTest_SinRegistro() {
        Long idTrabajoGrado = 1L;
        SolicitudExamenValoracion solicitudExamenValoracion = new SolicitudExamenValoracion();

        when(solicitudExamenValoracionRepository.findByTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.of(solicitudExamenValoracion));

        InformationException exception = assertThrows(InformationException.class, () -> {
            solicitudExamenValoracionService.listarInformacionCoordinadorFase1(idTrabajoGrado);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "No se han registrado datos";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void ListarInformacionCoordinadoFase1SEVTest_TrabajoGradoNoExiste() {
        Long idTrabajoGrado = 2L;

        when(solicitudExamenValoracionRepository.findByTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            solicitudExamenValoracionService.listarInformacionCoordinadorFase1(idTrabajoGrado);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Trabajo de grado con id 2 no encontrado";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}