package com.crm.service;

import com.crm.model.entity.Cliente;
import com.crm.model.entity.Incidencia;
import com.crm.model.entity.Tarea;
import com.crm.repository.ClienteRepository;
import com.crm.repository.IncidenciaRepository;
import com.crm.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class EstadisticasService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private IncidenciaRepository incidenciaRepository;
    
    @Autowired
    private TareaRepository tareaRepository;
    
    public Map<String, Object> getEstadisticasGenerales() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        // Clientes activos
        long clientesActivos = clienteRepository.findByActivoTrue().size();
        estadisticas.put("clientesActivos", clientesActivos);
        
        // Total de clientes
        long totalClientes = clienteRepository.count();
        estadisticas.put("totalClientes", totalClientes);
        
        // Incidencias por estado
        Map<String, Long> incidenciasPorEstado = new HashMap<>();
        for (Incidencia.EstadoIncidencia estado : Incidencia.EstadoIncidencia.values()) {
            long count = incidenciaRepository.findByEstado(estado).size();
            incidenciasPorEstado.put(estado.name(), count);
        }
        estadisticas.put("incidenciasPorEstado", incidenciasPorEstado);
        
        // Total de incidencias
        long totalIncidencias = incidenciaRepository.count();
        estadisticas.put("totalIncidencias", totalIncidencias);
        
        // Tareas por estado
        Map<String, Long> tareasPorEstado = new HashMap<>();
        for (Tarea.EstadoTarea estado : Tarea.EstadoTarea.values()) {
            long count = tareaRepository.findByEstado(estado).size();
            tareasPorEstado.put(estado.name(), count);
        }
        estadisticas.put("tareasPorEstado", tareasPorEstado);
        
        // Total de tareas
        long totalTareas = tareaRepository.count();
        estadisticas.put("totalTareas", totalTareas);
        
        // Tareas completadas
        long tareasCompletadas = tareaRepository.findByEstado(Tarea.EstadoTarea.COMPLETADA).size();
        estadisticas.put("tareasCompletadas", tareasCompletadas);
        
        return estadisticas;
    }
}

