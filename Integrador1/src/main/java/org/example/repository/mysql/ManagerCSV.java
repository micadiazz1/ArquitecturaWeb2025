package org.example.repository.mysql;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ManagerCSV {
    private static ManagerCSV instance;
    public static ManagerCSV getInstance(){
        if(instance == null){
            instance = new ManagerCSV();
        }
        return instance;
    }
    public CSVParser getRecords(String path) throws IOException {
        File csvFile = new File(path);
        if (!csvFile.exists()) {
            System.err.println("ERROR: El archivo CSV no existe en la ruta especificada");
            System.err.println("Ruta esperada: " + csvFile.getAbsolutePath());
            return null;
        }
        FileReader reader = new FileReader(csvFile);
            return CSVFormat.DEFAULT.withHeader().parse(reader);
    }
}
