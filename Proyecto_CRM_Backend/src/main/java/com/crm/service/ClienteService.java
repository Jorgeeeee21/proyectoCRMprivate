package com.crm.service;

import com.crm.model.entity.Cliente;
import com.crm.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }
    
    public List<Cliente> findActivos() {
        return clienteRepository.findByActivoTrue();
    }
    
    public List<Cliente> buscarPorFiltro(String filtro) {
        return clienteRepository.buscarPorFiltro(filtro);
    }
    
    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }
    
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    
    public Cliente update(Long id, Cliente clienteActualizado) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
        
        cliente.setNombre(clienteActualizado.getNombre());
        cliente.setCif(clienteActualizado.getCif());
        cliente.setDireccion(clienteActualizado.getDireccion());
        cliente.setCiudad(clienteActualizado.getCiudad());
        cliente.setCodigoPostal(clienteActualizado.getCodigoPostal());
        cliente.setProvincia(clienteActualizado.getProvincia());
        cliente.setTelefono(clienteActualizado.getTelefono());
        cliente.setEmail(clienteActualizado.getEmail());
        cliente.setActivo(clienteActualizado.getActivo());
        
        return clienteRepository.save(cliente);
    }
    
    public void delete(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }
}

