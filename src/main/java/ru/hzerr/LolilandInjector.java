package ru.hzerr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class LolilandInjector extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        final Parent ROOT = FXMLLoader.load(Objects.requireNonNull(LolilandInjector.class.getResource("/main.fxml")));
        stage.setTitle("LolilandInjector");
        stage.setResizable(false);
        stage.centerOnScreen();
        final Scene scene = new Scene(ROOT);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        LolilandInjector.launch(args);
    }

    @Override
    public void stop() {
        LolilandInjectController.getInjector().shutdownNow();
    }
}
