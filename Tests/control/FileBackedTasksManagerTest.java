package control;

import Tasks.*;
import exception.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    File file = new File(".\\resources\\HistorySaverTest.csv");

    @BeforeEach
    public void beforeEach() {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            throw new ManagerSaveException();
        }
        taskManager = new FileBackedTasksManager(file);
    }

    @Test
    void canTaskToString() {
        final int id = 0;
        final Task task = new Task("Задача 0", "Описание задачи...", StatusOfTask.NEW, id, TypeOfTask.TASK,
                LocalDateTime.of(2022, 10,10,12,30,0), 30);
        taskManager.createTask(task);
        taskManager.getTaskById(0);

        final String taskString = taskManager.toString(task);
        final String stringTaskexp = "0,TASK,Задача 0,NEW,Описание задачи...,2022-10-10T12:30,PT30M,2022-10-10T13:00";

        assertEquals(stringTaskexp, taskString);
        assertNotNull(taskString);
        assertFalse(taskManager.getHistory().isEmpty());
        assertFalse(taskManager.getTasks().isEmpty());
    }

    @Test
    void stringToTask() {
        final int id = 0;
        final Task task = new Task("Задача 0", "Описание задачи...", StatusOfTask.NEW, id, TypeOfTask.TASK,
                LocalDateTime.of(2022, 10,10,12,30,0), 30);
        final String stringTask = "0,TASK,Задача 0,NEW,Описание задачи...,2022-10-10T12:30,PT30M,2022-10-10T13:00";
        taskManager.createTask(task);
        taskManager.getTaskById(0);

        final Task taskNew = taskManager.fromString(stringTask);

        assertEquals(task.getId(), taskNew.getId());
        assertEquals(task.getType(), taskNew.getType());
        assertEquals(task.getStatus(), taskNew.getStatus());
        assertEquals(task.getName(), taskNew.getName());
        assertFalse(taskManager.getHistory().isEmpty());
        assertFalse(taskManager.getTasks().isEmpty());
        assertNotNull(taskNew);
    }

    @Test
    void shouldBeCorrectlySave() {
        final int id = 0;
        final Task task = new Task("Задача 0", "Описание задачи...", StatusOfTask.NEW, id, TypeOfTask.TASK,
                LocalDateTime.of(2022, 10,10,12,30,0), 30);
        final String stringExpected = "0,TASK,Задача 0,NEW,Описание задачи...,2022-10-10T12:30,PT30M,2022-10-10T13:00";

        taskManager.createTask(task);
        taskManager.getTaskById(0);

        String lineCheck;
        try (FileReader reader = new FileReader(file)) {
            BufferedReader br = new BufferedReader(reader);
            br.readLine();
            lineCheck = br.readLine();
        } catch (IOException e) {
            throw new ManagerSaveException();
        }

        assertNotEquals("", lineCheck);
        assertNotNull(lineCheck);
        assertEquals(stringExpected, lineCheck);
    }

    @Test
    void shouldBeCorrectlyLoad() {
        final int epid = 0;
        final Epic epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        taskManager.createEpic(epic);

        final int id = 1;
        final Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.NEW, epid, id,
                TypeOfTask.SUBTASK);
        taskManager.createSubtask(subtask, epid);

        taskManager.getEpicById(0);
        taskManager.getSubTaskById(1);

        FileBackedTasksManager fbtmTest = FileBackedTasksManager.loadFromFile(file);

        final Epic epicAfterLoad = fbtmTest.getEpicById(0);
        final Subtask subtaskAfterLoad = fbtmTest.getSubTaskById(1);

        assertFalse(fbtmTest.getHistory().isEmpty());
        assertNotEquals(0, fbtmTest.getHistory().size());

        assertEquals(epic.getId(), epicAfterLoad.getId());
        assertEquals(epic.getName(), epicAfterLoad.getName());
        assertEquals(epic.getStatus(), epicAfterLoad.getStatus());
        assertFalse(epicAfterLoad.getSubtaskList().isEmpty());

        assertEquals(subtask.getId(), subtaskAfterLoad.getId());
        assertEquals(subtask.getName(), subtaskAfterLoad.getName());
        assertEquals(subtask.getDescription(), subtaskAfterLoad.getDescription());
        assertEquals(subtask.getEpicId(), subtaskAfterLoad.getEpicId());

    }

    @Test
    void historyFromStringTest(){
        final int epid = 0;
        final Epic epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        taskManager.createEpic(epic);

        final int id = 1;
        final Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.NEW, epid, id,
                TypeOfTask.SUBTASK);
        taskManager.createSubtask(subtask, epid);

        String testStringTwo = "";

        FileBackedTasksManager fbtmTest = FileBackedTasksManager.loadFromFile(file);

        assertTrue(fbtmTest.getHistory().isEmpty());
    }

}
