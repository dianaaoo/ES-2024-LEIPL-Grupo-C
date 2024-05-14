package lei_pl_grupo_c;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void readCsvFile_PopulatesJsonArray() {
        // Arrange
        App app = new App();
        File csvFile = new File("files/preset/CaracterizaçãoDasSalas.csv");

        // Act
        try {
            List<String> lines = Files.readAllLines(csvFile.toPath());

            // Assert
            assertNotNull(app.getJsonArray());
            assertEquals(lines.size() - 1, app.getJsonArray().length()); // Exclude header line
        } catch (IOException e) {
            fail("Error reading CSV file: " + e.getMessage());
        }
    }

    @Test
    void readCsvFile_ValidatesJsonStructure() {
        // Arrange
        App app = new App();
        File csvFile = new File("files/preset/CaracterizaçãoDasSalas.csv");

        // Act
        try {
            List<String> lines = Files.readAllLines(csvFile.toPath());
            JSONArray jsonArray = app.getJsonArray();

            // Assert
            for (int i = 1; i < lines.size(); i++) { // Skip header line
                JSONObject jsonObject = jsonArray.getJSONObject(i - 1);
                String[] values = lines.get(i).split(";");
                assertEquals(values.length, jsonObject.length());

                // Validate keys
                for (String key : jsonObject.keySet()) {
                    assertTrue(lines.get(0).contains(key));
                }
            }
        } catch (IOException | JSONException e) {
            fail("Error validating JSON structure: " + e.getMessage());
        }
    }

    // Add more test methods here as needed

}
