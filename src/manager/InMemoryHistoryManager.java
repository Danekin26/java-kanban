package manager;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    final Integer HISTORYLIMIT = 10;
    List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() >= HISTORYLIMIT) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
