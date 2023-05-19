import manager.InMemoryTaskManager;
import manager.Managers;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TasksStatus;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        int numberMenu;
        Epic epic12;
        Task taskNew1;
        Subtask subtaskNew22;
        Managers managers = new Managers();
        Task task1 = new Task("Создать задачу", "Просто задача", TasksStatus.IN_PROGRESS);
        Task task2 = new Task("Создать вторую задачу", "Просто задача", TasksStatus.DONE);

        Epic epic1 = new Epic("Экзамен", "Нужно сдать экзамен");
        Subtask subtask11 = new Subtask("Расписать вопросы", "Подробное описание вопросов к экзамену",
                TasksStatus.DONE);

        Subtask subtask12 = new Subtask("Созвониться с одногруппниками",
                "Написать в группу и узнать удобное для всех время", TasksStatus.NEW);


        Epic epic2 = new Epic("Покупка компьютера", "Компьютер нужен для работы и игр");
        Subtask subtask21 = new Subtask("Выбрать компонентны",
                "Подобрать актуальные компонентны для 2023 года", TasksStatus.DONE);

        Subtask subtask22 = new Subtask("Мониторить цены",
                "Цены в разных магазинах отличаются друг от друга", TasksStatus.NEW);


        while (true) { // Проверка на работоспособность
            System.out.println("Введите номер пункта");
            numberMenu = scanner.nextInt();
            if (numberMenu == 1) {
                inMemoryTaskManager.createEpic(epic1);   // id = 1
            } else if (numberMenu == 2) {
                inMemoryTaskManager.createSubtask(subtask11); // id = 2
                epic1.setIdToSubtask(subtask11.getId());
                subtask11.setIdToEpic(epic1.getId());
                inMemoryTaskManager.createSubtask(subtask12); //id = 3
                epic1.setIdToSubtask(subtask12.getId());
                subtask12.setIdToEpic(epic1.getId());
                inMemoryTaskManager.updateEpic(epic1);
            } else if (numberMenu == 3) {
                inMemoryTaskManager.createEpic(epic2); // id = 4
            } else if (numberMenu == 4) {
                inMemoryTaskManager.createSubtask(subtask21); // id = 5
                epic2.setIdToSubtask(subtask21.getId());
                subtask21.setIdToEpic(epic2.getId());
                inMemoryTaskManager.createSubtask(subtask22); // id = 6
                epic2.setIdToSubtask(subtask22.getId());
                subtask22.setIdToEpic(epic2.getId());
                inMemoryTaskManager.updateEpic(epic2);
            } else if (numberMenu == 5) {
                inMemoryTaskManager.createTask(task1); // id = 7
                inMemoryTaskManager.createTask(task2); // id = 8
            } else if (numberMenu == 6) {
                epic12 = new Epic("Экзамен", "сдать экзамен", epic1.getStatus(), epic1.getId(),
                        epic1.getIdToSubtask());
                inMemoryTaskManager.updateEpic(epic12);
            } else if (numberMenu == 7) {
                subtaskNew22 = new Subtask("Мониторить цены",
                        "Цены в разных магазинах отличаются друг от друга", TasksStatus.DONE, subtask22.getId(),
                        subtask22.getIdToEpic());
                inMemoryTaskManager.updateSubtask(subtaskNew22); // Обновление подзадачи
            } else if (numberMenu == 8) {
                inMemoryTaskManager.getEpic(4); // Получение объекта по id
            } else if (numberMenu == 9) {
                taskNew1 = new Task("Создать задачу", "Просто задача", TasksStatus.DONE, task1.getId());
                inMemoryTaskManager.updateTask(taskNew1); // Обновление информации обычной задачи
            } else if (numberMenu == 10) { // Получение всех задач
                inMemoryTaskManager.getListTask();
                inMemoryTaskManager.getListEpic();
                inMemoryTaskManager.getListSubtask();
            } else if (numberMenu == 11) {
                inMemoryTaskManager.deleteEpic();
                inMemoryTaskManager.deleteTask();
                inMemoryTaskManager.deleteSubtask();
            } else if (numberMenu == 12) {
                inMemoryTaskManager.deleteById(6); // Удаление по id
            } else if (numberMenu == 13) {
                inMemoryTaskManager.getListOfTaskInEpic(epic2); // Получить список эпика
            } else if (numberMenu == 14) {
                inMemoryTaskManager.getListHistory();
            } else if (numberMenu == 15) {
                inMemoryTaskManager.getTask(7);
                inMemoryTaskManager.getTask(8);
                inMemoryTaskManager.getEpic(1);
                inMemoryTaskManager.getEpic(4);
                inMemoryTaskManager.getSubtask(2);
                inMemoryTaskManager.getSubtask(3);
                inMemoryTaskManager.getSubtask(5);
                inMemoryTaskManager.getSubtask(6);
                inMemoryTaskManager.getTask(7);
                inMemoryTaskManager.getTask(8);
                inMemoryTaskManager.getEpic(1);
                inMemoryTaskManager.getEpic(4);
            } else if (numberMenu == 16) {
                managers.getDefault().createTask(task1);
            } else if (numberMenu == 17) {
                Epic epic1New = new Epic("Экзамен", "Нужно сдать экзамен");
                Subtask subtask1 = new Subtask("Расписать вопросы", "Подробное описание вопросов к экзамену",
                        TasksStatus.DONE);
                inMemoryTaskManager.createEpic(epic1New); // id 1
                inMemoryTaskManager.createSubtask(subtask1); // id 2
                epic1New.setIdToSubtask(subtask1.getId());
                subtask1.setIdToEpic(epic1New.getId());
                Subtask subtask2 = new Subtask("Выучить вопросы", "Выучить вопросы",
                        TasksStatus.NEW);
                inMemoryTaskManager.createSubtask(subtask2); // id 3
                epic1New.setIdToSubtask(subtask2.getId());
                subtask2.setIdToEpic(epic1New.getId());
                Subtask subtask3 = new Subtask("Сдать экзамен", "Сдать экзамен",
                        TasksStatus.NEW);
                inMemoryTaskManager.createSubtask(subtask3); // id 4
                epic1New.setIdToSubtask(subtask3.getId());
                subtask3.setIdToEpic(epic1New.getId());

                Epic epic2New = new Epic("Получить диплом", "Нужно получить диплом");
                inMemoryTaskManager.createEpic(epic2New); // id 5

                inMemoryTaskManager.getEpic(1);
                inMemoryTaskManager.getSubtask(2);
                inMemoryTaskManager.getSubtask(3);
                inMemoryTaskManager.getSubtask(4);
                inMemoryTaskManager.getEpic(5);
                inMemoryTaskManager.getSubtask(2);
                inMemoryTaskManager.getSubtask(4);

                inMemoryTaskManager.getListHistory();

                inMemoryTaskManager.deleteById(1);// удаление эпика с подзадачами

                inMemoryTaskManager.getListHistory();

                inMemoryTaskManager.deleteSubtask();
                inMemoryTaskManager.getListHistory();
            }
        }
    }
}