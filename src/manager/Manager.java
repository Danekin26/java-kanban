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

    public int createTask(Task task) { // Создать задачу
        task.setId(nextId);
        nextId++;
        idTask.put(task.getId(), task);
        return task.getId();
    }

    public int createSubtask(Subtask subtask, Epic epic) { // Создание подзадачи
        subtask.setId(nextId);
        nextId++;
        idSubtask.put(subtask.getId(), subtask);
        epic.setIdToSubtask(subtask.getId()); // добавить id подзадачи в эпик
        subtask.setIdToEpic(epic.getId()); // Добавить id эпика в подзадачу
        assigningStatusToEpic(epic); // Обновление статуса эпика
        return subtask.getId();
    }

    public int createEpic(Epic epic) { // Создание эпика
        epic.setId(nextId);
        nextId++;
        idEpic.put(epic.getId(), epic);
        assigningStatusToEpic(epic);
        return epic.getId();
    }

    public ArrayList<Task> getTask() { // Получить список задач
        ArrayList<Task> tasks = new ArrayList<>(idTask.values());
        return tasks;
    }

    public ArrayList<Epic> getEpic() { // Получить список эпиков
        ArrayList<Epic> epics = new ArrayList<>(idEpic.values());
        return epics;
    }

    public ArrayList<Subtask> getSubtask() { // Получить список подзадач
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
    }

    public Task getByid(int id) { // Получить любую задачу по id
        if (idTask.get(id) != null) return idTask.get(id);
        if (idEpic.get(id) != null) return idEpic.get(id);
        if (idSubtask.get(id) != null) return idSubtask.get(id);
        else return null;
    }

    public void updateTask(Task task) { // Обновить таск
        for (Task searchTask : idTask.values()) {
            if (searchTask.getId() == task.getId()) {
                idTask.put(searchTask.getId(), task);
                task.setId(searchTask.getId());
            }
        }
    }

    public void updateEpic(Epic epic) { // обновить эпик
        for (Epic searchEpic : idEpic.values()) {
            if (searchEpic.getId() == epic.getId()) {
                idEpic.put(searchEpic.getId(), epic);
                epic.setId(searchEpic.getId());
            }
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
            idEpic.get(id).deleteAllIdToSubtask();
            deleteSubtaskFromEpic(id);
        }
        if(idSubtask.containsKey(id)) {
            int idToEpic = idSubtask.get(id).getIdToEpic();
            idEpic.get(idToEpic).deleteIdSubtask(id);
            deleteSubtaskFromEpic(id);
            Epic epic = idEpic.get(idToEpic);
            idSubtask.remove(id);
            assigningStatusToEpic(epic);
        }
    }

    private void deleteSubtaskFromEpic (int id) { // Удаление подзадачи
        int counter = 0;
        ArrayList<Integer> listIdSubtask = new ArrayList<>();
        for(Subtask sub : idSubtask.values()) {
            if(sub.getIdToEpic() == id) {
                counter++;
                listIdSubtask.add(sub.getId());
            }
        }
        for(int i = 0; i < counter; i++) {
            idSubtask.remove(listIdSubtask.get(i));
        }
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
        if (epic.getIdToSubtask().size() != 0) {
            for (int i = 0; i < epic.getIdToSubtask().size(); i++) {
                id = epic.getIdToSubtask().get(i);
                if(idSubtask.get(id).getStatus().equals("NEW")) countNew++;
                else if(idSubtask.get(id).getStatus().equals("DONE")) countDone++;
            }
            if (countNew == epic.getIdToSubtask().size()) epic.setStatus("NEW");
            else if (countDone == epic.getIdToSubtask().size()) epic.setStatus("DONE");
            else epic.setStatus("IN_PROGRESS");
        } else {
            epic.setStatus("NEW");
        }
        return epic.getStatus();
    }
}