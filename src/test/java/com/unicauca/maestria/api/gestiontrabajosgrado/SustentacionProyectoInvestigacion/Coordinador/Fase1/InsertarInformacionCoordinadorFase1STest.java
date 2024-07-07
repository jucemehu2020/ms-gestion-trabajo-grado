package com.unicauca.maestria.api.gestiontrabajosgrado.SustentacionProyectoInvestigacion.Coordinador.Fase1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientEgresados;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientExpertos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.SustentacionTrabajoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.EnvioEmailDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.ObtenerDocumentosParaEnvioDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.STICoordinadorFase1ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.SustentacionTrabajoInvestigacionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ServiceUnavailableException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaComiteSustentacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SustentacionProyectoInvestigacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class InsertarInformacionCoordinadorFase1STest {

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
    private ArchivoClientExpertos archivoClientExpertos;
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
                archivoClient,
                archivoClientExpertos,
                archivoClientEgresados);
        ReflectionTestUtils.setField(sustentacionProyectoInvestigacionServiceImpl, "envioCorreos",
                envioCorreos);
    }

    @Test
    void SustentacionProyectoInvestigacionServiceImplTest_RegistroExitosoTrue() {

        Long idTrabajoGrado = 1L;

        ObtenerDocumentosParaEnvioDto obtenerDocumentosParaEnvioDto = new ObtenerDocumentosParaEnvioDto();
        obtenerDocumentosParaEnvioDto.setB64FormatoF("cHJ1ZWJhIGRlIHRleHR");

        EnvioEmailDto envioEmailDto = new EnvioEmailDto();
        envioEmailDto.setAsunto("Solicitud revision comite");
        envioEmailDto.setMensaje("Comedidamente solicito revisar los documentos.");

        SustentacionTrabajoInvestigacionCoordinadorFase1Dto sustentacionTrabajoInvestigacionCoordinadorFase1Dto = new SustentacionTrabajoInvestigacionCoordinadorFase1Dto();
        sustentacionTrabajoInvestigacionCoordinadorFase1Dto.setConceptoCoordinador(ConceptoVerificacion.ACEPTADO);
        sustentacionTrabajoInvestigacionCoordinadorFase1Dto
                .setObtenerDocumentosParaEnvio(obtenerDocumentosParaEnvioDto);
        sustentacionTrabajoInvestigacionCoordinadorFase1Dto.setEnvioEmail(envioEmailDto);

        when(result.hasErrors()).thenReturn(false);

        SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionTrabajoInvestigacion();
        sustentacionTrabajoInvestigacionOld.setIdSustentacionTrabajoInvestigacion(1L);

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(idTrabajoGrado);
        trabajoGrado.setTitulo("Prueba test");
        trabajoGrado.setNumeroEstado(18);
        trabajoGrado.setIdEstudiante(123L);
        trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
        trabajoGrado.setIdSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));
        when(sustentacionProyectoInvestigacionRepository.findById(
                trabajoGrado.getIdSustentacionProyectoInvestigacion().getIdSustentacionTrabajoInvestigacion()))
                .thenReturn(Optional.of(sustentacionTrabajoInvestigacionOld));

        when(envioCorreos.enviarCorreoConAnexos(any(ArrayList.class), anyString(), anyString(), anyMap()))
                .thenReturn(true);

        SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacionNew = new SustentacionTrabajoInvestigacion();
        sustentacionTrabajoInvestigacionNew.setIdSustentacionTrabajoInvestigacion(1L);
        sustentacionTrabajoInvestigacionNew
                .setConceptoCoordinador(sustentacionTrabajoInvestigacionCoordinadorFase1Dto.getConceptoCoordinador());

        when(sustentacionProyectoInvestigacionRepository.save(sustentacionTrabajoInvestigacionOld))
                .thenReturn(sustentacionTrabajoInvestigacionNew);

        STICoordinadorFase1ResponseDto stiCoordinadorFase1ResponseDto = new STICoordinadorFase1ResponseDto();
        stiCoordinadorFase1ResponseDto.setIdSustentacionTrabajoInvestigacion(
                sustentacionTrabajoInvestigacionNew.getIdSustentacionTrabajoInvestigacion());
        stiCoordinadorFase1ResponseDto
                .setConceptoCoordinador(sustentacionTrabajoInvestigacionNew.getConceptoCoordinador());

        when(sustentacionProyectoInvestigacionResponseMapper.toCoordinadorFase1Dto(sustentacionTrabajoInvestigacionNew))
                .thenReturn(stiCoordinadorFase1ResponseDto);

        try (MockedStatic<FilesUtilities> utilities = mockStatic(FilesUtilities.class)) {
            utilities.when(() -> FilesUtilities.guardarArchivoNew2(anyString(), anyString()))
                    .thenReturn("path/to/new/file");

            STICoordinadorFase1ResponseDto resultado = sustentacionProyectoInvestigacionServiceImpl
                    .insertarInformacionCoordinadoFase1(idTrabajoGrado,
                            sustentacionTrabajoInvestigacionCoordinadorFase1Dto, result);

            assertNotNull(resultado);
            assertEquals(1L, resultado.getIdSustentacionTrabajoInvestigacion());
            assertEquals(ConceptoVerificacion.ACEPTADO, resultado.getConceptoCoordinador());
        }

    }

    @Test
    void SustentacionProyectoInvestigacionServiceImplTest_RegistroExitosoFalse() {
        Long idTrabajoGrado = 1L;

        EnvioEmailDto envioEmailDto = new EnvioEmailDto();
        envioEmailDto.setAsunto("Revision errores documento");
        envioEmailDto.setMensaje("Comedidamente solicito revisar los documentos para poder enviarlos a comite.");

        SustentacionTrabajoInvestigacionCoordinadorFase1Dto sustentacionTrabajoInvestigacionCoordinadorFase1Dto = new SustentacionTrabajoInvestigacionCoordinadorFase1Dto();
        sustentacionTrabajoInvestigacionCoordinadorFase1Dto.setConceptoCoordinador(ConceptoVerificacion.RECHAZADO);
        sustentacionTrabajoInvestigacionCoordinadorFase1Dto.setEnvioEmail(envioEmailDto);

        when(result.hasErrors()).thenReturn(false);

        SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionTrabajoInvestigacion();
        sustentacionTrabajoInvestigacionOld.setIdSustentacionTrabajoInvestigacion(1L);

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(idTrabajoGrado);
        trabajoGrado.setTitulo("Prueba test");
        trabajoGrado.setNumeroEstado(18);
        trabajoGrado.setIdEstudiante(123L);
        trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
        trabajoGrado.setIdSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));
        when(sustentacionProyectoInvestigacionRepository.findById(
                trabajoGrado.getIdSustentacionProyectoInvestigacion().getIdSustentacionTrabajoInvestigacion()))
                .thenReturn(Optional.of(sustentacionTrabajoInvestigacionOld));

        PersonaDto PersonaEstudianteDto = new PersonaDto();
        PersonaEstudianteDto.setIdentificacion(123L);
        PersonaEstudianteDto.setNombre("Juan");
        PersonaEstudianteDto.setApellido("Meneses");

        EstudianteResponseDtoAll estudianteResponseDtoAll = new EstudianteResponseDtoAll();
        estudianteResponseDtoAll.setPersona(PersonaEstudianteDto);

        when(archivoClient.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante()))
                .thenReturn(estudianteResponseDtoAll);

        when(envioCorreos.enviarCorreosCorrecion(any(ArrayList.class), anyString(), anyString()))
                .thenReturn(true);

        SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacionNew = new SustentacionTrabajoInvestigacion();
        sustentacionTrabajoInvestigacionNew.setIdSustentacionTrabajoInvestigacion(1L);
        sustentacionTrabajoInvestigacionNew
                .setConceptoCoordinador(sustentacionTrabajoInvestigacionCoordinadorFase1Dto.getConceptoCoordinador());

        when(sustentacionProyectoInvestigacionRepository.save(sustentacionTrabajoInvestigacionOld))
                .thenReturn(sustentacionTrabajoInvestigacionNew);

        STICoordinadorFase1ResponseDto stiCoordinadorFase1ResponseDto = new STICoordinadorFase1ResponseDto();
        stiCoordinadorFase1ResponseDto.setIdSustentacionTrabajoInvestigacion(
                sustentacionTrabajoInvestigacionNew.getIdSustentacionTrabajoInvestigacion());
        stiCoordinadorFase1ResponseDto
                .setConceptoCoordinador(sustentacionTrabajoInvestigacionNew.getConceptoCoordinador());

        when(sustentacionProyectoInvestigacionResponseMapper.toCoordinadorFase1Dto(sustentacionTrabajoInvestigacionNew))
                .thenReturn(stiCoordinadorFase1ResponseDto);

        try (MockedStatic<FilesUtilities> utilities = mockStatic(FilesUtilities.class)) {
            utilities.when(() -> FilesUtilities.guardarArchivoNew2(anyString(), anyString()))
                    .thenReturn("path/to/new/file");

            STICoordinadorFase1ResponseDto resultado = sustentacionProyectoInvestigacionServiceImpl
                    .insertarInformacionCoordinadoFase1(idTrabajoGrado,
                            sustentacionTrabajoInvestigacionCoordinadorFase1Dto, result);

            assertNotNull(resultado);
            assertEquals(1L, resultado.getIdSustentacionTrabajoInvestigacion());
            assertEquals(ConceptoVerificacion.RECHAZADO, resultado.getConceptoCoordinador());
        }
    }

    @Test
    void ActualizarInformacionDocenteSTest_FaltanAtributos() {

        Long idTrabajoGrado = 1L;

        SustentacionTrabajoInvestigacionDocenteDto sustentacionTrabajoInvestigacionDocenteDto = new SustentacionTrabajoInvestigacionDocenteDto();
        sustentacionTrabajoInvestigacionDocenteDto.setLinkFormatoF("linkFormatoF.txt-cHJ1ZWJhIGRlIHRleHR");
        sustentacionTrabajoInvestigacionDocenteDto.setUrlDocumentacion("www.archivoPrueba2.com");
        sustentacionTrabajoInvestigacionDocenteDto.setIdJuradoExterno(1L);

        FieldError fieldError = new FieldError("SustentacionTrabajoInvestigacionDocenteDto", "idJuradoInterno",
                "no debe ser nulo");
        when(result.hasErrors()).thenReturn(true);
        when(result.getFieldErrors()).thenReturn(List.of(fieldError));

        FieldErrorException exception = assertThrows(FieldErrorException.class, () -> {
            sustentacionProyectoInvestigacionServiceImpl
                    .actualizarInformacionDocente(idTrabajoGrado,
                            sustentacionTrabajoInvestigacionDocenteDto, result);
        });

        assertNotNull(exception.getResult());
        List<FieldError> fieldErrors = exception.getResult().getFieldErrors();
        assertFalse(fieldErrors.isEmpty());
        String actualMessage = "El campo: " + fieldErrors.get(0).getField() + ", "
                + fieldError.getDefaultMessage();
        String expectedMessage = "El campo: idJuradoInterno, no debe ser nulo";
        assertTrue(actualMessage.contains(expectedMessage));
    }

}
