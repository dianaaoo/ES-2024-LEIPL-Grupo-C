package lei_pl_grupo_c;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.json.*;

import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;


public class App extends JFrame {
    private final JButton readCsvButton;
    private final JButton saveJsonButton;
    private final JButton readJsonButton;
    private final JButton saveCsvButton;
    private JSONArray jsonArray;
    private final JTable dataTable;
    private final JScrollPane tableScrollPane;
    private final JComboBox<String> sortColumnComboBox;
    private final JButton sortButton;
    private final JButton filterButton;
    private final JLabel filterLabel;
    private final JTextField filterTextField;

    public App() {
        super("Schedule");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new FlowLayout());

        readCsvButton = new JButton("Read CSV");
        readJsonButton = new JButton("Read JSON");
        saveCsvButton = new JButton("Save CSV");
        saveJsonButton = new JButton("Save JSON");

        readCsvButton.addActionListener(e -> readCsvFileToMainWindow());
        readJsonButton.addActionListener(e -> readJsonFileToMainWindow());
        saveCsvButton.addActionListener(e -> saveCsvFile());
        saveJsonButton.addActionListener(e -> saveJsonFile());

        upperPanel.add(readCsvButton);
        upperPanel.add(readJsonButton);
        upperPanel.add(saveCsvButton);
        upperPanel.add(saveJsonButton);

        add(upperPanel, BorderLayout.NORTH);

        dataTable = new JTable();
        tableScrollPane = new JScrollPane(dataTable);
        add(tableScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        sortColumnComboBox = new JComboBox<>();
        sortColumnComboBox.addItem("Semana do ano");
        sortColumnComboBox.addItem("Semana do semestre");
        sortColumnComboBox.addItem("Curso");
        sortColumnComboBox.addItem("Unidade Curricular");
        sortColumnComboBox.addItem("Turno");
        sortColumnComboBox.addItem("Turma");
        sortColumnComboBox.addItem("Inscritos no turno");
        sortColumnComboBox.addItem("Dia da semana");
        sortColumnComboBox.addItem("Hora início da aula");
        sortColumnComboBox.addItem("Hora fim da aula");
        sortColumnComboBox.addItem("Data da aula");
        sortColumnComboBox.addItem("Características da sala pedida para a aula");
        sortColumnComboBox.addItem("Sala atribuída à aula");

        sortButton = new JButton("Sort");
        sortButton.addActionListener(e -> sortTableBySelectedColumn());

        bottomPanel.add(sortColumnComboBox);
        bottomPanel.add(sortButton);

        filterButton = new JButton("Filter");
        filterLabel = new JLabel("Filter by: ");
        filterTextField = new JTextField(20);

        filterButton.addActionListener(e -> filterTable());

        upperPanel.add(filterLabel);
        upperPanel.add(filterTextField);
        upperPanel.add(filterButton);

        JButton openSecondWindowButton = new JButton("Ver caracterização das salas");
        openSecondWindowButton.addActionListener(e -> openSecondWindow());

        bottomPanel.add(openSecondWindowButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void openSecondWindow() {
        SecondWindow secondWindow = new SecondWindow(this);
        secondWindow.setVisible(true);
    }

    private void readCsvFileToMainWindow() {
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
        displayDataInTable(jsonArray);
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
                JOptionPane.showMessageDialog(this, "JSON file saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving JSON file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        displayDataInTable(jsonArray);
    }

    private void readJsonFileToMainWindow() {
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

                JOptionPane.showMessageDialog(
                        this, "JSON file read successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | JSONException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        this, "Error reading JSON file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        displayDataInTable(jsonArray);
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

                JOptionPane.showMessageDialog(this, "CSV file saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving CSV file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void sortTableBySelectedColumn() {
        String selectedColumn = (String) sortColumnComboBox.getSelectedItem();
        int columnIndex = getColumnIndex(selectedColumn);
        if (columnIndex != -1) {
            sortTableByColumn(columnIndex);
        }
    }

    private void sortTableByColumn(int columnIndex) {
        SecondWindow.TableRowSorter(columnIndex, dataTable);
    }

    private int getColumnIndex(String columnName) {
        for (int i = 0; i < dataTable.getColumnCount(); i++) {
            if (dataTable.getColumnName(i).equals(columnName)) {
                return i;
            }
        }
        return -1;
    }

    private void displayDataInTable(JSONArray jsonArray) {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Semana do ano");
        model.addColumn("Semana do semestre");

        JSONObject firstRow = jsonArray.optJSONObject(0);
        if (firstRow != null) {
            for (String key : firstRow.keySet()) {
                if (!key.equals("1") && !key.equals("2")) {
                    model.addColumn(key);
                }
            }
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject row = jsonArray.optJSONObject(i);
            if (row != null) {
                Object[] rowData = new Object[model.getColumnCount()];
                int j = 0;
                String classDateStr = row.optString("Data da aula", "");
                int weekOfYear = calculateWeekOfYear(classDateStr, "02/09/2022");
                int weekOfSemester = calculateWeekOfYear(classDateStr, "01/02/2023");
                rowData[j++] = weekOfYear;
                rowData[j++] = weekOfSemester;
                for (String key : row.keySet()) {
                    if (!key.equals("1") && !key.equals("2")) {
                        rowData[j++] = row.get(key);
                    }
                }
                model.addRow(rowData);
            }
        }

        dataTable.setModel(model);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        dataTable.setRowSorter(sorter);
    }

    private int calculateWeekOfYear(String dateString, String referenceDateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = sdf.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            Date referenceDate = sdf.parse(referenceDateString);
            Calendar referenceCalendar = Calendar.getInstance();
            referenceCalendar.setTime(referenceDate);

            long diffInMillis = calendar.getTimeInMillis() - referenceCalendar.getTimeInMillis();
            int weeksDiff = (int) (diffInMillis / (1000 * 60 * 60 * 24 * 7));

            if (weeksDiff < 0) {
                return calculateWeekOfYear(dateString, "02/09/2022");
            } else {
                return weeksDiff + 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void filterTable() {
        SecondWindow.filterText(filterTextField, dataTable);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new App().setVisible(true);
            }
        });
    }
}
