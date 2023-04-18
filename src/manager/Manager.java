package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int nextId = 1;
    private HashMap<Integer, Task> idTask = new HashMap<>();
    private HashMap<Integer, Epic> idEpic = new HashMap<>();
    private HashMap<Integer, Subtask> idSubtask = new HashMap<>();

    public int createTask(Task task) { // Создание задач
        task.setId(nextId);
        nextId++;
        idTask.put(task.getId(), task);
        return task.getId();
    }

    public int createSubtask(Subtask subtask) { // Создание подзадачи
        subtask.setId(nextId);
        nextId++;
        idSubtask.put(subtask.getId(), subtask);
        return subtask.getId();
    }

    public int createEpic(Epic epic) { // Создание эпика
        epic.setId(nextId);
        nextId++;
        idEpic.put(epic.getId(), epic);
        assigningStatusToEpic(epic);
        return epic.getId();
    }

    public ArrayList<Task> getListTask() { // Получить список задач
        ArrayList<Task> tasks = new ArrayList<>(idTask.values());
        return tasks;
    }

    public ArrayList<Epic> getListEpic() { // Получить список эпиков
        ArrayList<Epic> epics = new ArrayList<>(idEpic.values());
        return epics;
    }

    public ArrayList<Subtask> getListSubtask() { // Получить список подзадач
        ArrayList<Subtask> subtasks = new ArrayList<>(idSubtask.values());
        return subtasks;
    }

    public void deleteTask() { // Удаление списка задач
        idTask.clear();
    }

    public void deleteSubtask() { // Удаление списка подзадач
        idSubtask.clear();
    }

    public void deleteEpic() { // Удаление подзадач в эпиках
        int counter = 0;
        ArrayList<Integer> listIdSubtask = new ArrayList<>();
        for(Epic epic : idEpic.values()){
            for(Subtask sub : idSubtask.values()){
                if(sub.getIdToEpic() == epic.getId()){
                    counter++;
                    listIdSubtask.add(sub.getId());
                }
            }
            epic.deleteAllIdToSubtask();
        }
        for(int i = 0; i < counter; i++){
            idSubtask.remove(listIdSubtask.get(i));
        }
        idEpic.clear();
    }

    public Task getByid(int id) { // Получить любую задачу по id
        if (idTask.get(id) != null) return idTask.get(id);
        if (idEpic.get(id) != null) return idEpic.get(id);
        if (idSubtask.get(id) != null) return idSubtask.get(id);
        else return null;
    }

    public void updateTask(Task task) { // Обновить таск
            if (idTask.get(task.getId()) != null && idTask.get(task.getId()).getId() == task.getId()) {
            idTask.put(task.getId(), task);
            task.setId(task.getId());
        }
    }

    public void updateEpic(Epic epic) { // обновить эпик
            if(idEpic.get(epic.getId()) != null && idEpic.get(epic.getId()).getId() == epic.getId()) {
            epic.setId(epic.getId());
            assigningStatusToEpic(epic);
            idEpic.put(epic.getId(), epic);
        }
    }

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

    public void deleteById(int id) {  // удаление по id
        if(idTask.containsKey(id)) {
            idTask.remove(id);
        }
        if(idEpic.containsKey(id)) {

            for(int i = 0; i < idEpic.get(id).getIdToSubtask().size(); i++) {
                int asd = idEpic.get(id).getIdToSubtask().get(i);
                idSubtask.remove(idEpic.get(id).getIdToSubtask().get(i));
            }
            idEpic.get(id).deleteAllIdToSubtask();
            idEpic.remove(id);
        }
        if(idSubtask.containsKey(id)) {
            int idToEpic = idSubtask.get(id).getIdToEpic();
            idEpic.get(idToEpic).deleteIdSubtask(id); // Тут я удаляю не весь список, а id определенной задачи
            deleteSubtaskFromEpic(id);
            Epic epic = idEpic.get(idToEpic);
            assigningStatusToEpic(epic);
        }
    }

    public void deleteSubtaskFromEpic (int id) { // Удаление подзадачи
        int idE = idSubtask.get(id).getIdToEpic();
        idEpic.get(idE).deleteIdSubtask(id);
        idSubtask.remove(id);
    }

    public ArrayList getListOfTaskInEpic(Epic epic) { // Получить список подзадач эпика
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        Integer idEpic = epic.getId();
        for(Subtask sub : idSubtask.values()){
            if(idEpic.equals(sub.getIdToEpic())) {
                subtaskList.add(sub);
            }
        }
        return subtaskList;
    }

    private String assigningStatusToEpic(Epic epic) { // статус эпика
        int id;
        int countNew = 0;
        int countDone = 0;
        int countInProgress = 0;
        if (epic.getIdToSubtask().size() != 0) {
            for (int i = 0; i < epic.getIdToSubtask().size(); i++) {
                id = epic.getIdToSubtask().get(i);
                if(idSubtask.get(id).getStatus().equals("NEW")) countNew++;
                else if(idSubtask.get(id).getStatus().equals("DONE")) countDone++;
                else if(idSubtask.get(id).getStatus().equals("IN_PROGRESS")) countInProgress++;
            }
            if ((countNew == epic.getIdToSubtask().size()) && (countInProgress == 0)) epic.setStatus("NEW");
            else if ((countDone == epic.getIdToSubtask().size()) && (countInProgress == 0)) epic.setStatus("DONE");
            else epic.setStatus("IN_PROGRESS");
        } else {
            epic.setStatus("NEW");
        }
        return epic.getStatus();
    }
}