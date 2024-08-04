package com.unicauca.maestria.api.gestiontrabajosgrado.TrabajoGradoInicial;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.EstadoMaestriaActual;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.Genero;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.TipoIdentificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.security.JwtUtil;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.embeddables.InformacionMaestriaActual;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ServiceUnavailableException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.TrabajoGradoResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TiemposPendientesRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.inicio_trabajo_grado.InicioTrabajoGradoServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CrearTrabajoGradoTest {

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
    @Mock
    private JwtUtil jwtTokenProvider;
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
        ReflectionTestUtils.setField(inicioTrabajoGradoServiceImpl, "jwtTokenProvider",
                jwtTokenProvider);
    }

    @Test
    void CrearTrabajoGradoTest_RegistroExitoso() {
        Long idEstudiante = 1L;
        String token = "token";
        String usuario = "usuario";
        String correoElectronico = "correo@example.com";

        InformacionMaestriaActual informacionMaestriaActual = new InformacionMaestriaActual();
        informacionMaestriaActual.setEstadoMaestria(EstadoMaestriaActual.ACTIVO);

        PersonaDto personaDto = new PersonaDto();
        personaDto.setIdentificacion(123L);
        personaDto.setNombre("Julio");
        personaDto.setApellido("Mellizo");
        personaDto.setCorreoElectronico("julio@example.com");

        EstudianteResponseDtoAll informacionEstudiante = new EstudianteResponseDtoAll();
        informacionEstudiante.setId(idEstudiante);
        informacionEstudiante.setPersona(personaDto);
        informacionEstudiante.setInformacionMaestria(informacionMaestriaActual);

        List<TrabajoGrado> trabajosGrado = new ArrayList<>();

        when(archivoClient.obtenerInformacionEstudiante(idEstudiante)).thenReturn(informacionEstudiante);
        when(trabajoGradoRepository.findByEstudianteId(idEstudiante)).thenReturn(trabajosGrado);
        when(jwtTokenProvider.getUserNameFromJwtToken(token)).thenReturn(usuario);
        //when(archivoClientLogin.obtenerCorreo(usuario)).thenReturn("{\"message\": \"" + correoElectronico + "\"}");

        String jsonString = "{\n" +
        "    \"id\": 1,\n" +
        "    \"usuario\": \"user\",\n" +
        "    \"contrasena\": \"$2a$10$YIQsFYd8fwfVYSAzQjhPdOyKKP5oCWxshHcquM/gVlcDcp9gLZr/e\",\n" +
        "    \"personaId\": 1,\n" +
        "    \"roles\": [\n" +
        "        {\n" +
        "            \"id\": 1,\n" +
        "            \"nombreRol\": \"ROLE_DOCENTE\"\n" +
        "        }\n" +
        "    ]\n" +
        "}";

        when(archivoClientLogin.obtenerPersonaId(usuario)).thenReturn(jsonString);

        PersonaDto persona = PersonaDto.builder()
                .id(1L)
                .identificacion(1098L)
                .nombre("Karla")
                .apellido("Ramirez")
                .correoElectronico("julio.mellizo@gse.com.co")
                .telefono("316-325-33-40")
                .genero(Genero.FEMENINO)
                .tipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA)
                .build();

        DocenteResponseDto docenteResponse = DocenteResponseDto.builder()
                .id(1L)
                .persona(persona)
                .build();

        when(archivoClient.obtenerDocentePorId(docenteResponse.getId())).thenReturn(docenteResponse);

        TrabajoGrado trabajoGradoConvert = new TrabajoGrado();
        trabajoGradoConvert.setIdEstudiante(idEstudiante);
        trabajoGradoConvert.setFechaCreacion(LocalDate.now());
        trabajoGradoConvert.setNumeroEstado(0);
        trabajoGradoConvert.setTitulo("");
        trabajoGradoConvert.setCorreoElectronicoTutor(correoElectronico);

        TrabajoGrado trabajoGradoGuardado = new TrabajoGrado();
        trabajoGradoGuardado.setId(1L);
        trabajoGradoGuardado.setIdEstudiante(idEstudiante);
        trabajoGradoGuardado.setFechaCreacion(LocalDate.now());
        trabajoGradoGuardado.setNumeroEstado(0);
        trabajoGradoGuardado.setTitulo("");
        trabajoGradoGuardado.setCorreoElectronicoTutor(correoElectronico);

        when(trabajoGradoRepository.save(any(TrabajoGrado.class))).thenReturn(trabajoGradoGuardado);

        TrabajoGradoResponseDto trabajoGradoResponseDto = new TrabajoGradoResponseDto();
        trabajoGradoResponseDto.setId(trabajoGradoGuardado.getId());
        trabajoGradoResponseDto.setNumeroEstado(trabajoGradoGuardado.getNumeroEstado());

        when(trabajoGradoResponseMapper.toDto(trabajoGradoGuardado)).thenReturn(trabajoGradoResponseDto);

        TrabajoGradoResponseDto resultado = inicioTrabajoGradoServiceImpl.crearTrabajoGrado(idEstudiante, token);

        assertNotNull(resultado);
        assertEquals(0, resultado.getNumeroEstado());
        assertEquals("Sin registrar solicitud de examen de valoración por parte del DOCENTE", resultado.getEstado());
    }

    @Test
    void CrearTrabajoGradoTest_EstudianteInactivo() {
        Long idEstudiante = 1L;
        String token = "token";

        InformacionMaestriaActual informacionMaestriaActual = new InformacionMaestriaActual();
        informacionMaestriaActual.setEstadoMaestria(EstadoMaestriaActual.EN_SUSPENSION);

        PersonaDto personaDto = new PersonaDto();
        personaDto.setIdentificacion(123L);
        personaDto.setNombre("Julio");
        personaDto.setApellido("Mellizo");
        personaDto.setCorreoElectronico("julio@example.com");

        EstudianteResponseDtoAll informacionEstudiante = new EstudianteResponseDtoAll();
        informacionEstudiante.setId(idEstudiante);
        informacionEstudiante.setPersona(personaDto);
        informacionEstudiante.setInformacionMaestria(informacionMaestriaActual);

        when(archivoClient.obtenerInformacionEstudiante(idEstudiante)).thenReturn(informacionEstudiante);

        InformationException exception = assertThrows(InformationException.class, () -> {
            inicioTrabajoGradoServiceImpl.crearTrabajoGrado(idEstudiante, token);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "El estudiante no esta actualmente ACTIVO";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void CrearTrabajoGradoTest_EstudianteAprobado() {
        Long idEstudiante = 1L;
        String token = "token";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        InformacionMaestriaActual informacionMaestriaActual = new InformacionMaestriaActual();
        informacionMaestriaActual.setEstadoMaestria(EstadoMaestriaActual.ACTIVO);

        PersonaDto personaDto = new PersonaDto();
        personaDto.setIdentificacion(123L);
        personaDto.setNombre("Julio");
        personaDto.setApellido("Mellizo");
        personaDto.setCorreoElectronico("julio@example.com");

        EstudianteResponseDtoAll informacionEstudiante = new EstudianteResponseDtoAll();
        informacionEstudiante.setId(idEstudiante);
        informacionEstudiante.setPersona(personaDto);
        informacionEstudiante.setInformacionMaestria(informacionMaestriaActual);

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(1L);
        trabajoGrado.setIdEstudiante(1L);
        trabajoGrado.setFechaCreacion(LocalDate.parse("2024-07-12", formatter));
        trabajoGrado.setNumeroEstado(31);
        trabajoGrado.setTitulo("Ciclo del software");

        List<TrabajoGrado> listaTrabajosGrado = new ArrayList<>();
        listaTrabajosGrado.add(trabajoGrado);

        when(archivoClient.obtenerInformacionEstudiante(idEstudiante)).thenReturn(informacionEstudiante);
        when(trabajoGradoRepository.findByEstudianteId(idEstudiante)).thenReturn(listaTrabajosGrado);

        InformationException exception = assertThrows(InformationException.class, () -> {
            inicioTrabajoGradoServiceImpl.crearTrabajoGrado(idEstudiante, token);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Ya existe un trabajo de grado aprobado";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void CrearTrabajoGradoTest_RegistroEnProceso() {
        Long idEstudiante = 1L;
        String token = "token";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        InformacionMaestriaActual informacionMaestriaActual = new InformacionMaestriaActual();
        informacionMaestriaActual.setEstadoMaestria(EstadoMaestriaActual.ACTIVO);

        PersonaDto personaDto = new PersonaDto();
        personaDto.setIdentificacion(123L);
        personaDto.setNombre("Julio");
        personaDto.setApellido("Mellizo");
        personaDto.setCorreoElectronico("julio@example.com");

        EstudianteResponseDtoAll informacionEstudiante = new EstudianteResponseDtoAll();
        informacionEstudiante.setId(idEstudiante);
        informacionEstudiante.setPersona(personaDto);
        informacionEstudiante.setInformacionMaestria(informacionMaestriaActual);

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(1L);
        trabajoGrado.setIdEstudiante(1L);
        trabajoGrado.setFechaCreacion(LocalDate.parse("2024-07-12", formatter));
        trabajoGrado.setNumeroEstado(20);
        trabajoGrado.setTitulo("Ciclo del software");

        List<TrabajoGrado> listaTrabajosGrado = new ArrayList<>();
        listaTrabajosGrado.add(trabajoGrado);

        when(archivoClient.obtenerInformacionEstudiante(idEstudiante)).thenReturn(informacionEstudiante);
        when(trabajoGradoRepository.findByEstudianteId(idEstudiante)).thenReturn(listaTrabajosGrado);

        InformationException exception = assertThrows(InformationException.class, () -> {
            inicioTrabajoGradoServiceImpl.crearTrabajoGrado(idEstudiante, token);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Ya existe un trabajo de grado en proceso";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void CrearTrabajoGradoTest_EstudianteNoExiste() {
        Long idEstudiante = 4L;
        String token = "token";

        when(archivoClient.obtenerInformacionEstudiante(idEstudiante))
                .thenThrow(new ResourceNotFoundException("Estudiantes con id "
                        + idEstudiante + " no encontrado"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            inicioTrabajoGradoServiceImpl.crearTrabajoGrado(idEstudiante, token);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Estudiantes con id 4 no encontrado";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void CrearTrabajoGradoTest_ServidorCaido() {
        Long idEstudiante = 1L;
        String token = "token";

        when(archivoClient.obtenerInformacionEstudiante(idEstudiante)).thenThrow(new ServiceUnavailableException(
                "Servidor externo actualmente fuera de servicio"));

        ServiceUnavailableException thrown = assertThrows(
                ServiceUnavailableException.class,
                () -> inicioTrabajoGradoServiceImpl.crearTrabajoGrado(idEstudiante, token),
                "Servidor externo actualmente fuera de servicio");

        assertNotNull(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
    }

}
