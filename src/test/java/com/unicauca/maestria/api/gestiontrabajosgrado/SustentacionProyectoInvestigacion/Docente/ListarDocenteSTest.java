package com.unicauca.maestria.api.gestiontrabajosgrado.SustentacionProyectoInvestigacion.Docente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientEgresados;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.docente.AbreviaturaTitulo;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.docente.CategoriaMinCiencia;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.Genero;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.TipoIdentificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.TituloDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.DocenteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ServiceUnavailableException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaComiteSustentacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SustentacionProyectoInvestigacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ListarDocenteSTest {

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
    private ArchivoClient archivoClient;
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
                null,
                archivoClient,
                archivoClientEgresados);
        ReflectionTestUtils.setField(sustentacionProyectoInvestigacionServiceImpl, "envioCorreos",
                envioCorreos);
    }

    @Test
    public void ListarDocenteSTest_Exito() {
        // Configurar los datos simulados
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

        TituloDto titulo = TituloDto.builder()
                .id(6L)
                .abreviatura(AbreviaturaTitulo.ING)
                .universidad("Universidad Del Valle")
                .categoriaMinCiencia(CategoriaMinCiencia.ASOCIADO)
                .linkCvLac("http:aall.uni")
                .build();

        DocenteResponseDto docenteResponse = DocenteResponseDto.builder()
                .id(6L)
                .persona(persona)
                .titulos(List.of(titulo))
                .build();

        // Configurar el mock para devolver la lista esperada
        when(archivoClient.listarDocentesRes()).thenReturn(List.of(docenteResponse));

        // Ejecutar el método
        List<DocenteInfoDto> result = sustentacionProyectoInvestigacionServiceImpl.listarDocentes();

        // Verificar que el método mockeado fue llamado
        verify(archivoClient).listarDocentesRes();

        // Verificar resultados
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Karla", result.get(0).getNombre());
        assertEquals("Ramirez", result.get(0).getApellido());
        assertEquals("julio.mellizo@gse.com.co", result.get(0).getCorreo());
        assertEquals("Universidad del Cauca", result.get(0).getUniversidad());
    }

    @Test
    public void ListarDocenteSTest_NoHayDocentes() {
        // Configurar el mock para devolver una lista vacía
        when(archivoClient.listarDocentesRes()).thenReturn(Collections.emptyList());

        // Ejecutar el método y verificar que lanza la excepción esperada
        InformationException thrown = assertThrows(
                InformationException.class,
                () -> sustentacionProyectoInvestigacionServiceImpl.listarDocentes(),
                "Expected listarDocentes() to throw, but it didn't");

        assertTrue(thrown.getMessage().contains("No hay docentes registrados"));

        // Verificar que el método mockeado fue llamado
        verify(archivoClient).listarDocentesRes();
    }

    @Test
    void ListarDocenteSTest_ServidorDocenteCaido() {
        when(archivoClient.listarDocentesRes())
                .thenThrow(new ServiceUnavailableException(
                        "Servidor externo actualmente fuera de servicio"));

        ServiceUnavailableException thrown = assertThrows(
                ServiceUnavailableException.class,
                () -> sustentacionProyectoInvestigacionServiceImpl.listarDocentes(),
                "Servidor externo actualmente fuera de servicio");

        assertNotNull(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
    }

}