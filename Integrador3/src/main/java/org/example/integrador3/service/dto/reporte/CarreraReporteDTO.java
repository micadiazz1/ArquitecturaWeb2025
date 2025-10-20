package org.example.integrador3.service.dto.reporte;

public class CarreraReporteDTO {

    private String nombreCarrera;
    private Integer anio;
    private Long cantidadInscriptos;
    private Long cantidadEgresados;

    public CarreraReporteDTO(String nombreCarrera, Integer anio, Long cantidadInscriptos, Long cantidadEgresados) {
        this.nombreCarrera = nombreCarrera;
        this.anio = anio;
        this.cantidadInscriptos = cantidadInscriptos;
        this.cantidadEgresados = cantidadEgresados;
    }

    // Getters y setters
    public String getNombreCarrera() { return nombreCarrera; }
    public Integer getAnio() { return anio; }
    public Long getCantidadInscriptos() { return cantidadInscriptos; }
    public Long getCantidadEgresados() { return cantidadEgresados; }
}
