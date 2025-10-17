package org.example.integrador3.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
}
