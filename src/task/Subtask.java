package task;

import java.time.LocalDateTime;

public class Subtask extends Task {

    public Subtask(String title, String description, TasksStatus status) {
        super(title, description, status);
    }

    public Subtask(Task task, Integer idEpic) {
        super(task.getTitle(), task.getDescription(), task.getStatus(), task.getId());
        this.setIdToEpic(idEpic);
        this.setStartTime(task.getStartTime());
        this.setDuration(task.getDuration());
    }

    public Subtask(String title, String description, TasksStatus status, int id) {
        super(title, description, status, id);
    }
    public Subtask(String title, String description, TasksStatus status, int id, Integer idEpic, TaskType type) {
        super(title, description, status, id);
        this.setIdToEpic(idEpic);
        setType(type);
    }
    public Subtask(String title, String description, TasksStatus status, int id, Integer idEpic, TaskType type,
                   LocalDateTime startTime, long duration) {
        super(title, description, status, id);
        this.setIdToEpic(idEpic);
        setType(type);
        this.setStartTime(startTime);
        this.setDuration(duration);
    }

    public Subtask(String title, String description, TasksStatus status, LocalDateTime startTime, long duration) {
        super(title, description, status);
        this.setStartTime(startTime);
        this.setDuration(duration);
    }

}