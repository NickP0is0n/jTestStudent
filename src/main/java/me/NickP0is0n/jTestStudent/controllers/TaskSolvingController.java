package me.NickP0is0n.jTestStudent.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import me.NickP0is0n.jTestStudent.models.Task;
import me.NickP0is0n.jTestStudent.service.FileUtils;
import me.NickP0is0n.jTestStudent.service.InternalApplication;
import me.NickP0is0n.jTestStudent.service.TestingEnvironment;

import java.io.File;
import java.io.IOException;

public class TaskSolvingController {

    private static final int CODE_CHECK_TIMEOUT = 1000;
    private static final int MAX_TEST_COUNT = 5;

    private Task currentTask;
    private int taskNumber;
    private Stage currentScene;

    private boolean emptyFieldAttention = false;

    @FXML
    private TextArea taskDescription;

    @FXML
    private TextArea inputData;

    @FXML
    private TextArea outputData;

    @FXML
    private TextArea codeEdit;

    @FXML
    void sendCode(ActionEvent event) throws InterruptedException, IOException {
        String codeText = codeEdit.getText();
        if (codeText.isEmpty() && !emptyFieldAttention) {
            showAttention();
            return;
        }

        int good = testStudentApp(codeText);

        new Alert(Alert.AlertType.INFORMATION, "The solution has been successfully verified. Passed " + good + " out of 5 tests.").showAndWait();
        currentScene.close();
    }

    @FXML
    void importFile(ActionEvent event) throws IOException {
        FileChooser chooser = new FileChooser(); //диалог открытия
        chooser.setTitle("Choose a file with code");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java source files (.java)", "*.java")); //фильтр файлов
        File taskFile = chooser.showOpenDialog(new Stage()); //показ диалога на отдельной сцене
        if(taskFile != null) codeEdit.setText(FileUtils.readFileToString(taskFile.getPath()));
    }

    void setCurrentTask (Task task, int taskNumber, Stage stage)
    {
        this.currentScene = stage;
        this.currentScene.setTitle("Task solving: " + task.getTaskName());
        this.taskNumber = taskNumber;
        this.currentTask = task;
        this.taskDescription.setText(task.getTaskDescription());
        this.inputData.setText(task.getTaskIn()[0]);
        this.outputData.setText(task.getTaskOut()[0]);
    }

    private void showAttention() {
        new Alert(Alert.AlertType.WARNING,"Are you sure that you want to send empty code? If yes, click on \"Send solution\" again.").showAndWait();
        emptyFieldAttention = true;
    }

    private int testStudentApp(String codeText) throws IOException, InterruptedException {
        InternalApplication studentApp = new InternalApplication(codeText, "Main");
        studentApp.compile();
        int good = 0;
        TestingEnvironment testEnv = new TestingEnvironment(new File("input.txt"), new File("output.txt"), "java Main");
        for (int i = 0; i < TaskSolvingController.MAX_TEST_COUNT; i++) {
            good = testEnv.test(currentTask.getTaskIn()[i], currentTask.getTaskOut()[i]);
        }
        Controller.currentStudent.addTask(taskNumber, good);
        studentApp.removeFiles();
        return good;
    }
}
