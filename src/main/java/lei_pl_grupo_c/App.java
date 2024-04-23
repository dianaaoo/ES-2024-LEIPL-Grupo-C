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
                JOptionPane.showMessageDialog(this, "JSON file saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
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
                App converter = new App();
                converter.setVisible(true);

                // Sample HTML content
                String htmlContent = "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                    "   <head>\n" +
                    "      <meta charset=\"utf-8\" />\n" +
                    "      <link href=\"https://unpkg.com/tabulator-tables@4.8.4/dist/css/tabulator.min.css\" rel=\"stylesheet\">\n" +
                    "      <script type=\"text/javascript\" src=\"https://unpkg.com/tabulator-tables@4.8.4/dist/js/tabulator.min.js\"></script>\n" +
                    "   </head>\n" +
                    "   <body>\n" +
                    "      <H1>Tipos de Salas de Aula</H1>\n" +
                    "      <div id=\"example-table\"></div>\n" +
                    "      <script type=\"text/javascript\">\n" +
                    "         var tabledata = [ /* Your data here */ ];\n" +
                    "         var table = new Tabulator(\"#example-table\", {\n" +
                    "            data: tabledata,\n" +
                    "            layout: \"fitDatafill\",\n" +
                    "            pagination: \"local\",\n" +
                    "            paginationSize: 10,\n" +
                    "            paginationSizeSelector: [5, 10, 20, 40],\n" +
                    "            movableColumns: true,\n" +
                    "            paginationCounter: \"rows\",\n" +
                    "            initialSort: [{ column: \"building\", dir: \"asc\" }],\n" +
                    "            columns: [\n" +
                    "               { title: \"Descrição do Tipo de Sala\", field: \"classroomtypedescription\", headerFilter: \"input\" },\n" +
                    "               { title: \"Quantidade\", field: \"amountofclassroomsofthistype\", headerFilter: \"input\" },\n" +
                    "               { title: \"Sala\", field: \"classroomsids\", headerFilter: \"input\" },\n" +
                    "            ],\n" +
                    "         });\n" +
                    "      </script>\n" +
                    "   </body>\n" +
                    "</html>";

                converter.displayHTMLContent(htmlContent);
            }
        });
    }
}
