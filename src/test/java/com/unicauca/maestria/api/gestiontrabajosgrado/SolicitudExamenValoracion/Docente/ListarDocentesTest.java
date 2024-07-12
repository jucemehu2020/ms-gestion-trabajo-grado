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

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.docente.AbreviaturaTitulo;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.docente.CategoriaMinCiencia;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.Genero;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.TipoIdentificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.TituloDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.DocenteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ServiceUnavailableException;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion.SolicitudExamenValoracionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ListarDocentesTest {

        @Mock
        private ArchivoClient archivoClient;

        @InjectMocks
        private SolicitudExamenValoracionServiceImpl docenteService;

        @BeforeEach
        public void setUp() {
                docenteService = new SolicitudExamenValoracionServiceImpl(null, null, null, null, null, null, null, null,
                                archivoClient, null);
        }

        @Test
        public void testListarDocentes_Exito() {
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
                List<DocenteInfoDto> result = docenteService.listarDocentes();
                System.out.println("Result: " + result); // Agregar mensaje de depuración

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
        public void testListarDocentes_NoHayDocentes() {
                // Configurar el mock para devolver una lista vacía
                when(archivoClient.listarDocentesRes()).thenReturn(Collections.emptyList());

                // Ejecutar el método y verificar que lanza la excepción esperada
                InformationException thrown = assertThrows(
                                InformationException.class,
                                () -> docenteService.listarDocentes(),
                                "Expected listarDocentes() to throw, but it didn't");

                assertTrue(thrown.getMessage().contains("No hay docentes registrados"));

                // Verificar que el método mockeado fue llamado
                verify(archivoClient).listarDocentesRes();
        }

        @Test
        void testListarInformacionDocente_ServidorDocenteCaido() {
                when(archivoClient.listarDocentesRes())
                                .thenThrow(new ServiceUnavailableException(
                                                "Servidor externo actualmente fuera de servicio"));

                ServiceUnavailableException thrown = assertThrows(
                                ServiceUnavailableException.class,
                                () -> docenteService.listarDocentes(),
                                "Servidor externo actualmente fuera de servicio");

                assertNotNull(thrown.getMessage());
                assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
        }

}
