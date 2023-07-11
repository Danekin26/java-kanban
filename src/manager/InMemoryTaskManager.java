package manager;

import exceptions.ExcludingCrossing;
import task.*;

import java.io.IOException;
import java.util.*;

/*
 * Менеджер, реализующий взаимодействие с задачами
 */
public class InMemoryTaskManager implements TaskManager {

    private Integer nextId = 0;
    private HashMap<Integer, Task> idTask = new HashMap<>();
    private HashMap<Integer, Epic> idEpic = new HashMap<>();
    private HashMap<Integer, Subtask> idSubtask = new HashMap<>();
    private HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    private TaskStartTimeComparator taskStartTimeComparator = new TaskStartTimeComparator();
    private Set<Task> sortedTasks = new TreeSet<>(taskStartTimeComparator);

    /*
     * Создание задачи
     */
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

    /*
     * Создание подзадачи
     */
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

    /*
     * Создание эпика
     */
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
        // addAndCheckingTasksForIntersection(epic);
        return id;
    }

    /*
     * Получить список всех задач
     */
    @Override
    public ArrayList<Task> getListTask() { // Получить список задач
        return new ArrayList<>(idTask.values());
    }

    /*
     * Получить список всех эпиков
     */
    @Override
    public ArrayList<Epic> getListEpic() { // Получить список эпиков
        return new ArrayList<>(idEpic.values());
    }

    /*
     * Получить список всех подзадач
     */
    @Override
    public ArrayList<Subtask> getListSubtask() { // Получить список подзадач
        return new ArrayList<>(idSubtask.values());
    }

    /*
     * Удалить все задачи из списка и из истории
     */
    @Override
    public void deleteTask() throws IOException {
        for (Task task : idTask.values()) {
            inMemoryHistoryManager.remove(task.getId());
        }
        idTask.clear();
    }

    /*
     * Удалить все подзадачи из списка и из истории
     */
    @Override
    public void deleteSubtask() throws IOException {
        for (Epic epic : idEpic.values()) {
            for (int i = 0; i < epic.getIdToSubtask().size(); i++) {
                inMemoryHistoryManager.remove(epic.getIdToSubtask().get(i));
            }
            epic.deleteAllIdToSubtask();
            assigningStatusToEpic(epic);
        }
        idSubtask.clear();
    }

    /*
     * Удалить все эпики из списка и из истории
     */
    @Override
    public void deleteEpic() throws IOException {
        for (Epic epic : idEpic.values()) {
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
    /*
     * Получить задачу по id
     */
    @Override
    public Task getTask(int id) throws IOException {
        Task task = idTask.get(id);
        if (task != null) {
            inMemoryHistoryManager.add(task);
            return task;
        } else {
            return null;
        }
    }
    /*
     * Получить эпик по id
     */
    @Override
    public Epic getEpic(int id) throws IOException {
        Epic epic = idEpic.get(id);
        if (epic != null) {
            inMemoryHistoryManager.add(epic);
            return epic;
        } else {
            return null;
        }
    }
    /*
     * Получить подзадачу по id
     */
    @Override
    public Subtask getSubtask(int id) throws IOException {
        Subtask subtask = idSubtask.get(id);
        if (subtask != null) {
            inMemoryHistoryManager.add(subtask);
            return subtask;
        } else {
            return null;
        }
    }
    /*
     * Обновить задачу
     */
    @Override
    public void updateTask(Task task) throws IOException { //
        if (idTask.get(task.getId()) != null) {
            sortedTasks.remove(task);
            idTask.put(task.getId(), task);
            deletingAnEntryInACollection(task);
            sortedTasks.add(task);
        }
    }
    /*
     * Обновить эпик
     */
    @Override
    public void updateEpic(Epic epic) throws IOException {
        if (idEpic.get(epic.getId()) != null) {
            deletingAnEntryInACollection(epic);
            epic.setId(epic.getId());
            assigningStatusToEpic(epic);
            idEpic.put(epic.getId(), epic);
            sortedTasks.add(epic);
        }
    }
    /*
     * Обновить подзадачу
     */
    @Override
    public void updateSubtask(Subtask subtask) throws IOException {
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
    /*
     * Удаление задачи по id
     */
    @Override
    public void deleteById(int id) throws IOException {
        if (idTask.containsKey(id)) {
            inMemoryHistoryManager.remove(idTask.get(id).getId());
            idTask.remove(id);
        }
        if (idEpic.containsKey(id)) {
            inMemoryHistoryManager.remove(idEpic.get(id).getId());
            for (int i = 0; i < idEpic.get(id).getIdToSubtask().size(); i++) {
                idSubtask.remove(idEpic.get(id).getIdToSubtask().get(i));
                inMemoryHistoryManager.remove(idEpic.get(id).getIdToSubtask().get(i));
            }
            idEpic.get(id).deleteAllIdToSubtask();
            idEpic.remove(id);
        }
        if (idSubtask.containsKey(id)) {
            inMemoryHistoryManager.remove(idSubtask.get(id).getId());
            int idToEpic = idSubtask.get(id).getIdToEpic();
            Epic epic = idEpic.get(idToEpic);
            deleteSubtaskFromEpic(id);
            assigningStatusToEpic(epic);
        }
    }
    /*
     * Получить список подзадач эпика
     */
    @Override
    public ArrayList getListOfTaskInEpic(Epic epic) { //
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        ArrayList<Integer> idSubtasksInEpic = epic.getIdToSubtask();
        for (Integer integer : idSubtasksInEpic) {
            subtaskList.add(idSubtask.get(integer));
        }
        return subtaskList;
    }
    /*
     * Получить список истории
     */
    @Override
    public List<Task> getListHistory() {
        return inMemoryHistoryManager.getHistory();
    }
    /*
     * Получить любую задачу по id
     */
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
    /*
     * Получить список отсортированный по приоритету
     */
    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedTasks);
    }
    /*
     * Получить менеджер истории
     */
    protected HistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }
    /*
     * Получить маппу задач
     */
    public HashMap<Integer, Task> getIdTask() {
        return idTask;
    }
    /*
     * Получить маппу эпиков
     */
    public HashMap<Integer, Epic> getIdEpic() {
        return idEpic;
    }
    /*
     * Получить маппу подзадач
     */
    public HashMap<Integer, Subtask> getIdSubtask() {
        return idSubtask;
    }

    protected TasksStatus assigningStatusToEpic(Epic epic) { // статус эпика
        int countNew = 0;
        int countDone = 0;
        int countInProgress = 0;
        ArrayList<Integer> idToSubtask = epic.getIdToSubtask();
        if (idToSubtask != null && idToSubtask.size() != 0) {
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