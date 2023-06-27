package manager;

import exceptions.ExcludingCrossing;
import task.*;

import java.io.IOException;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private Integer nextId = 0;
    private HashMap<Integer, Task> idTask = new HashMap<>();
    private HashMap<Integer, Epic> idEpic = new HashMap<>();
    private HashMap<Integer, Subtask> idSubtask = new HashMap<>();
    private HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    private TaskStartTimeComparator taskStartTimeComparator = new TaskStartTimeComparator();
    private Set<Task> sortedTasks = new TreeSet<>(taskStartTimeComparator);

    @Override
    public int createTask(Task task) throws IOException { // Создание задач
        int id = task.getId();
        if (id == 0) {
            nextId++;
            task.setId(nextId);
            id = nextId;
        }
        if (nextId < id) {
            nextId = id;
        }
        idTask.put(id, task);
        addAndCheckingTasksForIntersection(task);
        return id;
    }

    @Override
    public int createSubtask(Subtask subtask) throws IOException { // Создание подзадачи
        int id = subtask.getId();
        if (id == 0) {
            nextId++;
            subtask.setId(nextId);
            id = nextId;
        }
        if (nextId < id) {
            nextId = id;
        }
        idSubtask.put(id, subtask);
        addAndCheckingTasksForIntersection(subtask);
        return id;
    }

    @Override
    public int createEpic(Epic epic) throws IOException { // Создание эпика
        int id = epic.getId();
        if (id == 0) {
            nextId++;
            epic.setId(nextId);
            id = nextId;
        }
        idEpic.put(id, epic);
        assigningStatusToEpic(epic);
        if (nextId < id) {
            nextId = id;
        }
        addAndCheckingTasksForIntersection(epic);
        return id;
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
    public void deleteTask() throws IOException { // Удаление списка задач
        for (Task task : idTask.values()) {
            inMemoryHistoryManager.remove(task.getId()); // Удаление из истории
        }
        idTask.clear();
    }

    @Override
    public void deleteSubtask() throws IOException { // Удаление списка подзадач
        for (Epic epic : idEpic.values()) { // Удаление из истории
            for (int i = 0; i < epic.getIdToSubtask().size(); i++) {
                inMemoryHistoryManager.remove(epic.getIdToSubtask().get(i));
            }
            epic.deleteAllIdToSubtask();
            assigningStatusToEpic(epic);
        }
        idSubtask.clear();
    }

    @Override
    public void deleteEpic() throws IOException { // Удаление эпиков и их подзадач
        for (Epic epic : idEpic.values()) { // Удаление из истории
            inMemoryHistoryManager.remove(epic.getId());
        }
        for (Subtask sub : idSubtask.values()) {
            inMemoryHistoryManager.remove(sub.getId());
        }
        deletingAllTasksOfACertainType(TaskType.EPIC);
        deletingAllTasksOfACertainType(TaskType.SUBTASK);
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
    public void updateTask(Task task) throws IOException { // Обновить таск
        if (idTask.get(task.getId()) != null) {
            sortedTasks.remove(task);
            idTask.put(task.getId(), task);
            deletingAnEntryInACollection(task);
            sortedTasks.add(task);
        }
    }

    @Override
    public void updateEpic(Epic epic) throws IOException { // обновить эпик
        if (idEpic.get(epic.getId()) != null) {
            deletingAnEntryInACollection(epic);
            epic.setId(epic.getId());
            assigningStatusToEpic(epic);
            idEpic.put(epic.getId(), epic);
            sortedTasks.add(epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) throws IOException { // обновить сабтаск
        for (Subtask searchSubtask : idSubtask.values()) {
            if (searchSubtask.getId() == subtask.getId()) {
                subtask.setId(searchSubtask.getId());
                subtask.setIdToEpic(searchSubtask.getIdToEpic());
                idSubtask.put(searchSubtask.getId(), subtask);
                Epic epic = idEpic.get(searchSubtask.getIdToEpic());
                assigningStatusToEpic(epic);
                deletingAnEntryInACollection(searchSubtask);
                sortedTasks.add(searchSubtask);
            }
        }
    }

    @Override
    public void deleteById(int id) throws IOException {  // удаление по id
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
        for (Integer integer : idSubtasksInEpic) {
            subtaskList.add(idSubtask.get(integer));
        }
        return subtaskList;
    }

    @Override
    public List<Task> getListHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    @Override
    public Task getAnyTask(Integer id) {
        if (idTask.containsKey(id)) {
            return idTask.get(id);
        } else if (idEpic.containsKey(id)) {
            return idEpic.get(id);
        } else if (idSubtask.containsKey(id)) {
            return idSubtask.get(id);
        } else {
            return null;
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedTasks);
    }

    protected HistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }

    protected TasksStatus assigningStatusToEpic(Epic epic) { // статус эпика
        int countNew = 0;
        int countDone = 0;
        int countInProgress = 0;
        ArrayList<Integer> idToSubtask = epic.getIdToSubtask();
        if (idToSubtask.size() != 0) {
            for (Integer integer : idToSubtask) {
                if (idSubtask.get(integer).getStatus().equals(TasksStatus.NEW)) countNew++;
                else if (idSubtask.get(integer).getStatus().equals(TasksStatus.DONE)) countDone++;
                else if (idSubtask.get(integer).getStatus().equals(TasksStatus.IN_PROGRESS)) countInProgress++;
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

    private void deletingAnEntryInACollection(Task task) {
        ArrayList<Task> tasksSorted = new ArrayList<>(sortedTasks);
        tasksSorted.remove(task);
        sortedTasks.clear();
        sortedTasks.addAll(tasksSorted);
    }

    private void addAndCheckingTasksForIntersection(Task task) {
        sortedTasks.add(task);
        if (task.getStartTime() != null) {
            List<Task> listSortedTasks = getPrioritizedTasks();
            for (int i = 1; i < listSortedTasks.size(); i++) {
                Task prevTask = listSortedTasks.get(i - 1);
                Task currentTask = listSortedTasks.get(i);
                if (prevTask.getEndTime().isAfter(currentTask.getStartTime().plusSeconds(currentTask.getDuration()))) {
                    throw new ExcludingCrossing("В списке встречается пересечение.");
                }
            }
        }
    }

    private void deleteSubtaskFromEpic(int id) { // Удаление подзадачи
        int idE = idSubtask.get(id).getIdToEpic();
        idEpic.get(idE).deleteIdSubtask(id);
        idSubtask.remove(id);
    }

    private void deletingAllTasksOfACertainType(TaskType type) {
        ArrayList<Task> tasksSorted = new ArrayList<>(sortedTasks);
        sortedTasks.clear();
        for (Task task : tasksSorted) {
            if (!task.getType().equals(type)) {
                sortedTasks.add(task);
            }
        }
    }

    static class TaskStartTimeComparator implements Comparator<Task> {

        @Override
        public int compare(Task task1, Task task2) {
            if ((task1.getStartTime() == null) || (task2.getStartTime() == null)) return 2;
            if (task1.getStartTime().isBefore(task2.getStartTime())) return -1;
            if (task1.getStartTime().isAfter(task2.getStartTime())) return 1;
            else return 0;
        }
    }
}