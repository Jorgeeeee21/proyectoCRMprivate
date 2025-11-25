package com.crm.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tareas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String titulo;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoTarea estado = EstadoTarea.PENDIENTE;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PrioridadTarea prioridad = PrioridadTarea.MEDIA;
    
    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @Column
    private LocalDateTime fechaVencimiento;
    
    @Column
    private LocalDateTime fechaCompletada;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_asignado_id", nullable = false)
    private Usuario usuarioAsignado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_creador_id", nullable = false)
    private Usuario usuarioCreador;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    
    public enum EstadoTarea {
        PENDIENTE,
        EN_PROCESO,
        COMPLETADA,
        CANCELADA
    }
    
    public enum PrioridadTarea {
        BAJA,
        MEDIA,
        ALTA,
        URGENTE
    }
}

