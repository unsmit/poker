package gui;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class Gui extends Application {

    @Override
    public void start(Stage stage) {
        Label title = new Label("Poker Odds Calculator");
        Button simulate = new Button("Simulate");
        VBox layout = new VBox(20);
        layout.getChildren().addAll(title, simulate);
        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Poker Simulator");
        stage.show();
        stage.setResizable(false);

        
    }

    public static void main(String[] args) {
        launch(args);
    }

}