package lei_pl_grupo_c;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.regex.PatternSyntaxException;

import java.util.Arrays;

public class SecondWindow extends JDialog {
    private JSONArray jsonArray;
    private final JTable dataTable;
    private final JComboBox<String> sortColumnComboBox;
    private final JTextField filterTextField;

    public SecondWindow(JFrame parent) {
        super(parent, "Second Window", true);
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new FlowLayout());

        sortColumnComboBox = new JComboBox<>();
        sortColumnComboBox.addItem("Edifício");
        sortColumnComboBox.addItem("Nome sala");
        sortColumnComboBox.addItem("Capacidade Normal");
        sortColumnComboBox.addItem("Capacidade Exame");
        sortColumnComboBox.addItem("Nº características");
        sortColumnComboBox.addItem("Anfiteatro aulas");
        sortColumnComboBox.addItem("Apoio técnico eventos");
        sortColumnComboBox.addItem("Arq 1");
        sortColumnComboBox.addItem("Arq 2");
        sortColumnComboBox.addItem("Arq 3");
        sortColumnComboBox.addItem("Arq 4");
        sortColumnComboBox.addItem("Arq 5");
        sortColumnComboBox.addItem("Arq 6");
        sortColumnComboBox.addItem("Arq 9");
        sortColumnComboBox.addItem("BYOD (Bring Your Own Device)");
        sortColumnComboBox.addItem("Focus Group");
        sortColumnComboBox.addItem("Horário sala visível portal público");
        sortColumnComboBox.addItem("Laboratório de Arquitectura de Computadores I");
        sortColumnComboBox.addItem("Laboratório de Arquitectura de Computadores II");
        sortColumnComboBox.addItem("Laboratório de Bases de Engenharia");
        sortColumnComboBox.addItem("Laboratório de Electrónica");
        sortColumnComboBox.addItem("Laboratório de Informática");
        sortColumnComboBox.addItem("Laboratório de Jornalismo");
        sortColumnComboBox.addItem("Laboratório de Redes de Computadores I");
        sortColumnComboBox.addItem("Laboratório de Redes de Computadores II");
        sortColumnComboBox.addItem("Laboratório de Telecomunicações");
        sortColumnComboBox.addItem("Sala Aulas Mestrado");
        sortColumnComboBox.addItem("Sala Aulas Mestrado Plus");
        sortColumnComboBox.addItem("Sala NEE");
        sortColumnComboBox.addItem("Sala Provas");
        sortColumnComboBox.addItem("Sala Reunião");
        sortColumnComboBox.addItem("Sala de Arquitectura");
        sortColumnComboBox.addItem("Sala de Aulas normal");
        sortColumnComboBox.addItem("videoconferência");
        sortColumnComboBox.addItem("Átrio");

        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(e -> sortTableBySelectedColumn());

        upperPanel.add(sortColumnComboBox);
        upperPanel.add(sortButton);

        JButton filterButton = new JButton("Filter");
        JLabel filterLabel = new JLabel("Filter by: ");
        filterTextField = new JTextField(20);

        filterButton.addActionListener(e -> filterTable());

        upperPanel.add(filterLabel);
        upperPanel.add(filterTextField);
        upperPanel.add(filterButton);

        add(upperPanel, BorderLayout.NORTH);

        dataTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(dataTable);
        add(tableScrollPane, BorderLayout.CENTER);

        readCsvFileToSecondWindow();

        setLocationRelativeTo(parent);
    }

    private void readCsvFileToSecondWindow() {
        String filePath = "files/preset/CaracterizaçãoDasSalas.csv";
        File csvFile = new File(filePath);

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
                    if (values.length == headers.length) {
                        JSONObject jsonObject = new JSONObject();
                        for (int i = 0; i < headers.length; i++) {
                            jsonObject.put(headers[i], values[i]);
                        }
                        jsonArray.put(Collections.singleton(jsonObject));
                    } else {
                        System.err.println("Number of values doesn't match the number of headers: " + line);
                    }
                }
            }
            reader.close();
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }

        displayDataInTable(jsonArray);
    }

    private void displayDataInTable(JSONArray jsonArray) {
        DefaultTableModel model = new DefaultTableModel();

        List<String> columnOrder = Arrays.asList("Edifício", "Nome sala", "Capacidade Normal", "Capacidade Exame", "Nº características", "Anfiteatro aulas", "Apoio técnico eventos", "Arq 1", "Arq 2", "Arq 3", "Arq 4", "Arq 5", "Arq 6", "Arq 9", "BYOD (Bring Your Own Device)", "Focus Group", "Horário sala visível portal público", "Laboratório de Arquitectura de Computadores I", "Laboratório de Arquitectura de Computadores II", "Laboratório de Bases de Engenharia", "Laboratório de Electrónica", "Laboratório de Informática", "Laboratório de Jornalismo", "Laboratório de Redes de Computadores I", "Laboratório de Redes de Computadores II", "Laboratório de Telecomunicações", "Sala Aulas Mestrado", "Sala Aulas Mestrado Plus", "Sala NEE", "Sala Provas", "Sala Reunião", "Sala de Arquitectura", "Sala de Aulas normal", "videoconferência", "Átrio");

        for (String columnName : columnOrder) {
            model.addColumn(columnName);
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject row = jsonArray.optJSONObject(i);
            if (row != null) {
                Object[] rowData = new Object[columnOrder.size()];
                for (int j = 0; j < columnOrder.size(); j++) {
                    rowData[j] = row.opt(columnOrder.get(j));
                }
                model.addRow(rowData);
            }
        }

        dataTable.setModel(model);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        dataTable.setRowSorter(sorter);
    }

    private void sortTableBySelectedColumn() {
        String selectedColumn = (String) sortColumnComboBox.getSelectedItem();
        int columnIndex = getColumnIndex(selectedColumn);
        if (columnIndex != -1) {
            sortTableByColumn(columnIndex);
        }
    }

    private void sortTableByColumn(int columnIndex) {
        TableRowSorter(columnIndex, dataTable);
    }

    static void TableRowSorter(int columnIndex, JTable dataTable) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) dataTable.getModel());
        sorter.setSortable(dataTable.getColumnCount() - 1, false);
        dataTable.setRowSorter(sorter);

        List<RowSorter.SortKey> sortKeys = new ArrayList<>(sorter.getSortKeys());

        boolean sorted = false;
        for (RowSorter.SortKey sortKey : sortKeys) {
            if (sortKey.getColumn() == columnIndex) {
                sorter.setSortKeys(null);
                sorted = true;
                break;
            }
        }

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

    private void filterTable() {
        filterText(filterTextField, dataTable);
    }

    static void filterText(JTextField filterTextField, JTable dataTable) {
        String filterText = filterTextField.getText().trim();
        if (filterText.isEmpty()) {
            ((DefaultRowSorter) dataTable.getRowSorter()).setRowFilter(null);
            return;
        }

        RowFilter<DefaultTableModel, Object> rowFilter = RowFilter.regexFilter(filterText);

        try {
            ((DefaultRowSorter) dataTable.getRowSorter()).setRowFilter(rowFilter);
        } catch (PatternSyntaxException ex) {
            System.err.println("Invalid regex pattern for filtering: " + ex.getMessage());
        }
    }
}
