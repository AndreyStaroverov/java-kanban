package Servers;


import Tasks.Epic;
import Tasks.StatusOfTask;
import Tasks.Subtask;
import Tasks.Task;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import control.Managers;
import control.TaskManager;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {

    public static final int PORT = 8080;

    private final HttpServer httpServer;
    private Gson gson;

    public TaskManager getTaskManager() {
        return taskManager;
    }

    private final TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this.taskManager = Managers.getDefault();
        gson = Managers.getGson();
        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", this::handleTasks);
    }


    private void handleTasks(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET":
                    if (Pattern.matches("^/tasks/$", path)) { //allTypeTasks
                        String response = gson.toJson(taskManager.getPrioritizedTasks());
                        sendText(httpExchange, response);
                        break;
                    }
                    if (Pattern.matches("^/tasks/history$", path)) { //history
                        String response = gson.toJson(taskManager.getHistory());
                        sendText(httpExchange, response);
                        break;
                    }
                    if (Pattern.matches("^/tasks/subtask/epic/\\d+$", path)) { //epicSubtasks
                        String pathId = path.replaceFirst("/tasks/subtask/epic/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getEpicSubtasks(id));
                            sendText(httpExchange, response);
                            break;
                        } else {
                            System.out.println("Incorrect ID");
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    }
                    if (Pattern.matches("^/tasks/task/$", path)) { //allTasks
                        String response = gson.toJson(taskManager.getTasks());
                        sendText(httpExchange, response);
                        break;
                    }
                    if (Pattern.matches("^/tasks/task/\\d+$", path)) { //getTaskbyId
                        String pathId = path.replaceFirst("/tasks/task/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getTaskById(id));
                            sendText(httpExchange, response);
                            break;
                        } else {
                            System.out.println("Incorrect ID");
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    }
                    if (Pattern.matches("^/tasks/epic/$", path)) { //allEpics
                        String response = gson.toJson(taskManager.getEpics());
                        sendText(httpExchange, response);
                        break;
                    }
                    if (Pattern.matches("^/tasks/epic/\\d+$", path)) { //getEpicById
                        String pathId = path.replaceFirst("/tasks/epic/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getEpicById(id));
                            sendText(httpExchange, response);
                            break;
                        } else {
                            System.out.println("Incorrect ID");
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    }
                    if (Pattern.matches("^/tasks/subtask/$", path)) { //allSubTasks
                        String response = gson.toJson(taskManager.getSubTasks());
                        sendText(httpExchange, response);
                        break;
                    }
                    if (Pattern.matches("^/tasks/subtask/\\d+$", path)) { //getSubTaskById
                        String pathId = path.replaceFirst("/tasks/subtask/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getSubTaskById(id));
                            sendText(httpExchange, response);
                            break;
                        } else {
                            System.out.println("Incorrect Sub Id");
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    }
                    break;
                case "POST":
                    if (Pattern.matches("^/tasks/epic/$", path)) {
                        String jsonString = new String(httpExchange.getRequestBody().readAllBytes());
                        httpExchange.getRequestBody().close();
                        Epic epic = gson.fromJson(jsonString, Epic.class);
                        try {
                            taskManager.getEpicById(epic.getId());
                            taskManager.updateEpic(epic);
                            System.out.println("You updated epicTask");
                        } catch (NullPointerException e) {
                            taskManager.createEpic(epic);
                            System.out.println("You created epicTask");
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    }
                    if (Pattern.matches("^/tasks/subtask/$", path)) {
                        String jsonString = new String(httpExchange.getRequestBody().readAllBytes());
                        httpExchange.getRequestBody().close();
                        Subtask subtask = gson.fromJson(jsonString, Subtask.class);
                        int id = subtask.getEpicId();
                        try {
                            taskManager.getSubTaskById(subtask.getId());
                            taskManager.updateSubtask(id, subtask);
                            System.out.println("You updated subTask");
                        } catch (NullPointerException e) {
                            taskManager.createSubtask(subtask, id);
                            System.out.println("You created subTask");
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    }
                    if (Pattern.matches("^/tasks/task/$", path)) {
                        String jsonString = new String(httpExchange.getRequestBody().readAllBytes());
                        httpExchange.getRequestBody().close();
                        Task task = gson.fromJson(jsonString, Task.class);
                        try {
                            taskManager.getTaskById(task.getId());
                            taskManager.updateTask(task);
                            System.out.println("You updated Task");
                        } catch (NullPointerException e) {
                            taskManager.createTask(task);
                            System.out.println("You created Task");
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    }

                    break;
                case "DELETE":
                    if (Pattern.matches("^/tasks/task/$", path)) { //deleteAllTasks
                        taskManager.deleteAllTasks();
                        System.out.println("You delete all type Tasks");
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    }
                    if (Pattern.matches("^/tasks/task/\\d+$", path)) { //deleteTaskbyId
                        String pathId = path.replaceFirst("/tasks/task/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.deleteTask(id);
                            System.out.println("Delete Task w/ ID = " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        } else {
                            System.out.println("Incorrect ID");
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    }
                    if (Pattern.matches("^/tasks/subtask/\\d+$", path)) { //deleteSubTaskbyId
                        String pathId = path.replaceFirst("/tasks/subtask/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.deleteSubtask(id);
                            System.out.println("Delete Task w/ ID = " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        } else {
                            System.out.println("Incorrect ID");
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    }
                    if (Pattern.matches("^/tasks/epic/\\d+$", path)) { //deleteEPicById
                        String pathId = path.replaceFirst("/tasks/epic/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.deleteEpic(id);
                            System.out.println("Delete Task w/ ID = " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        } else {
                            System.out.println("Incorrect ID");
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    }
                    break;
                default:
                    System.out.println("Expected GET/POST/DELETE methods");
                    httpExchange.sendResponseHeaders(405, 0);

            }
        } catch (Exception e) {
            e.printStackTrace();
            httpExchange.sendResponseHeaders(405, 0);
        } finally {
            httpExchange.close();
        }
    }

    protected int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    public void start() {
        System.out.println("Запускаем HTTP сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Server is stoped on port - " + PORT);
    }
}