package com.crm.service;

import com.crm.model.entity.Incidencia;
import com.crm.model.entity.Cliente;
import com.crm.model.entity.Contacto;
import com.crm.model.entity.Usuario;
import com.crm.repository.IncidenciaRepository;
import com.crm.repository.ClienteRepository;
import com.crm.repository.ContactoRepository;
import com.crm.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class IncidenciaService {
    
    @Autowired
    private IncidenciaRepository incidenciaRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ContactoRepository contactoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public List<Incidencia> findAll() {
        return incidenciaRepository.findAll();
    }
    
    public List<Incidencia> findByClienteId(Long clienteId) {
        return incidenciaRepository.findByClienteId(clienteId);
    }
    
    public List<Incidencia> findByEstado(Incidencia.EstadoIncidencia estado) {
        return incidenciaRepository.findByEstado(estado);
    }
    
    public Optional<Incidencia> findById(Long id) {
        return incidenciaRepository.findById(id);
    }
    
    public Incidencia save(Incidencia incidencia) {
        Cliente cliente = clienteRepository.findById(incidencia.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        incidencia.setCliente(cliente);
        
        if (incidencia.getContacto() != null && incidencia.getContacto().getId() != null) {
            Contacto contacto = contactoRepository.findById(incidencia.getContacto().getId())
                    .orElse(null);
            incidencia.setContacto(contacto);
        }
        
        if (incidencia.getUsuarioAsignado() != null && incidencia.getUsuarioAsignado().getId() != null) {
            Usuario usuario = usuarioRepository.findById(incidencia.getUsuarioAsignado().getId())
                    .orElse(null);
            incidencia.setUsuarioAsignado(usuario);
        }
        
        return incidenciaRepository.save(incidencia);
    }
    
    public Incidencia update(Long id, Incidencia incidenciaActualizada) {
        Incidencia incidencia = incidenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada con id: " + id));
        
        incidencia.setTitulo(incidenciaActualizada.getTitulo());
        incidencia.setDescripcion(incidenciaActualizada.getDescripcion());
        incidencia.setEstado(incidenciaActualizada.getEstado());
        incidencia.setPrioridad(incidenciaActualizada.getPrioridad());
        
        if (incidenciaActualizada.getEstado() == Incidencia.EstadoIncidencia.RESUELTA 
            && incidencia.getFechaResolucion() == null) {
            incidencia.setFechaResolucion(LocalDateTime.now());
        }
        
        if (incidenciaActualizada.getCliente() != null) {
            Cliente cliente = clienteRepository.findById(incidenciaActualizada.getCliente().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            incidencia.setCliente(cliente);
        }
        
        if (incidenciaActualizada.getContacto() != null && incidenciaActualizada.getContacto().getId() != null) {
            Contacto contacto = contactoRepository.findById(incidenciaActualizada.getContacto().getId())
                    .orElse(null);
            incidencia.setContacto(contacto);
        }
        
        if (incidenciaActualizada.getUsuarioAsignado() != null && incidenciaActualizada.getUsuarioAsignado().getId() != null) {
            Usuario usuario = usuarioRepository.findById(incidenciaActualizada.getUsuarioAsignado().getId())
                    .orElse(null);
            incidencia.setUsuarioAsignado(usuario);
        }
        
        return incidenciaRepository.save(incidencia);
    }
    
    public void delete(Long id) {
        incidenciaRepository.deleteById(id);
    }
}

