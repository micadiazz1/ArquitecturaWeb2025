package org.example.integrador3.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inscripcion {
    @Id
    private int id;
    @ManyToOne
    @JoinColumn(name="id_estudiante", nullable=false)
    private Estudiante estudiante;
    @ManyToOne
    @JoinColumn(name="id_carrera", nullable = false)
    private Carrera carrera;
    private Timestamp fechaInscripcion;
    private Timestamp fechaGradiacopm;
    private int antiguedad;




}
