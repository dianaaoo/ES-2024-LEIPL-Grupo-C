package lei_pl_grupo_c;

// jfx imports
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// java lib imports
import java.io.File;
import java.net.URL;

public class App extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();

        MyFxmlController controller = new MyFxmlController();
        controller.setValue("New value");
        loader.setController(controller);

        File fxmlFile = new File("helloworld.fxml");
        URL fxmlUrl = fxmlFile.toURI().toURL();
        loader.setLocation(fxmlUrl);

        VBox vbox = loader.<VBox>load();

        // controller for fxml
        MyFxmlController controllerRef = loader.getController();
        // printing button text getters
        System.out.println(controllerRef.getValue());
        System.out.println(controllerRef.getLabel1Text());
        System.out.println(controllerRef.getLabel2Text());

        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}