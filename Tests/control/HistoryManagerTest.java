package control;

import Tasks.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    protected HistoryManager hm;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    @BeforeEach
    public void beforeEach(){

         hm = new InMemoryHistoryManager();

         task = new Task("tASK1", "Check Test for Manager", StatusOfTask.NEW, 0, TypeOfTask.TASK,
                LocalDateTime.of(2022, 10,10,12,30,0), 30);

         final int epid = 1;
         epic = new Epic("Epic1", epid, StatusOfTask.NEW, TypeOfTask.EPIC);

         subtask = new Subtask("Sub1", "Do subtask for EPic", StatusOfTask.NEW, epid, 2,
                TypeOfTask.SUBTASK);
    }

    @Test
    void notEmptyHistoryOfTasks() {

        hm.add(task);
        hm.add(epic);
        List<Task> history  = hm.getHistory();
        boolean check = history.isEmpty();

        assertFalse(check);
    }

    @Test
    void canAddTask() {

        assertEquals(0, hm.getHistory().size());

        hm.add(task);

        assertEquals(1, hm.getHistory().size());
        assertEquals(task, hm.getHistory().get(0));
    }

    @Test
    void canRemoveFirstTask() {

         hm.add(task);
         hm.add(epic);
         hm.add(subtask);

        assertEquals(3, hm.getHistory().size());

        hm.remove(0);

        assertEquals(2, hm.getHistory().size(), "Несовпадение по количеству в истории");
        assertEquals(epic, hm.getHistory().get(0), "Не совпадение по новой 1 задаче в истории");
    }

    @Test
    void canRemoveMiddleTask() {

        hm.add(task);
        hm.add(epic);
        hm.add(subtask);

        assertEquals(3, hm.getHistory().size());

        hm.remove(1);

        assertEquals(2, hm.getHistory().size(), "Несовпадение по количеству в истории");
        assertEquals(task, hm.getHistory().get(0), "Не совпадение по новой 1 задаче в истории");
        assertEquals(subtask, hm.getHistory().get(1), "Не совпадение по новой 1 задаче в истории");
    }

    @Test
    void canRemoveLastTask() {

        hm.add(task);    //id 0
        hm.add(epic);    //id 1
        hm.add(subtask); //id 2

        assertEquals(3, hm.getHistory().size());

        hm.remove(2);

        assertEquals(2, hm.getHistory().size(), "Несовпадение по количеству в истории");
        assertEquals(task, hm.getHistory().get(0));
        assertEquals(epic, hm.getHistory().get(1), "Не совпадение по  2 задаче в истории");
    }

    @Test
    void canBeDoubleTaskInHistory() {

        assertEquals(0, hm.getHistory().size());

        hm.add(epic);
        hm.add(task);
        hm.add(epic);

        assertEquals(2, hm.getHistory().size(), "Произошло дублирование, epic не удалился");
        assertEquals(task, hm.getHistory().get(0), "Произошло дублирование, epic не удалился");
        assertEquals(epic, hm.getHistory().get(1), "Неверный тип задачи или расхождение");
    }

}