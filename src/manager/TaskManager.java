package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    int createTask(Task task) throws IOException; // Создание задач

    int createSubtask(Subtask subtask) throws IOException; // Создание подзадачи

    int createEpic(Epic epic) throws IOException; // Создание эпика

    ArrayList<Task> getListTask(); // Получить список задач

    ArrayList<Epic> getListEpic(); // Получить список эпиков

    ArrayList<Subtask> getListSubtask(); // Получить список подзадач

    void deleteTask() throws IOException; // Удаление списка задач

    void deleteSubtask() throws IOException; // Удаление списка подзадач

    void deleteEpic() throws IOException; // Удаление эпиков и их подзадач

    Task getTask(int id);  // Получить задачу по id

    Epic getEpic(int id);  // Получить эпик по id

    Subtask getSubtask(int id);  // Получить подзадачу по id

    void updateTask(Task task) throws IOException; // Обновить таск

    void updateEpic(Epic epic) throws IOException; // обновить эпик

    void updateSubtask(Subtask subtask) throws IOException; // обновить сабтаск

    void deleteById(int id) throws IOException; // удаление по id

    ArrayList getListOfTaskInEpic(Epic epic); // Получить список подзадач эпика

    List<Task> getListHistory(); // Получить список истории

    Task getAnyTask(Integer id); // Получить любую задачу (добавил для того, что бы не засорять историю при вызове методов получения задач по id)
}