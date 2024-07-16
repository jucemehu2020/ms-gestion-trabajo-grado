package com.unicauca.maestria.api.gestiontrabajosgrado.SustentacionProyectoInvestigacion;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientEgresados;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientExpertos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.Genero;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.TipoIdentificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.ExpertoInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ServiceUnavailableException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaComiteSustentacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SustentacionProyectoInvestigacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TiemposPendientesRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ObtenerExpertoSTest {

        @Mock
        private SustentacionProyectoInvestigacionRepository sustentacionProyectoInvestigacionRepository;
        @Mock
        private SustentacionProyectoInvestigacionMapper sustentacionProyectoIngestigacionMapper;
        @Mock
        private SustentacionProyectoInvestigacionResponseMapper sustentacionProyectoInvestigacionResponseMapper;
        @Mock
        private RespuestaComiteSustentacionRepository respuestaComiteSustentacionRepository;
        @Mock
        private TrabajoGradoRepository trabajoGradoRepository;
        @Mock
        private TiemposPendientesRepository tiemposPendientesRepository;
        @Mock
        private ArchivoClient archivoClient;
        @Mock
        private ArchivoClientExpertos archivoClientExpertos;
        @Mock
        private ArchivoClientEgresados archivoClientEgresados;
        @Mock
        private BindingResult result;
        @Mock
        private EnvioCorreos envioCorreos;
        @InjectMocks
        private SustentacionProyectoInvestigacionServiceImpl sustentacionProyectoInvestigacionServiceImpl;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                sustentacionProyectoInvestigacionServiceImpl = new SustentacionProyectoInvestigacionServiceImpl(
                                sustentacionProyectoInvestigacionRepository,
                                sustentacionProyectoIngestigacionMapper,
                                sustentacionProyectoInvestigacionResponseMapper,
                                respuestaComiteSustentacionRepository,
                                trabajoGradoRepository,
                                tiemposPendientesRepository,
                                archivoClient,
                                archivoClientExpertos,
                                archivoClientEgresados);
                ReflectionTestUtils.setField(sustentacionProyectoInvestigacionServiceImpl, "envioCorreos",
                                envioCorreos);
        }

        @Test
        void ObtenerExpertoSTest_InformacionExitosa() {
                Long idExperto = 1L;

                PersonaDto persona = PersonaDto.builder()
                                .id(4L)
                                .identificacion(1098L)
                                .nombre("Cesar")
                                .apellido("Hurtado")
                                .correoElectronico("julio.mellizo@gse.com.co")
                                .telefono("316834759")
                                .genero(Genero.MASCULINO)
                                .tipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA)
                                .build();

                ExpertoResponseDto expertoResponse = ExpertoResponseDto.builder()
                                .id(6L)
                                .persona(persona)
                                .universidadtitexp("Universidad de Mexico")
                                .build();

                when(archivoClientExpertos.obtenerExpertoPorId(idExperto)).thenReturn(expertoResponse);

                ExpertoInfoDto informacionGeneralResponseDtoEsperado = ExpertoInfoDto.builder()
                                .id(expertoResponse.getId())
                                .nombre(expertoResponse.getPersona().getNombre())
                                .apellido(expertoResponse.getPersona().getApellido())
                                .correo(expertoResponse.getPersona().getCorreoElectronico())
                                .universidad(expertoResponse.getUniversidadtitexp())
                                .build();

                ExpertoInfoDto resultado = sustentacionProyectoInvestigacionServiceImpl.obtenerExperto(idExperto);

                assertNotNull(resultado);
                assertEquals(informacionGeneralResponseDtoEsperado, resultado);

        }

        @Test
        void ObtenerExpertoSTest_NoExisteDocente() {
                Long idExperto = 2L;

                when(archivoClientExpertos.obtenerExpertoPorId(idExperto))
                                .thenThrow(new ResourceNotFoundException("Expertos con id "
                                                + idExperto + " no encontrado"));

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.obtenerExperto(idExperto);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Expertos con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void ObtenerExpertoSTest_ServidorDocenteCaido() {
                Long idExperto = 1L;

                when(archivoClientExpertos.obtenerExpertoPorId(idExperto))
                                .thenThrow(new ServiceUnavailableException(
                                                "Servidor externo actualmente fuera de servicio"));

                ServiceUnavailableException thrown = assertThrows(
                                ServiceUnavailableException.class,
                                () -> sustentacionProyectoInvestigacionServiceImpl.obtenerExperto(idExperto),
                                "Servidor externo actualmente fuera de servicio");

                assertNotNull(thrown.getMessage());
                assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
        }

}
