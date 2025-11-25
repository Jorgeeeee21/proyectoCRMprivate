package com.crm.service;

import com.crm.model.entity.Incidencia;
import com.crm.model.entity.Tarea;
import com.crm.repository.IncidenciaRepository;
import com.crm.repository.TareaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TareasProgramadasService {
    
    private static final Logger logger = LoggerFactory.getLogger(TareasProgramadasService.class);
    
    @Autowired
    private TareaRepository tareaRepository;
    
    @Autowired
    private IncidenciaRepository incidenciaRepository;
    
    /**
     * Tarea programada que se ejecuta cada día a las 9:00 AM
     * Envía recordatorios de tareas próximas a vencer
     */
    @Scheduled(cron = "0 0 9 * * ?") // Cada día a las 9:00 AM
    @Async
    public CompletableFuture<Void> enviarRecordatoriosTareas() {
        logger.info("Iniciando envío de recordatorios de tareas...");
        
        LocalDateTime fechaLimite = LocalDateTime.now().plusDays(3);
        List<Tarea> tareasProximas = tareaRepository.findByFechaVencimientoBeforeAndEstadoNot(
            fechaLimite, 
            Tarea.EstadoTarea.COMPLETADA
        );
        
        for (Tarea tarea : tareasProximas) {
            logger.info("Recordatorio: Tarea '{}' vence el {}", 
                tarea.getTitulo(), 
                tarea.getFechaVencimiento());
            // Aquí se podría integrar un servicio de email o notificaciones
        }
        
        logger.info("Proceso de recordatorios completado. {} tareas próximas a vencer", tareasProximas.size());
        return CompletableFuture.completedFuture(null);
    }
    
    /**
     * Tarea programada que se ejecuta cada hora
     * Actualiza el estado de incidencias antiguas sin resolver
     */
    @Scheduled(fixedRate = 3600000) // Cada hora (3600000 ms)
    @Async
    public CompletableFuture<Void> actualizarIncidenciasAntiguas() {
        logger.info("Verificando incidencias antiguas...");
        
        List<Incidencia> incidenciasAbiertas = incidenciaRepository.findByEstado(Incidencia.EstadoIncidencia.ABIERTA);
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(30);
        
        int actualizadas = 0;
        for (Incidencia incidencia : incidenciasAbiertas) {
            if (incidencia.getFechaCreacion().isBefore(fechaLimite)) {
                incidencia.setEstado(Incidencia.EstadoIncidencia.EN_PROCESO);
                incidenciaRepository.save(incidencia);
                actualizadas++;
                logger.info("Incidencia {} actualizada a EN_PROCESO por antigüedad", incidencia.getId());
            }
        }
        
        logger.info("Proceso completado. {} incidencias actualizadas", actualizadas);
        return CompletableFuture.completedFuture(null);
    }
    
    /**
     * Tarea programada que se ejecuta cada día a medianoche
     * Genera un resumen diario de actividades
     */
    @Scheduled(cron = "0 0 0 * * ?") // Cada día a medianoche
    @Async
    public CompletableFuture<Void> generarResumenDiario() {
        logger.info("Generando resumen diario...");
        
        long tareasCompletadas = tareaRepository.findByEstado(Tarea.EstadoTarea.COMPLETADA)
            .stream()
            .filter(t -> t.getFechaCompletada() != null 
                && t.getFechaCompletada().toLocalDate().equals(LocalDateTime.now().toLocalDate()))
            .count();
        
        long incidenciasResueltas = incidenciaRepository.findByEstado(Incidencia.EstadoIncidencia.RESUELTA)
            .stream()
            .filter(i -> i.getFechaResolucion() != null 
                && i.getFechaResolucion().toLocalDate().equals(LocalDateTime.now().toLocalDate()))
            .count();
        
        logger.info("Resumen del día {}: {} tareas completadas, {} incidencias resueltas",
            LocalDateTime.now().toLocalDate(), tareasCompletadas, incidenciasResueltas);
        
        // Aquí se podría enviar un email o generar un informe
        return CompletableFuture.completedFuture(null);
    }
    
    /**
     * Método asíncrono para procesar tareas en segundo plano
     */
    @Async
    public CompletableFuture<String> procesarTareaAsincrona(String descripcion) {
        logger.info("Procesando tarea asíncrona: {}", descripcion);
        
        try {
            // Simular procesamiento
            Thread.sleep(2000);
            logger.info("Tarea asíncrona completada: {}", descripcion);
            return CompletableFuture.completedFuture("Tarea completada: " + descripcion);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.completedFuture("Error en tarea: " + descripcion);
        }
    }
}

