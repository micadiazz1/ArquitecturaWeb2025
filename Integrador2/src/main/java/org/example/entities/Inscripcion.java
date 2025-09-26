package org.example.entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.sql.Timestamp;

//@Entity
//@Table(name = "inscripcion")
//@Getter
//@Setter
//@ToString
//@NoArgsConstructor
//public class Inscripcion {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//
//    @ManyToOne
//    @MapsId("documento")
//    @JoinColumn(name = "id_estudiante")
//    private Estudiante estudiante;
//
//
//    @ManyToOne
//    @MapsId("id")
//    @JoinColumn(name = "id_carrera")
//    @NotFound(action = NotFoundAction.IGNORE)
//    private Carrera carrera;
//
//    @Column(name = "fecha_inscripcion")
//    private Timestamp fechaInscripcion;
//
//    @Column(name = "fecha_graduacion")
//    private Timestamp fechaGraduacion;
//
//    @Column
//    private int antiguedad;
//
//    // Constructor que inicializa la clave compuesta
//    public Inscripcion(int id, Estudiante estudiante, Carrera carrera, Timestamp fechaInscripcion, Timestamp fechaGraduacion, int antiguedad) {
//        this.id = id;
//        this.estudiante = estudiante;
//        this.carrera = carrera;
//        this.fechaInscripcion = fechaInscripcion;
//        this.fechaGraduacion = fechaGraduacion;
//        this.antiguedad = antiguedad;
//    }

    @Entity
    @Table(name = "inscripcion")
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public class Inscripcion {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;


        @ManyToOne
        @JoinColumn(name = "id_estudiante", nullable = false)
        private Estudiante estudiante;


        @ManyToOne
        @JoinColumn(name = "id_carrera", nullable = false)
        private Carrera carrera;

        @Column(name = "fecha_inscripcion")
        private Timestamp fechaInscripcion;

        @Column(name = "fecha_graduacion")
        private Timestamp fechaGraduacion;

        @Column
        private int antiguedad;


        public Inscripcion(Estudiante estudiante, Carrera carrera, Timestamp fechaInscripcion, Timestamp fechaGraduacion, int antiguedad) {
            this.estudiante = estudiante;
            this.carrera = carrera;
            this.fechaInscripcion = fechaInscripcion;
            this.fechaGraduacion = fechaGraduacion;
            this.antiguedad = antiguedad;
        }
    }

