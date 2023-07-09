package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import manager.Managers;
import manager.TaskManager;
import manager.server.HttpTaskServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private static HttpTaskServer server;
    private Gson gson;
    private static TaskManager taskManager;
    private static final Task task = new Task("Task1", "Description", TasksStatus.NEW, 1, TaskType.TASK,
            LocalDateTime.of(2023, 7, 26, 12, 0), 600);
    private static final Epic epic = new Epic("Epic1", "Duration1", TasksStatus.NEW, 2, TaskType.EPIC);
    private static final Subtask subtask1 = new Subtask("Subtask1", "Duration1", TasksStatus.NEW, 3, 2,
            TaskType.SUBTASK, LocalDateTime.of(2022, 7, 26, 12, 0), 900);
    private static final Subtask subtask2 = new Subtask("Subtask2", "Duration2", TasksStatus.NEW, 4, 2,
            TaskType.SUBTASK, LocalDateTime.of(2022, 7, 27, 12, 0), 900);

    @BeforeEach
    void initiate() throws IOException {
        taskManager = Managers.getServerManager();
        server = new HttpTaskServer(taskManager);

        if (epic.getIdToSubtask().size() == 0) {
            epic.setIdToSubtask(3);
            epic.setIdToSubtask(4);
        }

        taskManager.createTask(task);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createEpic(epic);


        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gson = gsonBuilder.create();
        server.start();
    }

    @AfterEach
    void stopServer() {
        server.stop(0);
    }

    @Test
    public void getTaskListTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> list = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());

        assertNotNull(list, "Список задач не возвращается.");
        assertEquals(taskManager.getListTask().size(), 1);
    }

    @Test
    public void getEpicListTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> list = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());

        assertNotNull(list, "Список эпиков не возвращается.");
        assertEquals(taskManager.getListEpic().size(), 1);
    }

    @Test
    public void getSubtaskListTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> list = gson.fromJson(response.body(), new TypeToken<List<Subtask>>() {}.getType());

        assertNotNull(list, "Список подзадач не возвращается.");
        assertEquals(taskManager.getListSubtask().size(), 2);
    }

    @Test
    public void checkingToGetTaskByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String taskTest = response.body();
        Gson gson1 = new Gson();
        String taskFinal = gson1.toJson(task);

        assertNotNull(taskTest, "Задача не считана.");
        assertEquals(taskTest, taskFinal);

    }

    @Test
    public void checkingToGetEpicByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String epicTest = response.body();
        Gson gson1 = new Gson();
        String epicFinal = gson1.toJson(epic);

        assertNotNull(epicTest, "Задача не считана.");
        assertEquals(epicTest, epicFinal);
    }

    @Test
    public void checkingToGetSubtaskByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String subtaskTest = response.body();
        Gson gson1 = new Gson();
        String subtaskFinal = gson1.toJson(subtask1);

        assertNotNull(subtaskTest, "Задача не считана.");
        assertEquals(subtaskTest, subtaskFinal);
    }

    @Test
    public void checkingToGetSubtaskInEpicTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic/subtask/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> list = gson.fromJson(response.body(), new TypeToken<List<Subtask>>() {}.getType());

        assertNotNull(list, "Список истории задач не возвращается.");
        assertEquals(taskManager.getListOfTaskInEpic(epic).size(), 2);
    }

    @Test
    public void getYourBrowsingHistoryTest() throws IOException, InterruptedException {
        taskManager.getTask(1);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> list = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());

        assertNotNull(list, "Список истории задач не возвращается.");
        assertEquals(taskManager.getListHistory().size(), 1);
    }

    @Test
    public void getAnyTaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/alltasks/?id=4");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String subtaskTest = response.body();
        Gson gson1 = new Gson();
        String subtaskFinal = gson1.toJson(subtask2);

        assertNotNull(subtaskTest, "Задача не считана.");
        assertEquals(subtaskTest, subtaskFinal);
    }

    @Test
    public void getTasksSortedByPriorityTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> list = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());

       assertNotNull(list, "Список отсортированных задач не возвращается.");
       assertEquals(taskManager.getPrioritizedTasks().size(), list.size());
    }

    @Test
    public void createTaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        String taskJson = gson.toJson(task);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void createEpicTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String epicJson = gson.toJson(epic);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(epicJson);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void createSubtaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String subtaskJson = gson.toJson(subtask1);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(subtaskJson);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void updateTaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        String taskJson = gson.toJson(task);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void updateEpicTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=2");
        String epicJson = gson.toJson(epic);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(epicJson);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void updateSubtaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        String subtaskJson = gson.toJson(subtask1);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(subtaskJson);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void deleteAllTaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String tasks = gson.toJson(response.body());

        assertEquals(tasks, "\"Задачи успешно удалены.\"");
    }

    @Test
    public void deleteAllEpicTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String epics = gson.toJson(response.body());


        assertEquals(epics, "\"Эпики успешно удалены.\"");
    }

    @Test
    public void deleteAllSubtaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String subtasks = gson.toJson(response.body());

        assertEquals(subtasks, "\"Подзадачи успешно удалены.\"");
    }


    @Test
    public void deleteByIdAnyTasksTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String tasks = gson.toJson(response.body());

        assertEquals(tasks, "\"Задача успешно удалена.\"");
    }
}