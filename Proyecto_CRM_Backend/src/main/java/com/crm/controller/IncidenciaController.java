package com.crm.controller;

import com.crm.model.entity.Incidencia;
import com.crm.service.IncidenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/incidencias")
@CrossOrigin(origins = "http://localhost:4200")
public class IncidenciaController {
    
    @Autowired
    private IncidenciaService incidenciaService;
    
    @GetMapping
    public ResponseEntity<List<Incidencia>> getAllIncidencias() {
        return ResponseEntity.ok(incidenciaService.findAll());
    }
    
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Incidencia>> getIncidenciasByCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(incidenciaService.findByClienteId(clienteId));
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Incidencia>> getIncidenciasByEstado(@PathVariable String estado) {
        try {
            Incidencia.EstadoIncidencia estadoEnum = Incidencia.EstadoIncidencia.valueOf(estado.toUpperCase());
            return ResponseEntity.ok(incidenciaService.findByEstado(estadoEnum));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Incidencia> getIncidenciaById(@PathVariable Long id) {
        Optional<Incidencia> incidencia = incidenciaService.findById(id);
        return incidencia.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Incidencia> createIncidencia(@RequestBody Incidencia incidencia) {
        Incidencia nuevaIncidencia = incidenciaService.save(incidencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaIncidencia);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Incidencia> updateIncidencia(@PathVariable Long id, @RequestBody Incidencia incidencia) {
        try {
            Incidencia incidenciaActualizada = incidenciaService.update(id, incidencia);
            return ResponseEntity.ok(incidenciaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncidencia(@PathVariable Long id) {
        try {
            incidenciaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

