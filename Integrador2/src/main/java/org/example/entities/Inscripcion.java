package org.example.entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor

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
    private Timestamp fechaInscripcion;

    @Column
    private Timestamp fechaGraduacion;
    @Column
    private int antiguedad; // en años, meses, etc.


    public Inscripcion(int idInscripcion, Estudiante estudiante, Carrera carrera, Timestamp fechaInscripcion, Timestamp fechaGraduacion, int antiguedad) {
        this.idInscripcion = idInscripcion;
        this.estudiante = estudiante;
        this.carrera = carrera;
        this.fechaInscripcion = fechaInscripcion;
        this.fechaGraduacion = fechaGraduacion;
        this.antiguedad = antiguedad;
    }
}

