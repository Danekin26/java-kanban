import manager.*;
import manager.server.HttpTaskServer;
import manager.server.KVServer;
import task.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        int numberMenu;
        Epic epic12;
        Task taskNew1;
        Subtask subtaskNew22;
        Managers managers = new Managers();
        Task task1 = new Task("Создать задачу", "Просто задача", TasksStatus.IN_PROGRESS);
        task1.setType(TaskType.TASK);
        Task task2 = new Task("Создать вторую задачу", "Просто задача", TasksStatus.DONE);
        task2.setType(TaskType.TASK);

        Epic epic1 = new Epic("Экзамен", "Нужно сдать экзамен");
        Subtask subtask11 = new Subtask("Расписать вопросы", "Подробное описание вопросов к экзамену",
                TasksStatus.DONE);
        epic1.setType(TaskType.EPIC);
        subtask11.setType(TaskType.SUBTASK);


        Subtask subtask12 = new Subtask("Созвониться с одногруппниками",
                "Написать в группу и узнать удобное для всех время", TasksStatus.NEW);
        subtask12.setType(TaskType.SUBTASK);

        Epic epic2 = new Epic("Покупка компьютера", "Компьютер нужен для работы и игр");
        Subtask subtask21 = new Subtask("Выбрать компонентны",
                "Подобрать актуальные компонентны для 2023 года", TasksStatus.DONE);
        epic2.setType(TaskType.EPIC);

        Subtask subtask22 = new Subtask("Мониторить цены",
                "Цены в разных магазинах отличаются друг от друга", TasksStatus.NEW);
        subtask22.setType(TaskType.SUBTASK);


        while (true) { // Проверка на работоспособность
            System.out.println("Введите номер пункта");
            numberMenu = scanner.nextInt();
            if (numberMenu == 1) {
                inMemoryTaskManager.createEpic(epic1);   // id = 1
                inMemoryTaskManager.createSubtask(subtask11); // id = 2
                epic1.setIdToSubtask(subtask11.getId());
                subtask11.setIdToEpic(epic1.getId());
                inMemoryTaskManager.createSubtask(subtask12); //id = 3
                epic1.setIdToSubtask(subtask12.getId());
                subtask12.setIdToEpic(epic1.getId());
                inMemoryTaskManager.updateEpic(epic1);
                inMemoryTaskManager.createEpic(epic2); // id = 4
                inMemoryTaskManager.createSubtask(subtask21); // id = 5
                epic2.setIdToSubtask(subtask21.getId());
                subtask21.setIdToEpic(epic2.getId());
                subtask21.setType(TaskType.SUBTASK);
                inMemoryTaskManager.createSubtask(subtask22); // id = 6
                epic2.setIdToSubtask(subtask22.getId());
                subtask22.setIdToEpic(epic2.getId());
                inMemoryTaskManager.updateEpic(epic2);
                inMemoryTaskManager.createTask(task1); // id = 7
                inMemoryTaskManager.createTask(task2); // id = 8
                inMemoryTaskManager.updateTask(task1);
                inMemoryTaskManager.updateEpic(epic1);
                inMemoryTaskManager.updateSubtask(subtask11);
                inMemoryTaskManager.deleteEpic();
            } else if (numberMenu == 2) {
                InMemoryTaskManager inMemoryTaskManager12 = new InMemoryTaskManager();
                System.out.println(inMemoryTaskManager12.getListHistory());
            } else if (numberMenu == 3) {

            } else if (numberMenu == 4) {
                inMemoryTaskManager.getEpic(4); // Получение объекта по id
            } else if (numberMenu == 5) {
                taskNew1 = new Task("Создать задачу", "Просто задача", TasksStatus.DONE, task1.getId());
                inMemoryTaskManager.updateTask(taskNew1); // Обновление информации обычной задачи
            } else if (numberMenu == 6) {
                inMemoryTaskManager.getListTask(); // Получение всех задач
                inMemoryTaskManager.getListEpic();
                inMemoryTaskManager.getListSubtask();
            } else if (numberMenu == 7) {
                inMemoryTaskManager.deleteEpic();
                inMemoryTaskManager.deleteTask();
                inMemoryTaskManager.deleteSubtask();
            } else if (numberMenu == 8) {
                inMemoryTaskManager.deleteById(6); // Удаление по id
            } else if (numberMenu == 9) {
                inMemoryTaskManager.getListOfTaskInEpic(epic2); // Получить список эпика
            } else if (numberMenu == 10) {
                inMemoryTaskManager.getListHistory();
            } else if (numberMenu == 11) {
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
            } else if (numberMenu == 12) {
                managers.getDefault().createTask(task1);
            } else if (numberMenu == 13) {
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
            } else if (numberMenu == 14) {
                Path path = Paths.get("C:\\Users\\Данисимо\\dev\\java-kanban\\src\\history.csv");
                FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(path);
                // fileBackedTasksManager.createTask(task1);
                //fileBackedTasksManager.createEpic(epic1);
                fileBackedTasksManager.createEpic(epic1);   // id = 1
                epic1.setType(TaskType.EPIC);
                fileBackedTasksManager.createSubtask(subtask11); // id = 2
                subtask11.setType(TaskType.SUBTASK);
                epic1.setIdToSubtask(subtask11.getId());
                subtask11.setIdToEpic(epic1.getId());
                fileBackedTasksManager.createSubtask(subtask12); //id = 3
                subtask12.setType(TaskType.SUBTASK);
                epic1.setIdToSubtask(subtask12.getId());
                subtask12.setIdToEpic(epic1.getId());
                fileBackedTasksManager.updateEpic(epic1);
                fileBackedTasksManager.createEpic(epic2); // id = 4
                epic2.setType(TaskType.EPIC);
                fileBackedTasksManager.createSubtask(subtask21); // id = 5
                subtask21.setType(TaskType.SUBTASK);

                epic2.setIdToSubtask(subtask21.getId());
                subtask21.setIdToEpic(epic2.getId());
                fileBackedTasksManager.createSubtask(subtask22); // id = 6
                subtask22.setType(TaskType.SUBTASK);

                epic2.setIdToSubtask(subtask22.getId());
                subtask22.setIdToEpic(epic2.getId());
                fileBackedTasksManager.updateEpic(epic2);
                fileBackedTasksManager.createTask(task1); // id = 7
                task1.setType(TaskType.TASK);

                fileBackedTasksManager.createTask(task2); // id = 8
                task2.setType(TaskType.TASK);

                fileBackedTasksManager.getTask(7);
                fileBackedTasksManager.getTask(8);
                fileBackedTasksManager.getEpic(1);
                fileBackedTasksManager.getSubtask(5);
                fileBackedTasksManager.deleteById(7);

            } else if (numberMenu == 15) {
                Epic epic = new Epic("Epic", "Description");
                Subtask subtask = new Subtask("Subtask", "Description", TasksStatus.NEW);

                inMemoryTaskManager.createEpic(epic);
                inMemoryTaskManager.createSubtask(subtask);
                epic.setIdToSubtask(subtask.getId());
                subtask.setIdToEpic(epic.getId());
                Subtask subtaskNew = new Subtask("Subtask Update", "Description Update", TasksStatus.NEW, 2);

                inMemoryTaskManager.updateSubtask(subtaskNew);
            } else if (numberMenu == 16) {
                LocalDateTime start = LocalDateTime.of(2023, 6, 26, 12, 0);

                Task task12 = new Task("Task12", "Desc", TasksStatus.NEW, start, 600);
                inMemoryTaskManager.createTask(task12);
                LocalDateTime start2 = LocalDateTime.of(2023, 7, 26, 12, 0);
                Task task2112 = new Task("Task11212", "Desc", TasksStatus.NEW, start2, 700);
                inMemoryTaskManager.createTask(task2112);
                LocalDateTime start3 = LocalDateTime.of(2023, 1, 26, 12, 0);
                Task task11 = new Task("Task11", "Desc", TasksStatus.NEW, start3, 700);
                inMemoryTaskManager.createTask(task11);
                Task task11ads = new Task("Task11", "Desc", TasksStatus.NEW);
                inMemoryTaskManager.createTask(task11ads);

                Task task11adsads = new Task("Task1ads1", "Desc", TasksStatus.NEW);
                inMemoryTaskManager.createTask(task11adsads);


                List<Task> asd = inMemoryTaskManager.getPrioritizedTasks();
                System.out.println(asd);

            } else if (numberMenu == 17) {
                KVServer kvServer = new KVServer();
                kvServer.start();
                HttpTaskServer httpTaskServer = new HttpTaskServer(new FileBackedTasksManager());
                httpTaskServer.start();
            }
        }
    }
}