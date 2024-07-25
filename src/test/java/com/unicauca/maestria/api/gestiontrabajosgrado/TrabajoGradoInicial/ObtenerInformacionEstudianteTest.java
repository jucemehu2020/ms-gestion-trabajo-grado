package com.unicauca.maestria.api.gestiontrabajosgrado.TrabajoGradoInicial;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

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
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ServiceUnavailableException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.TrabajoGradoResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TiemposPendientesRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.inicio_trabajo_grado.InicioTrabajoGradoServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ObtenerInformacionEstudianteTest {

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
    void ObtenerInformacionEstudianteTest_ListadoExitoso() {

        Long idEstudiante = 1L;

        PersonaDto personaDto = new PersonaDto();
        personaDto.setId(1L);
        personaDto.setTipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA);
        personaDto.setIdentificacion(123L);
        personaDto.setNombre("Julio");
        personaDto.setApellido("Mellizo");

        EstudianteResponseDtoAll estudianteResponseDto = new EstudianteResponseDtoAll();
        estudianteResponseDto.setId(1L);
        estudianteResponseDto.setCodigo("12354564");
        estudianteResponseDto.setPersona(personaDto);

        when(archivoClient.obtenerInformacionEstudiante(idEstudiante)).thenReturn(estudianteResponseDto);

        EstudianteInfoDto informacionEstudiante1 = new EstudianteInfoDto(
                personaDto.getId(),
                personaDto.getNombre(),
                personaDto.getApellido(),
                personaDto.getTipoIdentificacion(),
                personaDto.getIdentificacion(),
                estudianteResponseDto.getCodigo());

        EstudianteInfoDto resultado = inicioTrabajoGradoServiceImpl.obtenerInformacionEstudiante(idEstudiante);

        assertNotNull(resultado);
        assertEquals(informacionEstudiante1, resultado);
    }

    @Test
    void ObtenerInformacionEstudianteTest_EstudianteNoExiste() {
        Long idEstudiante = 4L;

        when(archivoClient.obtenerInformacionEstudiante(idEstudiante))
                .thenThrow(new ResourceNotFoundException("Estudiantes con id "
                        + idEstudiante + " no encontrado"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            inicioTrabajoGradoServiceImpl.obtenerInformacionEstudiante(idEstudiante);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Estudiantes con id 4 no encontrado";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void ObtenerInformacionEstudianteTest_ServidorCaido() {
        Long idEstudiante = 1L;

        when(archivoClient.obtenerInformacionEstudiante(idEstudiante)).thenThrow(new ServiceUnavailableException(
                "Servidor externo actualmente fuera de servicio"));

        ServiceUnavailableException thrown = assertThrows(
                ServiceUnavailableException.class,
                () -> inicioTrabajoGradoServiceImpl.obtenerInformacionEstudiante(idEstudiante),
                "Servidor externo actualmente fuera de servicio");

        assertNotNull(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
    }
}
