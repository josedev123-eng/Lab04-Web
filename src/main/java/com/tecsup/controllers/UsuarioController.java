package com.tecsup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.tecsup.models.Usuario;
import com.tecsup.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    // Listar usuarios con su perfil
    @GetMapping
    public List<Usuario> listar() {
        return service.listar();
    }

    // Registrar usuario con perfil
    @PostMapping
    public Usuario registrar(@RequestBody Usuario usuario) {
        return service.registrar(usuario);
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public Usuario obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    // Eliminar usuario (el perfil se elimina en cascada)
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
