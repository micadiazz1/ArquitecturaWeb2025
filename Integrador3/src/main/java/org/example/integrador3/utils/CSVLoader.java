package org.example.integrador3.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CSVLoader<T> {

    private final JpaRepository<T, ?> repository;

    public CSVLoader(JpaRepository<T, ?> repository) {
        this.repository = repository;
    }

    public void loadAndInsert(String resourceName, Function<CSVRecord, T> recordMapper) throws IOException {
        List<T> entities = new ArrayList<>();
        try (
                InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName);
                InputStreamReader reader = new InputStreamReader(is);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())
        ) {
            for (CSVRecord record : csvParser) {
                T entity = recordMapper.apply(record);
                if (entity != null) {
                    entities.add(entity);
                }
            }
        }
        repository.saveAll(entities); // Persistir todas las entidades en una sola operación
    }
}