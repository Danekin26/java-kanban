package test;

import manager.Managers;
import manager.TaskManager;
import manager.server.HttpTaskManager;
import manager.server.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    protected KVServer server;

    @BeforeEach
    public void initiate() throws IOException {
        server = new KVServer();
        server.start();
        manager = (HttpTaskManager) Managers.getServerManager();
    }

    @AfterEach
    public void stopServer() {
        server.stop(0);
    }

    @Test
    public void checkLoadFromServerTest() throws IOException {
        Task task = new Task("Task1", "Description", TasksStatus.NEW, 1, TaskType.TASK,
                LocalDateTime.of(2023, 7, 26, 12, 0), 600);
        Epic epic = new Epic("Epic1", "Duration1", TasksStatus.NEW, 2, TaskType.EPIC);
        Subtask subtask1 = new Subtask("Subtask1", "Duration1", TasksStatus.NEW, 3, 2,
                TaskType.SUBTASK, LocalDateTime.of(2022, 7, 26, 12, 0), 900);
        Subtask subtask2 = new Subtask("Subtask2", "Duration2", TasksStatus.NEW, 4, 2,
                TaskType.SUBTASK, LocalDateTime.of(2022, 7, 27, 12, 0), 900);

        manager.createTask(task);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createEpic(epic);

        manager.getTask(task.getId());
        manager.getSubtask(3);


        manager.load();

        Map<Integer, Subtask> subtaskMap = manager.getIdSubtask();
        Map<Integer, Task> taskMap = manager.getIdTask();
        Map<Integer, Epic> epicMap = manager.getIdEpic();
        List<Task> historyTask = manager.getListHistory();

        assertNotNull(subtaskMap);
        assertNotNull(taskMap);
        assertNotNull(epicMap);
        assertNotNull(historyTask);

        assertEquals(2, subtaskMap.size());
        assertEquals(1, taskMap.size());
        assertEquals(1, epicMap.size());
        assertEquals(2, historyTask.size());
    }

}