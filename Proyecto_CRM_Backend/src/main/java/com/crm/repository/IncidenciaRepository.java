package com.crm.repository;

import com.crm.model.entity.Incidencia;
import com.crm.model.entity.Incidencia.EstadoIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {
    List<Incidencia> findByClienteId(Long clienteId);
    List<Incidencia> findByEstado(EstadoIncidencia estado);
    List<Incidencia> findByUsuarioAsignadoId(Long usuarioId);
}

