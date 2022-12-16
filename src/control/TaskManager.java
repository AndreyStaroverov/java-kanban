package control;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask, Integer epicId);

    List<Task> getTasks();

    List<Subtask> getSubTasks();

    List<Epic> getEpics();

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(int epicId, Subtask subtask);

    void deleteTask(int id);

    void deleteAllTasks();

    void deleteSubtask(int id);

    void deleteEpic(int id);

    Task getTaskById(int id);

    Subtask getSubTaskById(int id);

    Epic getEpicById(int id);

    List<Subtask> getEpicSubtasks(int epicId);

    List<Task> getHistory();

}

