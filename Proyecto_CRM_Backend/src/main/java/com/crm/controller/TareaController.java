package com.crm.controller;

import com.crm.model.entity.Tarea;
import com.crm.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tareas")
@CrossOrigin(origins = "http://localhost:4200")
public class TareaController {
    
    @Autowired
    private TareaService tareaService;
    
    @GetMapping
    public ResponseEntity<List<Tarea>> getAllTareas() {
        return ResponseEntity.ok(tareaService.findAll());
    }
    
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Tarea>> getTareasByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(tareaService.findByUsuarioAsignadoId(usuarioId));
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Tarea>> getTareasByEstado(@PathVariable String estado) {
        try {
            Tarea.EstadoTarea estadoEnum = Tarea.EstadoTarea.valueOf(estado.toUpperCase());
            return ResponseEntity.ok(tareaService.findByEstado(estadoEnum));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Tarea> getTareaById(@PathVariable Long id) {
        Optional<Tarea> tarea = tareaService.findById(id);
        return tarea.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Tarea> createTarea(@RequestBody Tarea tarea) {
        Tarea nuevaTarea = tareaService.save(tarea);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaTarea);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Tarea> updateTarea(@PathVariable Long id, @RequestBody Tarea tarea) {
        try {
            Tarea tareaActualizada = tareaService.update(id, tarea);
            return ResponseEntity.ok(tareaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarea(@PathVariable Long id) {
        try {
            tareaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

