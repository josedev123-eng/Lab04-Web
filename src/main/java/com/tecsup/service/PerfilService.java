package com.tecsup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.tecsup.models.Perfil;
import com.tecsup.repository.PerfilRepository;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository repo;

    public List<Perfil> listar() {
        return repo.findAll();
    }

    public Perfil obtener(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
    }
}
