# Lab04-Web: Persistencia con Spring Boot y Hibernate

Ejercicios propuestos del Laboratorio 04: relaciones **@OneToOne** (Usuario–Perfil) y **@ManyToMany** (Estudiante–Curso)
con arquitectura en capas (Controller, Service, Repository).

Spring Boot 4.0.5 · Java 21+ · Maven · MySQL · paquete `com.tecsup`

## 1. Cómo ejecutarlo

1. Crear la base de datos en MySQL:
   ```sql
   CREATE DATABASE laboratorio_api;
   ```
2. Revisar usuario y contraseña de MySQL en `src/main/resources/application.properties`.
3. Ejecutar `Demo01Application`. Hibernate crea las tablas (`ddl-auto=update`).
4. En Postman: **Import** → `postman/Lab04-Postman.postman_collection.json`.

```
com.tecsup
├── controllers  UsuarioController, PerfilController, EstudianteController, CursoController
├── models       Usuario, Perfil, Estudiante, Curso
├── repository   UsuarioRepository, PerfilRepository, EstudianteRepository, CursoRepository
├── service      UsuarioService, PerfilService, EstudianteService, CursoService
└── exception    GlobalExceptionHandler
```

## 2. Ejercicio 1: @OneToOne (Usuario y Perfil)

- `Usuario` es el **dueño** de la relación: `@OneToOne(cascade = ALL, orphanRemoval = true)` y
  `@JoinColumn(name = "perfil_id", unique = true)`. La FK `perfil_id` queda en la tabla `usuario`.
- `Perfil` es el lado inverso: `@OneToOne(mappedBy = "perfil")` + `@JsonIgnore` para evitar el bucle infinito.
- El `password` usa `@JsonProperty(WRITE_ONLY)`: se recibe al registrar pero no se devuelve en las respuestas.

| Acción | Método y ruta |
|---|---|
| Registrar usuario con perfil | `POST /api/usuarios` |
| Listar usuarios con su perfil | `GET /api/usuarios` |
| Obtener usuario por ID | `GET /api/usuarios/{id}` |
| Eliminar usuario (borra su perfil en cascada) | `DELETE /api/usuarios/{id}` |
| Ver perfiles (para comprobar la cascada) | `GET /api/perfiles` |

```json
POST /api/usuarios
{
  "username": "jose",
  "password": "123456",
  "perfil": { "nombreCompleto": "Jose Perez", "direccion": "Av. Lima 123", "telefono": "987654321" }
}
```

## 3. Ejercicio 2: @ManyToMany (Estudiante y Curso)

- `Estudiante` es el dueño y define la tabla intermedia:
  ```java
  @ManyToMany
  @JoinTable(name = "estudiante_curso",
      joinColumns = @JoinColumn(name = "estudiante_id"),
      inverseJoinColumns = @JoinColumn(name = "curso_id"),
      uniqueConstraints = @UniqueConstraint(columnNames = {"estudiante_id", "curso_id"}))
  ```
- `Curso` es el lado inverso: `@ManyToMany(mappedBy = "cursos")` + `@JsonIgnore`.
- **Inscripción repetida**: se valida en `EstudianteService` (mensaje claro) y también en la BD (restricción única).

| Acción | Método y ruta |
|---|---|
| Crear curso / listar cursos | `POST /api/cursos` · `GET /api/cursos` |
| Crear estudiante con cursos | `POST /api/estudiantes` |
| Listar estudiantes con sus cursos | `GET /api/estudiantes` |
| Agregar curso a estudiante existente | `POST /api/estudiantes/{id}/cursos/{cursoId}` |
| Quitar curso | `DELETE /api/estudiantes/{id}/cursos/{cursoId}` |
| Listar cursos de un estudiante | `GET /api/estudiantes/{id}/cursos` |

```json
POST /api/estudiantes
{ "nombre": "Luis Rojas", "email": "luis@tecsup.edu.pe", "cursos": [ { "id": 1 }, { "id": 2 } ] }
```

Primero crea los cursos y luego el estudiante. Repetir `POST /api/estudiantes/1/cursos/3` devuelve
`400 {"error": "El estudiante ya está inscrito en el curso Redes"}`.

