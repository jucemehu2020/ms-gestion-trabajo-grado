package com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.SustentacionTrabajoInvestigacionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.comite.SustentacionTrabajoInvestigacionComiteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.comite.SustentacionTrabajoInvestigacionComiteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.SustentacionTrabajoInvestigacionCoordinadorDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.SustentacionTrabajoInvestigacionCoordinadorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteResponseDto;

public interface SustentacionProyectoInvestigacionService {

        // public SustentacionTrabajoInvestigacionDto
        // crear(SustentacionTrabajoInvestigacionDto oficio,
        // BindingResult result);

        public SustentacionTrabajoInvestigacionDocenteResponseDto insertarInformacionDocente(
                        SustentacionTrabajoInvestigacionDocenteDto sustentacionDto,
                        BindingResult result);

        public SustentacionTrabajoInvestigacionComiteResponseDto insertarInformacionComite(
                        SustentacionTrabajoInvestigacionComiteDto sustentacionDto,
                        BindingResult result);

        public SustentacionTrabajoInvestigacionCoordinadorResponseDto insertarInformacionCoordinador(
                        SustentacionTrabajoInvestigacionCoordinadorDto sustentacionDto,
                        BindingResult result);

        // public SustentacionTrabajoInvestigacionDto buscarPorId(Long idTrabajoGrado);

        public SustentacionTrabajoInvestigacionDocenteResponseDto listarInformacionDocente(Long idTrabajoGrado);

        public SustentacionTrabajoInvestigacionComiteResponseDto listarInformacionComite(Long idTrabajoGrado);

        public SustentacionTrabajoInvestigacionDto listarInformacionCoordinador(Long idTrabajoGrado);

        public SustentacionTrabajoInvestigacionDto actualizar(Long id,
                        SustentacionTrabajoInvestigacionDto examenValoracionDto, BindingResult result);

        public String descargarArchivo(RutaArchivoDto rutaArchivo);
}
