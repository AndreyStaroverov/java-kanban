package control;

import Tasks.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest <T extends TaskManager> {

    protected T taskManager;

    @Test
    public void canCreateTask() {
        final int id = 0;
        Task task = new Task("tASK1", "Check Test for Manager", StatusOfTask.NEW, id, TypeOfTask.TASK,
                LocalDateTime.of(2022, 10,10,12,30,0), 30);
        taskManager.createTask(task);
        final Task taskFromManager = taskManager.getTaskById(0);

        assertNotNull(taskFromManager, "Task not created");
        assertEquals(task, taskFromManager);

        final List<Task> listOfTasks = taskManager.getTasks();
        boolean check = listOfTasks.isEmpty();

        assertFalse(check, "list is Empty");
        assertEquals(task, listOfTasks.get(0), "Not equals tasks");
        assertEquals(1, listOfTasks.size(), "expected < listTask.size");
    }

    @Test
    void canCreateEpic() {
        final int id = 0;
        Epic epic = new Epic("Epic1", id, StatusOfTask.NEW, TypeOfTask.EPIC);
        taskManager.createEpic(epic);
        final Epic epicFromManager = taskManager.getEpicById(0);

        assertNotNull(epicFromManager, "Task not created");
        assertEquals(epic, epicFromManager);

        final List<Epic> listOfEpics = taskManager.getEpics();
        boolean check = listOfEpics.isEmpty();

        assertFalse(check, "list is Empty");
        assertEquals(epic, listOfEpics.get(0), "Not equals tasks");
        assertEquals(1, listOfEpics.size(), "expected < listTask.size");
        assertEquals(0, epicFromManager.getSubtaskList().size(), "SubList is not empty");
    }

    @Test
    void canCreateSubtask() {
        final int epid = 0;
        Epic epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        taskManager.createEpic(epic);

        final int id = 1;
        Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.NEW, epid, id,
                TypeOfTask.SUBTASK);
        taskManager.createSubtask(subtask, epid);
        final Subtask subTaskFromManager = taskManager.getSubTaskById(1);

        assertNotNull(subTaskFromManager, "Task not created");
        assertEquals(subtask, subTaskFromManager);

        final List<Subtask> listOfsubTasks = taskManager.getSubTasks();
        boolean check = listOfsubTasks.isEmpty();

        assertFalse(check, "list is Empty");
        assertEquals(subtask, listOfsubTasks.get(0), "Not equals tasks");
        assertEquals(1, listOfsubTasks.size(), "expected < listTask.size");
        assertEquals(1, taskManager.getEpics().get(0).getSubtaskList().size(), "SubList epic error");
    }

    @Test
    void getTasks() {
        final List<Task> tasks = taskManager.getTasks();

        boolean check = tasks.isEmpty();
        assertTrue(check);

        Task task = new Task("tASK1", "Check Test for Manager", StatusOfTask.NEW, 0, TypeOfTask.TASK,
                LocalDateTime.of(2022, 10,10,12,30,0), 30);
        taskManager.createTask(task);

        final List<Task> tasksAfterCreate = taskManager.getTasks();
        boolean checkAfterCreate = tasksAfterCreate.isEmpty();

        assertFalse(checkAfterCreate, "list is Empty");
        assertEquals(1, tasksAfterCreate.size(), "expected < listTask.size");
        assertEquals(task, tasksAfterCreate.get(0),"Not equals tasks");
    }

    @Test
    void getSubTasks() {
        final List<Subtask> subtasks = taskManager.getSubTasks();
        boolean check = subtasks.isEmpty();
        assertTrue(check);

        final int epid = 0;
        Epic epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.NEW, 0, 1,
                TypeOfTask.SUBTASK);
        taskManager.createSubtask(subtask,epid);

        final List<Subtask> subtasksAfterCreate = taskManager.getSubTasks();
        boolean checkAfterCreate = subtasksAfterCreate.isEmpty();

        assertFalse(checkAfterCreate, "list is Empty");
        assertEquals(1, subtasksAfterCreate.size(), "expected < listTask.size");
        assertEquals(subtask, subtasksAfterCreate.get(0), "Not equals tasks");
    }

    @Test
    void getEpics() {
        final List<Epic> epics = taskManager.getEpics();
        boolean check = epics.isEmpty();
        assertTrue(check);

        int epid = 0;
        Epic epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        taskManager.createEpic(epic);

        final List<Epic> epicsAfterCreate = taskManager.getEpics();
        boolean checkAfterCreate = epicsAfterCreate.isEmpty();

        assertFalse(checkAfterCreate, "list is Empty");
        assertEquals(1, epicsAfterCreate.size(), "expected < listTask.size");
        assertEquals(epic, epicsAfterCreate.get(0), "Not equals tasks");
    }

    @Test
    void updateTask() {
        Task task = new Task("tASK1", "Check Test for Manager", StatusOfTask.NEW, 0, TypeOfTask.TASK,
                LocalDateTime.of(2022, 10,10,12,30,0), 30);
        taskManager.createTask(task);
        Task updateTask = new Task("tASK1", "Check Test for Manager", StatusOfTask.IN_PROGRESS, 0, TypeOfTask.TASK,
                LocalDateTime.of(2022, 10,10,12,30,0), 30);

        taskManager.updateTask(updateTask);

        assertNotEquals(task, taskManager.getTasks().get(0));
        assertEquals(task.getId(), taskManager.getTasks().get(0).getId());
        assertNotNull(taskManager.getTasks().get(0));
    }

    @Test
    void updateEpic() {
        final int epid = 0;
        Epic epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.IN_PROGRESS, epid, 1,
                TypeOfTask.SUBTASK);
        taskManager.createSubtask(subtask,epid);

        final List<Subtask> subTaskList =  taskManager.getEpicSubtasks(0);

        Epic updateEpic = new Epic("Epic1",subTaskList, epid);
        taskManager.updateEpic(updateEpic);

        assertNotEquals(epic, taskManager.getEpics().get(0), "Эпики одинаковые");
        assertEquals(epic.getId(), taskManager.getEpics().get(0).getId(), "Id not equals");
        assertNotNull(taskManager.getEpics().get(0), "Epic = null");
    }

    @Test
    void updateSubtask() {
        final int epid = 0;
        Epic epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        taskManager.createEpic(epic);
        final StatusOfTask epicStatus = epic.getStatus();

        Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.NEW, epid, 1,
                TypeOfTask.SUBTASK);
        taskManager.createSubtask(subtask,epid);
        Subtask updateSubtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.DONE, epid, 1,
                TypeOfTask.SUBTASK);
        taskManager.updateSubtask(0,updateSubtask);

        final List<Subtask> subList = taskManager.getEpicSubtasks(epid);
        boolean checkSubList = subList.isEmpty();

        assertFalse(checkSubList);
        assertEquals(1, subList.size());
        assertEquals(updateSubtask, subList.get(0));
        assertNotEquals(epicStatus, taskManager.getEpics().get(0).getStatus(), "Статусы совпадают " +
                "после обновления");
        assertNotEquals(subtask, taskManager.getSubTasks().get(0), "SubTask одинаковые");
        assertEquals(subtask.getId(), taskManager.getSubTasks().get(0).getId(), "Id not equals");
        assertNotNull(taskManager.getSubTasks().get(0), "SubTask = null");
    }

    @Test
    void deleteTask() {
        Task task = new Task("tASK1", "Check Test for Manager", StatusOfTask.NEW, 0, TypeOfTask.TASK,
                LocalDateTime.of(2022, 10,10,12,30,0), 30);
        taskManager.createTask(task);
        taskManager.deleteTask(0);

        assertEquals(0, taskManager.getTasks().size());

        boolean historyCheck = taskManager.getHistory().contains(task);

        assertFalse(historyCheck);
    }

    @Test
    void shouldBeExceptionDeleteTask() {
        Task task = new Task("tASK1", "Check Test for Manager", StatusOfTask.NEW, 0, TypeOfTask.TASK,
                LocalDateTime.of(2022, 10,10,12,30,0), 30);
        taskManager.createTask(task);

        try {
            taskManager.deleteTask(10);
        } catch (NullPointerException ex) {
            assertTrue(ex instanceof NullPointerException);
        }
    }

    @Test
    void deleteAllTasks() {
        Task task = new Task("tASK1", "Check Test for Manager", StatusOfTask.NEW, 0, TypeOfTask.TASK,
                LocalDateTime.of(2022, 10,10,12,30,0), 30);
        taskManager.createTask(task);

        final int epid = 1;
        Epic epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.NEW, epid, 2,
                TypeOfTask.SUBTASK);
        taskManager.createSubtask(subtask,epid);

        taskManager.deleteAllTasks();

        assertEquals(0, taskManager.getTasks().size());
        assertEquals(0, taskManager.getSubTasks().size());
        assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    void deleteSubtask() {
        final int epid = 0;
        Epic epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.DONE, epid, 1,
                TypeOfTask.SUBTASK);
        taskManager.createSubtask(subtask,epid);
        final StatusOfTask epicStatus = taskManager.getEpics().get(0).getStatus();

        taskManager.deleteSubtask(1);
        boolean check = taskManager.getHistory().contains(subtask);

        assertEquals(0, taskManager.getSubTasks().size());
        assertEquals(0, taskManager.getEpics().get(0).getSubtaskList().size());
        assertNotEquals(epicStatus, taskManager.getEpics().get(0).getStatus());
        assertEquals(StatusOfTask.NEW, taskManager.getEpics().get(0).getStatus());
        assertFalse(check);

    }

    @Test
    void shouldBeExceptionDeleteSubtask() {
        final int epid = 0;
        Epic epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.DONE, epid, 1,
                TypeOfTask.SUBTASK);
        taskManager.createSubtask(subtask,epid);

        try {
            taskManager.deleteSubtask(10);
        } catch (NullPointerException ex) {
            assertTrue(ex instanceof NullPointerException);
        }
    }

    @Test
    void deleteEpic() {
        final int epid = 0;
        Epic epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.DONE, epid, 1,
                TypeOfTask.SUBTASK);
        taskManager.createSubtask(subtask,epid);

        taskManager.deleteEpic(0);
        boolean check = taskManager.getHistory().contains(epic);

        assertFalse(check);
        assertEquals(0, taskManager.getEpics().size());
        assertEquals(0, taskManager.getSubTasks().size());
    }

    @Test
    void shouldBeExceptionDeleteEpic() {
        final int epid = 0;
        Epic epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        taskManager.createEpic(epic);

        try {
            taskManager.deleteEpic(10);
        } catch (NullPointerException ex) {
            assertTrue(ex instanceof NullPointerException);
        }
    }

    @Test
    void canWeGetTaskById() {
        Task task = new Task("tASK1", "Check Test for Manager", StatusOfTask.NEW, 0, TypeOfTask.TASK,
                LocalDateTime.of(2022, 10,10,12,30,0), 30);
        taskManager.createTask(task);

        final Task taskGet = taskManager.getTaskById(0);

       assertEquals(task, taskGet);
       assertEquals(1, taskManager.getHistory().size());

    }
    @Test
    public void IncorrectGetTaskById(){
        Task task = new Task("tASK1", "Check Test for Manager", StatusOfTask.NEW, 0, TypeOfTask.TASK,
                LocalDateTime.of(2022, 10,10,12,30,0), 30);
        taskManager.createTask(task);

        try {
            taskManager.getTaskById(1);
        } catch (NullPointerException ex) {
            assertTrue(ex instanceof NullPointerException);
        }
    }

    @Test
    void canWeGetSubTaskById() {
        final int epid = 0;
        Epic epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.DONE, epid, 1,
                TypeOfTask.SUBTASK);
        taskManager.createSubtask(subtask, epid);

        final Subtask subtaskGet = taskManager.getSubTaskById(1);

        assertEquals(subtask, subtaskGet);
        assertEquals(1, taskManager.getHistory().size());

        try {
            Subtask subtaskGetBad = taskManager.getSubTaskById(10);
        } catch (NullPointerException ex) {
            assertTrue(ex instanceof NullPointerException);
        }
    }

    @Test
    public void IncorrectGetSubtaskById(){
        final int epid = 0;
        Epic epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.DONE, epid, 1,
                TypeOfTask.SUBTASK);
        taskManager.createSubtask(subtask,epid);

        try {
            taskManager.getSubTaskById(10);
        } catch (NullPointerException ex) {
            assertTrue(ex instanceof NullPointerException);
        }
    }

    @Test
    void getEpicById() {
        final int epid = 0;
        Epic epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        taskManager.createEpic(epic);

        final Epic epicGet = taskManager.getEpicById(0);

        assertEquals(epic, epicGet);
        assertEquals(1, taskManager.getHistory().size());
        assertEquals(epic, taskManager.getHistory().get(0));
    }

    @Test
    public void IncorrectGetEpicById(){
        final int epid = 0;
        Epic epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        taskManager.createEpic(epic);

        try {
            taskManager.getEpicById(10);
        } catch (NullPointerException ex) {
            assertTrue(ex instanceof NullPointerException);
        }
    }

    @Test
    void CanGetEpicSubtasksIncorrectEpic() {
        final int epid = 0;
        Epic epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.DONE, epid, 1,
                TypeOfTask.SUBTASK);
        taskManager.createSubtask(subtask,epid);

        assertNull(taskManager.getEpicSubtasks(10), "Такой Эпик существует");
    }

    @Test
    void CanGetEpicSubtasks() {
        final int epid = 0;
        Epic epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);
        taskManager.createEpic(epic);

        assertEquals(0, taskManager.getEpicSubtasks(0).size());

        Subtask subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.DONE, epid, 1,
                TypeOfTask.SUBTASK);
        taskManager.createSubtask(subtask,epid);

        assertEquals(1, taskManager.getEpicSubtasks(0).size());
    }

    @Test
    void CanGetHistory() {
        final List<Task> history = taskManager.getHistory();

        assertEquals(0, history.size());

        Task task = new Task("tASK1", "Check Test for Manager", StatusOfTask.NEW, 0, TypeOfTask.TASK,
                LocalDateTime.of(2022, 10,10,12,30,0), 30);
        taskManager.createTask(task);
        final Task taskGet = taskManager.getTaskById(0);

        assertEquals(task, taskGet);
        assertEquals(1, taskManager.getHistory().size());
        assertEquals(task, taskManager.getHistory().get(0));
    }
}
