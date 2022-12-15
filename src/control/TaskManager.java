package control;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    public void createTask(Task task);

    public void createEpic(Epic epic);

    public void createSubtask(Subtask subtask, Integer epicId);

    public List<Task> getTasks();

    public List<Subtask> getSubTasks();

    public List<Epic> getEpics();

    public void updateTask(Task task);

    public void updateEpic(Epic epic);

    public void updateSubtask(int epicId, Subtask subtask);

    public void deleteTask(int id);

    public void deleteAllTasks();

    public void deleteSubtask(int id);

    public void deleteEpic(int id);

    public Task getTaskById(int id);

    public Subtask getSubTaskById(int id);

    public Epic getEpicById(int id);

    public ArrayList<Subtask> getEpicSubtasks(int epicId);

    public List<Task> getHistory();

}

