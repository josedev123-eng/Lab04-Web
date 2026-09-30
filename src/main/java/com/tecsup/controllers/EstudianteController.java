package com.tecsup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.tecsup.models.Curso;
import com.tecsup.models.Estudiante;
import com.tecsup.service.EstudianteService;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService service;

    // Listar estudiantes con sus cursos
    @GetMapping
    public List<Estudiante> listar() {
        return service.listar();
    }

    // Crear estudiante con cursos
    @PostMapping
    public Estudiante crear(@RequestBody Estudiante estudiante) {
        return service.crear(estudiante);
    }

    @GetMapping("/{id}")
    public Estudiante obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    // Listar cursos de un estudiante
    @GetMapping("/{id}/cursos")
    public List<Curso> listarCursos(@PathVariable Long id) {
        return service.listarCursos(id);
    }

    // Agregar curso a estudiante existente
    @PostMapping("/{id}/cursos/{cursoId}")
    public Estudiante agregarCurso(@PathVariable Long id, @PathVariable Long cursoId) {
        return service.agregarCurso(id, cursoId);
    }

    // Quitar curso de un estudiante
    @DeleteMapping("/{id}/cursos/{cursoId}")
    public Estudiante quitarCurso(@PathVariable Long id, @PathVariable Long cursoId) {
        return service.quitarCurso(id, cursoId);
    }
}
