package lei_pl_grupo_c;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import org.json.*;


public class App {

    private JButton readCsvButton;
    private JButton saveJsonButton;
    private JSONArray jsonArray;
    private JEditorPane htmlPane;

    public App() {
        frame = new JFrame("Gestão de Horários");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Menu bar for functionalities
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

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
                horario = ScheduleParser.parseScheduleCSV(file);
                JOptionPane.showMessageDialog(frame, "Horário carregado com sucesso!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Erro ao carregar o horário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveSchedule() {
        int returnValue = fileChooser.showSaveDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            //TODO Implement logic to save Schedule to CSV or JSON file
            JOptionPane.showMessageDialog(frame, "Horário salvo com sucesso!");
        }
    }

    private void showSchedulePane() {
        if (horario == null) {
            JOptionPane.showMessageDialog(frame, "Carregue o horário antes de visualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
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
                new App().frame.setVisible(true);
            }
        });
    }
}
