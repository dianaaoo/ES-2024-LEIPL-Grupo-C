package lei_pl_grupo_c;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
    
public class App extends JFrame {
    private JSONArray jsonArray;
    private JEditorPane htmlPane;

    private final JFrame frame;
    private final JFileChooser fileChooser;
    private List<ScheduleEntry> horario; // List to store Schedule entries

    public App() {  
        frame = new JFrame("Gestão de Horários");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Menu bar for functionalities
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        // Menu for File operations
        JMenu fileMenu = new JMenu("Ficheiro");
        menuBar.add(fileMenu);
        JMenuItem loadScheduleMenuItem = new JMenuItem("Ler JSON");
        fileMenu.add(loadScheduleMenuItem);
        loadScheduleMenuItem.addActionListener((ActionEvent e) -> readJsonFile());
        JMenuItem saveScheduleMenuItem = new JMenuItem("Ler CSV");
        fileMenu.add(saveScheduleMenuItem);
        saveScheduleMenuItem.addActionListener((ActionEvent e) -> readCsvFile());

        // Menu for Schedule view and manipulation
        JMenu ScheduleMenu = new JMenu("Horário");
        menuBar.add(ScheduleMenu);
        JMenuItem viewScheduleMenuItem = new JMenuItem("Visualizar Horário");
        ScheduleMenu.add(viewScheduleMenuItem);
        //viewScheduleMenuItem.addActionListener((ActionEvent e) -> showSchedulePane());
        // - Sugerir Substituição (Suggest Substitution)
        // - Sugerir Aulas UC (Suggest Course)
        
        // Button to open Schedule in web browser
        JButton button = new JButton("Mostrar Salas no Browser Web");  
        button.setBounds(20,20,250,50);     
        //button.addActionListener((ActionEvent e) -> openScheduleInBrowser());
        htmlPane = new JEditorPane();
        htmlPane.setContentType("text/html");
        htmlPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(htmlPane);
        add(scrollPane, BorderLayout.CENTER);
        
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir"))); // Set default directory
        frame.pack();
    }

    private void readCsvFile() {
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

        fileChooser.setDialogTitle("Save CSV File");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File csvFile = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(csvFile)) {
                // Write headers
                JSONObject firstRow = jsonArray.optJSONObject(0);
                if (firstRow != null) {
                    Iterator<String> keys = firstRow.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        writer.append(key);
                        if (keys.hasNext()) {
                            writer.append(",");
                        }
                    }
                    writer.append("\n");
                }

                // Write data
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject row = jsonArray.optJSONObject(i);
                    if (row != null) {
                        Iterator<String> values = row.keys();
                        while (values.hasNext()) {
                            String value = row.optString(values.next(), "");
                            writer.append(value);
                            if (values.hasNext()) {
                                writer.append(",");
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

    private void displayHTMLContent(String htmlContent) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                htmlPane.setText(htmlContent);
            }
        });
    }

    public static void main(String[] args) {
        new App().frame.setVisible(true);
    }
}
