package manager;

import manager.server.HttpTaskManager;

import java.nio.file.Path;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getFileBackedTasksManager(Path dir) {
        return new FileBackedTasksManager(dir);
    }

    public static TaskManager getServerManager() {
        HttpTaskManager manager = new HttpTaskManager("http://localhost:8087");
        return manager;
    }
}
