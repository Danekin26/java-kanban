package task;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Task { // Отдельная задача
    private String title;
    private String description;
    private TasksStatus status;
    private int id;
    private TaskType type;
    private Integer idToEpic;
    private LocalDateTime startTime;
    private long duration;
    private LocalDateTime endTime;

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Task(String title, String description, TasksStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Task(String title, String description, TasksStatus status, LocalDateTime startTime, long duration) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        endTime = startTime.plusSeconds(duration);
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

    public Task(String title, String description, TasksStatus status, int id, TaskType type) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.id = id;
        this.type = type;
    }

    public Task(String title, String description, TasksStatus status, int id, TaskType type,
                LocalDateTime startTime, long duration) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.id = id;
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
        endTime = startTime.plusSeconds(duration);
    }

    public Task(String title, String description, TasksStatus status,  TaskType type,
                LocalDateTime startTime, long duration) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
        endTime = startTime.plusSeconds(duration);
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public LocalDateTime getStartTime(){
        return startTime;
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

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}