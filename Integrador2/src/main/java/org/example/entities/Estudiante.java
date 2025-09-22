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
@Entity(name = "estudiante")
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Ciudad ciudad;

    @Column(unique = true)
    private int numLibreta;

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inscripcion> inscripciones = new ArrayList<>();


    @Column
    private boolean graduado = false;

    @Enumerated(EnumType.STRING)
    private Genero genero; //Masculino, Femenino

    public Estudiante(int numLibreta, Ciudad ciudad, int documento, int edad, String apellido, String nombre, Genero genero) {
        this.numLibreta = numLibreta;
        this.ciudad = ciudad;
        this.documento = documento;
        this.edad = edad;
        this.apellido = apellido;
        this.nombre = nombre;
        this.genero = genero;
    }
}
