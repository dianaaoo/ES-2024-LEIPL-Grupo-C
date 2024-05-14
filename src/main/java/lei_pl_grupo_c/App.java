package lei_pl_grupo_c;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.json.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableRowSorter;
import java.util.regex.PatternSyntaxException;

import java.util.ArrayList;
import java.util.List;

public class App extends JFrame {
    private JButton readCsvButton;
    private JButton saveJsonButton;
    private JButton readJsonButton;
    private JButton saveCsvButton;
    private JSONArray jsonArray;
    private JTable dataTable;
    private JScrollPane tableScrollPane;
    private JComboBox<String> sortColumnComboBox;
    private JButton sortButton;
    private JButton filterButton;
    private JLabel filterLabel;
    private JTextField filterTextField;

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
        sortButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortTableBySelectedColumn();
            }
        });

        bottomPanel.add(sortColumnComboBox);
        bottomPanel.add(sortButton);

        // Create filter components
        filterButton = new JButton("Filter");
        filterLabel = new JLabel("Filter by: ");
        filterTextField = new JTextField(20);

        // Add action listener for filter button
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterTable();
            }
        });

        // Add filter components to upperPanel
        upperPanel.add(filterLabel);
        upperPanel.add(filterTextField);
        upperPanel.add(filterButton);

        add(bottomPanel, BorderLayout.SOUTH);
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
                JOptionPane.showMessageDialog(this, "JSON file saved successfully", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving JSON file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        displayDataInTable(jsonArray);
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

                JOptionPane.showMessageDialog(this, "CSV file saved successfully", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
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
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) dataTable.getModel());
        sorter.setSortable(dataTable.getColumnCount() - 1, false); // Disable sorting for last column
        dataTable.setRowSorter(sorter);

        // Get the current sort keys
        List<RowSorter.SortKey> sortKeys = new ArrayList<>(sorter.getSortKeys());

        // Check if the selected column is already sorted
        boolean sorted = false;
        for (RowSorter.SortKey sortKey : sortKeys) {
            if (sortKey.getColumn() == columnIndex) {
                // Toggle sorting order
                sorter.setSortKeys(null);
                sorted = true;
                break;
            }
        }

        // If the column is not already sorted, sort it in ascending order
        if (!sorted) {
            sortKeys.clear();
            sortKeys.add(new RowSorter.SortKey(columnIndex, SortOrder.ASCENDING));
            sorter.setSortKeys(sortKeys);
        }
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

        // Add the new columns "Semana do ano" and "Semana do semestre"
        model.addColumn("Semana do ano");
        model.addColumn("Semana do semestre");

        // Add other existing columns
        JSONObject firstRow = jsonArray.optJSONObject(0);
        if (firstRow != null) {
            for (String key : firstRow.keySet()) {
                if (!key.equals("1") && !key.equals("2")) { // Exclude columns 1 and 2
                    model.addColumn(key);
                }
            }
        }

        // Add data rows
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject row = jsonArray.optJSONObject(i);
            if (row != null) {
                Object[] rowData = new Object[model.getColumnCount()];
                int j = 0;
                // Add data for the new columns "Semana do ano" and "Semana do semestre"
                String classDateStr = row.optString("Data da aula", "");
                int weekOfYear = calculateWeekOfYear(classDateStr, "02/09/2022");
                int weekOfSemester = calculateWeekOfYear(classDateStr, "01/02/2023");
                rowData[j++] = weekOfYear;
                rowData[j++] = weekOfSemester;
                // Add data for other existing columns
                for (String key : row.keySet()) {
                    if (!key.equals("1") && !key.equals("2")) { // Exclude columns 1 and 2
                        rowData[j++] = row.get(key);
                    }
                }
                model.addRow(rowData);
            }
        }

        // Set the table model
        dataTable.setModel(model);

        // Enable sorting
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

            // Calculate the difference in weeks
            long diffInMillis = calendar.getTimeInMillis() - referenceCalendar.getTimeInMillis();
            int weeksDiff = (int) (diffInMillis / (1000 * 60 * 60 * 24 * 7));

            // Check if the week is before the reset date
            if (weeksDiff < 0) {
                // Use the week count from the "Semana do ano" column
                return calculateWeekOfYear(dateString, "02/09/2022");
            } else {
                // Adjust to start from week 1
                return weeksDiff + 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Error occurred, return 0
        }
    }

    private void filterTable() {
        // Get the text entered in the filter text field
        String filterText = filterTextField.getText().trim();

        // If the filter text is empty, reset the table to display all rows
        if (filterText.isEmpty()) {
            ((DefaultRowSorter) dataTable.getRowSorter()).setRowFilter(null);
            return;
        }

        // Create a RowFilter to filter rows based on the filter text
        RowFilter<DefaultTableModel, Object> rowFilter = RowFilter.regexFilter(filterText);

        // Apply the RowFilter to the TableRowSorter of the table
        try {
            ((DefaultRowSorter) dataTable.getRowSorter()).setRowFilter(rowFilter);
        } catch (PatternSyntaxException ex) {
            // If the filter text is not a valid regex pattern, ignore the filter
            // You can handle this case based on your requirements
            System.err.println("Invalid regex pattern for filtering: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new App().setVisible(true);
            }
        });
    }
}
