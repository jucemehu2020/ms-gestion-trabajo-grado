package com.unicauca.maestria.api.gestiontrabajosgrado.services.generacion_resolucion;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.DirectorAndCodirectorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.GeneracionResolucionDto;

public interface GeneracionResolucionService {

        public List<DirectorAndCodirectorResponseDto> listarDirectorAndCodirector();

        public GeneracionResolucionDto crear(GeneracionResolucionDto generacionResolucion,
                        BindingResult result);

        public List<GeneracionResolucionDto> buscarPorId(Long idTrabajoGrado);

        public GeneracionResolucionDto actualizar(Long id, GeneracionResolucionDto generacionResolucion,
                        BindingResult result);

        public String descargarArchivo(RutaArchivoDto rutaArchivo);
}
