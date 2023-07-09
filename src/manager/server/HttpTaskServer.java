package manager.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static manager.Managers.getServerManager;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new Gson();
    private static HttpServer httpServer;
    private static TaskManager taskManager;



    public static void main(String[] args) throws IOException {
        taskManager = getServerManager();
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new InquiryProcessor());
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        httpServer.start();
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.taskManager = taskManager;
        httpServer.createContext("/tasks", new InquiryProcessor());
    }

    public void start() {
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        httpServer.start();
    }

    public void stop(int delay){
        System.out.println("HTTP-сервер на " + PORT + " порту остановлен!");
        httpServer.stop(delay);
    }

    static class InquiryProcessor implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();

            switch (method) {
                case ("GET"):
                    requestForGetMethod(exchange);
                    break;
                case ("POST"):
                    requestForPostMethod(exchange);
                    break;
                case ("DELETE"):
                    requestForDeleteMethod(exchange);
                    break;

            }
        }

        private void requestForGetMethod(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String userInfo = exchange.getRequestURI().getQuery();
            String[] elementPath = path.split("/");

            if ((elementPath.length == 2)) {
                writeResponse(exchange, gson.toJson(taskManager.getPrioritizedTasks()), 200);
            }

            if (!((elementPath[2].equals("task") || elementPath[2].equals("epic") || elementPath[2].equals("subtask")
                    || elementPath[2].equals("alltasks") || elementPath[2].equals("history"))
                    && elementPath[1].equals("tasks"))) {
                writeResponse(exchange, "Проверьте правильность введенной ссылки.", 400);
            }

            if ((userInfo == null) && (elementPath.length == 3) && (elementPath[2].equals("task") ||
                    elementPath[2].equals("epic") || elementPath[2].equals("subtask"))) {
                if (elementPath[2].equals("task")) {
                    writeResponse(exchange, gson.toJson(taskManager.getListTask()), 200);
                } else if (elementPath[2].equals("epic")) {
                    writeResponse(exchange, gson.toJson(taskManager.getListEpic()), 200);
                } else {
                    writeResponse(exchange, gson.toJson(taskManager.getListSubtask()), 200);
                }
            }

            if ((userInfo != null) && (elementPath.length == 3) && (elementPath[2].equals("task") ||
                    elementPath[2].equals("epic") || elementPath[2].equals("subtask"))) {
                String[] splitUserInfo = userInfo.split("=");
                int id = Integer.parseInt(splitUserInfo[1]);
                if (elementPath[2].equals("task") && taskManager.getTask(id) != null) {
                    String asd = gson.toJson(taskManager.getTask(id));
                    System.out.println(asd);
                    writeResponse(exchange, gson.toJson(taskManager.getTask(id)), 200);
                } else if (elementPath[2].equals("epic") && taskManager.getEpic(id) != null) {
                    writeResponse(exchange, gson.toJson(taskManager.getEpic(id)), 200);
                } else if (elementPath[2].equals("subtask") && taskManager.getSubtask(id) != null) {
                    writeResponse(exchange, gson.toJson(taskManager.getSubtask(id)), 200);
                } else {
                    writeResponse(exchange, "Введенный id не соответствует типу задачи.", 400);
                }
            }

            if ((userInfo == null) && (elementPath.length == 3) && elementPath[2].equals("history")) {
                writeResponse(exchange, gson.toJson(taskManager.getListHistory()), 200);
            }

            if ((userInfo != null) && (elementPath.length == 3) && (elementPath[2].equals("alltasks"))) {
                String[] splitUserInfo = userInfo.split("=");
                int id = Integer.parseInt(splitUserInfo[1]);
                if (taskManager.getAnyTask(id) != null) {
                    writeResponse(exchange, gson.toJson(taskManager.getAnyTask(id)), 200);
                } else {
                    writeResponse(exchange, "Задачи с введенным id не существует.", 400);
                }
            }

            if ((userInfo != null) && (elementPath.length == 4) && (elementPath[2].equals("epic"))
                    && (elementPath[3].equals("subtask"))) {
                String[] splitUserInfo = userInfo.split("=");
                int id = Integer.parseInt(splitUserInfo[1]);
                if (taskManager.getAnyTask(id).getType() == TaskType.EPIC) {
                    writeResponse(exchange,
                            gson.toJson(taskManager.getListOfTaskInEpic((Epic) taskManager.getAnyTask(id))),
                            200);
                } else {
                    writeResponse(exchange, "Epic с введенным id не существует.", 400);
                }
            }
        }

        private void requestForPostMethod(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String[] elementPath = path.split("/");
            InputStream inputStream = exchange.getRequestBody();
            StringBuilder buildBody = new StringBuilder(new String(inputStream.readAllBytes(), DEFAULT_CHARSET));
            String userInfo = exchange.getRequestURI().getQuery();
            int id = -1;

            if (userInfo != null) {
                String[] splitUserInfo = userInfo.split("=");
                id = Integer.parseInt(splitUserInfo[1]);
            }
            while (buildBody.indexOf("\n") != -1) {
                buildBody.deleteCharAt(buildBody.indexOf("\n"));
            }
            while (buildBody.indexOf("\t") != -1) {
                buildBody.deleteCharAt(buildBody.indexOf("\t"));
            }
            if (buildBody.charAt(0) == '[') {
                buildBody.deleteCharAt(0);
            }
            if (buildBody.charAt(buildBody.length() - 1) == ']') {
                buildBody.deleteCharAt(buildBody.length() - 1);
            }
            String finalBody = buildBody.toString();

            if (!((elementPath[1].equals("tasks")) && (elementPath[2].equals("task") || elementPath[2].equals("epic")
                    || elementPath[2].equals("subtask")) && elementPath.length == 3)) {
                writeResponse(exchange, "Проверьте правильность введенной ссылки.", 400);
            }

            if (userInfo == null) {
                if (elementPath[2].equals("task")) {
                    Task task = gson.fromJson(finalBody, Task.class);
                    task.setType(TaskType.TASK);
                    taskManager.updateTask(task);
                    writeResponse(exchange, "Задача успешно добавлена. id задачи = " + task.getId(), 200);
                } else if (elementPath[2].equals("epic")) {
                    Epic epic = gson.fromJson(finalBody, Epic.class);
                    epic.setType(TaskType.EPIC);
                    taskManager.updateTask(epic);
                    writeResponse(exchange, "Задача успешно добавлена. id эпика = " + epic.getId(), 200);
                } else {
                    Subtask subtask = gson.fromJson(finalBody, Subtask.class);
                    subtask.setType(TaskType.SUBTASK);
                    taskManager.updateTask(subtask);
                    writeResponse(exchange, "Задача успешно добавлена. id подзадачи = " + subtask.getId(),
                            200);
                }
            } else if (taskManager.getAnyTask(id) != null) {
                if (elementPath[2].equals("task")) {
                    Task task = gson.fromJson(finalBody, Task.class);
                    task.setId(id);
                    task.setType(TaskType.TASK);
                    taskManager.createTask(task);
                    writeResponse(exchange, "Задача успешно обновлена.", 200);
                } else if (elementPath[2].equals("epic")) {
                    Epic epic = gson.fromJson(finalBody, Epic.class);
                    epic.setId(id);
                    epic.setType(TaskType.EPIC);
                    taskManager.createEpic(epic);
                    writeResponse(exchange, "Эпик успешно обновлен.", 200);
                } else {
                    Subtask subtask = gson.fromJson(finalBody, Subtask.class);
                    subtask.setId(id);
                    subtask.setType(TaskType.SUBTASK);
                    taskManager.createSubtask(subtask);
                    writeResponse(exchange, "Подзадача успешно обновлена.", 200);
                }
            } else {
                writeResponse(exchange,
                        "Произошла ошибка при создании или обновлении задач. Повторите попытку.", 400);
            }
        }

        private void requestForDeleteMethod(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String[] elementPath = path.split("/");
            String userInfo = exchange.getRequestURI().getQuery();

            if (!((elementPath[1].equals("tasks")) && (elementPath[2].equals("tasks") || elementPath[2].equals("task")
                    || elementPath[2].equals("epic") || elementPath[2].equals("subtask")))) {
                writeResponse(exchange, "Проверьте правильность введенной ссылки.", 400);
            }

            if (userInfo == null) {
                if (elementPath[2].equals("task")) {
                    taskManager.deleteTask();
                    writeResponse(exchange, "Задачи успешно удалены.", 200);
                } else if (elementPath[2].equals("epic")) {
                    taskManager.deleteEpic();
                    writeResponse(exchange, "Эпики успешно удалены.", 200);
                } else {
                    taskManager.deleteSubtask();
                    writeResponse(exchange, "Подзадачи успешно удалены.", 200);
                }
            } else {
                String[] splitUserInfo = userInfo.split("=");
                int id = Integer.parseInt(splitUserInfo[1]);
                if (taskManager.getAnyTask(id).getType().equals(TaskType.TASK)) {
                    taskManager.deleteById(id);
                    writeResponse(exchange, "Задача успешно удалена.", 200);
                } else if (taskManager.getAnyTask(id).getType().equals(TaskType.EPIC)) {
                    taskManager.deleteById(id);
                    writeResponse(exchange, "Эпик успешно удален.", 200);
                } else if (taskManager.getAnyTask(id).getType().equals(TaskType.SUBTASK)) {
                    taskManager.deleteById(id);
                    writeResponse(exchange, "Подзадача успешно удалена.", 200);
                } else {
                    writeResponse(exchange, "Задач с введенным id не существует.", 400);
                }
            }
        }
    }

    private static void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
        exchange.sendResponseHeaders(responseCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
        exchange.close();
    }
}
