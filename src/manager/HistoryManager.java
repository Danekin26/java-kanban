package manager;

import task.Task;

import java.util.List;

public interface HistoryManager {
    Integer numOfTasks = 10;

    void add(Task task);

    List<Task> getHistory();
}