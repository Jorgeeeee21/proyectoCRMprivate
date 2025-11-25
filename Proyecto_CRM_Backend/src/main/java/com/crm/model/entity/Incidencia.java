package com.crm.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "incidencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Incidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String titulo;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoIncidencia estado = EstadoIncidencia.ABIERTA;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PrioridadIncidencia prioridad = PrioridadIncidencia.MEDIA;
    
    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @Column
    private LocalDateTime fechaResolucion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contacto_id")
    private Contacto contacto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_asignado_id")
    private Usuario usuarioAsignado;
    
    public enum EstadoIncidencia {
        ABIERTA,
        EN_PROCESO,
        RESUELTA,
        CERRADA
    }
    
    public enum PrioridadIncidencia {
        BAJA,
        MEDIA,
        ALTA,
        URGENTE
    }
}

