package com.unicauca.maestria.api.gestiontrabajosgrado.TrabajoGradoInicial;

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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientLogin;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.Genero;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.TipoIdentificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.security.JwtUtil;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.TrabajoGradoResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TiemposPendientesRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.inicio_trabajo_grado.InicioTrabajoGradoServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class VerificacionDocenteTest {

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
    @Mock
    private JwtUtil jwtTokenProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inicioTrabajoGradoServiceImpl = new InicioTrabajoGradoServiceImpl(
                trabajoGradoRepository,
                tiemposPendientesRepository,
                trabajoGradoResponseMapper,
                archivoClient,
                archivoClientLogin);
        ReflectionTestUtils.setField(inicioTrabajoGradoServiceImpl, "jwtTokenProvider",
                jwtTokenProvider);
    }

    @Test
    void VerificacionDocenteTest_ValidacionExitosa() {
        String idTrabajoGrado = "1";
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkb2NlbnRlIiwicm9sZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfRE9DRU5URSJ9XSwiaWF0IjoxNzIzMjcwNjg2LCJleHAiOjE3MjMzNTcwODZ9.w2DRnztPNczOi3cdwvNXXuZ8iSMcJURs0dhpSd5XV-SzdQb5E_3ployFHNmRAxQ50XDyELwm1Nu8L-2HrsjWkw";
        String usuario = "prueba";

        // Mock para extraer el usuario del token
        when(jwtTokenProvider.getUserNameFromJwtToken(token)).thenReturn(usuario);

        // Mock para encontrar el TrabajoGrado por id
        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(1L);
        trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

        when(trabajoGradoRepository.findById(Long.parseLong(idTrabajoGrado))).thenReturn(Optional.of(trabajoGrado));

        // Mock para la respuesta del archivoClientLogin con el JSON del docente
        String jsonString = "{\"personaId\":4,\"roles\":[{\"nombreRol\":\"ROLE_DOCENTE\"}]}";
        when(archivoClientLogin.obtenerPersonaId(usuario)).thenReturn(jsonString);

        // Mock para obtener el docente con el id 4
        PersonaDto persona = PersonaDto.builder()
                .id(4L)
                .identificacion(1098L)
                .nombre("Karla")
                .apellido("Ramirez")
                .correoElectronico("juliomellizo24@gmail.com") // El correo debe coincidir con el del tutor
                .telefono("316-325-33-40")
                .genero(Genero.FEMENINO)
                .tipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA)
                .build();

        DocenteResponseDto docenteResponse = DocenteResponseDto.builder()
                .id(6L)
                .persona(persona)
                .build();

        when(archivoClient.obtenerDocentePorId(persona.getId())).thenReturn(docenteResponse);

        // Ejecución del método a probar
        Boolean resultado = inicioTrabajoGradoServiceImpl.verificarDocente(idTrabajoGrado, token);

        // Validaciones
        assertNotNull(resultado);
        assertTrue(resultado); // Debería ser true ya que los correos coinciden
    }

    @Test
    void VerificacionDocenteTest_TrabajoGradoNoExiste() {
        String idTrabajoGrado = "2";
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkb2NlbnRlIiwicm9sZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfRE9DRU5URSJ9XSwiaWF0IjoxNzIzMjcwNjg2LCJleHAiOjE3MjMzNTcwODZ9.w2DRnztPNczOi3cdwvNXXuZ8iSMcJURs0dhpSd5XV-SzdQb5E_3ployFHNmRAxQ50XDyELwm1Nu8L-2HrsjWkw";
        String usuario = "prueba";

        // Mock para extraer el usuario del token
        when(jwtTokenProvider.getUserNameFromJwtToken(token)).thenReturn(usuario);

        // Mock para encontrar el TrabajoGrado por id
        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(1L);
        trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

        when(trabajoGradoRepository.findById(Long.parseLong(idTrabajoGrado))).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            inicioTrabajoGradoServiceImpl.verificarDocente(idTrabajoGrado, token);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Trabajo de grado con id 2 no encontrado";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

}
