package lei_pl_grupo_c;

import javax.swing.*;
// import java.io.BufferedReader;
import java.io.IOException;
// import java.io.InputStream;
// import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                App app = new App();
                app.setVisible(true);

                // Load HTML content from file
                Path htmlFilePath = Paths.get("src", "main", "java", "lei_pl_grupo_c", "table.html");
                String htmlContent = Files.readString(htmlFilePath);

                // Display HTML content
                app.displayHTMLContent(htmlContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
