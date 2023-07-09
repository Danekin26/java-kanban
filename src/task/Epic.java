package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> idToSubtask = new ArrayList<>();
    //private LocalDateTime endTime;

    public Epic(String title, String description, TasksStatus status, int id, ArrayList<Integer> idToSubtask, TaskType type) {
        super(title, description, status, id);
        this.idToSubtask = idToSubtask;
        setType(type);
    }

    public Epic(String title, String description, TasksStatus status, int id, TaskType type) {
        super(title, description, status, id);
        setType(type);
    }

    public Epic(String title, String description, TasksStatus status, int id, ArrayList<Integer> idToSubtask,
                TaskType type, LocalDateTime startTime, long duration) {
        super(title, description, status, id);
        this.idToSubtask = idToSubtask;
        setType(type);
        this.setStartTime(startTime);
        this.setDuration(duration);
        setEndTime(this.getStartTime().plusSeconds(this.getDuration()));
    }

    public Epic(Task task) {
        super(task.getTitle(), task.getDescription(), task.getStatus(), task.getId());
        this.setStartTime(task.getStartTime());
        this.setDuration(task.getDuration());
        this.setEndTime(task.getEndTime());
    }

    public Epic(String title, String description) {
        super(title, description);
    }

    public ArrayList<Integer> getIdToSubtask() {
        return idToSubtask;
    }

    public void setIdToSubtask(int numIdSubtask) {
        this.idToSubtask.add(numIdSubtask);
    }

    public void deleteAllIdToSubtask() {
        idToSubtask.clear();
    }

    public void deleteIdSubtask(int id) {
        if (idToSubtask.contains(id)) idToSubtask.remove(idToSubtask.indexOf(id));
    }

    public void setSubtaskDuration(Subtask subtask) {
        if (getStartTime() == null) {
            setStartTime(subtask.getStartTime());
            setDuration(subtask.getDuration());
            setEndTime(getStartTime().plusSeconds(getDuration()));

        }
        if (subtask.getStartTime().isBefore(getStartTime())) {
            setStartTime(subtask.getStartTime());
            setDuration(subtask.getDuration() + getDuration());
        }
        if(getEndTime().isBefore(subtask.getStartTime().plusSeconds(subtask.getDuration()))){
            setEndTime(subtask.getStartTime().plusSeconds(subtask.getDuration()));
            setDuration(Duration.between(getStartTime(), getEndTime()).toSeconds());
        }
    }

    @Override
    public Integer getIdToEpic() {
        return null;
    }
}