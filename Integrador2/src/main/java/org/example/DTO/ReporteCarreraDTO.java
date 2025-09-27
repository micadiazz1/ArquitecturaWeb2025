package org.example.DTO;

public record ReporteCarreraDTO(
        String nombreCarrera,
        int anio,
        long inscriptos,
        long egresados
) {}

