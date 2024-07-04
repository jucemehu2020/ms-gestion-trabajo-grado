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

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientExpertos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.Genero;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.TipoIdentificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.ExpertoInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ServiceUnavailableException;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion.SolicitudExamenValoracionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ListarExpertosTest {

        @Mock
        private ArchivoClientExpertos archivoClientExpertos;

        @InjectMocks
        private SolicitudExamenValoracionServiceImpl expertoService;

        @BeforeEach
        public void setUp() {
                expertoService = new SolicitudExamenValoracionServiceImpl(null, null, null, null, null, null, null,
                                null,
                                archivoClientExpertos);
        }

        @Test
        public void testListarExperto_Exito() {
                // Configurar los datos simulados
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
                                .universidad("Universidad de Mexico")
                                .build();

                // Configurar el mock para devolver la lista esperada
                when(archivoClientExpertos.listar()).thenReturn(List.of(expertoResponse));

                // Ejecutar el método
                List<ExpertoInfoDto> result = expertoService.listarExpertos();

                // Verificar que el método mockeado fue llamado
                verify(archivoClientExpertos).listar();

                // Verificar resultados
                assertNotNull(result);
                assertEquals(1, result.size());
                assertEquals("Cesar", result.get(0).getNombre());
                assertEquals("Hurtado", result.get(0).getApellido());
                assertEquals("julio.mellizo@gse.com.co", result.get(0).getCorreo());
                assertEquals("Universidad de Mexico", result.get(0).getUniversidad());
        }

        @Test
        public void testListarExpertos_NoHayExperto() {
                // Configurar el mock para devolver una lista vacía
                when(archivoClientExpertos.listar()).thenReturn(Collections.emptyList());

                // Ejecutar el método y verificar que lanza la excepción esperada
                InformationException thrown = assertThrows(
                                InformationException.class,
                                () -> expertoService.listarExpertos(),
                                "Expected listarExpertos() to throw, but it didn't");

                assertTrue(thrown.getMessage().contains("No hay expertos registrados"));

                // Verificar que el método mockeado fue llamado
                verify(archivoClientExpertos).listar();
        }

        @Test
        void testListarInformacionDocente_ServidorExpertoCaido() {
                when(archivoClientExpertos.listar())
                                .thenThrow(new ServiceUnavailableException(
                                                "Servidor externo actualmente fuera de servicio"));

                ServiceUnavailableException thrown = assertThrows(
                                ServiceUnavailableException.class,
                                () -> expertoService.listarExpertos(),
                                "Servidor externo actualmente fuera de servicio");

                assertNotNull(thrown.getMessage());
                assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
        }

}
