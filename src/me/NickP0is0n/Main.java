package me.NickP0is0n;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("jTest.fxml"));
        new JMetro(JMetro.Style.LIGHT).applyTheme(root);
        primaryStage.getIcons().add(new Image(new File("resources/logo.png").toURI().toString()));
        com.apple.eawt.Application.getApplication().setDockIconImage(new ImageIcon("resources/logo.png").getImage()); //для иконки в доке macOS
        primaryStage.setTitle("jTest Student");
        primaryStage.setScene(new Scene(root, 640, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static FXMLLoader makeLoader (String fxmlFile) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFile));
        return loader;
    }

    public static Stage startStage (FXMLLoader loader, String title, int width, int height, boolean resizable) throws IOException {
        Stage stage = new Stage();
        stage.getIcons().add(new Image("file:/resources/logo.png"));
        stage.setTitle(title);
        Parent root = (Parent)loader.load();
        new JMetro(JMetro.Style.LIGHT).applyTheme(root);
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.setResizable(resizable);
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
