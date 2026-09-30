package com.tecsup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.tecsup.models.Usuario;
import com.tecsup.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repo;

    public List<Usuario> listar() {
        return repo.findAll();
    }

    // Registrar usuario con perfil (el perfil se guarda en cascada)
    public Usuario registrar(Usuario usuario) {
        if (usuario.getPerfil() == null) {
            throw new RuntimeException("El usuario debe registrarse con su perfil");
        }
        if (repo.existsByUsername(usuario.getUsername())) {
            throw new RuntimeException("El username ya está registrado");
        }
        usuario.getPerfil().setUsuario(usuario);
        return repo.save(usuario);
    }

    public Usuario obtener(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // Al eliminar el usuario, su perfil se elimina en cascada
    public void eliminar(Long id) {
        Usuario usuario = obtener(id);
        repo.delete(usuario);
    }
}
