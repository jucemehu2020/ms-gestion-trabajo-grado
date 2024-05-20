package com.unicauca.maestria.api.gestiontrabajosgrado.services.generacion_resolucion;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.DirectorAndCodirectorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.GeneracionResolucionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.comite.GeneracionResolucionComiteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.comite.GeneracionResolucionComiteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.GeneracionResolucionCoordinadorDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.GeneracionResolucionCoordinadorResponseDto;

public interface GeneracionResolucionService {

        public List<DirectorAndCodirectorResponseDto> listarDirectorAndCodirector();

        // public GeneracionResolucionDto crear(GeneracionResolucionDto
        // generacionResolucion,
        // BindingResult result);

        public GeneracionResolucionCoordinadorResponseDto insertarInformacionCoordinador(
                        GeneracionResolucionCoordinadorDto generacionResolucion,
                        BindingResult result);

        public GeneracionResolucionComiteResponseDto insertarInformacionComite(
                        GeneracionResolucionComiteDto generacionResolucion,
                        BindingResult result);

        // public GeneracionResolucionDto buscarPorId(Long idTrabajoGrado);

        public GeneracionResolucionCoordinadorResponseDto listarInformacionCoordinador(Long idTrabajoGrado);

        public GeneracionResolucionDto listarInformacionComite(Long idTrabajoGrado);

        public GeneracionResolucionDto actualizar(Long id, GeneracionResolucionDto generacionResolucion,
                        BindingResult result);

        public String descargarArchivo(RutaArchivoDto rutaArchivo);
}
