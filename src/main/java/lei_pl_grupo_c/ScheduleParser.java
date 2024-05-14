package lei_pl_grupo_c;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleParser {

    public static List<ScheduleEntry> parseScheduleCSV(File file) throws IOException {
        List<ScheduleEntry> horario = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                ScheduleEntry entry = new ScheduleEntry();
                entry.setCurso(data[0]);
                entry.setUnidadeCurricular(data[1]);
                entry.setTurno(data[2]);
                entry.setTurma(data[3]);
                entry.setInscritos(Integer.parseInt(data[4]));
                entry.setDiaSemana(data[5]);
                entry.setHoraInicio(data[6]);
                entry.setHoraFim(data[7]);
                entry.setDataAula(data[8]);
                entry.setCaracteristicasSala(data[9]);
                entry.setSalaAtribuida(data[10]);
                horario.add(entry);
            }
        }
        return horario;
    }
}
