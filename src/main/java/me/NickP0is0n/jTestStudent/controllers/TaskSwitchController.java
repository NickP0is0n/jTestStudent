package me.NickP0is0n.jTestStudent.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import me.NickP0is0n.jTestStudent.models.TaskSet;
import me.NickP0is0n.jTestStudent.Main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

public class TaskSwitchController{

    private TaskSet currentTaskSet;
    public boolean[] isTaskFinished;
    private Stage currentStage;

    @FXML
    private ChoiceBox<String> taskSelector;

    @FXML
    private Button startTaskBtn;

    @FXML
    void endWork(ActionEvent event) {
        for (int i = 0; i < currentTaskSet.size(); i++)
        {
            if (!isTaskFinished[i]) Controller.currentStudent.addTask(i, 0);
        }
        Controller.currentStudent.setFinishTime(new Date());
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save the result file");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("jTest result files (.jres)", "*.jres")); //фильтр файлов
        File taskFile = chooser.showSaveDialog(new Stage());
        if (taskFile != null) {
            try {
                taskFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("An error occurred while creating a file!", Alert.AlertType.ERROR);
            }
        }
        if (taskFile != null) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(taskFile))) {
                oos.writeObject(Controller.currentStudent);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("An error occurred while saving a file!", Alert.AlertType.ERROR);
            }
            showAlert("Your work has been successfully completed and saved.", Alert.AlertType.INFORMATION);
            currentStage.close();
        }
    }

    @FXML
    void startTask(ActionEvent event) throws IOException {
        if (isTaskFinished[getSelectedItemIndex(taskSelector.getValue())]) showAlert("You have already tried to perform this task!", Alert.AlertType.ERROR);
        else
        {
            FXMLLoader loader = Main.makeLoader("taskSolving.fxml");
            Stage stage = Main.startStage(loader, "Task solving", 600, 634, false);
            TaskSolvingController controller = loader.getController();
            controller.setCurrentTask(currentTaskSet.get(getSelectedItemIndex(taskSelector.getValue())), getSelectedItemIndex(taskSelector.getValue()), stage);
            stage.show();
            isTaskFinished[getSelectedItemIndex(taskSelector.getValue())] = true;
        }
    }

    void updateSelector(int selectedItem)
    {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = 0; i < currentTaskSet.size(); i++) items.add((i + 1) + ". " + currentTaskSet.get(i).getTaskName());
        taskSelector.setItems(items);
        taskSelector.setValue((selectedItem + 1) + ". " + currentTaskSet.get(selectedItem).getTaskName());
    }

    void setCurrentTaskSet(TaskSet currentTaskSet, Stage stage) {
        this.currentTaskSet = currentTaskSet;
        isTaskFinished = new boolean[currentTaskSet.size()];
        for (boolean task:
                isTaskFinished) {
            task = false;
        }
        updateSelector(0);
        this.currentStage = stage;
    }

    int getSelectedItemIndex(String item)
    {
        for (int i = 0; i < taskSelector.getItems().size(); i++)
        {
            if (taskSelector.getItems().get(i).equals(item)) return i;
        }
        return -1; //У випадку помилки
    }

    void showAlert(String text, Alert.AlertType type)
    {
        Alert error = new Alert(type); //Создание окна ошибки
        error.setTitle("Information");
        error.setContentText(text);
        error.showAndWait();
    }
}
