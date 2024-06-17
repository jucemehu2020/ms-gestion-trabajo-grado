package com.unicauca.maestria.api.gestiontrabajosgrado.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.ExamenValoracionCancelado;

public interface ExamenValoracionCanceladoRepository extends JpaRepository<ExamenValoracionCancelado, Long> {

}
