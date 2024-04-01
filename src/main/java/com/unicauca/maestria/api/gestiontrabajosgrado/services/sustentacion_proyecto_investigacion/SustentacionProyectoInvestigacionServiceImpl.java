package com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.ConvertString;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.SustentacionTrabajoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.CamposUnicosGenerarResolucionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.GeneracionResolucionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.CamposUnicosSustentacionProyectoInvestigacionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.SustentacionTrabajoInvestigacionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldUniqueException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SustentacionProyectoInvestigacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.generacion_resolucion.InformacionUnicaGeneracionResolucion;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SustentacionProyectoInvestigacionServiceImpl implements SustentacionProyectoInvestigacionService {

        private final SustentacionProyectoInvestigacionRepository sustentacionProyectoInvestigacionRepository;
        private final SustentacionProyectoInvestigacionMapper sustentacionProyectoIngestigacionMapper;
        private final TrabajoGradoRepository trabajoGradoRepository;
        private final InformacionUnicaSustentacionProyectoInvestigacion informacionUnicaSustentacionProyectoInvestigacion;

        @Override
        @Transactional
        public SustentacionTrabajoInvestigacionDto crear(SustentacionTrabajoInvestigacionDto sustentacionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(sustentacionDto.getIdTrabajoGrados())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + sustentacionDto.getIdTrabajoGrados()
                                                                + " No encontrado"));

                Map<String, String> validacionCamposUnicos = validacionCampoUnicos(
                                obtenerCamposUnicos(sustentacionDto),
                                null);
                if (!validacionCamposUnicos.isEmpty()) {
                        throw new FieldUniqueException(validacionCamposUnicos);
                }

                // Obtener iniciales del trabajo de grado
                String procesoVa = "Sustentacion_Proyecto_Investigacion";
                String tituloTrabajoGrado = ConvertString.obtenerIniciales(trabajoGrado.getTitulo());

                Long idenficiacionEstudiante = trabajoGrado.getEstudiante().getPersona().getIdentificacion();
                String nombreEstudiante = trabajoGrado.getEstudiante().getPersona().getNombre();
                String apellidoEstudiante = trabajoGrado.getEstudiante().getPersona().getApellido();
                String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

                // Mapear DTO a entidad
                SustentacionTrabajoInvestigacion examenValoracion = sustentacionProyectoIngestigacionMapper
                                .toEntity(sustentacionDto);

                // Establecer la relación uno a uno
                examenValoracion.setIdTrabajoGrado(trabajoGrado);
                trabajoGrado.setIdSustentacionProyectoInvestigacion(examenValoracion);

                // Se cambia el numero de estado
                int numEstado = validarEstado(sustentacionDto.getRespuestaSustentacion());
                trabajoGrado.setNumeroEstado(numEstado);

                // Guardar la entidad ExamenValoracion
                examenValoracion.setLinkRemisionDocumentoFinal(
                                FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                examenValoracion.getLinkRemisionDocumentoFinal(), nombreCarpeta));
                examenValoracion.setLinkRemisionDocumentoFinalCF(
                                FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                examenValoracion.getLinkRemisionDocumentoFinalCF(), nombreCarpeta));
                examenValoracion.setLinkConstanciaDocumentoFinal(
                                FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                examenValoracion.getLinkConstanciaDocumentoFinal(), nombreCarpeta));
                examenValoracion
                                .setLinkActaSustentacion(FilesUtilities
                                                .guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                                examenValoracion.getLinkActaSustentacion(),
                                                                nombreCarpeta));
                examenValoracion.setLinkActaSustentacionPublica(
                                FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                examenValoracion.getLinkActaSustentacionPublica(), nombreCarpeta));
                examenValoracion.setLinkEstudioHojaVidaAcademica(
                                FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                examenValoracion.getLinkEstudioHojaVidaAcademica(), nombreCarpeta));

                SustentacionTrabajoInvestigacion examenValoracionRes = sustentacionProyectoInvestigacionRepository
                                .save(examenValoracion);

                return sustentacionProyectoIngestigacionMapper.toDto(examenValoracionRes);
        }

        @Override
        @Transactional(readOnly = true)
        public SustentacionTrabajoInvestigacionDto buscarPorId(Long idTrabajoGrado) {
                return sustentacionProyectoInvestigacionRepository.findById(idTrabajoGrado)
                                .map(sustentacionProyectoIngestigacionMapper::toDto)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Examen de valoracion con id: " + idTrabajoGrado + " no encontrado"));
        }

        @Override
        public SustentacionTrabajoInvestigacionDto actualizar(Long id,
                        SustentacionTrabajoInvestigacionDto sustentacionDto, BindingResult result) {

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                SustentacionTrabajoInvestigacion examenValoracionTmp = sustentacionProyectoInvestigacionRepository
                                .findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion trabajo de investigacion con id: " + id
                                                                + " no encontrado"));

                // Busca el trabajo de grado
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(id).orElseThrow(
                                () -> new ResourceNotFoundException(
                                                "Trabajo de grado con id: " + id + " no encontrado"));

                String procesoVa = "Sustentacion_Proyecto_Investigacion";
                String tituloTrabajoGrado = ConvertString.obtenerIniciales(trabajoGrado.getTitulo());
                Long idenficiacionEstudiante = trabajoGrado.getEstudiante().getPersona().getIdentificacion();
                String nombreEstudiante = trabajoGrado.getEstudiante().getPersona().getNombre();
                String apellidoEstudiante = trabajoGrado.getEstudiante().getPersona().getApellido();
                String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

                SustentacionTrabajoInvestigacion responseExamenValoracion = null;
                if (examenValoracionTmp != null) {
                        if (sustentacionDto.getLinkRemisionDocumentoFinal()
                                        .compareTo(examenValoracionTmp.getLinkRemisionDocumentoFinal()) != 0) {
                                sustentacionDto
                                                .setLinkRemisionDocumentoFinal(FilesUtilities.guardarArchivoNew(
                                                                tituloTrabajoGrado, procesoVa,
                                                                sustentacionDto.getLinkRemisionDocumentoFinal(),
                                                                nombreCarpeta));
                                FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkRemisionDocumentoFinal());
                        }
                        if (sustentacionDto.getLinkRemisionDocumentoFinalCF()
                                        .compareTo(examenValoracionTmp.getLinkRemisionDocumentoFinalCF()) != 0) {
                                sustentacionDto
                                                .setLinkRemisionDocumentoFinalCF(FilesUtilities.guardarArchivoNew(
                                                                tituloTrabajoGrado, procesoVa,
                                                                sustentacionDto.getLinkRemisionDocumentoFinalCF(),
                                                                nombreCarpeta));
                                FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkRemisionDocumentoFinalCF());
                        }
                        if (sustentacionDto.getLinkConstanciaDocumentoFinal()
                                        .compareTo(examenValoracionTmp.getLinkConstanciaDocumentoFinal()) != 0) {
                                sustentacionDto
                                                .setLinkConstanciaDocumentoFinal(FilesUtilities.guardarArchivoNew(
                                                                tituloTrabajoGrado, procesoVa,
                                                                sustentacionDto.getLinkConstanciaDocumentoFinal(),
                                                                nombreCarpeta));
                                FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkConstanciaDocumentoFinal());
                        }
                        if (sustentacionDto.getLinkActaSustentacion()
                                        .compareTo(examenValoracionTmp.getLinkActaSustentacion()) != 0) {
                                sustentacionDto
                                                .setLinkActaSustentacion(FilesUtilities.guardarArchivoNew(
                                                                tituloTrabajoGrado, procesoVa,
                                                                sustentacionDto.getLinkActaSustentacion(),
                                                                nombreCarpeta));
                                FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkActaSustentacion());
                        }
                        if (sustentacionDto.getLinkActaSustentacionPublica()
                                        .compareTo(examenValoracionTmp.getLinkActaSustentacionPublica()) != 0) {
                                sustentacionDto
                                                .setLinkActaSustentacionPublica(FilesUtilities.guardarArchivoNew(
                                                                tituloTrabajoGrado, procesoVa,
                                                                sustentacionDto.getLinkActaSustentacionPublica(),
                                                                nombreCarpeta));
                                FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkActaSustentacionPublica());
                        }
                        if (sustentacionDto.getLinkEstudioHojaVidaAcademica()
                                        .compareTo(examenValoracionTmp.getLinkEstudioHojaVidaAcademica()) != 0) {
                                sustentacionDto
                                                .setLinkEstudioHojaVidaAcademica(FilesUtilities.guardarArchivoNew(
                                                                tituloTrabajoGrado, procesoVa,
                                                                sustentacionDto.getLinkEstudioHojaVidaAcademica(),
                                                                nombreCarpeta));
                                FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkEstudioHojaVidaAcademica());
                        }
                        updateExamenValoracionValues(examenValoracionTmp, sustentacionDto);
                        responseExamenValoracion = sustentacionProyectoInvestigacionRepository
                                        .save(examenValoracionTmp);
                }
                return sustentacionProyectoIngestigacionMapper.toDto(responseExamenValoracion);
        }

        @Override
        @Transactional(readOnly = true)
        public String descargarArchivo(RutaArchivoDto rutaArchivo) {
                return FilesUtilities.recuperarArchivo(rutaArchivo.getRutaArchivo());
        }

        // Funciones privadas
        private void updateExamenValoracionValues(SustentacionTrabajoInvestigacion examenValoracion,
                        SustentacionTrabajoInvestigacionDto sustentacionDto) {
                examenValoracion.setUrlDocumentacion(sustentacionDto.getUrlDocumentacion());
                examenValoracion.setRespuestaSustentacion(sustentacionDto.getRespuestaSustentacion());
                examenValoracion.setNumeroActaTrabajoFinal(examenValoracion.getNumeroActaTrabajoFinal());
                examenValoracion.setFechaActa(sustentacionDto.getFechaActa());
        }

        private int validarEstado(Boolean estado) {
                int numEstado = 0;
                if (!estado) {
                        numEstado = 8;
                } else {
                        numEstado = 9;
                }
                return numEstado;
        }

        private CamposUnicosSustentacionProyectoInvestigacionDto obtenerCamposUnicos(
                        SustentacionTrabajoInvestigacionDto respuesta) {
                return informacionUnicaSustentacionProyectoInvestigacion.apply(respuesta);
        }

        private Map<String, String> validacionCampoUnicos(CamposUnicosSustentacionProyectoInvestigacionDto camposUnicos,
                        CamposUnicosSustentacionProyectoInvestigacionDto camposUnicosBD) {

                Map<String, Function<CamposUnicosSustentacionProyectoInvestigacionDto, Boolean>> mapCamposUnicos = new HashMap<>();

                mapCamposUnicos.put("idTrabajoGrados",
                                dto -> (camposUnicosBD == null || !dto.getIdTrabajoGrados()
                                                .equals(camposUnicosBD.getIdTrabajoGrados()))
                                                && sustentacionProyectoInvestigacionRepository
                                                                .countByTrabajoGradoId(dto.getIdTrabajoGrados()) > 0);

                Predicate<Field> existeCampoUnico = campo -> mapCamposUnicos.containsKey(campo.getName());
                Predicate<Field> existeCampoBD = campoBD -> mapCamposUnicos.get(campoBD.getName()).apply(camposUnicos);
                Predicate<Field> campoInvalido = existeCampoUnico.and(existeCampoBD);

                return Arrays.stream(camposUnicos.getClass().getDeclaredFields())
                                .filter(campoInvalido)
                                .peek(field -> field.setAccessible(true))
                                .collect(Collectors.toMap(Field::getName, field -> {
                                        Object valorCampo = null;
                                        try {
                                                valorCampo = field.get(camposUnicos);
                                        } catch (IllegalAccessException e) {
                                                e.printStackTrace();
                                        }
                                        return mensajeException(field.getName(), valorCampo);
                                }));

        }

        private <T> String mensajeException(String nombreCampo, T valorCampo) {
                return "Campo único, ya se ha registrado una SUSTENTACION PROYECTO DE INVESTIGACION al trabajo de grado: "
                                + valorCampo;
        }
}
