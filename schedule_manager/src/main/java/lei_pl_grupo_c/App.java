package lei_pl_grupo_c;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

// Apache Commons CSV 
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        System.out.println("Running Java Main");

        try {
            readCSV();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readCSV() throws IOException {

        // String[] HEADERS = { "Curso", "Unidade Curricular", "Turno", "Turma",
        // "Inscritos no turno", "Dia da semana",
        // "Hora início da aula", "Hora fima da aula", "Data da aula",
        // "Características da sala pedida para a aula", "Sala atribuída à aula" };

        // try (
        // Reader reader =
        // Files.newBufferedReader(Paths.get("schedule_manager/src/samples/HorarioDeExemplo.csv"));
        // // salta os headers no ficheiro
        // CSVParser csvParser = new CSVParser(reader,
        // CSVFormat.DEFAULT.builder().setHeader(HEADERS).setSkipHeaderRecord(true).build());)
        // {

        // for (CSVRecord csvRecord : csvParser) {

        // // Accessing Values by Column Index
        // String curso = csvRecord.get(0);
        // // String unidadeCurricular = csvRecord.get(1);
        // // String turno = csvRecord.get(2);
        // // String turma = csvRecord.get(3);

        // System.out.println("Curso: " + curso);
        // // System.out.println("Unidade curricular: " + unidadeCurricular);
        // // System.out.println("Turno: " + turno);
        // // System.out.println("Turma: " + turma);

        // }

        // }

        // T3

        Scanner scanner = new Scanner(new File("schedule_manager/src/samples/HorarioDeExemplo.csv"));

        scanner.useDelimiter(";"); // separa por ';'

        while (scanner.hasNext()) {
            System.out.print(scanner.next());
        }
        
        scanner.close();

    }
}
