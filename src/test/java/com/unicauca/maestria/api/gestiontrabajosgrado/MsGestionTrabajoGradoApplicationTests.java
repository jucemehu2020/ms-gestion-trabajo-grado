package com.unicauca.maestria.api.gestiontrabajosgrado;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.unicauca.maestria.api.gestiontrabajosgrado.SolicitudExamenValoracion.Docente.ListarDocentesSEVTest;

@ExtendWith({ MockitoExtension.class, SpringExtension.class })
@SpringBootTest
class MsGestionTrabajoGradoApplicationTests {

    @Nested
    @SpringBootTest
    @ExtendWith(MockitoExtension.class)
    class MsGestionTrabajoGradoIntegrationTests2Test extends ListarDocentesSEVTest {

        @Test
        public void testListarDocentes_Exito() {
            super.testListarDocentes_Exito();
        }

        @Test
        public void testListarDocentes_NoHayDocentes() {
            super.testListarDocentes_NoHayDocentes();
        }

        // @Test
        // public void testListarDocentes_ErrorConexion() {
        // super.testListarDocentes_ErrorConexion();
        // }
    }
}
