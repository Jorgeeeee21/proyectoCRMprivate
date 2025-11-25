package com.crm.config;

import com.crm.model.entity.Rol;
import com.crm.model.entity.Usuario;
import com.crm.repository.RolRepository;
import com.crm.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Crear roles si no existen
        if (rolRepository.count() == 0) {
            Rol rolAdmin = new Rol();
            rolAdmin.setNombre(Rol.RolNombre.ADMIN);
            rolRepository.save(rolAdmin);
            
            Rol rolComercial = new Rol();
            rolComercial.setNombre(Rol.RolNombre.COMERCIAL);
            rolRepository.save(rolComercial);
            
            Rol rolGestor = new Rol();
            rolGestor.setNombre(Rol.RolNombre.GESTOR);
            rolRepository.save(rolGestor);
            
            System.out.println("Roles creados: ADMIN, COMERCIAL, GESTOR");
        }
        
        // Crear usuario admin por defecto si no existe
        if (!usuarioRepository.existsByUsername("admin")) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@crm.com");
            admin.setNombre("Administrador");
            admin.setApellidos("Sistema");
            admin.setActivo(true);
            
            Set<Rol> roles = new HashSet<>();
            roles.add(rolRepository.findByNombre(Rol.RolNombre.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado")));
            admin.setRoles(roles);
            
            usuarioRepository.save(admin);
            System.out.println("Usuario admin creado: username=admin, password=admin123");
        }
    }
}

