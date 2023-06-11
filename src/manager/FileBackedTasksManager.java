package manager;

import task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private Path dir;

    public FileBackedTasksManager(Path dir) {
        this.dir = dir;
    }

    public static void main(String[] args) throws IOException {
        // Запись в файл
        Path path = Paths.get("C:\\Users\\Данисимо\\dev\\java-kanban\\src\\history.csv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(path);

        Epic epic1 = new Epic("Экзамен", "Нужно сдать экзамен");
        Subtask subtask11 = new Subtask("Расписать вопросы", "Подробное описание вопросов к экзамену",
                TasksStatus.DONE);
        epic1.setType(TaskType.EPIC);
        subtask11.setType(TaskType.SUBTASK);


        Subtask subtask12 = new Subtask("Созвониться с одногруппниками",
                "Написать в группу и узнать удобное для всех время", TasksStatus.NEW);
        subtask12.setType(TaskType.SUBTASK);

        Epic epic2 = new Epic("Покупка компьютера", "Компьютер нужен для работы и игр");
        Subtask subtask21 = new Subtask("Выбрать компонентны",
                "Подобрать актуальные компонентны для 2023 года", TasksStatus.DONE);
        epic2.setType(TaskType.EPIC);

        Subtask subtask22 = new Subtask("Мониторить цены",
                "Цены в разных магазинах отличаются друг от друга", TasksStatus.NEW);
        subtask22.setType(TaskType.SUBTASK);
        Task task1 = new Task("Создать задачу", "Просто задача", TasksStatus.IN_PROGRESS);
        task1.setType(TaskType.TASK);
        Task task2 = new Task("Создать вторую задачу", "Просто задача", TasksStatus.DONE);
        task2.setType(TaskType.TASK);

        fileBackedTasksManager.createEpic(epic1);   // id = 1
        epic1.setType(TaskType.EPIC);
        fileBackedTasksManager.createSubtask(subtask11); // id = 2
        subtask11.setType(TaskType.SUBTASK);
        epic1.setIdToSubtask(subtask11.getId());
        subtask11.setIdToEpic(epic1.getId());
        fileBackedTasksManager.createSubtask(subtask12); //id = 3
        subtask12.setType(TaskType.SUBTASK);
        epic1.setIdToSubtask(subtask12.getId());
        subtask12.setIdToEpic(epic1.getId());
        fileBackedTasksManager.updateEpic(epic1);
        fileBackedTasksManager.createEpic(epic2); // id = 4
        epic2.setType(TaskType.EPIC);
        fileBackedTasksManager.createSubtask(subtask21); // id = 5
        subtask21.setType(TaskType.SUBTASK);

        epic2.setIdToSubtask(subtask21.getId());
        subtask21.setIdToEpic(epic2.getId());
        fileBackedTasksManager.createSubtask(subtask22); // id = 6
        subtask22.setType(TaskType.SUBTASK);

        epic2.setIdToSubtask(subtask22.getId());
        subtask22.setIdToEpic(epic2.getId());
        fileBackedTasksManager.updateEpic(epic2);
        fileBackedTasksManager.createTask(task1); // id = 7
        task1.setType(TaskType.TASK);

        fileBackedTasksManager.createTask(task2); // id = 8
        task2.setType(TaskType.TASK);

        fileBackedTasksManager.getTask(7);
        fileBackedTasksManager.getTask(8);
        fileBackedTasksManager.getEpic(1);
        fileBackedTasksManager.getSubtask(5);
        fileBackedTasksManager.deleteById(7);



        // загрузка из файла
        FileBackedTasksManager fileBackedTasksManager2
                = loadFromFile(new File("C:\\Users\\Данисимо\\dev\\java-kanban\\src\\history.csv"));
        assigningId(fileBackedTasksManager2);

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


    private void save() throws IOException {
        List<Task> allTask = new ArrayList<>(); // Список со всеми задачами
        allTask.addAll(getListTask());
        allTask.addAll(getListEpic());
        allTask.addAll(getListSubtask());
        String tableOfContents = "id,type,name,status,description,epic\n"; // Не до конца понимаю куда нужно было вынести переменную, но выносить ее вне метода, мне кажется, не верным решением.
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dir.toFile())); // запись в файл
        bufferedWriter.write(tableOfContents);
        for (Task task : allTask) {
            bufferedWriter.write(taskToString(task));   // записываются все задачи
        }
        try {
            bufferedWriter.write(historyToString(getInMemoryHistoryManager())); // запись истории, если она пустая, то игнорируется
        } catch (NullPointerException ignored) {
            System.out.println("История пуста.");
        }
        bufferedWriter.close();
    }

    private String taskToString(Task task) { // запись задач в строку
        String id = String.valueOf(task.getIdToEpic());
        return task.getId() + "," + task.getType() + "," + task.getTitle() + "," + task.getStatus()
                + "," + task.getDescription() + "," + id + "\n";
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
        if (type.equals(TaskType.SUBTASK)) {
            task.setIdToEpic(Integer.parseInt(split[5]));
        }
        return task;
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
            } else if (file.getListEpic().contains(file.getEpic(integer))) {
            } else {
                file.getSubtask(integer);
            }
        }
        file.save();
    }
}