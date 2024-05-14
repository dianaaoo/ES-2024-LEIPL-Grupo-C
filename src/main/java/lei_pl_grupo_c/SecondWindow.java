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
import java.util.ArrayList;
import java.util.List;

import java.util.regex.PatternSyntaxException;

public class SecondWindow extends JDialog {
    private JSONArray jsonArray;
    private JTable dataTable;
    private JScrollPane tableScrollPane;
    private JComboBox<String> sortColumnComboBox;
    private JButton sortButton;
    private JButton filterButton;
    private JLabel filterLabel;
    private JTextField filterTextField;

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

        sortButton = new JButton("Sort");
        sortButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortTableBySelectedColumn();
            }
        });

        upperPanel.add(sortColumnComboBox);
        upperPanel.add(sortButton);

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

        add(upperPanel, BorderLayout.NORTH);

        dataTable = new JTable();
        tableScrollPane = new JScrollPane(dataTable);
        add(tableScrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(parent); // Center the window relative to the parent frame
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
}
