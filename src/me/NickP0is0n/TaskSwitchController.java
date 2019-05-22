package me.NickP0is0n;

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

    void setCurrentTaskSet(TaskSet currentTaskSet, Stage stage) {
        this.currentTaskSet = currentTaskSet;
        isTaskFinished = new boolean[currentTaskSet.size()];
        for (boolean i:
             isTaskFinished) {
                i = false;
        }
        updateSelector(0);
        this.currentStage = stage;
    }

    @FXML
    void endWork(ActionEvent event) {
        for (int i = 0; i < currentTaskSet.size(); i++)
        {
            if (!isTaskFinished[i]) Controller.currentStudent.addTask(i, 0);
        }
        Controller.currentStudent.setFinishTime(new Date());
        FileChooser chooser = new FileChooser(); //діалог збереження
        chooser.setTitle("Збережіть файл з результатами");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файли результатів (.jres)", "*.jres")); //фильтр файлов
        File taskFile = chooser.showSaveDialog(new Stage()); //показ диалога на отдельной сцене
        if (taskFile != null) {
            try {
                taskFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Виникла помилка при створенні файлу!", Alert.AlertType.ERROR);
            }
        }
        if (taskFile != null) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(taskFile))) {
                oos.writeObject(Controller.currentStudent);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Виникла помилка при збереженні файлу!", Alert.AlertType.ERROR);
            }
            showAlert("Ваша робота успішно завершена і збережена.", Alert.AlertType.INFORMATION);
            currentStage.close();
        }
    }

    @FXML
    void startTask(ActionEvent event) throws IOException {
        if (isTaskFinished[getSelectedItemIndex(taskSelector.getValue())]) showAlert("Ви вже намагалися виконати це завдання!", Alert.AlertType.ERROR);
        else
        {
            FXMLLoader loader = Main.makeLoader("taskSolving.fxml");
            Stage stage = Main.startStage(loader, "Виконання завдання", 600, 634, false);
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
        error.setTitle("Повідомлення");
        error.setContentText(text);
        error.showAndWait();
    }
}
