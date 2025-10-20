package org.example.integrador3.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Data
@NoArgsConstructor

public class Carrera {
    @Id
    private int id;
    private String nombre;
    private int duracion;
    @OneToMany(mappedBy = "carrera")
    private Set<Inscripcion> inscripciones;

}