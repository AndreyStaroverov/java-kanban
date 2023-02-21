package control;

import Servers.KVServer;
import Tasks.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    private KVServer kvServer;

    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = (HttpTaskManager) Managers.getDefault();
    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
    }

    @Test
    void checkCorrectlyMethodSaveToServer() throws IOException, InterruptedException {
        final int id = 0;
        final Task task = new Task("Задача 0", "Описание задачи...", StatusOfTask.NEW, id, TypeOfTask.TASK,
                LocalDateTime.of(2022, 10,10,12,30,0), 30);
        final Task taskTwo = new Task("Задача 1", "Описание задачи...1", StatusOfTask.NEW, 1, TypeOfTask.TASK,
                LocalDateTime.of(2022, 10,10,15,30,0), 30);
        final String stringExpected = "0,TASK,Задача 0,NEW,Описание задачи...,2022-10-10T12:30,PT30M,2022-10-10T13:00/";

        taskManager.createTask(task);
        final Task taskTest = taskManager.getTaskById(0);

        final String result = taskManager.kvTaskClient.load("manager");

        assertEquals(stringExpected, result);
        assertEquals(task.getName(), taskTest.getName());
        assertEquals(task.getId(), taskTest.getId());

        taskManager.createTask(taskTwo);
        final Task taskTestTwo = taskManager.getTaskById(1);
        final String exp = "0,1";
        final String res = taskManager.kvTaskClient.load("history");

        assertEquals(2, taskManager.getHistory().size());
        assertEquals(exp, res);
    }

    @Test
    void checkCorrectlyLoadFromServer() {
        final int id = 0;
        final Task task = new Task("Задача 0", "Описание задачи...", StatusOfTask.NEW, id, TypeOfTask.TASK,
                LocalDateTime.of(2022, 10,10,12,30,0), 30);
        final int epid = 1;
        final Epic epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        final int sid = 2;
        final Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.NEW, epid, sid,
                TypeOfTask.SUBTASK);
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask, epid);
        final Task taskTest = taskManager.getTaskById(0);
        final Epic epicTest = taskManager.getEpicById(1);
        final Subtask subtaskTest = taskManager.getSubTaskById(2);

        final List<Task> historyTest = taskManager.getHistory();
        final List<Task> priorityTest = taskManager.getPrioritizedTasks();

        HttpTaskManager manager = HttpTaskManager.loadFromServer(taskManager.kvTaskClient); // новый менеджер

        final List<Task> historyTestLoad = manager.getHistory();
        final List<Task> priorityTestLoad = manager.getPrioritizedTasks();

        final Task taskTestLoad = taskManager.getTaskById(0);
        final Epic epicTestLoad = taskManager.getEpicById(1);
        final Subtask subtaskTestLoad = taskManager.getSubTaskById(2);

        assertEquals(taskTest.getName(), taskTestLoad.getName());
        assertEquals(taskTest.getId(), taskTestLoad.getId());
        assertEquals(taskTest.getDuration(), taskTestLoad.getDuration());

        assertEquals(epicTest.getName(), epicTestLoad.getName());
        assertEquals(epicTest.getId(), epicTestLoad.getId());
        assertEquals(epicTest.getDuration(), epicTestLoad.getDuration());

        assertEquals(subtaskTest.getName(), subtaskTestLoad.getName());
        assertEquals(subtaskTest.getId(), subtaskTestLoad.getId());
        assertEquals(subtaskTest.getDuration(), subtaskTestLoad.getDuration());

        assertEquals(historyTest.size(), historyTestLoad.size());
        assertEquals(historyTest.get(0).getId(), historyTestLoad.get(0).getId());
        assertEquals(historyTest.get(2).getId(), historyTestLoad.get(2).getId());

        assertEquals(priorityTest.size(), priorityTestLoad.size());
        assertEquals(priorityTest.get(0).getId(), priorityTestLoad.get(0).getId());
        assertEquals(priorityTest.get(1).getId(), priorityTestLoad.get(1).getId());
    }
}