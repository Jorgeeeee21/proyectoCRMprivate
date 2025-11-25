package com.crm.repository;

import com.crm.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByActivoTrue();
    
    @Query("SELECT c FROM Cliente c WHERE c.activo = true AND " +
           "(LOWER(c.nombre) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
           "LOWER(c.ciudad) LIKE LOWER(CONCAT('%', :filtro, '%')))")
    List<Cliente> buscarPorFiltro(@Param("filtro") String filtro);
}

