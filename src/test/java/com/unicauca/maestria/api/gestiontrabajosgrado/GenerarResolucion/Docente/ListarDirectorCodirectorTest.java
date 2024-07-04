package com.unicauca.maestria.api.gestiontrabajosgrado.GenerarResolucion.Docente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.TipoIdentificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.DirectorAndCodirectorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ServiceUnavailableException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.GeneracionResolucionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.GeneracionResolucionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.GeneracionResolucionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaComiteGeneracionResolucionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.generacion_resolucion.GeneracionResolucionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ListarDirectorCodirectorTest {

    @Mock
    private GeneracionResolucionRepository generacionResolucionRepository;
    @Mock
    private GeneracionResolucionMapper generacionResolucionMapper;
    @Mock
    private GeneracionResolucionResponseMapper generacionResolucionResponseMapper;
    @Mock
    private TrabajoGradoRepository trabajoGradoRepository;
    @Mock
    private RespuestaComiteGeneracionResolucionRepository respuestaComiteGeneracionResolucionRepository;
    @Mock
    private ArchivoClient archivoClient;
    @Mock
    private BindingResult result;
    @Mock
    private EnvioCorreos envioCorreos;
    @InjectMocks
    private GeneracionResolucionServiceImpl generacionResolucionServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        generacionResolucionServiceImpl = new GeneracionResolucionServiceImpl(
                generacionResolucionRepository,
                generacionResolucionMapper,
                generacionResolucionResponseMapper,
                trabajoGradoRepository,
                respuestaComiteGeneracionResolucionRepository,
                archivoClient);
        ReflectionTestUtils.setField(generacionResolucionServiceImpl, "envioCorreos", envioCorreos);
    }

    @Test
    void testListarDirectorCodirectorTest_Exito() {

        PersonaDto persona1Dto = new PersonaDto();
        persona1Dto.setTipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA);
        persona1Dto.setIdentificacion(123L);
        persona1Dto.setNombre("Luis");
        persona1Dto.setApellido("Perez");

        PersonaDto persona2Dto = new PersonaDto();
        persona2Dto.setTipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA);
        persona2Dto.setIdentificacion(1234L);
        persona2Dto.setNombre("Karla");
        persona2Dto.setApellido("Ortiz");

        DocenteResponseDto docenteResponse1Dto = new DocenteResponseDto();
        docenteResponse1Dto.setId(1L);
        docenteResponse1Dto.setPersona(persona1Dto);

        DocenteResponseDto docenteResponse2Dto = new DocenteResponseDto();
        docenteResponse2Dto.setId(2L);
        docenteResponse2Dto.setPersona(persona2Dto);

        List<DocenteResponseDto> listaDicrectorCodirector = new ArrayList<>();
        listaDicrectorCodirector.add(docenteResponse1Dto);
        listaDicrectorCodirector.add(docenteResponse2Dto);

        when(archivoClient.listarDocentesRes()).thenReturn(listaDicrectorCodirector);

        List<DirectorAndCodirectorResponseDto> resultado = generacionResolucionServiceImpl
                .listarDirectorAndCodirector();

        assertEquals(2, resultado.size());

        DirectorAndCodirectorResponseDto resultado1 = resultado.get(0);
        assertEquals(1L, resultado1.getId());
        assertEquals(TipoIdentificacion.CEDULA_CIUDADANIA, resultado1.getTipoIdentificacion());
        assertEquals(123L, resultado1.getIdentificacion());
        assertEquals("Luis", resultado1.getNombre());
        assertEquals("Perez", resultado1.getApellido());

        DirectorAndCodirectorResponseDto resultado2 = resultado.get(1);
        assertEquals(2L, resultado2.getId());
        assertEquals(TipoIdentificacion.CEDULA_CIUDADANIA, resultado2.getTipoIdentificacion());
        assertEquals(1234L, resultado2.getIdentificacion());
        assertEquals("Karla", resultado2.getNombre());
        assertEquals("Ortiz", resultado2.getApellido());
    }

    @Test
    void testListarDirectorCodirectorTest_NoHayDocentes() {

        when(archivoClient.listarDocentesRes()).thenReturn(new ArrayList<>());

        InformationException exception = assertThrows(InformationException.class, () -> {
            generacionResolucionServiceImpl.listarDirectorAndCodirector();
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "No hay docentes registrados";

        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void testListarDirectorCodirectorTest_ServidorDocenteCaido() {
        when(archivoClient.listarDocentesRes())
                .thenThrow(new ServiceUnavailableException(
                        "Servidor externo actualmente fuera de servicio"));

        ServiceUnavailableException thrown = assertThrows(
                ServiceUnavailableException.class,
                () -> generacionResolucionServiceImpl.listarDirectorAndCodirector(),
                "Servidor externo actualmente fuera de servicio");

        assertNotNull(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
    }

}
