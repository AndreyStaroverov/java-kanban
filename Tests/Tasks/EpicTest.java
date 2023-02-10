package Tasks;

import control.InMemoryTaskManager;
import control.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
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

    @Test
    void correctlyEpicStartTime(){
        final Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.DONE, epid, 1,
                TypeOfTask.SUBTASK, LocalDateTime.of(2022,10,10,12,50,0),
                Duration.ofMinutes(15));
        taskManager.createSubtask(subtask,epid);

        final Subtask subtaskTwo = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.DONE, epid, 1,
                TypeOfTask.SUBTASK, LocalDateTime.of(2022,10,10,14,50,0),
                Duration.ofMinutes(15));
        taskManager.createSubtask(subtaskTwo,epid);

        final LocalDateTime expected = LocalDateTime.of(2022,10,10,12,50,0);
        final LocalDateTime startTime = epic.getStartTime();

       assertEquals(expected, startTime);
       assertNotNull(startTime);
    }

    @Test
    void correctlyEpicEndTime(){
        final Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.DONE, epid, 1,
                TypeOfTask.SUBTASK, LocalDateTime.of(2022,10,10,12,50,0),
                Duration.ofMinutes(15));
        taskManager.createSubtask(subtask,epid);

        final Subtask subtaskTwo = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.DONE, epid, 1,
                TypeOfTask.SUBTASK, LocalDateTime.of(2022,10,10,14,50,0),
                Duration.ofMinutes(15));
        taskManager.createSubtask(subtaskTwo,epid);

        final LocalDateTime expected = LocalDateTime.of(2022,10,10,15,05,0);
        final LocalDateTime endTime = epic.getEndTime();

        assertEquals(expected, endTime);
        assertNotNull(endTime);
    }

    @Test
    void correctlyEpicDuration(){
        final Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.DONE, epid, 1,
                TypeOfTask.SUBTASK, LocalDateTime.of(2022,10,10,12,50,0),
                Duration.ofMinutes(15));
        taskManager.createSubtask(subtask,epid);

        final Subtask subtaskTwo = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.DONE, epid, 1,
                TypeOfTask.SUBTASK, LocalDateTime.of(2022,10,10,14,50,0),
                Duration.ofMinutes(15));
        taskManager.createSubtask(subtaskTwo,epid);

        final Duration expected = Duration.ofMinutes(30);
        final Duration duration = epic.getDuration();

        assertNotNull(duration);
        assertEquals(expected, duration);
    }
}
