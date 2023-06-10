package task;

public class Subtask extends Task {

    public Subtask(String title, String description, TasksStatus status) {
        super(title, description, status);
    }

    public Subtask(Task task, Integer idEpic) {
        super(task.getTitle(), task.getDescription(), task.getStatus(), task.getId());
        this.setIdToEpic(idEpic);
    }
}