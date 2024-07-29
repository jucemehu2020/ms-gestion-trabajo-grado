package com.unicauca.maestria.api.gestiontrabajosgrado.common.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.egresado.CursoSaveDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.egresado.EmpresaSaveDto;

@FeignClient(name = "ms-gestion-egresados", url = "http://localhost:8084")
public interface ArchivoClientEgresados {

    @GetMapping("/api/curso/listarCursosDictados/{idEstudiante}")
    public List<CursoSaveDto> obtenerCursosPorIdEstudiante(@PathVariable Long idEstudiante);

    @GetMapping("/api/empresa/listarEmpresas/{idEstudiante}")
    public List<EmpresaSaveDto> obtenerEmpresasPorIdEstudiante(@PathVariable Long idEstudiante);
}
