package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TasksStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int nextId = 1;
    private HashMap<Integer, Task> idTask = new HashMap<>();
    private HashMap<Integer, Epic> idEpic = new HashMap<>();
    private HashMap<Integer, Subtask> idSubtask = new HashMap<>();
    private HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

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
        return new ArrayList<>(idTask.values());
    }

    @Override
    public ArrayList<Epic> getListEpic() { // Получить список эпиков
        return new ArrayList<>(idEpic.values());
    }

    @Override
    public ArrayList<Subtask> getListSubtask() { // Получить список подзадач
        return new ArrayList<>(idSubtask.values());
    }

    @Override
    public void deleteTask() { // Удаление списка задач
        for (Task task : idTask.values()){
            inMemoryHistoryManager.remove(task.getId()); // Удаление из истории
        }
        idTask.clear();
    }

    @Override
    public void deleteSubtask() { // Удаление списка подзадач
        for (Epic epic : idEpic.values()) { // Удаление из истории
            for(int i = 0; i< epic.getIdToSubtask().size(); i++) {
                inMemoryHistoryManager.remove(epic.getIdToSubtask().get(i));
            }
            epic.deleteAllIdToSubtask();
            assigningStatusToEpic(epic);
        }
        idSubtask.clear();
    }

    @Override
    public void deleteEpic() { // Удаление эпиков и их подзадач
        for (Epic epic : idEpic.values()) { // Удаление из истории
            inMemoryHistoryManager.remove(epic.getId());
        }
        for (Subtask sub : idSubtask.values()){
            inMemoryHistoryManager.remove(sub.getId());
        }
        idEpic.clear();
        idSubtask.clear();
    }

    @Override
    public Task getTask(int id) {  // Получить задачу по id
        Task task = idTask.get(id);
        if (task != null) {
            inMemoryHistoryManager.add(task);
            return task;
        } else {
            return null;
        }
    }

    @Override
    public Epic getEpic(int id) {  // Получить эпик по id
        Epic epic = idEpic.get(id);
        if (epic != null) {
            inMemoryHistoryManager.add(epic);
            return epic;
        } else {
            return null;
        }
    }

    @Override
    public Subtask getSubtask(int id) {  // Получить подзадачу по id
        Subtask subtask = idSubtask.get(id);
        if (subtask != null) {
            inMemoryHistoryManager.add(subtask);
            return subtask;
        } else {
            return null;
        }
    }

    @Override
    public void updateTask(Task task) { // Обновить таск
        if (idTask.get(task.getId()) != null && idTask.get(task.getId()).getId() == task.getId()) {
            idTask.put(task.getId(), task);
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
        for (Subtask searchSubtask : idSubtask.values()) {
            if (searchSubtask.getId() == subtask.getId()) {
                subtask.setId(searchSubtask.getId());
                subtask.setIdToEpic(searchSubtask.getIdToEpic());
                idSubtask.put(searchSubtask.getId(), subtask);
                Epic epic = idEpic.get(searchSubtask.getIdToEpic());
                assigningStatusToEpic(epic);
            }
        }
    }

    @Override
    public void deleteById(int id) {  // удаление по id
        if (idTask.containsKey(id)) {
            inMemoryHistoryManager.remove(idTask.get(id).getId()); // удаление из истории
            idTask.remove(id);
        }
        if (idEpic.containsKey(id)) {
            inMemoryHistoryManager.remove(idEpic.get(id).getId()); // Удаление из истории
            for (int i = 0; i < idEpic.get(id).getIdToSubtask().size(); i++) {
                idSubtask.remove(idEpic.get(id).getIdToSubtask().get(i));
                inMemoryHistoryManager.remove(idEpic.get(id).getIdToSubtask().get(i)); // Удаление из истории
            }
            idEpic.get(id).deleteAllIdToSubtask();
            idEpic.remove(id);
        }
        if (idSubtask.containsKey(id)) {
            inMemoryHistoryManager.remove(idSubtask.get(id).getId()); // Удаление из истории
            int idToEpic = idSubtask.get(id).getIdToEpic();
            Epic epic = idEpic.get(idToEpic);
            deleteSubtaskFromEpic(id);
            assigningStatusToEpic(epic);
        }
    }

    @Override
    public ArrayList getListOfTaskInEpic(Epic epic) { // Получить список подзадач эпика
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        ArrayList<Integer> idSubtasksInEpic = epic.getIdToSubtask();
        for (int i = 0; i < idSubtasksInEpic.size(); i++) {
            subtaskList.add(idSubtask.get(idSubtasksInEpic.get(i)));
        }
        return subtaskList;
    }

    @Override
    public List<Task> getListHistory() {
        System.out.println(inMemoryHistoryManager.getHistory()); // Печать для проверки
        return inMemoryHistoryManager.getHistory();
    }

    private TasksStatus assigningStatusToEpic(Epic epic) { // статус эпика
        int id;
        int countNew = 0;
        int countDone = 0;
        int countInProgress = 0;
        ArrayList<Integer> idToSubtask = epic.getIdToSubtask();
        if (idToSubtask.size() != 0) {
            for (int i = 0; i < idToSubtask.size(); i++) {
                id = idToSubtask.get(i);
                if (idSubtask.get(id).getStatus().equals(TasksStatus.NEW)) countNew++;
                else if (idSubtask.get(id).getStatus().equals(TasksStatus.DONE)) countDone++;
                else if (idSubtask.get(id).getStatus().equals(TasksStatus.IN_PROGRESS)) countInProgress++;
            }
            if ((countNew == idToSubtask.size()) && (countInProgress == 0)) epic.setStatus(TasksStatus.NEW);
            else if ((countDone == idToSubtask.size()) && (countInProgress == 0))
                epic.setStatus(TasksStatus.DONE);
            else epic.setStatus(TasksStatus.IN_PROGRESS);
        } else {
            epic.setStatus(TasksStatus.NEW);
        }
        return epic.getStatus();
    }

    private void deleteSubtaskFromEpic(int id) { // Удаление подзадачи
        int idE = idSubtask.get(id).getIdToEpic();
        idEpic.get(idE).deleteIdSubtask(id);
        idSubtask.remove(id);
    }
}