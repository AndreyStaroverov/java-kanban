package Servers;

import Tasks.*;
import com.google.gson.Gson;
import control.HttpTaskManager;
import control.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    private static HttpTaskServer httpTaskServer;
    private static KVServer kvServer;
    private Gson gson = Managers.getGson();
    private static HttpTaskManager httpTaskManager;

    public static void setTasksSet() {
        final int id = 0;
        final Task task = new Task("Задача 0", "Описание задачи...", StatusOfTask.NEW, id, TypeOfTask.TASK,
                LocalDateTime.of(2022, 10, 10, 12, 30, 0), 30);
        final int epid = 1;
        final Epic epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        final int sid = 2;
        final Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.NEW, epid, sid,
                TypeOfTask.SUBTASK);
        httpTaskManager.createTask(task);
        httpTaskManager.createEpic(epic);
        httpTaskManager.createSubtask(subtask, epid);
        final Task taskTest = httpTaskManager.getTaskById(0);
        final Epic epicTest = httpTaskManager.getEpicById(1);
        final Subtask subtaskTest = httpTaskManager.getSubTaskById(2);
    }

    @BeforeEach
    public void beforeEach() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        httpTaskManager = (HttpTaskManager) httpTaskServer.taskManager;
    }

    @AfterEach
    public void afterEach() {
        httpTaskServer.stop();
        kvServer.stop();
    }


    @Test
    void testingPOSTTask() throws IOException, InterruptedException {

        final int id = 0;
        final Task task = new Task("Задача 0", "Описание задачи...", StatusOfTask.NEW, id, TypeOfTask.TASK,
                LocalDateTime.of(2022, 10, 10, 12, 30, 0), 30);
        String json = gson.toJson(task);

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create("http://localhost:8080/tasks/task/"))
                .version(HttpClient.Version.HTTP_1_1).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body();
        Task taskTest = httpTaskManager.getTaskById(0);

        assertNotNull(body, "List is Empty");
        assertEquals(task.getId(), taskTest.getId(), "Не совпадают");
        assertEquals(task.getName(), taskTest.getName());
    }

    @Test
    void testingPOSTEpic() throws IOException, InterruptedException {


        final int epid = 0;
        final Epic epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        String json = gson.toJson(epic);

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create("http://localhost:8080/tasks/epic/"))
                .version(HttpClient.Version.HTTP_1_1).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body();
        Epic taskTest = httpTaskManager.getEpicById(0);

        assertNotNull(body, "List is Empty");
        assertEquals(epic.getId(), taskTest.getId(), "Не совпадают");
        assertEquals(epic.getName(), taskTest.getName());
    }

    @Test
    void testingPOSTSubtask() throws IOException, InterruptedException {

        final int epid = 0;
        final Epic epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        final int sid = 1;
        final Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.NEW, epid, sid,
                TypeOfTask.SUBTASK);
        String jsonE = gson.toJson(epic);
        String jsonS = gson.toJson(subtask);

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonE))
                .uri(URI.create("http://localhost:8080/tasks/epic/"))
                .version(HttpClient.Version.HTTP_1_1).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        HttpClient clientS = HttpClient.newBuilder().build();
        HttpRequest requestS = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonS))
                .uri(URI.create("http://localhost:8080/tasks/subtask/"))
                .version(HttpClient.Version.HTTP_1_1).build();
        HttpResponse<String> responseS = clientS.send(requestS, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseS.statusCode());

        String body = response.body();
        Subtask taskTest = httpTaskManager.getSubTaskById(1);

        assertNotNull(body, "List is Empty");
        assertEquals(subtask.getId(), taskTest.getId(), "Не совпадают");
        assertEquals(subtask.getName(), taskTest.getName());
    }

    @Test
    void testingGETpriorityzTasks() throws IOException, InterruptedException {

        setTasksSet();

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/tasks/")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body();
        String exp = gson.toJson(httpTaskManager.getPrioritizedTasks());

        assertFalse(body.isEmpty(), "List is Empty");
        assertEquals(exp, body, "Не совпадают");

    }

    @Test
    void testingGETHistory() throws IOException, InterruptedException {
        setTasksSet();

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/tasks/history")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body();
        String exp = gson.toJson(httpTaskManager.getHistory());

        assertFalse(body.isEmpty(), "List is Empty");
        assertEquals(exp, body, "Не совпадают");
    }

    @Test
    void testingGETTasks() throws IOException, InterruptedException {

        setTasksSet();

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/tasks/task/")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body();
        String exp = gson.toJson(httpTaskManager.getTasks());

        assertFalse(body.isEmpty(), "List is Empty");
        assertEquals(exp, body, "Не совпадают");

    }

    @Test
    void testingGETEpics() throws IOException, InterruptedException {
        setTasksSet();

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/tasks/epic/")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body();
        String exp = gson.toJson(httpTaskManager.getEpics());

        assertFalse(body.isEmpty(), "List is Empty");
        assertEquals(exp, body, "Не совпадают");
    }

    @Test
    void testingGETSubtasks() throws IOException, InterruptedException {

        setTasksSet();

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/tasks/subtask/")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body();
        String exp = gson.toJson(httpTaskManager.getSubTasks());

        assertFalse(body.isEmpty(), "List is Empty");
        assertEquals(exp, body, "Не совпадают");
    }

    @Test
    void testingGETEpicSubtasks() throws IOException, InterruptedException {

        setTasksSet();

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/tasks/subtask/epic/1")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body();
        String exp = gson.toJson(httpTaskManager.getEpicSubtasks(1));

        assertFalse(body.isEmpty(), "List is Empty");
        assertEquals(exp, body, "Не совпадают");
    }

    @Test
    void testingGETTaskById() throws IOException, InterruptedException {

        setTasksSet();

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/tasks/task/0")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body();
        String exp = gson.toJson(httpTaskManager.getTaskById(0));

        Task task = gson.fromJson(response.body(), Task.class);
        Task taskTwo = httpTaskManager.getTaskById(0);

        assertFalse(body.isEmpty(), "List is Empty");
        assertEquals(exp, body, "Не совпадают");
        assertEquals(task.getId(), taskTwo.getId());
        assertEquals(task.getName(), taskTwo.getName());
    }

    @Test
    void testingGETEpicById() throws IOException, InterruptedException {

        setTasksSet();

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/tasks/epic/1")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body();

        Epic task = gson.fromJson(response.body(), Epic.class);
        Epic taskTwo = httpTaskManager.getEpicById(1);

        assertFalse(body.isEmpty(), "List is Empty");
        assertEquals(task.getId(), taskTwo.getId());
        assertEquals(task.getName(), taskTwo.getName());
    }

    @Test
    void testingGETSubtaskById() throws IOException, InterruptedException {

        setTasksSet();

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/tasks/subtask/2")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body();

        Subtask task = gson.fromJson(response.body(), Subtask.class);
        Subtask taskTwo = httpTaskManager.getSubTaskById(2);

        assertFalse(body.isEmpty(), "List is Empty");
        assertEquals(task.getId(), taskTwo.getId());
        assertEquals(task.getName(), taskTwo.getName());
    }

    @Test
    void testingDELETEall() throws IOException, InterruptedException {

        setTasksSet();

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(URI.create("http://localhost:8080/tasks/task/")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body();

        assertTrue(body.isEmpty(), "List is Empty");
        assertTrue(httpTaskManager.getTasks().isEmpty());
        assertTrue(httpTaskManager.getEpics().isEmpty());
        assertTrue(httpTaskManager.getSubTasks().isEmpty());
    }

    @Test
    void testingDELETETask() throws IOException, InterruptedException {

        setTasksSet();

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(URI.create("http://localhost:8080/tasks/task/0")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body();

        assertTrue(body.isEmpty(), "List is Empty");
        assertTrue(httpTaskManager.getTasks().isEmpty());
        assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                httpTaskManager.getTaskById(0);
            }
        });
    }

    @Test
    void testingDELETEEpic() throws IOException, InterruptedException {

        setTasksSet();

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(URI.create("http://localhost:8080/tasks/epic/1")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body();

        assertTrue(body.isEmpty(), "List is Empty");
        assertTrue(httpTaskManager.getEpics().isEmpty());
        assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                httpTaskManager.getEpicById(1);
            }
        });
        assertTrue(httpTaskManager.getSubTasks().isEmpty());
        assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                httpTaskManager.getSubTaskById(2);
            }
        });
    }

    @Test
    void testingDELETESubtask() throws IOException, InterruptedException {

        setTasksSet();

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(URI.create("http://localhost:8080/tasks/subtask/2")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body();

        assertTrue(body.isEmpty(), "List is Empty");
        assertTrue(httpTaskManager.getSubTasks().isEmpty());
        assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                httpTaskManager.getSubTaskById(2);
            }
        });
        assertTrue(httpTaskManager.getEpicSubtasks(1).isEmpty());
    }

    @Test
    void testNewChecker() throws IOException, InterruptedException {

        setTasksSet();

        final int id = 0;
        final Task task = new Task("Задача 0", "Описание задачи...", StatusOfTask.IN_PROGRESS, id, TypeOfTask.TASK,
                LocalDateTime.of(2022, 10, 10, 12, 30, 0), 30);
        String json = gson.toJson(task);

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create("http://localhost:8080/tasks/task/"))
                .version(HttpClient.Version.HTTP_1_1).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String body = response.body();
        Task taskTest = httpTaskManager.getTaskById(0);

        assertNotNull(body, "List is Empty");
        assertEquals(task.getId(), taskTest.getId(), "Не совпадают");
        assertEquals(task.getName(), taskTest.getName());
        assertNotEquals(task.getStatus(), taskTest.getStatus());

    }

}