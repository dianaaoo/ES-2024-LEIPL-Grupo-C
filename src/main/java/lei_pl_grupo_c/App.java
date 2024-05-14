package lei_pl_grupo_c;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Iterator;
import org.json.*;

public class App extends JFrame {
    private JButton readCsvButton;
    private JButton saveJsonButton;
    private JButton readJsonButton;
    private JButton saveCsvButton;
    private JSONArray jsonArray;
    private JEditorPane htmlPane;

    public App() {
        super("Schedule");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new FlowLayout());

        readCsvButton = new JButton("Read CSV");
        readJsonButton = new JButton("Read JSON");
        saveCsvButton = new JButton("Save CSV");
        saveJsonButton = new JButton("Save JSON");

        readCsvButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                readCsvFile();
            }
        });

        readJsonButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                readJsonFile();
            }
        });

        saveCsvButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveCsvFile();
            }
        });

        saveJsonButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveJsonFile();
            }
        });

        upperPanel.add(readCsvButton);
        upperPanel.add(readJsonButton);
        upperPanel.add(saveCsvButton);
        upperPanel.add(saveJsonButton);

        add(upperPanel, BorderLayout.NORTH);

        htmlPane = new JEditorPane();
        htmlPane.setContentType("text/html");
        htmlPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(htmlPane);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void readCsvFile() {
        JFileChooser fileChooser = new JFileChooser(new File("files"));
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
                        headers = line.split(";");
                    } else {
                        String[] values = line.split(";");
                        JSONObject jsonObject = new JSONObject();
                        for (int i = 0; i < headers.length && i < values.length; i++) {
                            jsonObject.put(headers[i], values[i]);
                        }
                        jsonArray.put(jsonObject);
                    }
                }
                reader.close();
            } catch (IOException | JSONException ex) {
                ex.printStackTrace();
            }
        }
        displayDataInHTML(jsonArray);
    }

    private void saveJsonFile() {
        if (jsonArray == null) {
            JOptionPane.showMessageDialog(this, "No data to save", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser(new File("files"));
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
        displayDataInHTML(jsonArray);
    }

    private void readJsonFile() {
        JFileChooser fileChooser = new JFileChooser(new File("files"));
        fileChooser.setDialogTitle("Choose JSON File");
        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File jsonFile = fileChooser.getSelectedFile();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(jsonFile));
                StringBuilder jsonContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line);
                }
                reader.close();

                jsonArray = new JSONArray(jsonContent.toString());

                JOptionPane.showMessageDialog(this, "JSON file read successfully", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | JSONException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error reading JSON file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveCsvFile() {
        if (jsonArray == null || jsonArray.length() == 0) {
            JOptionPane.showMessageDialog(this, "No data to save", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser(new File("files"));
        fileChooser.setDialogTitle("Save CSV File");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File csvFile = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(csvFile)) {
                JSONObject firstRow = jsonArray.optJSONObject(0);
                if (firstRow != null) {
                    Iterator<String> keys = firstRow.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        writer.append(key);
                        if (keys.hasNext()) {
                            writer.append(";");
                        }
                    }
                    writer.append("\n");
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject row = jsonArray.optJSONObject(i);
                    if (row != null) {
                        Iterator<String> values = row.keys();
                        while (values.hasNext()) {
                            String value = row.optString(values.next(), "");
                            writer.append(value);
                            if (values.hasNext()) {
                                writer.append(";");
                            }
                        }
                        writer.append("\n");
                    }
                }

                JOptionPane.showMessageDialog(this, "CSV file saved successfully", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving CSV file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    void displayHTMLContent(String htmlContent) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                htmlPane.setText(htmlContent);
            }
        });
    }





























    
    private String generateHTMLTable(JSONArray jsonArray) {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html>");
        htmlBuilder.append("<head>");
        htmlBuilder.append("<title>Schedule</title>");
        htmlBuilder.append("</head>");
        htmlBuilder.append("<body>");
        htmlBuilder.append("<h1>Schedule</h1>");
        htmlBuilder.append("<table border='1'>");

        JSONObject firstRow = jsonArray.optJSONObject(0);
        if (firstRow != null) {
            htmlBuilder.append("<tr>");
            Iterator<String> keys = firstRow.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                htmlBuilder.append("<th>").append(key).append("</th>");
            }
            // Add the extra columns
            htmlBuilder.append("<th>1</th>");
            htmlBuilder.append("<th>2</th>");
            htmlBuilder.append("</tr>");
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject row = jsonArray.optJSONObject(i);
            if (row != null) {
                htmlBuilder.append("<tr>");
                Iterator<String> values = row.keys();
                while (values.hasNext()) {
                    String value = row.optString(values.next(), "");
                    htmlBuilder.append("<td>").append(value).append("</td>");
                }
                // Add the values for the extra columns
                htmlBuilder.append("<td>testes</td>");
                htmlBuilder.append("<td>8</td>");
                htmlBuilder.append("</tr>");
            }
        }

        htmlBuilder.append("</table>");
        htmlBuilder.append("</body>");
        htmlBuilder.append("</html>");

        return htmlBuilder.toString();
    }



























    void displayDataInHTML(JSONArray jsonArray) {
        String htmlTable = generateHTMLTable(jsonArray);
        displayHTMLContent(htmlTable);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new App().setVisible(true);
            }
        });
    }
}
