package org.example;

import javax.persistence.EntityManager;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.util.function.Function;

import java.io.InputStream;
import java.io.InputStreamReader;


public class CSVLoader<T> {

    private final EntityManager em;

    public CSVLoader(EntityManager em) {
        this.em = em;
    }

    public void loadAndInsert(String resourceName, Function<CSVRecord, T> recordMapper) throws IOException {
        try (
                InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName);
                InputStreamReader reader = new InputStreamReader(is);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
        ) { // Abre el stream del archivo y comienza a parsear línea por línea.
            for (CSVRecord record : csvParser) {
                T entity = recordMapper.apply(record); // La función convierte la fila CSV en un objeto Entidad.
                if (entity != null) {
                    em.persist(entity); // Se inserta la entidad en la base de datos (aún en memoria).
                }
            }
        }
    }
}
