package com.unicauca.maestria.api.gestiontrabajosgrado.SolicitudExamenValoracion.Coordinador.Fase2;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.Concepto;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.RespuestaComiteExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.RespuestaComiteExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.SolicitudExamenValoracionCoordinadorFase2ResponseDto;
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
public class ListarInformacionCoordinadorFase2SEVTest {

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
    public void ListarInformacionCoordinadorFase2SEVTest_Exito() {

        Long idTrabajoGrado = 1L;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        SolicitudExamenValoracion solicitudExamenValoracionOld = new SolicitudExamenValoracion();

        RespuestaComiteExamenValoracion respuestaComite = RespuestaComiteExamenValoracion.builder()
                .conceptoComite(Concepto.APROBADO)
                .numeroActa("AX1-3445")
                .fechaActa(LocalDate.parse("2023-05-24", formatter))
                .solicitudExamenValoracion(solicitudExamenValoracionOld)
                .build();

        List<RespuestaComiteExamenValoracion> listaRespuestaComiteExamenValoracion = new ArrayList<>();
        listaRespuestaComiteExamenValoracion.add(respuestaComite);

        SolicitudExamenValoracion solicitudExamenValoracion = new SolicitudExamenValoracion();
        solicitudExamenValoracion.setId(1L);
        solicitudExamenValoracion.setActaFechaRespuestaComite(listaRespuestaComiteExamenValoracion);
        solicitudExamenValoracion.setLinkOficioDirigidoEvaluadores(
                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/30-06-24/20240630181114-oficio.txt");
        solicitudExamenValoracion.setFechaMaximaEvaluacion(LocalDate.parse("2023-05-29", formatter));

        when(solicitudExamenValoracionRepository.findByTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.of(solicitudExamenValoracion));

        RespuestaComiteExamenValoracionDto respuestaComiteExamenValoracionDto = new RespuestaComiteExamenValoracionDto();
        respuestaComiteExamenValoracionDto.setConceptoComite(Concepto.APROBADO);
        respuestaComiteExamenValoracionDto.setNumeroActa("AX1-3445");
        respuestaComiteExamenValoracionDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

        List<RespuestaComiteExamenValoracionDto> listaRespuestaComite = new ArrayList();
        listaRespuestaComite.add(respuestaComiteExamenValoracionDto);

        SolicitudExamenValoracionCoordinadorFase2ResponseDto solicitudExamenValoracionCoordinadorResponseDto = new SolicitudExamenValoracionCoordinadorFase2ResponseDto();
        solicitudExamenValoracionCoordinadorResponseDto
                .setId(1L);
        solicitudExamenValoracionCoordinadorResponseDto.setActaFechaRespuestaComite(listaRespuestaComite);
        solicitudExamenValoracionCoordinadorResponseDto.setLinkOficioDirigidoEvaluadores(
                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/30-06-24/20240630181114-oficio.txt");
        solicitudExamenValoracionCoordinadorResponseDto
                .setFechaMaximaEvaluacion(LocalDate.parse("2023-05-29", formatter));

        when(examenValoracionResponseMapper.toCoordinadorFase2Dto(solicitudExamenValoracion))
                .thenReturn(solicitudExamenValoracionCoordinadorResponseDto);

        SolicitudExamenValoracionCoordinadorFase2ResponseDto resultado = solicitudExamenValoracionService
                .listarInformacionCoordinadorFase2(idTrabajoGrado);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(Concepto.APROBADO, resultado.getActaFechaRespuestaComite().get(0).getConceptoComite());
        assertEquals("AX1-3445", resultado.getActaFechaRespuestaComite().get(0).getNumeroActa());
        assertEquals(LocalDate.parse("2023-05-24", formatter),
                resultado.getActaFechaRespuestaComite().get(0).getFechaActa());
        assertEquals("./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/30-06-24/20240630181114-oficio.txt",
                resultado.getLinkOficioDirigidoEvaluadores());
        assertEquals(LocalDate.parse("2023-05-29", formatter), resultado.getFechaMaximaEvaluacion());

    }

    @Test
    void ListarInformacionCoordinadorFase2SEVTest_SinRegistro() {

        Long idTrabajoGrado = 1L;
        SolicitudExamenValoracion solicitudExamenValoracion = new SolicitudExamenValoracion();

        when(solicitudExamenValoracionRepository.findByTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.of(solicitudExamenValoracion));

        InformationException exception = assertThrows(InformationException.class, () -> {
            solicitudExamenValoracionService.listarInformacionCoordinadorFase2(idTrabajoGrado);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "No se han registrado datos";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void ListarInformacionCoordinadorFase2SEVTest_TrabajoGradoNoExiste() {
        Long idTrabajoGrado = 2L;

        when(solicitudExamenValoracionRepository.findByTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            solicitudExamenValoracionService.listarInformacionCoordinadorFase2(idTrabajoGrado);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Trabajo de grado con id 2 no encontrado";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

}
