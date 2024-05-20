package com.unicauca.maestria.api.gestiontrabajosgrado;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.docente.EstadoPersona;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.Genero;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.TipoIdentificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.estudiante.Persona;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.DocenteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion.SolicitudExamenValoracionServiceImpl;

@SpringBootTest
class MsGestionTrabajoGradoApplicationTests {

	@Mock
	private ArchivoClient archivoClient;

	@InjectMocks
	private SolicitudExamenValoracionServiceImpl solicitudExamenValoracionService;

	@Test
	void contextLoads() {
	}

	// @Test
	// public void listarDocentesTest() {
	// 	// Preparar datos de prueba
	// 	List<DocenteResponseDto> docentesRes = new ArrayList<>();
	// 	DocenteResponseDto docente = new DocenteResponseDto();
	// 	PersonaDto persona = new PersonaDto();
	// 	//Datos estado de la persona
	// 	docente.setEstado(EstadoPersona.ACTIVO);
	// 	//Datos persona
	// 	persona.setIdentificacion(Long.valueOf(123456789));
	// 	persona.setNombre("Julio");
	// 	persona.setApellido("Mellizo");
	// 	persona.setCorreoElectronico("correo@ejemplo.com");
	// 	persona.setTelefono("8243058");
	// 	persona.setGenero(Genero.MASCULINO);
	// 	persona.setTipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA);
	// 	docente.setPersona(persona);
	// 	docentesRes.add(docente);

	// 	// Configurar el comportamiento del mock
	// 	when(archivoClient.listarDocentesRes()).thenReturn(docentesRes);

	// 	// Llamar al método a probar
	// 	List<DocenteInfoDto> resultado = solicitudExamenValoracionService.listarDocentes();

	// 	// Verificar el resultado
	// 	assertEquals(1, resultado.size());
	// 	assertEquals("Julio", resultado.get(0).getNombre());
	// 	assertEquals("Mellizo", resultado.get(0).getApellido());
	// 	assertEquals("correo@ejemplo.com", resultado.get(0).getCorreoUniversitario());


	// 	// Verificar que el método del mock fue llamado
	// 	verify(archivoClient, times(1)).listarDocentesRes();
	// }

}
