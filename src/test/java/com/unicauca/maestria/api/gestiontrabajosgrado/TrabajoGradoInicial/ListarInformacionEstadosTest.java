package com.unicauca.maestria.api.gestiontrabajosgrado.TrabajoGradoInicial;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
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
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.InformacionTrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.TrabajoGradoResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TiemposPendientesRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.inicio_trabajo_grado.InicioTrabajoGradoServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ListarInformacionEstadosTest {

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
    void ListarInformacionEstadosTest_Exitoso() {
        ArrayList<Integer> capturaEstadosDto = new ArrayList<>(Arrays.asList(1, 2, 3));

        TrabajoGrado trabajoGrado1 = new TrabajoGrado();
        trabajoGrado1.setId(1L);
        trabajoGrado1.setIdEstudiante(1L);
        trabajoGrado1.setNumeroEstado(1);
        trabajoGrado1.setFechaCreacion(LocalDate.now());
        trabajoGrado1.setTitulo("Trabajo de Grado 1");

        TrabajoGrado trabajoGrado2 = new TrabajoGrado();
        trabajoGrado2.setId(2L);
        trabajoGrado2.setIdEstudiante(2L);
        trabajoGrado2.setNumeroEstado(2);
        trabajoGrado2.setFechaCreacion(LocalDate.now());
        trabajoGrado2.setTitulo("Trabajo de Grado 2");

        List<TrabajoGrado> trabajosGrado = Arrays.asList(trabajoGrado1, trabajoGrado2);

        PersonaDto personaDto1 = new PersonaDto();
        personaDto1.setId(1L);
        personaDto1.setIdentificacion(123L);
        personaDto1.setNombre("Julio");
        personaDto1.setApellido("Mellizo");
        personaDto1.setCorreoElectronico("julio@example.com");

        EstudianteResponseDtoAll estudianteResponseDtoAll1 = new EstudianteResponseDtoAll();
        estudianteResponseDtoAll1.setId(1L);
        estudianteResponseDtoAll1.setPersona(personaDto1);

        PersonaDto personaDto2 = new PersonaDto();
        personaDto2.setId(2L);
        personaDto2.setIdentificacion(456L);
        personaDto2.setNombre("Luis");
        personaDto2.setApellido("Perez");
        personaDto2.setCorreoElectronico("luis@example.com");

        EstudianteResponseDtoAll estudianteResponseDtoAll2 = new EstudianteResponseDtoAll();
        estudianteResponseDtoAll2.setId(2L);
        estudianteResponseDtoAll2.setPersona(personaDto2);

        when(trabajoGradoRepository.findAll()).thenReturn(trabajosGrado);
        when(archivoClient.obtenerInformacionEstudiante(1L)).thenReturn(estudianteResponseDtoAll1);
        when(archivoClient.obtenerInformacionEstudiante(2L)).thenReturn(estudianteResponseDtoAll2);

        List<InformacionTrabajoGradoResponseDto> resultado = inicioTrabajoGradoServiceImpl
                .listarInformacionEstados(capturaEstadosDto);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        InformacionTrabajoGradoResponseDto dto1 = resultado.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals(1L, dto1.getEstudianteId());
        assertEquals(123L, dto1.getIdentificacion());
        assertEquals("Julio Mellizo", dto1.getNombreCompleto());
        assertEquals("julio@example.com", dto1.getCorreoElectronico());
        assertEquals(1, dto1.getNumeroEstado());
        assertEquals("Pendiente revision por parte del coordinador", dto1.getEstado());

        InformacionTrabajoGradoResponseDto dto2 = resultado.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals(2L, dto2.getEstudianteId());
        assertEquals(456L, dto2.getIdentificacion());
        assertEquals("Luis Perez", dto2.getNombreCompleto());
        assertEquals("luis@example.com", dto2.getCorreoElectronico());
        assertEquals(2, dto2.getNumeroEstado());
        assertEquals("Se ha devuelto el examen de valoracion para correciones solicitadas del coordinador", dto2.getEstado());
    }

    @Test
    void ListarInformacionEstadosTest_RangoNoValido() {
        ArrayList<Integer> numerosEstado = new ArrayList<Integer>();
        numerosEstado.add(37);

        InformationException exception = assertThrows(InformationException.class, () -> {
            inicioTrabajoGradoServiceImpl.listarInformacionEstados(numerosEstado);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "El rango de estados es del 0 a 35";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

}
