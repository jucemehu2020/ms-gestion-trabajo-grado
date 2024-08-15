package com.unicauca.maestria.api.gestiontrabajosgrado.SolicitudExamenValoracion.Docente;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

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
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ServiceUnavailableException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.AnexoSolicitudExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.AnexosSolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion.SolicitudExamenValoracionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ListarExpertosSEVTest {

        @Mock
        private SolicitudExamenValoracionRepository solicitudExamenValoracionRepository;
        @Mock
        private AnexosSolicitudExamenValoracionRepository anexosSolicitudExamenValoracionRepository;
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
                                anexosSolicitudExamenValoracionRepository,
                                trabajoGradoRepository,
                                null,
                                examenValoracionMapper,
                                examenValoracionResponseMapper,
                                anexoSolicitudExamenValoracionMapper,
                                archivoClient);
        }

        @Test
        void ListarExpertosSEVTest_Exito() {

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

                when(archivoClient.listarExpertos()).thenReturn(List.of(expertoResponse));

                List<ExpertoInfoDto> result = solicitudExamenValoracionService.listarExpertos();

                // Verificar resultados
                assertNotNull(result);
                assertEquals(1, result.size());
                assertEquals("Cesar", result.get(0).getNombre());
                assertEquals("Hurtado", result.get(0).getApellido());
                assertEquals("julio.mellizo@gse.com.co", result.get(0).getCorreo());
                assertEquals("Universidad de Mexico", result.get(0).getUniversidad());
        }

        @Test
        void ListarExpertosSEVTest_NoHayExperto() {
                when(archivoClient.listarExpertos()).thenReturn(Collections.emptyList());

                InformationException thrown = assertThrows(
                                InformationException.class,
                                () -> solicitudExamenValoracionService.listarExpertos(),
                                "Expected listarExpertos() to throw, but it didn't");

                assertTrue(thrown.getMessage().contains("No hay expertos registrados"));
        }

        @Test
        void testListarInformacionDocente_ServidorExpertoCaido() {
                when(archivoClient.listarExpertos())
                                .thenThrow(new ServiceUnavailableException(
                                                "Servidor externo actualmente fuera de servicio"));

                ServiceUnavailableException thrown = assertThrows(
                                ServiceUnavailableException.class,
                                () -> solicitudExamenValoracionService.listarExpertos(),
                                "Servidor externo actualmente fuera de servicio");

                assertNotNull(thrown.getMessage());
                assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
        }

}
