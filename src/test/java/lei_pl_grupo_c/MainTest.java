package lei_pl_grupo_c;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void main_StartsAppWithoutError() {
        // Arrange: No special arrangement needed
        
        // Act: Run the main method
        assertDoesNotThrow(() -> Main.main(new String[]{}));
        
        // Assert: Ensure no exceptions were thrown
    }
}
