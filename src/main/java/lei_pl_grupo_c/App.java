package lei_pl_grupo_c;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.*;

public class App {

    private static final String HORARIO_CSV_EXAMPLE = "HorarioDeExemplo.csv";
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
        JMenuItem loadScheduleMenuItem = new JMenuItem("Carregar Horário");
        fileMenu.add(loadScheduleMenuItem);
        loadScheduleMenuItem.addActionListener((ActionEvent e) -> loadSchedule());
        JMenuItem saveScheduleMenuItem = new JMenuItem("Gravar Horário");
        fileMenu.add(saveScheduleMenuItem);
        saveScheduleMenuItem.addActionListener((ActionEvent e) -> saveSchedule());
        // Menu for Schedule view and manipulation
        JMenu ScheduleMenu = new JMenu("Horário");
        menuBar.add(ScheduleMenu);
        JMenuItem viewScheduleMenuItem = new JMenuItem("Visualizar Horário");
        ScheduleMenu.add(viewScheduleMenuItem);

        // Other menus for functionalities (implement similar structure)
        // - Cadastro de Salas (Classrooms)
        // - Sugerir Substituição (Suggest Substitution)
        // - Sugerir Aulas UC (Suggest Course)
        
        // Button to open Schedule in web browser
        JButton button = new JButton("Mostrar Salas no Browser Web");  
        button.setBounds(20,20,250,50);     
        button.addActionListener((ActionEvent e) -> openScheduleInBrowser());
        frame.getContentPane().add(button);

        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir"))); // Set default directory
    }

    private void loadSchedule() {
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
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
        // TODO Implement logic to display Schedule Pane
    }

    private void openScheduleInBrowser() {
        if (horario == null) {
            JOptionPane.showMessageDialog(frame, "Carregue o horário antes de abrir no navegador.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // TODO Open Schedule file in web browser
        try {
            Desktop desk = Desktop.getDesktop(); 
            desk.browse(new java.net.URI("file://" + System.getProperty("user.dir") + File.separator + HORARIO_CSV_EXAMPLE));
        } catch (IOException | URISyntaxException e) {
            JOptionPane.showMessageDialog(frame, "Erro ao abrir o horário no navegador: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new App().frame.setVisible(true);
            }
        });
    }
}
