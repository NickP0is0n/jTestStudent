package me.NickP0is0n;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TaskSolvingController {

    private Task currentTask;
    private int taskNumber;
    private Stage currentScene;

    @FXML
    private TextArea taskDescription;

    @FXML
    private TextArea inputData;

    @FXML
    private TextArea outputData;

    @FXML
    private TextArea codeEdit;

    private boolean emptyFieldAttention = false;

    private final int CODE_CHECK_TIMEOUT = 1000;
    private final int MAX_TEST_COUNT = 5;

    @FXML
    void sendCode(ActionEvent event) throws InterruptedException, IOException {
        String codeText = codeEdit.getText();
        File codeFile = new File("Main.java");
        codeFile.createNewFile();
        PrintWriter fileWrite = null;
        fileWrite = new PrintWriter(codeFile);
        fileWrite.print(codeText);
        fileWrite.close();
        File output = new File("output.txt");
        output.createNewFile();
        Process p;
        p = Runtime.getRuntime().exec("javac Main.java");
        Thread.sleep(3000);
        int good = 0;
        for (int i = 0; i < MAX_TEST_COUNT; i++) {
            File inputSim = new File("input.txt");
            inputSim.createNewFile();
            PrintWriter inWriter = null;
            inWriter = new PrintWriter(inputSim);
            inWriter.print(currentTask.getTaskIn()[i]);
            inWriter.close();

            p = Runtime.getRuntime().exec("java Main");
            Thread.sleep(CODE_CHECK_TIMEOUT);

            String fileCode = null;
            fileCode = readFileToString("output.txt");
            if (fileCode.equals(currentTask.getTaskOut()[i])) good++;
            inputSim.delete();

        }
        Controller.currentStudent.addTask(taskNumber, good);
        try {
            Files.delete(Paths.get("output.txt"));
            Files.delete(Paths.get("Main.java"));
            Files.delete(Paths.get("Main.class"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        showAlert(new StringBuilder()
                .append("The solution has been successfully verified. Passed ")
                .append(good)
                .append(" out of 5 tests.").toString(), Alert.AlertType.INFORMATION);
        currentScene.close();
    }

    @FXML
    void importFile(ActionEvent event) throws IOException {
        FileChooser chooser = new FileChooser(); //диалог открытия
        chooser.setTitle("Choose a file with code");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java source files (.java)", "*.java")); //фильтр файлов
        File taskFile = chooser.showOpenDialog(new Stage()); //показ диалога на отдельной сцене
        if(taskFile != null) codeEdit.setText(readFileToString(taskFile.getPath()));
    }

    private String readFileToString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    void setCurrentTask (Task task, int taskNumber, Stage stage)
    {
        this.currentScene = stage;
        this.taskNumber = taskNumber;
        this.currentTask = task;
        this.taskDescription.setText(task.getTaskDescription());
        this.inputData.setText(task.getTaskIn()[0]);
        this.outputData.setText(task.getTaskOut()[0]);
    }

    void showAlert(String text, Alert.AlertType type)
    {
        Alert error = new Alert(type); //Создание окна ошибки
        error.setTitle("Information");
        error.setContentText(text);
        error.showAndWait();
    }

}
