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
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.DocenteInfoDto;
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
public class ListarDocentesSEVTest {

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
        void ListarDocentesSEVTest_Exito() {

                PersonaDto persona = PersonaDto.builder()
                                .id(4L)
                                .identificacion(1098L)
                                .nombre("Karla")
                                .apellido("Ramirez")
                                .correoElectronico("julio.mellizo@gse.com.co")
                                .telefono("316-325-33-40")
                                .genero(Genero.FEMENINO)
                                .tipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA)
                                .build();

                DocenteResponseDto docenteResponse = DocenteResponseDto.builder()
                                .id(6L)
                                .persona(persona)
                                .build();

                when(archivoClient.listarDocentesRes()).thenReturn(List.of(docenteResponse));

                List<DocenteInfoDto> result = solicitudExamenValoracionService.listarDocentes();


                assertNotNull(result);
                assertEquals(1, result.size());
                assertEquals("Karla", result.get(0).getNombre());
                assertEquals("Ramirez", result.get(0).getApellido());
                assertEquals("julio.mellizo@gse.com.co", result.get(0).getCorreo());
                assertEquals("Universidad del Cauca", result.get(0).getUniversidad());
        }

        @Test
        void ListarDocentesSEVTest_NoHayDocentes() {
                when(archivoClient.listarDocentesRes()).thenReturn(Collections.emptyList());

                InformationException thrown = assertThrows(
                                InformationException.class,
                                () -> solicitudExamenValoracionService.listarDocentes(),
                                "Expected listarDocentes() to throw, but it didn't");

                assertTrue(thrown.getMessage().contains("No hay docentes registrados"));

                verify(archivoClient).listarDocentesRes();
        }

        @Test
        void ListarDocentesSEVTest_ServidorDocenteCaido() {
                when(archivoClient.listarDocentesRes())
                                .thenThrow(new ServiceUnavailableException(
                                                "Servidor externo actualmente fuera de servicio"));

                ServiceUnavailableException thrown = assertThrows(
                                ServiceUnavailableException.class,
                                () -> solicitudExamenValoracionService.listarDocentes(),
                                "Servidor externo actualmente fuera de servicio");

                assertNotNull(thrown.getMessage());
                assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
        }

}
