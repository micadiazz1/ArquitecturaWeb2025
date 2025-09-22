package org.example.entities;

import lombok.*;
import org.example.utils.Genero;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@NamedQuery(
        name = "Estudiante.findAll",
        query = "SELECT e FROM Estudiante e"
)

@NamedQuery(
        name = "Estudiante.findAllOrderByNombreApellido",
        query = "SELECT e FROM Estudiante e ORDER BY e.nombre, e.apellido ASC"
)
@Entity
public class Estudiante {

    @Id
    @Column(name = "idEstudiante")
    private int documento;

    @Column(length = 100)
    private String nombre;

    @Column(length = 100)
    private String apellido;

    @Column
    private int edad;
    @Enumerated(EnumType.STRING)
    private Genero genero; //Masculino, Femenino
    @Column
    private String ciudad;

    @Column(unique = true)
    private int numLibreta;
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    public Estudiante(int documento, String nombre, String apellido, int edad, Genero genero, String ciudad, int numLibreta) {
        this.documento = documento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.genero = genero;
        this.ciudad = ciudad;
        this.numLibreta = numLibreta;
    }
}
