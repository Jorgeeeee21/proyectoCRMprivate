package com.crm.repository;

import com.crm.model.entity.Tarea;
import com.crm.model.entity.Tarea.EstadoTarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    List<Tarea> findByUsuarioAsignadoId(Long usuarioId);
    List<Tarea> findByEstado(EstadoTarea estado);
    List<Tarea> findByUsuarioAsignadoIdAndEstado(Long usuarioId, EstadoTarea estado);
    List<Tarea> findByFechaVencimientoBeforeAndEstadoNot(LocalDateTime fecha, EstadoTarea estado);
}

