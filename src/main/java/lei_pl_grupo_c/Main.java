package lei_pl_grupo_c;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                App app = new App();
                app.setVisible(true);

                try {
                    // Load HTML content from file
                    Path htmlFilePath = Paths.get("table.html");
                    String htmlContent = Files.readString(htmlFilePath);

                    // Display HTML content
                    app.displayHTMLContent(htmlContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
