import task.Epic;
import task.Subtask;
import task.Task;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Manager manager = new Manager();
        int numberMenu;
        Task task1 = new Task("Создать задачу", "Просто задача", "NEW");
        Task taskNew1 = new Task("Создать задачу", "Просто задача", "DONE");
        Epic epic1 = new Epic("Экзамен", "Нужно сдать экзамен");
        Subtask subtask11 = new Subtask("Расписать вопросы", "Подробное описание вопросов к экзамену",
                "DONE");
        Subtask subtask12 = new Subtask("Созвониться с одногруппниками",
                "Написать в группу и узнать удобное для всех время", "NEW");
        Epic epic2 = new Epic("Покупка компьютера", "Компьютер нужен для работы и игр");
        Subtask subtask21 = new Subtask("Выбрать компонентны",
                "Подобрать актуальные компонентны для 2023 года", "DONE");
        Subtask subtask22 = new Subtask("Мониторить цены",
                "Цены в разных магазинах отличаются друг от друга", "NEW");
        Subtask subtaskNew22 = new Subtask("Мониторить цены",
                "Цены в разных магазинах отличаются друг от друга", "DONE");

        while (true) { // Проверка на работоспособность
            System.out.println("Введите номер пункта");
            numberMenu = scanner.nextInt();
            if (numberMenu == 1) {
                manager.createEpic(epic1);   // id = 1
            } else if (numberMenu == 2) {
                manager.createSubtask(subtask11); // id = 2
                manager.createSubtask(subtask12); //id = 3
                manager.addSubtaskToEpic(epic1, subtask11); // Добавление подзадач в эпик
                manager.addSubtaskToEpic(epic1, subtask12);
                manager.assigningStatusToEpic(epic1); // Обновление статуса эпика
            } else if (numberMenu == 3) {
                manager.createEpic(epic2); // id = 4
            } else if (numberMenu == 4) {
                manager.createSubtask(subtask21); // id = 5
                manager.createSubtask(subtask22); // id = 6
                manager.addSubtaskToEpic(epic2, subtask21);
                manager.addSubtaskToEpic(epic2, subtask22);
                manager.assigningStatusToEpic(epic2);
            } else if (numberMenu == 5) {
                manager.createTask(task1); // id = 7
            } else if (numberMenu == 6) {
                manager.getAllListTask();  // Вывод списка всех задач
            } else if (numberMenu == 7) {
                manager.updateSubtask(subtaskNew22); // Обновление подзадачи
            } else if (numberMenu == 8) {
                manager.getByid(4); // Получение объекта по id
            } else if (numberMenu == 9) {
                manager.updateTask(taskNew1); // Обновление информации обычной задачи
            }
        }
    }
}