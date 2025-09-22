package org.example.entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idInscripcion;

    @ManyToOne
    private Estudiante estudiante;

    @ManyToOne
    private Carrera carrera;

    @Column
    private int antiguedad; // en años, meses, etc.

    @Column
    private Timestamp fechaInscripcion;

    @Column
    private Timestamp fechaGraduacion;

    public Inscripcion(Estudiante estudiante, Carrera carrera, Timestamp fechaInscripcion, Timestamp fechaGraduacion) {
        this.estudiante = estudiante;
        this.carrera = carrera;
        this.antiguedad = 0;
        this.fechaInscripcion = fechaInscripcion;
        this.fechaGraduacion = fechaGraduacion;
    }
}

