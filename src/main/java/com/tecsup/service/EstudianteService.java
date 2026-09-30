package com.tecsup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import com.tecsup.models.Curso;
import com.tecsup.models.Estudiante;
import com.tecsup.repository.CursoRepository;
import com.tecsup.repository.EstudianteRepository;

@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepo;

    @Autowired
    private CursoRepository cursoRepo;

    public List<Estudiante> listar() {
        return estudianteRepo.findAll();
    }

    public Estudiante obtener(Long id) {
        return estudianteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
    }

    // Crear estudiante con cursos: se reciben solo los id de los cursos
    @Transactional
    public Estudiante crear(Estudiante estudiante) {
        if (estudianteRepo.existsByEmail(estudiante.getEmail())) {
            throw new RuntimeException("Ya existe un estudiante con ese email");
        }
        List<Curso> cursos = new ArrayList<>();
        if (estudiante.getCursos() != null) {
            for (Curso c : estudiante.getCursos()) {
                Curso curso = buscarCurso(c.getId());
                if (cursos.contains(curso)) {
                    throw new RuntimeException("El curso " + curso.getNombre() + " está repetido en la inscripción");
                }
                cursos.add(curso);
            }
        }
        estudiante.setCursos(cursos);
        return estudianteRepo.save(estudiante);
    }

    // Agregar curso a estudiante existente (validando que no se repita)
    @Transactional
    public Estudiante agregarCurso(Long estudianteId, Long cursoId) {
        Estudiante estudiante = obtener(estudianteId);
        Curso curso = buscarCurso(cursoId);
        if (estudiante.getCursos().contains(curso)) {
            throw new RuntimeException("El estudiante ya está inscrito en el curso " + curso.getNombre());
        }
        estudiante.getCursos().add(curso);
        return estudianteRepo.save(estudiante);
    }

    // Quitar curso de un estudiante
    @Transactional
    public Estudiante quitarCurso(Long estudianteId, Long cursoId) {
        Estudiante estudiante = obtener(estudianteId);
        Curso curso = buscarCurso(cursoId);
        if (!estudiante.getCursos().remove(curso)) {
            throw new RuntimeException("El estudiante no está inscrito en el curso " + curso.getNombre());
        }
        return estudianteRepo.save(estudiante);
    }

    // Listar cursos de un estudiante
    public List<Curso> listarCursos(Long estudianteId) {
        return obtener(estudianteId).getCursos();
    }

    public void eliminar(Long id) {
        estudianteRepo.delete(obtener(id));
    }

    private Curso buscarCurso(Long cursoId) {
        if (cursoId == null) {
            throw new RuntimeException("Debe indicar el id del curso");
        }
        return cursoRepo.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso " + cursoId + " no existe"));
    }
}
