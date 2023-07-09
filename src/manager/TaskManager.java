package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    int createTask(Task task) throws IOException; // Создание задач     POST   /tasks/task/      *Body

    int createSubtask(Subtask subtask) throws IOException; // Создание подзадачи    POST   /tasks/subtask/      *Body

    int createEpic(Epic epic) throws IOException; // Создание эпика     POST    /tasks/epic/      *Body

    ArrayList<Task> getListTask(); // Получить список задач   GET /tasks/task

    ArrayList<Epic> getListEpic(); // Получить список эпиков    GET /tasks/epic

    ArrayList<Subtask> getListSubtask(); // Получить список подзадач    GET /tasks/subtask

    void deleteTask() throws IOException; // Удаление списка задач    DELETE   /tasks/task/

    void deleteSubtask() throws IOException; // Удаление списка подзадач      DELETE   /tasks/subtask/

    void deleteEpic() throws IOException; // Удаление эпиков и их подзадач      DELETE /tasks/epic/

    Task getTask(int id) throws IOException;  // Получить задачу по id   GET /tasks/task/?id=...

    Epic getEpic(int id) throws IOException;  // Получить эпик по id   GET /tasks/epic/?id=...

    Subtask getSubtask(int id) throws IOException;  // Получить подзадачу по id    GET /tasks/subtask/?id=...

    void updateTask(Task task) throws IOException; // Обновить таск     POST  /tasks/task/?id=...      *Body

    void updateEpic(Epic epic) throws IOException; // обновить эпик     POST    /tasks/epic/?id=...      *Body

    void updateSubtask(Subtask subtask) throws IOException; // обновить сабтаск     POST  /tasks/subtask/?id=...      *Body

    void deleteById(int id) throws IOException; // удаление по id      DELETE     /tasks/tasks/?id=...

    ArrayList getListOfTaskInEpic(Epic epic); // Получить список подзадач эпика   GET /tasks/epic/subtask/?id=...

    List<Task> getListHistory(); // Получить список истории   GET /tasks/history/

    Task getAnyTask(Integer id); // Получить любую задачу    GET /tasks/alltasks/?id=...

    List<Task> getPrioritizedTasks(); // Получить список отсортированный по приоритету    GET /tasks/
}