package com.crm.repository;

import com.crm.model.entity.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Long> {
    List<Contacto> findByClienteId(Long clienteId);
    List<Contacto> findByClienteIdAndActivoTrue(Long clienteId);
}

