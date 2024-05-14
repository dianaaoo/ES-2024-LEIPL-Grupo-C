package lei_pl_grupo_c;

import org.json.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.table.DefaultTableModel;
import static org.junit.jupiter.api.Assertions.*;

public class SecondWindowTest {

    private SecondWindow secondWindow;

    @BeforeEach
    public void setUp() {
        secondWindow = new SecondWindow(null); // Pass null as JFrame since it's not used in the tests
    }

    @Test
    public void testReadCsvFileToSecondWindow() {
        // Assuming your sample file is available
        secondWindow.readCsvFileToSecondWindow();
        JSONArray jsonArray = secondWindow.getJsonArray();
        assertNotNull(jsonArray);
        assertEquals(16, jsonArray.length());
    }

    @Test
    public void testSortTableByColumn() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Edifício");
        model.addColumn("Nome sala");
        model.addColumn("Capacidade Normal");
        secondWindow.getDataTable().setModel(model);
        secondWindow.sortTableByColumn(2); // Sort by "Capacidade Normal"
        assertEquals(0, secondWindow.getDataTable().getSelectedRow());
        assertEquals("Capacidade Normal", secondWindow.getDataTable().getColumnName(2));
    }

    @Test
    public void testFilterTable() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Edifício");
        model.addColumn("Nome sala");
        model.addColumn("Capacidade Normal");
        secondWindow.getDataTable().setModel(model);
        secondWindow.filterTextField.setText("AA2.23"); // Filter for a specific room
        secondWindow.filterTable();
        assertEquals(1, secondWindow.getDataTable().getRowCount());
    }
}
