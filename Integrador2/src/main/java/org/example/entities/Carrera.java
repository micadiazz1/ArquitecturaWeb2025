package org.example.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor

@Entity
public class Carrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCarrera;

    @Column(length = 250, nullable = false)
    private String nombreCarrera;

    @Column(name = "anioDuracion")
    private int duracion;

    @OneToMany(mappedBy = "carrera", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    public Carrera(int idCarrera, String nombreCarrera, int duracion) {
        this.idCarrera = idCarrera;
        this.nombreCarrera = nombreCarrera;
        this.duracion = duracion;
    }


}
