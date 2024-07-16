package com.unicauca.maestria.api.gestiontrabajosgrado.TrabajoGradoInicial;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.TipoIdentificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.EstudianteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ServiceUnavailableException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.TrabajoGradoResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TiemposPendientesRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.inicio_trabajo_grado.InicioTrabajoGradoServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ListarEstudiantesTest {

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
    void ListarEstudiantesTest_ListadoExitoso() {
        PersonaDto personaDto1 = new PersonaDto();
        personaDto1.setId(1L);
        personaDto1.setTipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA);
        personaDto1.setIdentificacion(123L);
        personaDto1.setNombre("Julio");
        personaDto1.setApellido("Mellizo");

        PersonaDto personaDto2 = new PersonaDto();
        personaDto2.setId(2L);
        personaDto2.setTipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA);
        personaDto2.setIdentificacion(456L);
        personaDto2.setNombre("Luis");
        personaDto2.setApellido("Perez");

        EstudianteResponseDtoAll estudianteResponseDto1 = new EstudianteResponseDtoAll();
        estudianteResponseDto1.setId(1L);
        estudianteResponseDto1.setCodigo("12345");
        estudianteResponseDto1.setPersona(personaDto1);

        EstudianteResponseDtoAll estudianteResponseDto2 = new EstudianteResponseDtoAll();
        estudianteResponseDto2.setId(2L);
        estudianteResponseDto2.setCodigo("67890");
        estudianteResponseDto2.setPersona(personaDto2);

        List<EstudianteResponseDtoAll> listaEstudiantes = Arrays.asList(estudianteResponseDto1, estudianteResponseDto2);

        when(archivoClient.obtenerEstudiantes()).thenReturn(listaEstudiantes);

        EstudianteInfoDto estudianteInfoDto1 = new EstudianteInfoDto(
                estudianteResponseDto1.getId(),
                estudianteResponseDto1.getPersona().getNombre(),
                estudianteResponseDto1.getPersona().getApellido(),
                estudianteResponseDto1.getPersona().getTipoIdentificacion(),
                estudianteResponseDto1.getPersona().getIdentificacion(),
                estudianteResponseDto1.getCodigo());

        EstudianteInfoDto estudianteInfoDto2 = new EstudianteInfoDto(
                estudianteResponseDto2.getId(),
                estudianteResponseDto2.getPersona().getNombre(),
                estudianteResponseDto2.getPersona().getApellido(),
                estudianteResponseDto2.getPersona().getTipoIdentificacion(),
                estudianteResponseDto2.getPersona().getIdentificacion(),
                estudianteResponseDto2.getCodigo());

        List<EstudianteInfoDto> listaEstudiantesEsperada = Arrays.asList(estudianteInfoDto1, estudianteInfoDto2);

        List<EstudianteInfoDto> resultado = inicioTrabajoGradoServiceImpl.listarEstudiantes();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(listaEstudiantesEsperada, resultado);
    }

    @Test
    void ListarEstudiantesTest_NoHayRegistroEstudiantes() {
        when(archivoClient.obtenerEstudiantes()).thenReturn(new ArrayList<>());

        InformationException exception = assertThrows(InformationException.class, () -> {
            inicioTrabajoGradoServiceImpl.listarEstudiantes();
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "No hay estudiantes registrados";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void ListarEstudiantesTest_ServidorCaido() {
        when(archivoClient.obtenerEstudiantes()).thenThrow(new ServiceUnavailableException(
                "Servidor externo actualmente fuera de servicio"));

        ServiceUnavailableException thrown = assertThrows(
                ServiceUnavailableException.class,
                () -> inicioTrabajoGradoServiceImpl.listarEstudiantes(),
                "Servidor externo actualmente fuera de servicio");

        assertNotNull(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
    }
}
