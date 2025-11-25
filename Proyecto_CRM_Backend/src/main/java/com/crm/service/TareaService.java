package com.crm.service;

import com.crm.model.entity.Tarea;
import com.crm.model.entity.Usuario;
import com.crm.model.entity.Cliente;
import com.crm.repository.TareaRepository;
import com.crm.repository.UsuarioRepository;
import com.crm.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TareaService {
    
    @Autowired
    private TareaRepository tareaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    public List<Tarea> findAll() {
        return tareaRepository.findAll();
    }
    
    public List<Tarea> findByUsuarioAsignadoId(Long usuarioId) {
        return tareaRepository.findByUsuarioAsignadoId(usuarioId);
    }
    
    public List<Tarea> findByEstado(Tarea.EstadoTarea estado) {
        return tareaRepository.findByEstado(estado);
    }
    
    public Optional<Tarea> findById(Long id) {
        return tareaRepository.findById(id);
    }
    
    public Tarea save(Tarea tarea) {
        Usuario usuarioAsignado = usuarioRepository.findById(tarea.getUsuarioAsignado().getId())
                .orElseThrow(() -> new RuntimeException("Usuario asignado no encontrado"));
        tarea.setUsuarioAsignado(usuarioAsignado);
        
        Usuario usuarioCreador = usuarioRepository.findById(tarea.getUsuarioCreador().getId())
                .orElseThrow(() -> new RuntimeException("Usuario creador no encontrado"));
        tarea.setUsuarioCreador(usuarioCreador);
        
        if (tarea.getCliente() != null && tarea.getCliente().getId() != null) {
            Cliente cliente = clienteRepository.findById(tarea.getCliente().getId())
                    .orElse(null);
            tarea.setCliente(cliente);
        }
        
        return tareaRepository.save(tarea);
    }
    
    public Tarea update(Long id, Tarea tareaActualizada) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada con id: " + id));
        
        tarea.setTitulo(tareaActualizada.getTitulo());
        tarea.setDescripcion(tareaActualizada.getDescripcion());
        tarea.setEstado(tareaActualizada.getEstado());
        tarea.setPrioridad(tareaActualizada.getPrioridad());
        tarea.setFechaVencimiento(tareaActualizada.getFechaVencimiento());
        
        if (tareaActualizada.getEstado() == Tarea.EstadoTarea.COMPLETADA 
            && tarea.getFechaCompletada() == null) {
            tarea.setFechaCompletada(LocalDateTime.now());
        }
        
        if (tareaActualizada.getUsuarioAsignado() != null) {
            Usuario usuarioAsignado = usuarioRepository.findById(tareaActualizada.getUsuarioAsignado().getId())
                    .orElseThrow(() -> new RuntimeException("Usuario asignado no encontrado"));
            tarea.setUsuarioAsignado(usuarioAsignado);
        }
        
        if (tareaActualizada.getCliente() != null && tareaActualizada.getCliente().getId() != null) {
            Cliente cliente = clienteRepository.findById(tareaActualizada.getCliente().getId())
                    .orElse(null);
            tarea.setCliente(cliente);
        }
        
        return tareaRepository.save(tarea);
    }
    
    public void delete(Long id) {
        tareaRepository.deleteById(id);
    }
}

