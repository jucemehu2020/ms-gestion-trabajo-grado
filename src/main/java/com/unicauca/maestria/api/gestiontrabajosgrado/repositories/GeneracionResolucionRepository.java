package com.unicauca.maestria.api.gestiontrabajosgrado.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;

public interface GeneracionResolucionRepository  extends JpaRepository<GeneracionResolucion, Long>{
    
}
