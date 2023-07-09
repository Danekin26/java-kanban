package manager;

import exceptions.ManagerSaveException;
import task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private Path dir;
    private String tableOfContents = "id,type,name,status,description,epic,time start,time end,duration in sec\n";
    private HistoryManager historyManager;

    public FileBackedTasksManager(Path dir) {
        this.dir = dir;
    }

    public FileBackedTasksManager() {
        this.dir = Paths.get("./src/history.csv");
    }

    public static void main(String[] args) throws IOException {
        //загрузка из файла
        FileBackedTasksManager fileBackedTasksManager2 = loadFromFile(new File("./src/history.csv"));
        assigningId(fileBackedTasksManager2);
        // Запись в файл
        /*Path path = Paths.get("./src/history.csv");
        FileBackedTasksManager fileBackedTasksManager2 = new FileBackedTasksManager(path);

        assigningId(fileBackedTasksManager2);
        Epic epic1 = new Epic("Экзамен", "Нужно сдать экзамен");
        Subtask subtask11 = new Subtask("Расписать вопросы", "Подробное описание вопросов к экзамену",
                TasksStatus.DONE, LocalDateTime.of(2023, 6, 25, 10, 30), 600);
        epic1.setType(TaskType.EPIC);
        subtask11.setType(TaskType.SUBTASK);
        epic1.setSubtaskDuration(subtask11);


        Subtask subtask12 = new Subtask("Созвониться с одногруппниками",
                "Написать в группу и узнать удобное для всех время", TasksStatus.NEW,
                LocalDateTime.of(2023, 6, 26, 20, 0), 900);
        subtask12.setType(TaskType.SUBTASK);
        epic1.setSubtaskDuration(subtask12);

        Epic epic2 = new Epic("Покупка компьютера", "Компьютер нужен для работы и игр");
        Subtask subtask21 = new Subtask("Выбрать компонентны",
                "Подобрать актуальные компонентны для 2023 года", TasksStatus.DONE,
                LocalDateTime.of(2023, 7, 27, 10, 30), 600);
        epic2.setType(TaskType.EPIC);
        epic2.setSubtaskDuration(subtask21);

        Subtask subtask22 = new Subtask("Мониторить цены",
                "Цены в разных магазинах отличаются друг от друга", TasksStatus.NEW,
                LocalDateTime.of(2023, 8, 28, 20, 0), 900);
        subtask22.setType(TaskType.SUBTASK);
        epic2.setSubtaskDuration(subtask22);

        Task task1 = new Task("Создать задачу", "Просто задача", TasksStatus.IN_PROGRESS,
                LocalDateTime.of(2023, 9, 29, 20, 0), 900);
        task1.setType(TaskType.TASK);
        Task task2 = new Task("Создать вторую задачу", "Просто задача", TasksStatus.DONE,
                LocalDateTime.of(2023, 10, 30, 10, 30), 600);
        task2.setType(TaskType.TASK);

        fileBackedTasksManager2.createEpic(epic1);   // id = 1
        epic1.setType(TaskType.EPIC);
        fileBackedTasksManager2.createSubtask(subtask11); // id = 2
        epic1.setIdToSubtask(subtask11.getId());
        subtask11.setIdToEpic(epic1.getId());
        subtask12.setType(TaskType.SUBTASK);
        fileBackedTasksManager2.createSubtask(subtask12); //id = 3
        epic1.setIdToSubtask(subtask12.getId());
        subtask12.setIdToEpic(epic1.getId());
        fileBackedTasksManager2.updateEpic(epic1);
        epic2.setType(TaskType.EPIC);
        fileBackedTasksManager2.createEpic(epic2); // id = 4
        subtask21.setType(TaskType.SUBTASK);
        fileBackedTasksManager2.createSubtask(subtask21); // id = 5

        epic2.setIdToSubtask(subtask21.getId());
        subtask21.setIdToEpic(epic2.getId());
        subtask22.setType(TaskType.SUBTASK);
        fileBackedTasksManager2.createSubtask(subtask22); // id = 6

        epic2.setIdToSubtask(subtask22.getId());
        subtask22.setIdToEpic(epic2.getId());
        fileBackedTasksManager2.updateEpic(epic2);
        task1.setType(TaskType.TASK);
        fileBackedTasksManager2.createTask(task1); // id = 7

        task2.setType(TaskType.TASK);
        fileBackedTasksManager2.createTask(task2); // id = 8

        fileBackedTasksManager2.getTask(7);
        fileBackedTasksManager2.getSubtask(5);
        fileBackedTasksManager2.getEpic(1);
        fileBackedTasksManager2.getTask(8);
        fileBackedTasksManager2.getSubtask(3);

        fileBackedTasksManager2.deleteById(7);*/
        //ArrayList<Task> asd = getListTask();
    }

    @Override
    public int createTask(Task task) throws IOException {
        super.createTask(task);
        save();
        return task.getId();
    }

    @Override
    public int createEpic(Epic epic) throws IOException {
        super.createEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public int createSubtask(Subtask subtask) throws IOException {
        super.createSubtask(subtask);
        save();
        return subtask.getId();
    }

    @Override
    public void deleteTask() throws IOException {
        super.deleteTask();
        save();
    }

    @Override
    public void deleteSubtask() throws IOException {
        super.deleteSubtask();
        save();
    }

    @Override
    public void deleteEpic() throws IOException {
        super.deleteEpic();
        save();
    }

    @Override
    public void updateTask(Task task) throws IOException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) throws IOException {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws IOException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteById(int id) throws IOException {
        super.deleteById(id);
        save();
    }

    @Override
    public List<Task> getListHistory() {
        return super.getListHistory();
    }

    @Override
    public Task getTask(int id) throws IOException {  // Получить задачу по id
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) throws IOException {  // Получить эпик по id
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) throws IOException {  // Получить подзадачу по id
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    public static Task fromString(String value) { // Восстановление задачи из строки
        String[] split = value.split(",");
        split[3] = split[3].toUpperCase();
        TasksStatus status = TasksStatus.valueOf(split[3]);
        Task task = new Task(split[2], split[4], status);
        task.setId(Integer.parseInt(split[0]));
        split[1] = split[1].toUpperCase();
        TaskType type = TaskType.valueOf(split[1]);
        task.setType(type);
        task.setStartTime(LocalDateTime.parse(split[6]));
        task.setEndTime(LocalDateTime.parse(split[7]));
        task.setDuration(Long.parseLong(split[8]));
        if (type.equals(TaskType.SUBTASK)) {
            task.setIdToEpic(Integer.parseInt(split[5]));
        }
        return task;
    }


    public static FileBackedTasksManager loadFromFile(File file) throws IOException { // восстановление всей информации из файла
        String datFile;
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file.toPath());
        try {
            datFile = Files.readString(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при считывании." + e.getMessage());
        }
        if (datFile.isBlank()) {
            return fileBackedTasksManager;
        }
        String[] str = datFile.split("\n");
        for (int i = 1; i < str.length - 2; i++) {
            Task task = fromString(str[i]);
            TaskType te = task.getType();
            if (te.equals(TaskType.TASK)) {
                task.setType(te);
                fileBackedTasksManager.createTask(task); // Не знаю как лучше сделать, была мысль записывать через геттеры мап задач в менеджере
            } else if (task.getType().equals(TaskType.EPIC)) {
                Epic epic = new Epic(task);
                epic.setType(te);
                fileBackedTasksManager.createEpic(epic);
            } else {
                Subtask subtask = new Subtask(task, task.getIdToEpic());
                subtask.setType(te);
                fileBackedTasksManager.createSubtask(subtask);
            }
        }
        assigningId(fileBackedTasksManager); // вызов присваивания эпикам id подзадач
        String lastLineArrays = str[str.length - 1];
        List<Integer> historyFromString = historyFromString(lastLineArrays);
        repairHistory(fileBackedTasksManager, historyFromString); // Восстановление истории просмотров из списка id
        return fileBackedTasksManager;
    }

    private static void assigningId(FileBackedTasksManager file) { //присваивание id подзадач в эпик
        ArrayList<Epic> listEpic = file.getListEpic();
        ArrayList<Subtask> subtasks = file.getListSubtask();
        for (Epic e : listEpic) {
            for (Subtask sub : subtasks) {
                if (e.getId() == sub.getIdToEpic()) {
                    Epic epic = (Epic) file.getAnyTask(e.getId());
                    epic.setIdToSubtask(sub.getId());
                }
            }
            file.assigningStatusToEpic(e);
        }
    }

    private static void repairHistory(FileBackedTasksManager file, List<Integer> historyFromString) throws IOException {
        for (Integer integer : historyFromString) {
            if (file.getListTask().contains(file.getTask(integer))) {
                file.getTask(integer);
            } else if (file.getListEpic().contains(file.getEpic(integer))) {
                file.getEpic(integer);
            } else {
                file.getSubtask(integer);
            }
        }
        file.save();
    }

    private static String historyToString(HistoryManager manager) {  // запись истории в строку
        StringBuilder build = new StringBuilder();
        build.append("\n");
        for (int i = 0; i < manager.getHistory().size(); i++) {
            build.append(manager.getHistory().get(i).getId());
            if (i != manager.getHistory().size() - 1) {
                build.append(",");
            }
        }
        return build.toString();
    }

    private static List<Integer> historyFromString(String value) { // запись id истории в список
        String[] split = value.split(",");
        List<Integer> idHistory = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            idHistory.add(Integer.parseInt(split[i]));
        }
        return idHistory;
    }

    public void save() throws IOException {
        List<Task> allTask = new ArrayList<>(); // Список со всеми задачами
        allTask.addAll(getListTask());
        allTask.addAll(getListEpic());
        allTask.addAll(getListSubtask());
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dir.toFile())); // запись в файл
        bufferedWriter.write(tableOfContents);
        for (Task task : allTask) {
            bufferedWriter.write(taskToString(task));   // записываются все задачи
        }
        try {
            bufferedWriter.write(historyToString(getInMemoryHistoryManager())); // запись истории, если она пустая, то игнорируется
        } catch (NullPointerException e) {
            System.out.println("История пуста.");
        }
        bufferedWriter.close();
    }

    private String taskToString(Task task) { // запись задач в строку
        String id = String.valueOf(task.getIdToEpic());
        if ((task.getDuration() == 0) || (task.getStartTime() == null) || (task.getEndTime() == null)) {
            return task.getId() + "," + task.getType() + "," + task.getTitle() + "," + task.getStatus()
                    + "," + task.getDescription() + "," + id + "\n";
        } else {
            return task.getId() + "," + task.getType() + "," + task.getTitle() + "," + task.getStatus()
                    + "," + task.getDescription() + "," + id + "," + task.getStartTime() + ","
                    + task.getEndTime() + "," + task.getDuration() + "\n";
        }
    }
}