package com.tecsup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.tecsup.models.Curso;
import com.tecsup.models.Estudiante;
import com.tecsup.repository.CursoRepository;

@Service
public class CursoService {

    @Autowired
    private CursoRepository repo;

    public List<Curso> listar() {
        return repo.findAll();
    }

    public Curso guardar(Curso curso) {
        return repo.save(curso);
    }

    public Curso obtener(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
    }

    // Antes de borrar un curso se quitan sus inscripciones de la tabla intermedia
    @Transactional
    public void eliminar(Long id) {
        Curso curso = obtener(id);
        for (Estudiante e : curso.getEstudiantes()) {
            e.getCursos().remove(curso);
        }
        repo.delete(curso);
    }
}
