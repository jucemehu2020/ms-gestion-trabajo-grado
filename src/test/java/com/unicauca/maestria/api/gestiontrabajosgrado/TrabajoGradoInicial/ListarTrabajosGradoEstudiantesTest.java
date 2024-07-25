package com.unicauca.maestria.api.gestiontrabajosgrado.TrabajoGradoInicial;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

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
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ServiceUnavailableException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.TrabajoGradoResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TiemposPendientesRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.inicio_trabajo_grado.InicioTrabajoGradoServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ListarTrabajosGradoEstudiantesTest {

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
    void listarTrabajosGradoEstudiantesTest_ListadoExitoso() {
        Long idEstudiante = 1L;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        TrabajoGrado trabajoGrado1 = new TrabajoGrado();
        trabajoGrado1.setId(1L);
        trabajoGrado1.setIdEstudiante(1L);
        trabajoGrado1.setFechaCreacion(LocalDate.parse("2024-07-12", formatter));
        trabajoGrado1.setNumeroEstado(1);
        trabajoGrado1.setTitulo("Ciclo del software");

        List<TrabajoGrado> trabajosGrado = Arrays.asList(trabajoGrado1);

        when(trabajoGradoRepository.findByEstudianteId(idEstudiante)).thenReturn(trabajosGrado);

        TrabajoGradoResponseDto trabajoGradoResponseDto1 = TrabajoGradoResponseDto.builder()
                .id(trabajoGrado1.getId())
                .estado(EstadoTrabajoGrado.values()[trabajoGrado1.getNumeroEstado()].getMensaje())
                .fechaCreacion(trabajoGrado1.getFechaCreacion())
                .titulo(trabajoGrado1.getTitulo() != null ? trabajoGrado1.getTitulo() : "Título no disponible")
                .numeroEstado(trabajoGrado1.getNumeroEstado())
                .build();

        EstudianteResponseDto estudianteResponseDtoEsperado = EstudianteResponseDto.builder()
                .idEstudiante(idEstudiante)
                .trabajoGrado(Arrays.asList(trabajoGradoResponseDto1))
                .build();

        EstudianteResponseDto resultado = inicioTrabajoGradoServiceImpl.listarTrabajosGradoEstudiante(idEstudiante);

        assertNotNull(resultado);
        assertEquals(estudianteResponseDtoEsperado, resultado);
    }

    @Test
    void ListarTrabajosGradoEstudiantesTest_EstudianteNoExiste() {
        Long idEstudiante = 4L;

        when(trabajoGradoRepository.findByEstudianteId(idEstudiante))
                .thenThrow(new ResourceNotFoundException("Estudiantes con id "
                        + idEstudiante + " no encontrado"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            inicioTrabajoGradoServiceImpl.listarTrabajosGradoEstudiante(idEstudiante);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Estudiantes con id 4 no encontrado";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void ListarTrabajosGradoEstudiantesTest_ServidorEstudianteCaido() {
        Long idEstudiante = 1L;

        when(trabajoGradoRepository.findByEstudianteId(idEstudiante)).thenThrow(new ServiceUnavailableException(
                "Servidor externo actualmente fuera de servicio"));

        ServiceUnavailableException thrown = assertThrows(
                ServiceUnavailableException.class,
                () -> inicioTrabajoGradoServiceImpl.listarTrabajosGradoEstudiante(idEstudiante),
                "Servidor externo actualmente fuera de servicio");

        assertNotNull(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
    }

}
