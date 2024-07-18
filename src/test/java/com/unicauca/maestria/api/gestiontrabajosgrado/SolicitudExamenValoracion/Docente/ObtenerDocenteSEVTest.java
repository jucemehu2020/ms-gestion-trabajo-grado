package com.unicauca.maestria.api.gestiontrabajosgrado.SolicitudExamenValoracion.Docente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.AnexoSolicitudExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion.SolicitudExamenValoracionServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.Genero;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.TipoIdentificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.DocenteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ServiceUnavailableException;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ObtenerDocenteSEVTest {
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
        Long idDocente = 1L;

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

        when(archivoClient.obtenerDocentePorId(idDocente)).thenReturn(docenteResponse);

        DocenteInfoDto informacionGeneralResponseDtoEsperado = DocenteInfoDto.builder()
                .id(docenteResponse.getId())
                .nombre(docenteResponse.getPersona().getNombre())
                .apellido(docenteResponse.getPersona().getApellido())
                .correo(docenteResponse.getPersona().getCorreoElectronico())
                .universidad("Universidad del Cauca")
                .build();

        DocenteInfoDto resultado = solicitudExamenValoracionService.obtenerDocente(idDocente);

        assertNotNull(resultado);
        assertEquals(informacionGeneralResponseDtoEsperado, resultado);

    }

    @Test
    void ObtenerExpertoSEVTest_NoExisteDocente() {
        Long idDocente = 2L;

        when(archivoClient.obtenerDocentePorId(idDocente))
                .thenThrow(new ResourceNotFoundException("Docentes con id "
                        + idDocente + " no encontrado"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            solicitudExamenValoracionService.obtenerDocente(idDocente);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Docentes con id 2 no encontrado";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void ObtenerExpertoSEVTest_ServidorDocenteCaido() {
        Long idDocente = 1L;

        when(archivoClient.obtenerDocentePorId(idDocente))
                .thenThrow(new ServiceUnavailableException(
                        "Servidor externo actualmente fuera de servicio"));

        ServiceUnavailableException thrown = assertThrows(
                ServiceUnavailableException.class,
                () -> solicitudExamenValoracionService.obtenerDocente(idDocente),
                "Servidor externo actualmente fuera de servicio");

        assertNotNull(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
    }
}
