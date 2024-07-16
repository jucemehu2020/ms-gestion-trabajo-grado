package com.unicauca.maestria.api.gestiontrabajosgrado.TrabajoGradoInicial;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientLogin;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.TrabajoGradoResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TiemposPendientesRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.inicio_trabajo_grado.InicioTrabajoGradoServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class DescargarArchivoTest {

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
    }

    @Test
    void DescargarArchivoTest_Exitoso() {
        String rutaArchivo = "ruta/al/archivo.txt";
        String contenidoEsperado = "Contenido del archivo";

        try (MockedStatic<FilesUtilities> utilities = mockStatic(FilesUtilities.class)) {
            utilities.when(() -> FilesUtilities.recuperarArchivo(rutaArchivo)).thenReturn(contenidoEsperado);

            String resultado = inicioTrabajoGradoServiceImpl.descargarArchivo(rutaArchivo);

            assertNotNull(resultado);
            assertEquals(contenidoEsperado, resultado);

            utilities.verify(() -> FilesUtilities.recuperarArchivo(rutaArchivo), times(1));
        }
    }

    @Test
    void DescargarArchivoTest_ArchivoNoExiste() {
        String rutaArchivo = "ruta/al/archivo_no_existe.txt";

        try (MockedStatic<FilesUtilities> utilities = mockStatic(FilesUtilities.class)) {
            utilities.when(() -> FilesUtilities.recuperarArchivo(rutaArchivo))
                    .thenThrow(new InformationException("Error al recuperar el archivo"));

            InformationException exception = assertThrows(InformationException.class, () -> {
                inicioTrabajoGradoServiceImpl.descargarArchivo(rutaArchivo);
            });

            assertNotNull(exception.getMessage());
            String expectedMessage = "Error al recuperar el archivo";
            assertTrue(exception.getMessage().contains(expectedMessage));
        }
    }
}
