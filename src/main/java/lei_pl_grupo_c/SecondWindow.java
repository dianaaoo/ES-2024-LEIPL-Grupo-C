package lei_pl_grupo_c;

import javax.swing.*;

public class SecondWindow extends JDialog {
    public SecondWindow(JFrame parent) {
        super(parent, "Second Window", true);
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Add your components to the second window here...
        JLabel label = new JLabel("This is the second window");
        add(label);

        setLocationRelativeTo(parent); // Center the window relative to the parent frame
    }
}
