package Tasks;

import control.InMemoryTaskManager;
import control.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    TaskManager taskManager = new InMemoryTaskManager();
    protected Epic epic;
    protected int epid;

    @BeforeEach
    public void beforeEach(){
        epid = 0;
        epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        taskManager.createEpic(epic);
    }

    @Test
    void setEpicStatusEmptyList() {
        List<Subtask> subtasks = epic.getSubtaskList();
        epic.setEpicStatus();

        assertTrue(subtasks.isEmpty());
        assertEquals(StatusOfTask.NEW, epic.getStatus());
    }

    @Test
    void setEpicStatusInProgress() {
        Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.IN_PROGRESS, epid, 1,
                TypeOfTask.SUBTASK);
        taskManager.createSubtask(subtask,epid);
        epic.setEpicStatus();

        assertEquals(StatusOfTask.IN_PROGRESS, epic.getStatus());
    }
    @Test
    void setEpicStatusNEW() {
        Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.NEW, epid, 1,
                TypeOfTask.SUBTASK);
        taskManager.createSubtask(subtask,epid);
        epic.setEpicStatus();

        assertEquals(StatusOfTask.NEW, epic.getStatus());
    }

    @Test
    void setEpicStatusDone() {
        Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.DONE, epid, 1,
                TypeOfTask.SUBTASK);
        taskManager.createSubtask(subtask,epid);
        epic.setEpicStatus();

        assertEquals(StatusOfTask.DONE, epic.getStatus());
    }

    @Test
    void setEpicStatusNewAndDone() {
        Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.NEW, epid, 1,
                TypeOfTask.SUBTASK);
        taskManager.createSubtask(subtask,epid);
        Subtask subtaskDone = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.DONE, epid, 2,
                TypeOfTask.SUBTASK);
        taskManager.createSubtask(subtaskDone,epid);
        epic.setEpicStatus();

        assertEquals(StatusOfTask.IN_PROGRESS, epic.getStatus());
    }
}
