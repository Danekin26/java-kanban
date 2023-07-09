package manager.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.FileBackedTasksManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;
import java.util.Map;

public class HttpTaskManager extends FileBackedTasksManager {
    private final Gson gson = new Gson();
    private final KVTaskClient kvTaskClient;

    public HttpTaskManager(String url) {
        this.kvTaskClient = new KVTaskClient(url);
    }

    @Override
    public void save(){
        String tasks = gson.toJson(getIdTask());
        kvTaskClient.put("task", tasks);
        String epics = gson.toJson(getIdEpic());
        kvTaskClient.put("epic", epics);
        String subtasks = gson.toJson(getIdSubtask());
        kvTaskClient.put("subtask", subtasks);
        String history = gson.toJson(getListHistory());
        kvTaskClient.put("history", history);
        String prior = gson.toJson(getPrioritizedTasks());
        kvTaskClient.put("prioritized", prior);
    }

    public void load(){
        String tasks = kvTaskClient.load("task");
        if(!tasks.isEmpty()) {
            Map<Integer, Task> taskMap = gson.fromJson(tasks, new TypeToken<Map<Integer, Task>>(){}.getType());
            getIdTask().putAll(taskMap);
        }
        String epics = kvTaskClient.load("epic");
        if(!epics.isEmpty()){
            Map<Integer, Epic> epicMap = gson.fromJson(epics, new TypeToken<Map<Integer, Epic>>(){}.getType());
            getIdEpic().putAll(epicMap);
        }
        String subtask = kvTaskClient.load("subtask");
        if(!subtask.isEmpty()) {
            Map<Integer, Subtask> subtaskMap = gson.fromJson(subtask, new TypeToken<Map<Integer, Subtask>>(){}.getType());
            getIdSubtask().putAll(subtaskMap);
        }
        String history = kvTaskClient.load("history");
        if(!history.equals("null")){
            List<Task> historyList = gson.fromJson(history, new TypeToken<List<Task>>(){}.getType());
            getListHistory().addAll(historyList);
        }
        String prior = kvTaskClient.load("prioritized");
        if(!prior.isEmpty()) {
            List<Task> prioritList = gson.fromJson(prior, new TypeToken<List<Task>>(){}.getType());
            getPrioritizedTasks().addAll(prioritList);
        }


    }
}
