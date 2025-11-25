package com.crm.service;

import com.crm.model.entity.Contacto;
import com.crm.model.entity.Cliente;
import com.crm.repository.ContactoRepository;
import com.crm.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContactoService {
    
    @Autowired
    private ContactoRepository contactoRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    public List<Contacto> findAll() {
        return contactoRepository.findAll();
    }
    
    public List<Contacto> findByClienteId(Long clienteId) {
        return contactoRepository.findByClienteIdAndActivoTrue(clienteId);
    }
    
    public Optional<Contacto> findById(Long id) {
        return contactoRepository.findById(id);
    }
    
    public Contacto save(Contacto contacto) {
        Cliente cliente = clienteRepository.findById(contacto.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        contacto.setCliente(cliente);
        return contactoRepository.save(contacto);
    }
    
    public Contacto update(Long id, Contacto contactoActualizado) {
        Contacto contacto = contactoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contacto no encontrado con id: " + id));
        
        contacto.setNombre(contactoActualizado.getNombre());
        contacto.setApellidos(contactoActualizado.getApellidos());
        contacto.setCargo(contactoActualizado.getCargo());
        contacto.setTelefono(contactoActualizado.getTelefono());
        contacto.setEmail(contactoActualizado.getEmail());
        contacto.setActivo(contactoActualizado.getActivo());
        
        if (contactoActualizado.getCliente() != null) {
            Cliente cliente = clienteRepository.findById(contactoActualizado.getCliente().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            contacto.setCliente(cliente);
        }
        
        return contactoRepository.save(contacto);
    }
    
    public void delete(Long id) {
        Contacto contacto = contactoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contacto no encontrado con id: " + id));
        contacto.setActivo(false);
        contactoRepository.save(contacto);
    }
}

