package com.unicauca.maestria.api.gestiontrabajosgrado.TrabajoGradoInicial;

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
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientLogin;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.EstadoTrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.TrabajoGradoResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TiemposPendientesRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.inicio_trabajo_grado.InicioTrabajoGradoServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class BuscarTrabajoGradoTest {

    @Mock
    private TrabajoGradoRepository trabajoGradoRepository;
    @Mock
    private TiemposPendientesRepository tiemposPendientesRepository;
    @Mock
    private TrabajoGradoResponseMapper trabajoGradoResponseMapper;
    @Mock
    private ArchivoClient archivoClient;
    @Mock
    private ArchivoClientLogin archivoClientLogin;
    @Mock
    private BindingResult result;
    @InjectMocks
    private InicioTrabajoGradoServiceImpl inicioTrabajoGradoServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inicioTrabajoGradoServiceImpl = new InicioTrabajoGradoServiceImpl(
                trabajoGradoRepository,
                tiemposPendientesRepository,
                trabajoGradoResponseMapper,
                archivoClient,
                archivoClientLogin);
    }

    @Test
    void BuscarTrabajoGradoTest_ListadoExitoso() {
        Long idTrabajoGrado = 1L;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(1L);
        trabajoGrado.setIdEstudiante(1L);
        trabajoGrado.setFechaCreacion(LocalDate.parse("2025-07-12", formatter));
        trabajoGrado.setNumeroEstado(1);
        trabajoGrado.setTitulo("Ciclo del software");

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

        TrabajoGradoResponseDto trabajoGradoResponseDto = new TrabajoGradoResponseDto();
        trabajoGradoResponseDto.setId(trabajoGrado.getId());
        trabajoGradoResponseDto.setEstado(EstadoTrabajoGrado.values()[trabajoGrado.getNumeroEstado()].getMensaje());
        trabajoGradoResponseDto.setFechaCreacion(trabajoGrado.getFechaCreacion());
        trabajoGradoResponseDto.setNumeroEstado(trabajoGrado.getNumeroEstado());
        trabajoGradoResponseDto.setTitulo(trabajoGrado.getTitulo());

        when(trabajoGradoResponseMapper.toDto(trabajoGrado)).thenReturn(trabajoGradoResponseDto);

        TrabajoGradoResponseDto resultado = inicioTrabajoGradoServiceImpl.buscarTrabajoGrado(idTrabajoGrado);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Pendiente revision por parte del COORDINADOR", resultado.getEstado());
        assertEquals(LocalDate.parse("2025-07-12", formatter), resultado.getFechaCreacion());
        assertEquals(1, resultado.getNumeroEstado());
        assertEquals("Ciclo del software", resultado.getTitulo());
    }

    @Test
    void BuscarTrabajoGradoTest_NoExisteTrabajoGrado() {
        Long idTrabajoGrado = 2L;

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            inicioTrabajoGradoServiceImpl.buscarTrabajoGrado(idTrabajoGrado);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Trabajo de grado con id 2 no encontrado";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

}
