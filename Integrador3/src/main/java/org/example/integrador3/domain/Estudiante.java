package org.example.integrador3.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.integrador3.domain.utils.Genero;
import org.example.integrador3.service.dto.estudiante.request.EstudianteRequestDTO;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
public class Estudiante {
    private int numLibreta;
    private String nombre;
    private String apellido;
    private int edad;
    @Id
    private long documento;
    private Genero genero;
    private String ciudad;
    @OneToMany(mappedBy = "estudiante")
    private Set<Inscripcion> inscripciones;

    public Estudiante(EstudianteRequestDTO estudiante){
        this.numLibreta=estudiante.getNumLibreta();
        this.nombre=estudiante.getNombre();
        this.apellido=estudiante.getApellido();
        this.edad=estudiante.getEdad();
        this.documento=estudiante.getDocumento();
        this.genero= estudiante.getGenero();
        this.ciudad=estudiante.getCiudad();
    }


}
