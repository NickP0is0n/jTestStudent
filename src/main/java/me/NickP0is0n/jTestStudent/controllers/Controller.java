package me.NickP0is0n.jTestStudent.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.NickP0is0n.jTestStudent.Main;
import me.NickP0is0n.jTestStudent.models.Student;
import me.NickP0is0n.jTestStudent.models.TaskSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;

public class Controller {

    static Student currentStudent;

    @FXML
    private TextField nameField;

    @FXML
    private TextField SurnameField;

    @FXML
    private TextField gradeField;

    @FXML
    private PasswordField passField;

    private Alert makeAlert(Alert.AlertType type, String title, String header, String context)
    {
        Alert aboutAlert = new Alert(type); //Создание окна
        aboutAlert.setTitle(title);
        aboutAlert.setHeaderText(header);
        aboutAlert.setContentText(context);
        return aboutAlert;
    }

    @FXML
    void onAboutBtn(ActionEvent event) throws IOException {
        FXMLLoader loader = Main.makeLoader("about.fxml");
        Stage stage = Main.startStage(loader, "About jTest Student", 600, 400, false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    @FXML
    void onExitBtn(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void onOpenBtn(ActionEvent event) throws IOException {
        if (nameField.getText().isEmpty() || SurnameField.getText().isEmpty() || gradeField.getText().isEmpty()) showError("You haven't filled all the fields. Please fill them and try again!"); //защита от дурака
        else {
            FileChooser chooser = new FileChooser(); //диалог открытия
            chooser.setTitle("Choose task set file");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Task set files (.jt)", "*.jt")); //фильтр файлов
            File taskFile = chooser.showOpenDialog(new Stage()); //показ диалога на отдельной сцене
            if(taskFile != null)
            {
                TaskSet selectedTaskSet = null;
                try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(taskFile)))
                {
                    selectedTaskSet = (TaskSet) ois.readObject();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                    showError("File reading error!");
                }
                assert selectedTaskSet != null;
                if(selectedTaskSet.isPasswordProtected() && !selectedTaskSet.getPassword().equals(passField.getText()))
                {
                    showError("This file encrypted with password. Entered password is incorrect!");
                }
                else
                {
                    FXMLLoader loader = Main.makeLoader("taskSelection.fxml");
                    currentStudent = new Student(nameField.getText(), SurnameField.getText(), gradeField.getText());
                    currentStudent.setStartTime(new Date());
                    Stage stage = Main.startStage(loader, "Choose task", 574, 172, false);
                    TaskSwitchController controller = loader.getController();
                    controller.setCurrentTaskSet(selectedTaskSet, stage);
                    stage.show();
                }
            }
        }
    }

    @FXML
    void onTutorBtn(ActionEvent event) {
        Alert aboutAlert = makeAlert(Alert.AlertType.INFORMATION, "Tutorial", "How to use jTest",
                "In this program you can solve task sets of computer science.");
        aboutAlert.showAndWait();
        aboutAlert = makeAlert(Alert.AlertType.INFORMATION, "Tutorial", "How to use jTest",
                "In the main window of the program you have to enter your name, surname, class. If you have a task file (with .jt extension) that is password protected, you must enter it in the appropriate field.");
        aboutAlert.showAndWait();
        aboutAlert = makeAlert(Alert.AlertType.INFORMATION, "Tutorial", "How to use jTest",
                "When you have entered all the necessary data, you need to click on the 'Open Task File' button and select the desired file. After that you will see a window where you can choose a task.");
        aboutAlert.showAndWait();
        aboutAlert = makeAlert(Alert.AlertType.INFORMATION, "Tutorial", "How to use jTest",
                "After choosing a task, you will see a window to solve it. PLEASE BE AWARE that you can open the task only once! After that it will be blocked.");
        aboutAlert.showAndWait();
        aboutAlert = makeAlert(Alert.AlertType.INFORMATION, "Tutorial", "How to use jTest",
                "In order for the program to be successfully tested and work properly, some rules must be followed:\n\n" +
                        "1. You must read data from the input.txt file, and write output to the output.txt file\n" +
                        "2. Do not forget about using import and exception handling, otherwise the program may not start.\n"+
                        "3. Your solution must contain exactly one public class named Main.");
        aboutAlert.showAndWait();
        aboutAlert = makeAlert(Alert.AlertType.INFORMATION, "Tutorial", "How to use jTest",
                "When you have finished your test, press 'Submit solution' to save the result.");
        aboutAlert.showAndWait();
    }

    private void showError(String text)
    {
        Alert error = new Alert(Alert.AlertType.ERROR); //Создание окна ошибки
        error.setTitle("Error");
        error.setContentText(text);
        error.showAndWait();
    }

}
