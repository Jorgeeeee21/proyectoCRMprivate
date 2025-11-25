package com.crm.controller;

import com.crm.model.entity.Contacto;
import com.crm.service.ContactoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contactos")
@CrossOrigin(origins = "http://localhost:4200")
public class ContactoController {
    
    @Autowired
    private ContactoService contactoService;
    
    @GetMapping
    public ResponseEntity<List<Contacto>> getAllContactos() {
        return ResponseEntity.ok(contactoService.findAll());
    }
    
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Contacto>> getContactosByCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(contactoService.findByClienteId(clienteId));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Contacto> getContactoById(@PathVariable Long id) {
        Optional<Contacto> contacto = contactoService.findById(id);
        return contacto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Contacto> createContacto(@RequestBody Contacto contacto) {
        Contacto nuevoContacto = contactoService.save(contacto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoContacto);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Contacto> updateContacto(@PathVariable Long id, @RequestBody Contacto contacto) {
        try {
            Contacto contactoActualizado = contactoService.update(id, contacto);
            return ResponseEntity.ok(contactoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContacto(@PathVariable Long id) {
        try {
            contactoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

