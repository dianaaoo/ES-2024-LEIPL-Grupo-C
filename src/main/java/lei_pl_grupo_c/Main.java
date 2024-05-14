package lei_pl_grupo_c;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * contains the main method that serves as the starting point
 */

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                App app = new App();
                app.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
