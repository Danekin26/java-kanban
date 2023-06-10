package task;

public class Task { // Отдельная задача
    private String title;
    private String description;
    private TasksStatus status;
    private int id;
    private TaskType type;
    private Integer idToEpic; // принял решение перенести из подзадачи это поле в класс-родитель, в эпиках и задачах записывать здесь null


    public Task(String title, String description, TasksStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Task(String title, String description, TasksStatus status, int id) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TasksStatus getStatus() {
        return status;
    }

    public void setStatus(TasksStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIdToEpic() {
        return idToEpic;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public void setIdToEpic(int id) {
        this.idToEpic = id;
    }
}