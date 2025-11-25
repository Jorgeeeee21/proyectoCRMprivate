package com.crm.service;

import com.crm.model.dto.JwtResponse;
import com.crm.model.dto.LoginRequest;
import com.crm.model.entity.Rol;
import com.crm.model.entity.Usuario;
import com.crm.repository.RolRepository;
import com.crm.repository.UsuarioRepository;
import com.crm.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        
        Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        List<String> roles = usuario.getRoles().stream()
                .map(rol -> rol.getNombre().name())
                .collect(Collectors.toList());
        
        return new JwtResponse(
            jwt,
            "Bearer",
            usuario.getId(),
            usuario.getUsername(),
            usuario.getEmail(),
            roles
        );
    }
    
    public Usuario crearUsuario(String username, String password, String email, String nombre, String apellidos, Set<String> rolesStr) {
        if (usuarioRepository.existsByUsername(username)) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        
        if (usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("El email ya está registrado");
        }
        
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setEmail(email);
        usuario.setNombre(nombre);
        usuario.setApellidos(apellidos);
        usuario.setActivo(true);
        
        Set<Rol> roles = new HashSet<>();
        if (rolesStr == null || rolesStr.isEmpty()) {
            Rol rolComercial = rolRepository.findByNombre(Rol.RolNombre.COMERCIAL)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            roles.add(rolComercial);
        } else {
            for (String rolStr : rolesStr) {
                Rol.RolNombre rolNombre = Rol.RolNombre.valueOf(rolStr);
                Rol rol = rolRepository.findByNombre(rolNombre)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolStr));
                roles.add(rol);
            }
        }
        usuario.setRoles(roles);
        
        return usuarioRepository.save(usuario);
    }
}

