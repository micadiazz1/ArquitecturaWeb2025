package org.example.entities;

import org.example.utils.Genero;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "estudiante")
@Getter
@Setter
@NoArgsConstructor // Genera el constructor sin argumentos, necesario para JPA.

@NamedQuery(
        name = "Estudiante.findAll",
        query = "SELECT e FROM Estudiante e"
)
@NamedQuery(
        name = "Estudiante.findAllOrderByNombreApellido",
        query = "SELECT e FROM Estudiante e ORDER BY e.nombre, e.apellido ASC"
)
@NamedQuery(
        name = "Estudiante.findByID",
        query = "SELECT e FROM Estudiante e WHERE e.documento = :documento"
)
public class Estudiante {

    @Column(name = "num_libreta", unique = true, nullable = false)
    private int numLibreta;

    @Column(length = 100)
    private String nombre;

    @Column(length = 100)
    private String apellido;

    @Column
    private int edad;

    @Id
    @Column(unique = true, nullable = false)
    private int documento;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    @Column(length = 100)
    private String ciudad;

    @OneToMany(mappedBy = "estudiante")
    private Set<Inscripcion> inscripciones;
}
