package lei_pl_grupo_c;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import org.json.*;

public class App extends JFrame {

    private JButton readCsvButton;
    private JButton saveJsonButton;
    private JSONArray jsonArray;
    private JEditorPane htmlPane;

    public App() {
        super("Schedule");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new FlowLayout());

        readCsvButton = new JButton("Read CSV");
        saveJsonButton = new JButton("Save JSON");

        readCsvButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                readCsvFile();
            }
        });

        saveJsonButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveJsonFile();
            }
        });

        upperPanel.add(readCsvButton);
        upperPanel.add(saveJsonButton);

        add(upperPanel, BorderLayout.NORTH);

        htmlPane = new JEditorPane();
        htmlPane.setContentType("text/html");
        htmlPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(htmlPane);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void readCsvFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose CSV File");
        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File csvFile = fileChooser.getSelectedFile();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(csvFile));
                String line;
                jsonArray = new JSONArray();
                String[] headers = null;
                while ((line = reader.readLine()) != null) {
                    if (headers == null) {
                        headers = line.split(",");
                    } else {
                        String[] values = line.split(",");
                        JSONObject jsonObject = new JSONObject();
                        for (int i = 0; i < headers.length; i++) {
                            jsonObject.put(headers[i], values[i]);
                        }
                        jsonArray.put(jsonObject);
                    }
                }
                reader.close();
                System.out.println(jsonArray.toString());
            } catch (IOException | JSONException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void saveJsonFile() {
        if (jsonArray == null) {
            JOptionPane.showMessageDialog(this, "No data to save", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save JSON File");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File jsonFile = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(jsonFile)) {
                jsonArray.write(writer);
                JOptionPane.showMessageDialog(this, "JSON file saved successfully", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving JSON file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void displayHTMLContent(String htmlContent) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                htmlPane.setText(htmlContent);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                App app = new App();
                app.setVisible(true);

                String htmlContent = "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                        "    <head>\n" +
                        "        <meta charset=\"utf-8\" />\n" +
                        "        <link href=\"https://unpkg.com/tabulator-tables@4.8.4/dist/css/tabulator.min.css\" rel=\"stylesheet\">\n"
                        +
                        "        <script type=\"text/javascript\" src=\"https://code.jquery.com/jquery-3.6.0.min.js\"></script>\n"
                        +
                        "        <script type=\"text/javascript\" src=\"https://unpkg.com/tabulator-tables@4.8.4/dist/js/tabulator.min.js\"></script>\n"
                        +
                        "    </head>\n" +
                        "    <body>\n" +
                        "        <H1>Tipos de Salas de Aula</H1>\n" +
                        "        <div id=\"example-table\"></div>\n" +
                        "        <script type=\"text/javascript\">\n" +
                        "            var tabledata = [\n" +
                        "                {classroomtypedescription:\"Anfiteatro_aulas\", amountofclassroomsofthistype:\"9\", classroomsids:\"[Auditório_B1.03, Auditório_B1.04, Auditório_B2.04, Auditório_0NE01, Auditório_0NE02-Caiano_Pereira, Auditório_0NE03_-_Mário_Murteira, Auditório_1, Auditório_2, Auditório_4]\",},\n"
                        +
                        "                {classroomtypedescription:\"Arq_1\", amountofclassroomsofthistype:\"1\", classroomsids:\"[B3.01]\",},\n"
                        +
                        "                {classroomtypedescription:\"Arq_2\", amountofclassroomsofthistype:\"1\", classroomsids:\"[B3.02]\",},\n"
                        +
                        "                {classroomtypedescription:\"Arq_3\", amountofclassroomsofthistype:\"1\", classroomsids:\"[B3.03]\",},\n"
                        +
                        "                {classroomtypedescription:\"Arq_4\", amountofclassroomsofthistype:\"1\", classroomsids:\"[B3.04]\",},\n"
                        +
                        "                {classroomtypedescription:\"Arq_5\", amountofclassroomsofthistype:\"1\", classroomsids:\"[B3.05]\",},\n"
                        +
                        "                {classroomtypedescription:\"Arq_6\", amountofclassroomsofthistype:\"1\", classroomsids:\"[B3.06]\",},\n"
                        +
                        "                {classroomtypedescription:\"Arq_9\", amountofclassroomsofthistype:\"1\", classroomsids:\"[B3.05]\",},\n"
                        +
                        "                {classroomtypedescription:\"BYOD_(Bring_Your_Own_Device)\", amountofclassroomsofthistype:\"4\", classroomsids:\"[D1.05, D1.07, C6.07, C6.08]\",},\n"
                        +
                        "                {classroomtypedescription:\"Focus_Group\", amountofclassroomsofthistype:\"2\", classroomsids:\"[D0.03, D0.05]\",},\n"
                        +
                        "                {classroomtypedescription:\"Laboratório_de_Arquitectura_de_Computadores_I\", amountofclassroomsofthistype:\"1\", classroomsids:\"[C7.05]\",},\n"
                        +
                        "                {classroomtypedescription:\"Laboratório_de_Arquitectura_de_Computadores_II\", amountofclassroomsofthistype:\"1\", classroomsids:\"[C7.10]\",},\n"
                        +
                        "                {classroomtypedescription:\"Laboratório_de_Bases_de_Engenharia\", amountofclassroomsofthistype:\"1\", classroomsids:\"[C7.07]\",},\n"
                        +
                        "                {classroomtypedescription:\"Laboratório_de_Electrónica\", amountofclassroomsofthistype:\"1\", classroomsids:\"[C7.08]\",},\n"
                        +
                        "                {classroomtypedescription:\"Laboratório_de_Informática\", amountofclassroomsofthistype:\"12\", classroomsids:\"[D1.01, D1.02, D1.03, D1.04, D1.06, D1.09, D1.10, D1.11, D1.12, D1.14, 0S01, 0S02]\",},\n"
                        +
                        "                {classroomtypedescription:\"Laboratório_de_Jornalismo\", amountofclassroomsofthistype:\"1\", classroomsids:\"[C4.09]\",},{classroomtypedescription:\"Laboratório_de_Redes_de_Computadores_I\", amountofclassroomsofthistype:\"1\", classroomsids:\"[C7.06]\",},\n"
                        +
                        "                {classroomtypedescription:\"Laboratório_de_Redes_de_Computadores_II\", amountofclassroomsofthistype:\"1\", classroomsids:\"[C7.09]\",},\n"
                        +
                        "                {classroomtypedescription:\"Laboratório_de_Telecomunicações\", amountofclassroomsofthistype:\"2\", classroomsids:\"[C6.06, C6.09]\",},{classroomtypedescription:\"Sala_Aulas_Mestrado\", amountofclassroomsofthistype:\"68\", classroomsids:\"[Auditório_Afonso_de_Barros, Auditório_Silva_Leal, AA2.23, AA2.24, AA2.25, AA2.26, AA2.28, AA2.29, AA3.23, AA3.24, AA3.25, AA3.26, AA3.28, AA3.29, AA3.30, AA3.40, Auditório_B1.03, Auditório_B1.04, Auditório_C1.03, Auditório_C1.04, C1.01, D1.05, D1.07, Auditório_B2.04, B2.01, B2.02, C2.01, C2.02, C3.01, C3.02, C4.01, C4.02, C4.05, C4.06, C4.07, C4.08, C5.01, C5.02, C5.05, C5.06, C5.07, C5.08, C5.09, C6.01, C6.02, C6.07, C6.08, C6.10, Auditório_0NE01, Auditório_0NE02-Caiano_Pereira, Auditório_0NE03_-_Mário_Murteira, 1E04, 1E05, 1E06, 1E07, 1E08, 1E10, Auditório_1, 2E02, 2E03, 2E04, 2E05, 2E06, 2E07, 2E08, 2E10, Auditório_2, Auditório_4]\",},\n"
                        +
                        "                {classroomtypedescription:\"Sala_Aulas_Mestrado_Plus\", amountofclassroomsofthistype:\"68\", classroomsids:\"[Auditório_Afonso_de_Barros, Auditório_Silva_Leal, AA2.23, AA2.24, AA2.25, AA2.26, AA2.28, AA2.29, AA3.23, AA3.24, AA3.25, AA3.26, AA3.28, AA3.29, AA3.30, AA3.40, Auditório_B1.03, Auditório_B1.04, Auditório_C1.03, Auditório_C1.04, C1.01, D1.05, D1.07, Auditório_B2.04, B2.01, B2.02, C2.01, C2.02, C3.01, C3.02, C4.01, C4.02, C4.05, C4.06, C4.07, C4.08, C5.01, C5.02, C5.05, C5.06, C5.07, C5.08, C5.09, C6.01, C6.02, C6.07, C6.08, C6.10, Auditório_0NE01, Auditório_0NE02-Caiano_Pereira, Auditório_0NE03_-_Mário_Murteira, 1E04, 1E05, 1E06, 1E07, 1E08, 1E10, Auditório_1, 2E02, 2E03, 2E04, 2E05, 2E06, 2E07, 2E08, 2E10, Auditório_2, Auditório_4]\",},\n"
                        +
                        "                {classroomtypedescription:\"Sala_de_Arquitectura\", amountofclassroomsofthistype:\"6\", classroomsids:\"[B3.01, B3.02, B3.03, B3.04, B3.05, B3.06]\",},\n"
                        +
                        "                {classroomtypedescription:\"Sala_de_Aulas_normal\", amountofclassroomsofthistype:\"70\", classroomsids:\"[Auditório_Afonso_de_Barros, Auditório_Silva_Leal, AA2.23, AA2.24, AA2.25, AA2.26, AA2.28, AA2.29, AA3.23, AA3.24, AA3.25, AA3.26, AA3.28, AA3.29, AA3.30, AA3.40, Auditório_B1.03, Auditório_B1.04, Auditório_C1.03, Auditório_C1.04, C1.01, D1.05, D1.07, Auditório_B2.04, B2.01, B2.02, C2.01, C2.02, C3.01, C3.02, C4.01, C4.02, C4.05, C4.06, C4.07, C4.08, C5.01, C5.02, C5.05, C5.06, C5.07, C5.08, C5.09, C6.01, C6.02, C6.07, C6.08, C6.10, Auditório_0NE01, Auditório_0NE02-Caiano_Pereira, Auditório_0NE03_-_Mário_Murteira, 1E02, 1E03, 1E04, 1E05, 1E06, 1E07, 1E08, 1E10, Auditório_1, 2E02, 2E03, 2E04, 2E05, 2E06, 2E07, 2E08, 2E10, Auditório_2, Auditório_4]\",}\n"
                        +
                        "            ];\n" +
                        "            var table = new Tabulator(\"#example-table\", {\n" +
                        "                data:tabledata,\n" +
                        "                layout:\"fitDatafill\",\n" +
                        "                pagination:\"local\",\n" +
                        "                paginationSize:10,\n" +
                        "                paginationSizeSelector:[5, 10, 20, 40],\n" +
                        "                movableColumns:true,\n" +
                        "                paginationCounter:\"rows\",\n" +
                        "                initialSort:[{column:\"building\",dir:\"asc\"}],\n" +
                        "                columns:[\n" +
                        "                    {title:\"Descrição do Tipo de Sala\", field:\"classroomtypedescription\", headerFilter:\"input\"},\n"
                        +
                        "                    {title:\"Quantidade\", field:\"amountofclassroomsofthistype\", headerFilter:\"input\"},\n"
                        +
                        "                    {title:\"Sala\", field:\"classroomsids\", headerFilter:\"input\"},\n" +
                        "                ],\n" +
                        "            });\n" +
                        "        </script>\n" +
                        "    </body>\n" +
                        "</html>";

                app.displayHTMLContent(htmlContent);
            }
        });
    }
}
