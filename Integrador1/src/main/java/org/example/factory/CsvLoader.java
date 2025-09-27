package org.example.factory;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;


public class CsvLoader<T> {

    public void loadAndInsert(String filePath, Object dao, Function<CSVRecord, T> recordMapper) throws IOException {
        List<T> objects = new ArrayList<>();

        // Obtener el recurso desde el classpath (la carpeta src/main/resources)
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);

        if (inputStream == null) {
            System.err.println("Error: No se pudo encontrar el archivo " + filePath + ". Asegúrate de que esté en la carpeta src/main/resources.");
            return;
        }

        try (Reader reader = new InputStreamReader(inputStream)) {
            try (CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(reader)) {
                for (CSVRecord record : parser) {
                    objects.add(recordMapper.apply(record));
                }
            }
        }

        // Usar reflexión para llamar al método insertAll() del DAO
        try {
            Method insertAllMethod = dao.getClass().getMethod("insertAll", List.class);
            insertAllMethod.invoke(dao, objects);
            System.out.println("Datos de " + filePath + " insertados con éxito.");
        } catch (Exception e) {
            System.err.println("Error al invocar el método insertAll: " + e.getMessage());
        }
    }
}