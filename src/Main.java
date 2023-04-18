import manager.Manager;
import task.Epic;
import task.Subtask;
import task.Task;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Manager manager = new Manager();
        int numberMenu;
        Epic epic12;
        Task taskNew1;
        Subtask subtaskNew22;
        Task task1 = new Task("Создать задачу", "Просто задача", "IN_PROGRESS");
        Task task2 = new Task("Создать вторую задачу", "Просто задача", "DONE");

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


        while (true) { // Проверка на работоспособность
            System.out.println("Введите номер пункта");
            numberMenu = scanner.nextInt();
            if (numberMenu == 1) {
                manager.createEpic(epic1);   // id = 1
            } else if (numberMenu == 2) {
                manager.createSubtask(subtask11); // id = 2
                epic1.setIdToSubtask(subtask11.getId());
                subtask11.setIdToEpic(epic1.getId());
                manager.createSubtask(subtask12); //id = 3
                epic1.setIdToSubtask(subtask12.getId());
                subtask12.setIdToEpic(epic1.getId());
                manager.updateEpic(epic1);
            } else if (numberMenu == 3) {
                manager.createEpic(epic2); // id = 4
            } else if (numberMenu == 4) {
                manager.createSubtask(subtask21); // id = 5
                epic2.setIdToSubtask(subtask21.getId());
                subtask21.setIdToEpic(epic2.getId());
                manager.createSubtask(subtask22); // id = 6
                epic2.setIdToSubtask(subtask22.getId());
                subtask22.setIdToEpic(epic2.getId());
                manager.updateEpic(epic2);
            } else if (numberMenu == 5) {
                manager.createTask(task1); // id = 7
                manager.createTask(task2); // id = 8
            } else if (numberMenu == 6) {
                epic12 = new Epic("Экзамен", "сдать экзамен", epic1.getStatus(), epic1.getId(),
                        epic1.getIdToSubtask());
                manager.updateEpic(epic12);
            } else if (numberMenu == 7) {
                subtaskNew22 = new Subtask("Мониторить цены",
                        "Цены в разных магазинах отличаются друг от друга", "DONE", subtask22.getId(),
                        subtask22.getIdToEpic());
                manager.updateSubtask(subtaskNew22); // Обновление подзадачи
            } else if (numberMenu == 8) {
                manager.getByid(4); // Получение объекта по id
            } else if (numberMenu == 9) {
                taskNew1 = new Task("Создать задачу", "Просто задача", "DONE", task1.getId());
                manager.updateTask(taskNew1); // Обновление информации обычной задачи
            } else if (numberMenu == 10) {
                manager.getListTask();
                manager.getListEpic();
                manager.getListSubtask();
            } else if (numberMenu == 11){
                manager.deleteEpic();
                manager.deleteTask();
                manager.deleteSubtask();
            } else if (numberMenu == 12) {
                manager.deleteById(1);
            } else if (numberMenu == 13) {
                manager.getListOfTaskInEpic(epic2);
            } else if (numberMenu == 14) {
                manager.deleteSubtaskFromEpic(5);
            }
        }
    }
}