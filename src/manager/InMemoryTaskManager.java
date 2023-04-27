package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TasksStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int nextId = 1;
    private HashMap<Integer, Task> idTask = new HashMap<>();
    private HashMap<Integer, Epic> idEpic = new HashMap<>();
    private HashMap<Integer, Subtask> idSubtask = new HashMap<>();
    private InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    @Override
    public int createTask(Task task) { // Создание задач
        task.setId(nextId);
        nextId++;
        idTask.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int createSubtask(Subtask subtask) { // Создание подзадачи
        subtask.setId(nextId);
        nextId++;
        idSubtask.put(subtask.getId(), subtask);
        return subtask.getId();
    }

    @Override
    public int createEpic(Epic epic) { // Создание эпика
        epic.setId(nextId);
        nextId++;
        idEpic.put(epic.getId(), epic);
        assigningStatusToEpic(epic);
        return epic.getId();
    }

    @Override
    public ArrayList<Task> getListTask() { // Получить список задач
        ArrayList<Task> tasks = new ArrayList<>(idTask.values());
        for (int i = 0; i < tasks.size(); i++) {
            inMemoryHistoryManager.add(tasks.get(i));
        }
        return tasks;
    }

    @Override
    public ArrayList<Epic> getListEpic() { // Получить список эпиков
        ArrayList<Epic> epics = new ArrayList<>(idEpic.values());
        for (int i = 0; i < epics.size(); i++) {
            inMemoryHistoryManager.add(epics.get(i));
        }
        return epics;
    }

    @Override
    public ArrayList<Subtask> getListSubtask() { // Получить список подзадач
        ArrayList<Subtask> subtasks = new ArrayList<>(idSubtask.values());
        for (int i = 0; i < subtasks.size(); i++) {
            inMemoryHistoryManager.add(subtasks.get(i));
        }
        return subtasks;
    }

    @Override
    public void deleteTask() { // Удаление списка задач
        idTask.clear();
    }

    @Override
    public void deleteSubtask() { // Удаление списка подзадач
        idSubtask.clear();
    }

    @Override
    public void deleteEpic() { // Удаление эпиков и их подзадач
        idEpic.clear();
        idSubtask.clear();
    }

    @Override
    public Task getTask(int id) {  // Получить задачу по id
        if (idTask.get(id) != null) {
            return idTask.get(id);
        } else {
            return null;
        }
    }

    @Override
    public Epic getEpic(int id) {  // Получить эпик по id
        if (idEpic.get(id) != null) {
            return idEpic.get(id);
        } else {
            return null;
        }
    }

    @Override
    public Subtask getSubtask(int id) {  // Получить подзадачу по id
        if (idSubtask.get(id) != null) {
            return idSubtask.get(id);
        } else {
            return null;
        }
    }

    @Override
    public void updateTask(Task task) { // Обновить таск
        if (idTask.get(task.getId()) != null && idTask.get(task.getId()).getId() == task.getId()) {
            idTask.put(task.getId(), task);
            task.setId(task.getId());
        }
    }

    @Override
    public void updateEpic(Epic epic) { // обновить эпик
        if (idEpic.get(epic.getId()) != null && idEpic.get(epic.getId()).getId() == epic.getId()) {
            epic.setId(epic.getId());
            assigningStatusToEpic(epic);
            idEpic.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) { // обновить сабтаск
        Epic epic;
        for (Subtask searchSubtask : idSubtask.values()) {
            if (searchSubtask.getId() == subtask.getId()) {
                subtask.setId(searchSubtask.getId());
                subtask.setIdToEpic(searchSubtask.getIdToEpic());
                idSubtask.put(searchSubtask.getId(), subtask);
                epic = idEpic.get(searchSubtask.getIdToEpic());
                assigningStatusToEpic(epic);
            }
        }
    }

    @Override
    public void deleteById(int id) {  // удаление по id
        if (idTask.containsKey(id)) {
            idTask.remove(id);
        }
        if (idEpic.containsKey(id)) {
            for (int i = 0; i < idEpic.get(id).getIdToSubtask().size(); i++) {
                idSubtask.remove(idEpic.get(id).getIdToSubtask().get(i));
            }
            idEpic.get(id).deleteAllIdToSubtask();
            idEpic.remove(id);
        }
        if (idSubtask.containsKey(id)) {
            int idToEpic = idSubtask.get(id).getIdToEpic();
            idEpic.get(idToEpic).deleteIdSubtask(id); // Тут я удаляю не весь список, а id определенной задачи
            deleteSubtaskFromEpic(id);
            Epic epic = idEpic.get(idToEpic);
            assigningStatusToEpic(epic);
        }
    }

    private void deleteSubtaskFromEpic(int id) { // Удаление подзадачи
        int idE = idSubtask.get(id).getIdToEpic();
        idEpic.get(idE).deleteIdSubtask(id);
        idSubtask.remove(id);
    }

    @Override
    public ArrayList getListOfTaskInEpic(Epic epic) { // Получить список подзадач эпика
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        Integer idEpic = epic.getId();
        for (Subtask sub : idSubtask.values()) {
            if (idEpic.equals(sub.getIdToEpic())) {
                subtaskList.add(sub);
            }
        }
        return subtaskList;
    }

    private TasksStatus assigningStatusToEpic(Epic epic) { // статус эпика
        int id;
        int countNew = 0;
        int countDone = 0;
        int countInProgress = 0;
        if (epic.getIdToSubtask().size() != 0) {
            for (int i = 0; i < epic.getIdToSubtask().size(); i++) {
                id = epic.getIdToSubtask().get(i);
                if (idSubtask.get(id).getStatus().equals("NEW")) countNew++;
                else if (idSubtask.get(id).getStatus().equals("DONE")) countDone++;
                else if (idSubtask.get(id).getStatus().equals("IN_PROGRESS")) countInProgress++;
            }
            if ((countNew == epic.getIdToSubtask().size()) && (countInProgress == 0)) epic.setStatus(TasksStatus.NEW);
            else if ((countDone == epic.getIdToSubtask().size()) && (countInProgress == 0))
                epic.setStatus(TasksStatus.DONE);
            else epic.setStatus(TasksStatus.IN_PROGRESS);
        } else {
            epic.setStatus(TasksStatus.NEW);
        }
        return epic.getStatus();
    }

    public InMemoryHistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }
}