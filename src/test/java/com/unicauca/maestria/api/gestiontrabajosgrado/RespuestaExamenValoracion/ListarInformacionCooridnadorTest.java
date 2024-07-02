package com.unicauca.maestria.api.gestiontrabajosgrado.RespuestaExamenValoracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientExpertos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.respuesta_examen_valoracion.ConceptoRespuesta;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.respuesta_examen_valoracion.TipoEvaluador;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.AnexoRespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.RespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.AnexoRespuestaExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.AnexoRespuestaExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.ExamenValoracionCanceladoMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.RespuestaExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.RespuestaExamenValoracionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.AnexosRespuestaExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.ExamenValoracionCanceladoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.respuesta_examen_valoracion.RespuestaExamenValoracionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ListarInformacionCooridnadorTest {

    @Mock
    private RespuestaExamenValoracionRepository respuestaExamenValoracionRepository;
    @Mock
    private ExamenValoracionCanceladoRepository examenValoracionCanceladoRepository;
    @Mock
    private AnexosRespuestaExamenValoracionRepository anexosRespuestaExamenValoracionRepository;
    @Mock
    private RespuestaExamenValoracionMapper respuestaExamenValoracionMapper;
    @Mock
    private RespuestaExamenValoracionResponseMapper respuestaExamenValoracionResponseMapper;
    @Mock
    private AnexoRespuestaExamenValoracionMapper anexoRespuestaExamenValoracionMapper;
    @Mock
    private ExamenValoracionCanceladoMapper examenValoracionCanceladoMapper;
    @Mock
    private TrabajoGradoRepository trabajoGradoRepository;
    @Mock
    private ArchivoClient archivoClient;
    @Mock
    private ArchivoClientExpertos archivoClientExpertos;
    @Mock
    private BindingResult result;
    @Mock
    private EnvioCorreos envioCorreos;
    @InjectMocks
    private RespuestaExamenValoracionServiceImpl respuestaExamenValoracionServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        respuestaExamenValoracionServiceImpl = new RespuestaExamenValoracionServiceImpl(
                respuestaExamenValoracionRepository,
                examenValoracionCanceladoRepository,
                anexosRespuestaExamenValoracionRepository,
                respuestaExamenValoracionMapper,
                respuestaExamenValoracionResponseMapper,
                anexoRespuestaExamenValoracionMapper,
                examenValoracionCanceladoMapper,
                trabajoGradoRepository,
                archivoClient,
                archivoClientExpertos);
        ReflectionTestUtils.setField(respuestaExamenValoracionServiceImpl, "envioCorreos", envioCorreos);
    }

    @Test
    void testListarInformacionCoordinador_Exito() {
        Long idTrabajoGrado = 1L;

        List<AnexoRespuestaExamenValoracion> listaAnexos = new ArrayList<>();
        listaAnexos.add(AnexoRespuestaExamenValoracion.builder()
                .linkAnexo(
                        "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-Anexos.txt")
                .build());

        RespuestaExamenValoracion respuestaExamenValoracion = new RespuestaExamenValoracion();
        respuestaExamenValoracion.setLinkFormatoB(
                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoB.txt");
        respuestaExamenValoracion.setLinkFormatoC(
                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoC.txt");
        respuestaExamenValoracion.setLinkObservaciones(
                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-observaciones.txt");
        respuestaExamenValoracion.setAnexos(listaAnexos);
        respuestaExamenValoracion.setRespuestaExamenValoracion(ConceptoRespuesta.APROBADO);
        respuestaExamenValoracion.setIdEvaluador(1L);
        respuestaExamenValoracion.setTipoEvaluador(TipoEvaluador.EXTERNO);

        when(respuestaExamenValoracionRepository.findByIdTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.of(new RespuestaExamenValoracion()));

        when(respuestaExamenValoracionRepository.findByIdTrabajoGrado(idTrabajoGrado))
                .thenReturn(Collections.singletonList(respuestaExamenValoracion));

        List<AnexoRespuestaExamenValoracionDto> listaAnexosResponseDto = new ArrayList<>();
        listaAnexosResponseDto.add(AnexoRespuestaExamenValoracionDto.builder()
                .linkAnexo(respuestaExamenValoracion.getAnexos().get(0).getLinkAnexo())
                .build());

        RespuestaExamenValoracionResponseDto respuestaExamenValoracionResponseDto = new RespuestaExamenValoracionResponseDto();
        respuestaExamenValoracionResponseDto.setIdRespuestaExamenValoracion(idTrabajoGrado);
        respuestaExamenValoracionResponseDto.setLinkFormatoB(respuestaExamenValoracion.getLinkFormatoB());
        respuestaExamenValoracionResponseDto.setLinkFormatoC(respuestaExamenValoracion.getLinkFormatoC());
        respuestaExamenValoracionResponseDto.setLinkObservaciones(respuestaExamenValoracion.getLinkObservaciones());
        respuestaExamenValoracionResponseDto.setAnexos(listaAnexosResponseDto);
        respuestaExamenValoracionResponseDto
                .setRespuestaExamenValoracion(respuestaExamenValoracion.getRespuestaExamenValoracion());
        respuestaExamenValoracionResponseDto.setFechaMaximaEntrega(respuestaExamenValoracion.getFechaMaximaEntrega());
        respuestaExamenValoracionResponseDto.setIdEvaluador(respuestaExamenValoracion.getIdEvaluador());
        respuestaExamenValoracionResponseDto.setTipoEvaluador(respuestaExamenValoracion.getTipoEvaluador());

        when(respuestaExamenValoracionResponseMapper.toDto(respuestaExamenValoracion))
                .thenReturn(respuestaExamenValoracionResponseDto);

        Map<String, List<RespuestaExamenValoracionResponseDto>> resultado = respuestaExamenValoracionServiceImpl
                .buscarPorId(idTrabajoGrado);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get("evaluador_externo").size());
        RespuestaExamenValoracionResponseDto dto = resultado.get("evaluador_externo").get(0);

        assertEquals(1L, dto.getIdRespuestaExamenValoracion());
        assertEquals(
                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoB.txt",
                dto.getLinkFormatoB());
        assertEquals(
                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoC.txt",
                dto.getLinkFormatoC());
        assertEquals(
                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-observaciones.txt",
                dto.getLinkObservaciones());
        assertEquals("./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-Anexos.txt",
                dto.getAnexos().get(0).getLinkAnexo());
        assertEquals(ConceptoRespuesta.APROBADO, dto.getRespuestaExamenValoracion());
        assertEquals(null, dto.getFechaMaximaEntrega());
        assertEquals(1L, dto.getIdEvaluador());
        assertEquals(TipoEvaluador.EXTERNO, dto.getTipoEvaluador());

    }

    @Test
    void testListarInformacionCoordinador_NoHayRegistro() {
        Long idTrabajoGrado = 1L;


        when(respuestaExamenValoracionRepository.findByIdTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.of(new RespuestaExamenValoracion()));

        when(respuestaExamenValoracionRepository.findByIdTrabajoGrado(idTrabajoGrado))
                .thenReturn(Collections.emptyList());

        InformationException exception = assertThrows(InformationException.class, () -> {
            respuestaExamenValoracionServiceImpl.buscarPorId(idTrabajoGrado);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "No se han registrado datos";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void testListarInformacionCoordinadoFase2Test_TrabajoGradoNoExiste() {
        Long idTrabajoGrado = 2L;

        when(respuestaExamenValoracionRepository.findByIdTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            respuestaExamenValoracionServiceImpl.buscarPorId(idTrabajoGrado);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Trabajo de grado con id 2 no encontrado";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

}
