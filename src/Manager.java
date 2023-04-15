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
    private HashMap<String, Object> typeTask = new HashMap<>();

    public int createTask(Task task) { // Создать задачу
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

    public void addSubtaskToEpic(Epic epic, Subtask subtask) { // Добавить подзадачу в эпик
        epic.setIdToSubtask(subtask.getId()); // Добавляем id подзадачи в лист эпика
        subtask.setIdToEpic(epic.getId()); // Добавляем id эпика к подзадаче
    }

    public int createEpic(Epic epic) { // Создание эпика
        epic.setId(nextId);
        nextId++;
        idEpic.put(epic.getId(), epic);
        assigningStatusToEpic(epic);
        return epic.getId();
    }
    // Пункт 2.1 из ТЗ "Получение списка всех задач." Не совсем понял, поэтому реализовал 2 метода (getAllListTask и
    // getAllListTaskInHash)
    // В первом методе в консоль выводятся все задачи, во втором возвращается мапа, в которой хранятся все задачи

    public void getAllListTask() {  // Получение списка всех задач
        System.out.println("Обычные задачи:");
        for (Task task : idTask.values()) {
            System.out.println("- " + task.getTitle());
        }
        for (Epic epic : idEpic.values()) {
            System.out.println("Задача эпик: " + epic.getTitle());
            System.out.println("Подзадачи эпика:");
            for (Subtask subtask : idSubtask.values()) {
                if (subtask.getIdToEpic() == epic.getId()) {
                    System.out.println("- " + subtask.getTitle());
                } else if (epic.getIdToSubtask().equals(0)) {
                    System.out.println("Подзадачи не заданы.");
                }
            }
        }
    }

    public HashMap getAllListTaskInHash() { // Получение списка со всеми задачами
        typeTask.put("Task", idTask);
        typeTask.put("Epic", idEpic);
        typeTask.put("Subtask", idSubtask);
        return typeTask;
    }

    public void deleteAllTask() { // Удаление всех задач
        idSubtask.clear();
        idTask.clear();
        idEpic.clear();
    }

    public Object getByid(int id) { // Получить любую задачу по id
        Object retrievedObject = null;
        for (int numberId : idTask.keySet()) {
            if (numberId == id) {
                retrievedObject = idTask.get(numberId);
            }
        }
        for (int numberId : idEpic.keySet()) {
            if (numberId == id) {
                retrievedObject = idEpic.get(numberId);
            }
        }
        for (int numberId : idSubtask.keySet()) {
            if (numberId == id) {
                retrievedObject = idSubtask.get(numberId);
            }
        }
        return retrievedObject;
    }

    public void updateTask(Task task) { // Обновить таск
        for (Task searchTitleTask : idTask.values()) {
            if (searchTitleTask.getTitle().equals(task.getTitle())) {
                idTask.put(searchTitleTask.getId(), task);
                task.setId(searchTitleTask.getId());
            }
        }
    }

    public void updateEpic(Epic epic) { // обновить эпик
        for (Epic searchTitleEpic : idEpic.values()) {
            if (searchTitleEpic.getTitle().equals(epic.getTitle())) {
                idEpic.put(searchTitleEpic.getId(), epic);
                epic.setId(searchTitleEpic.getId());
            }
        }
    }

    public void updateSubtask(Subtask subtask) { // обновить сабтаск
        Epic epic;
        for (Subtask searchTitleSubtask : idSubtask.values()) {
            if (searchTitleSubtask.getTitle().equals(subtask.getTitle())) {
                subtask.setId(searchTitleSubtask.getId());
                subtask.setIdToEpic(searchTitleSubtask.getIdToEpic());
                idSubtask.put(searchTitleSubtask.getId(), subtask);
                epic = idEpic.get(searchTitleSubtask.getIdToEpic());
                assigningStatusToEpic(epic);
            }
        }
    }

    public void deleteById(int id) {  // удаление по id
        if(idTask.containsKey(id)) {
            idTask.remove(id);
        }
        if(idEpic.containsKey(id)) {
            idEpic.remove(id);
        }
        if(idSubtask.containsKey(id)) {
            idSubtask.remove(id);
        }
    }

    public ArrayList getListOfTaskInEpic(Epic epic) { // Получить список подзадач эпика
        return epic.getIdToSubtask();
    }

    public String assigningStatusToEpic(Epic epic) { // статус эпика
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