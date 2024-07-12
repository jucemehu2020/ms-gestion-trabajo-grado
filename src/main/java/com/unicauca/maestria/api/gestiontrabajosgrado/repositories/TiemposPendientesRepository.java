package com.unicauca.maestria.api.gestiontrabajosgrado.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.TiemposPendientes;

public interface TiemposPendientesRepository extends JpaRepository<TiemposPendientes, Long> {

    Optional<TiemposPendientes> findByTrabajoGradoId(Long idTrabajoGrado);
    
}
