package com.unicauca.maestria.api.gestiontrabajosgrado.SolicitudExamenValoracion.Docente;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.Genero;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.TipoIdentificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.ExpertoInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ServiceUnavailableException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.AnexoSolicitudExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion.SolicitudExamenValoracionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ObtenerExpertoSEVTest {

        @Mock
        private SolicitudExamenValoracionRepository solicitudExamenValoracionRepository;
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

        @InjectMocks
        private SolicitudExamenValoracionServiceImpl solicitudExamenValoracionService;

        @BeforeEach
        void setUp() {
                solicitudExamenValoracionService = new SolicitudExamenValoracionServiceImpl(
                                solicitudExamenValoracionRepository,
                                null,
                                null,
                                trabajoGradoRepository,
                                null,
                                examenValoracionMapper,
                                examenValoracionResponseMapper,
                                anexoSolicitudExamenValoracionMapper,
                                archivoClient);
        }

        @Test
        void ObtenerExpertoSEVTest_InformacionExitosa() {
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

                when(archivoClient.obtenerExpertoPorId(idExperto)).thenReturn(expertoResponse);

                ExpertoInfoDto informacionGeneralResponseDtoEsperado = ExpertoInfoDto.builder()
                                .id(expertoResponse.getId())
                                .nombre(expertoResponse.getPersona().getNombre())
                                .apellido(expertoResponse.getPersona().getApellido())
                                .correo(expertoResponse.getPersona().getCorreoElectronico())
                                .universidad(expertoResponse.getUniversidadtitexp())
                                .build();

                ExpertoInfoDto resultado = solicitudExamenValoracionService.obtenerExperto(idExperto);

                assertNotNull(resultado);
                assertEquals(informacionGeneralResponseDtoEsperado, resultado);

        }

        @Test
        void ObtenerExpertoSEVTest_NoExisteDocente() {
                Long idExperto = 2L;

                when(archivoClient.obtenerExpertoPorId(idExperto))
                                .thenThrow(new ResourceNotFoundException("Expertos con id "
                                                + idExperto + " no encontrado"));

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        solicitudExamenValoracionService.obtenerExperto(idExperto);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Expertos con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void ObtenerExpertoSEVTest_ServidorDocenteCaido() {
                Long idExperto = 1L;

                when(archivoClient.obtenerExpertoPorId(idExperto))
                                .thenThrow(new ServiceUnavailableException(
                                                "Servidor externo actualmente fuera de servicio"));

                ServiceUnavailableException thrown = assertThrows(
                                ServiceUnavailableException.class,
                                () -> solicitudExamenValoracionService.obtenerExperto(idExperto),
                                "Servidor externo actualmente fuera de servicio");

                assertNotNull(thrown.getMessage());
                assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
        }

}
