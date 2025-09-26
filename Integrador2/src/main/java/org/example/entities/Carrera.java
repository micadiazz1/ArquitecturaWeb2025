package org.example.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.AllArgsConstructor; // Importar la anotación

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrera")
@Getter
@Setter
@ToString
@NoArgsConstructor // Genera un constructor sin argumentos

@NamedQuery(
        name = "Carrera.findByID",
        query = "SELECT c FROM Carrera c WHERE c.id = :id"
)

public class Carrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 250, nullable = false)
    private String nombre;

    @Column(name = "anio_duracion")
    private int duracion;

    @OneToMany(mappedBy = "carrera")
    private Set<Inscripcion> inscripciones;
}




