package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    int createTask(Task task); // Создание задач

    int createSubtask(Subtask subtask); // Создание подзадачи

    int createEpic(Epic epic); // Создание эпика

    ArrayList<Task> getListTask(); // Получить список задач

    ArrayList<Epic> getListEpic(); // Получить список эпиков

    ArrayList<Subtask> getListSubtask(); // Получить список подзадач

    void deleteTask(); // Удаление списка задач

    void deleteSubtask(); // Удаление списка подзадач

    void deleteEpic(); // Удаление эпиков и их подзадач

    Task getTask(int id);  // Получить задачу по id

    Epic getEpic(int id);  // Получить эпик по id

    Subtask getSubtask(int id);  // Получить подзадачу по id

    void updateTask(Task task); // Обновить таск

    void updateEpic(Epic epic); // обновить эпик

    void updateSubtask(Subtask subtask); // обновить сабтаск

    void deleteById(int id); // удаление по id

    ArrayList getListOfTaskInEpic(Epic epic); // Получить список подзадач эпика

    List<Task> getListHistory(); // Получить список истории
}