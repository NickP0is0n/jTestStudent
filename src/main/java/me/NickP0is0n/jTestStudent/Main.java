package me.NickP0is0n.jTestStudent;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro8.JMetro;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("jTest.fxml"));
        new JMetro(JMetro.Style.LIGHT).applyTheme(root);
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("logo.png")));
        primaryStage.setTitle("jTest Student");
        primaryStage.setScene(new Scene(root, 649, 380));
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        java.awt.Image logo = SwingFXUtils.fromFXImage(new Image(Main.class.getClassLoader().getResourceAsStream("logo.png")), null);
        if(System.getProperty("os.name").equals("Mac OS X")) com.apple.eawt.Application.getApplication().setDockIconImage(logo); //для иконки в доке macOS
        primaryStage.show();
    }

    public static FXMLLoader makeLoader (String fxmlFile) {
        FXMLLoader loader = new FXMLLoader(Main.class.getClassLoader().getResource(fxmlFile));
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
