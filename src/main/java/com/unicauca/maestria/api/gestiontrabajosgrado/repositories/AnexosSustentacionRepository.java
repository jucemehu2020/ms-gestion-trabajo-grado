package com.unicauca.maestria.api.gestiontrabajosgrado.repositories;

import org.springframework.data.jpa.repository.Query;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_proyecto_investigacion.AnexoSustentacion;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnexosSustentacionRepository extends JpaRepository<AnexoSustentacion, Long> {

    @Query("SELECT asev FROM AnexoSustentacion asev WHERE asev.sustentacionProyectoInvestigacion.id = ?1")
    List<AnexoSustentacion> obtenerAnexosPorId(Long sustentacionId);

}
