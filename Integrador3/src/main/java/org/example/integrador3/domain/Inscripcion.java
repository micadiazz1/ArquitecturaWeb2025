package org.example.integrador3.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.integrador3.service.dto.inscripcion.request.InscripcionRequestDTO;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name="id_estudiante", nullable=false)
    private Estudiante estudiante;
    @ManyToOne
    @JoinColumn(name="id_carrera", nullable = false)
    private Carrera carrera;
    private Timestamp fechaInscripcion;
    private Timestamp fechaGraduacion;
    private int antiguedad;

    public Inscripcion(InscripcionRequestDTO i){
        this.id = i.getId();
        this.estudiante = i.getEstudiante();
        this.carrera = i.getCarrera();
        this.fechaInscripcion = i.getFechaInscripcion();
        this.fechaGraduacion = i.getFechaGraduacion();
        this.antiguedad = i.getAntiguedad();
    }

    public Inscripcion(Estudiante estudiante, Carrera carrera, Timestamp timestamp, Timestamp timestamp1, int antiguedad) {
        this.estudiante = estudiante;
        this.carrera = carrera;
        this.fechaInscripcion = timestamp;
        this.fechaGraduacion = timestamp1;
        this.antiguedad = antiguedad;
    }
}
