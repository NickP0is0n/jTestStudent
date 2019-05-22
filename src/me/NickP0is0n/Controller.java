package me.NickP0is0n;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    void onAboutBtn(ActionEvent event) {
        Alert aboutAlert = makeAlert(Alert.AlertType.INFORMATION, "Про програму", "jTest Student", "Версія 1.0.0\n\n" +
                "jTest Student є кліентською частиною програмного комплексу " +
                "jTest для тестування учнів з інформатики.\n" +
                "Початковий код захищено 3-пунктовою ліцензією BSD.");//Создание окна
        aboutAlert.setGraphic(new ImageView(new File("resources/logo.png").toURI().toString()));
        aboutAlert.showAndWait();
    }

    @FXML
    void onExitBtn(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void onOpenBtn(ActionEvent event) throws IOException {
        if (nameField.getText().isEmpty() || SurnameField.getText().isEmpty() || gradeField.getText().isEmpty()) showError("Ви ввели не всі необхідні дані. Будь-ласка, заповніть всі поля і спробуйте ще раз!"); //защита от дурака
        else {
            FileChooser chooser = new FileChooser(); //диалог открытия
            chooser.setTitle("Оберіть файл із завданнями");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файли завдань (.jt)", "*.jt")); //фильтр файлов
            File taskFile = chooser.showOpenDialog(new Stage()); //показ диалога на отдельной сцене
            if(taskFile != null)
            {
                TaskSet selectedTaskSet = null;
                try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(taskFile)))
                {
                    selectedTaskSet = (TaskSet) ois.readObject();
                }
                catch(Exception ex){
                    showError("Виникла помилка при читанні файлу!");
                }
                if(selectedTaskSet.isPasswordProtected() && !selectedTaskSet.getPassword().equals(passField.getText()))
                {
                    showError("Цей файл завдань зашифровано паролем. Введений пароль невірний.");
                }
                else
                {
                    FXMLLoader loader = Main.makeLoader("taskSelection.fxml");
                    currentStudent = new Student(nameField.getText(), SurnameField.getText(), gradeField.getText());
                    currentStudent.setStartTime(new Date());
                    Stage stage = Main.startStage(loader, "Вибір завдання", 574, 172, false);
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
                "У цій програмі ви маєте можливість пройти тестування з інформатики, яке було підготовано вашим вчителем.");
        aboutAlert.showAndWait();
        aboutAlert = makeAlert(Alert.AlertType.INFORMATION, "Tutorial", "How to use jTest",
                "У головному вікні програми ви маєте ввести своє ім'я, прізвище, клас. Якщо ваш вчитель надав вам файл завдань (з розширенням .jt), захищений паролем, то ви маєте його ввести у поле.");
        aboutAlert.showAndWait();
        aboutAlert = makeAlert(Alert.AlertType.INFORMATION, "Tutorial", "How to use jTest",
                "Коли ви ввели всі потрібні дані, потрібно натиснути на кнопку 'Відкрити файл завдань' і обрати файл, який підготував ваш вчитель. Після чого вам відкриється вікно вибору завдань.");
        aboutAlert.showAndWait();
        aboutAlert = makeAlert(Alert.AlertType.INFORMATION, "Tutorial", "How to use jTest",
                "Після вибору завдання вам відкриється вікно його розв'язання. ЗВЕРНІТЬ УВАГУ, що відкрити завдання ви можете лише 1 раз! Після цього воно буде заблоковане.");
        aboutAlert.showAndWait();
        aboutAlert = makeAlert(Alert.AlertType.INFORMATION, "Tutorial", "How to use jTest",
                "Щоб програма успішно перевірялась і правильно працювала, потрібно дотримуватись деяких правил:\n\n" +
                        "1. Ви маєте читати дані з файлу input.txt, а виводити в файл output.txt\n" +
                        "2. Не забувати про використання import та обробку виключень, інакше програма може не запуститися.");
        aboutAlert.showAndWait();
        aboutAlert = makeAlert(Alert.AlertType.INFORMATION, "Tutorial", "How to use jTest",
                "Коли ви закінчили виконання роботи, натисніть 'Здати роботу', щоб зберегти результат.");
        aboutAlert.showAndWait();
    }

    void showError(String text)
    {
        Alert error = new Alert(Alert.AlertType.ERROR); //Создание окна ошибки
        error.setTitle("Помилка");
        error.setContentText(text);
        error.showAndWait();
    }

}
