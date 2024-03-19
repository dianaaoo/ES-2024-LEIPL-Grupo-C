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
import java.util.ArrayList;
import java.util.Arrays;
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

        // FIXME - temos de definir como é que indicamos o ficheiro certo i.e., como
        // passamos o argumento do file browser
        Scanner scanner = new Scanner(new File("schedule_manager/src/samples/HorarioDeExemplo.csv"));

        ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();

        while (scanner.hasNextLine()) {

            String[] lineContents = scanner.nextLine().toString().split(";");

            ArrayList<String> line = new ArrayList<String>();

            for (String content : lineContents) {
                line.add(content);
            }

            System.out.println(line);

        }

        scanner.close();

    }
}
