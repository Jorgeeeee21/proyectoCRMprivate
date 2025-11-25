package com.crm.controller;

import com.crm.service.EstadisticasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/estadisticas")
@CrossOrigin(origins = "http://localhost:4200")
public class EstadisticasController {
    
    @Autowired
    private EstadisticasService estadisticasService;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getEstadisticas() {
        return ResponseEntity.ok(estadisticasService.getEstadisticasGenerales());
    }
}

