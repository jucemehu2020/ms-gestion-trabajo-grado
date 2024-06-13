package com.unicauca.maestria.api.gestiontrabajosgrado;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.easymock.EasyMockExtension;
import org.easymock.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.docente.AbreviaturaTitulo;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.docente.CategoriaMinCiencia;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.Genero;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.TipoIdentificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.TituloDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.DocenteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion.SolicitudExamenValoracionServiceImpl;

import feign.FeignException;

@ExtendWith(EasyMockExtension.class)
@SpringBootTest
class MsGestionTrabajoGradoApplicationTests {

	@Mock
	private ArchivoClient archivoClient;

	private SolicitudExamenValoracionServiceImpl docenteService;

	@BeforeEach
	public void setUp() {
		docenteService = new SolicitudExamenValoracionServiceImpl(null, null, null, null, archivoClient, null, null);
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
		expect(archivoClient.listarDocentesRes()).andReturn(List.of(docenteResponse));

		// Activar el mock
		replay(archivoClient);

		// Ejecutar el método
		List<DocenteInfoDto> result = docenteService.listarDocentes();
		System.out.println("Result: " + result); // Agregar mensaje de depuración

		// Verificar que el método mockeado fue llamado
		verify(archivoClient);

		// Verificar resultados
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("Karla", result.get(0).getNombre());
		assertEquals("Ramirez", result.get(0).getApellido());
		assertEquals("julio.mellizo@gse.com.co", result.get(0).getCorreo());
		assertEquals("Universidad Del Valle", result.get(0).getUniversidad());
	}

	@Test
	public void testListarDocentes_NoHayDocentes() {
		expect(archivoClient.listarDocentesRes()).andReturn(Collections.emptyList());
		replay(archivoClient);

		InformationException thrown = assertThrows(
				InformationException.class,
				() -> docenteService.listarDocentes(),
				"Expected listarDocentes() to throw, but it didn't");

		assertTrue(thrown.getMessage().contains("No hay docentes registrados"));

		// Verificar que el método mockeado fue llamado
		verify(archivoClient);
	}

	@Test
	public void testListarDocentes_ErrorConexion() {
		// Simula un error 500 utilizando FeignException
		expect(archivoClient.listarDocentesRes()).andThrow(FeignException.errorStatus("listarDocentesRes",
				feign.Response.builder()
						.status(500)
						.reason("Internal Server Error")
						.request(feign.Request.create(feign.Request.HttpMethod.GET, "/docentes", Collections.emptyMap(),
								null, null, null))
						.build()));
		replay(archivoClient);

		FeignException thrown = assertThrows(
				FeignException.class,
				() -> docenteService.listarDocentes(),
				"Expected listarDocentes() to throw, but it didn't");

		assertTrue(thrown.getMessage().contains("500"));

		// Verificar que el método mockeado fue llamado
		verify(archivoClient);
	}
}
