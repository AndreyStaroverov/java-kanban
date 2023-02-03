package control;

import Tasks.*;
import exception.ManagerSaveException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    protected final File file;

    protected FileBackedTasksManager(File file) {
        this.file = file;
    }


    protected void save() {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,epic \n");
            for (Task task : super.getTasks()) {
                String ts = toString(task);
                fileWriter.write(ts + "\n");
            }
            for (Task epic : super.getEpics()) {
                String ts = toString(epic);
                fileWriter.write(ts + "\n");
            }
            for (Task subtask : super.getSubTasks()) {
                String ts = toString(subtask);
                fileWriter.write(ts + "\n");
            }
            fileWriter.write("\n");
            fileWriter.write(historyToString(getHistoryManager()));

        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubTaskById(int id) {
        Subtask task = super.getSubTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic task = super.getEpicById(id);
        save();
        return task;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask, Integer epicId) {
        super.createSubtask(subtask, epicId);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(int epicId, Subtask subtask) {
        super.updateSubtask(epicId, subtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fbm = new FileBackedTasksManager(file);
        try (FileReader reader = new FileReader(file)) {
            BufferedReader br = new BufferedReader(reader);
            int lastId = 0;
            br.readLine();

            while (br.ready()) {
                String line = br.readLine();
                if (line.isEmpty()) {
                    List<Integer> history = fbm.historyFromString(br.readLine());
                    for (Integer i : history) {
                        if (fbm.getTaskMap().containsKey(i)) {
                            fbm.getHistoryManager().add(fbm.getTaskMap().get(i));
                        }
                        if (fbm.getEpicMap().containsKey(i)) {
                            fbm.getHistoryManager().add(fbm.getEpicMap().get(i));
                        }
                        if (fbm.getSubTaskMap().containsKey(i)) {
                            fbm.getHistoryManager().add(fbm.getSubTaskMap().get(i));
                        }
                    }
                } else {
                    lastId = fbm.fromString(line).getId();
                }
            }
            fbm.setId(lastId + 1);
        } catch (IOException e) {
            throw new ManagerSaveException("Error in read file");
        }
        return fbm;
    }


    public static String toString(Task task) {
        switch (task.getType()) {
            case SUBTASK:
                Subtask subtask = (Subtask) task;
                String taskToString = task.getId() + "," +
                        task.getType() + "," +
                        task.getName() + "," +
                        task.getStatus() + "," +
                        task.getDescription() + "," +
                        subtask.getEpicId();
                return taskToString;
            case TASK:
                taskToString = task.getId() + "," +
                        task.getType() + "," +
                        task.getName() + "," +
                        task.getStatus() + "," +
                        task.getDescription();
                return taskToString;
            case EPIC:
                taskToString = task.getId() + "," +
                        task.getType() + "," +
                        task.getName() + "," +
                        task.getStatus();
                return taskToString;
            default:
                throw new IllegalArgumentException("Argument is invalid or null");
        }
    }

    protected Task fromString(String value) {
        String[] tsk = value.split(",");
        Integer id = Integer.parseInt(tsk[0]);
        StatusOfTask st = StatusOfTask.getStatus(tsk[3]);
        TypeOfTask tp = TypeOfTask.getType(tsk[1]);
        switch (tp) {
            case SUBTASK:
                int epid = Integer.parseInt(tsk[5]);
                Subtask subtask = new Subtask(tsk[2], tsk[4], st, epid, id, tp);
                super.setSubtaskMap(subtask);
                super.getEpicMap().get(epid).getSubtaskList().add(subtask);
                super.getEpicMap().get(epid).setEpicStatus();
                return subtask;
            case TASK:
                Task task = new Task(tsk[2], tsk[4], st, id, tp);
                super.setTaskMap(task);
                return task;
            case EPIC:
                Epic epic = new Epic(tsk[2], id, st, tp);
                super.setEpicMap(epic);
                return epic;
            default:
                throw new IllegalArgumentException("Argument is invalid or null");
            }
    }

   protected static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        List<String> hs = new ArrayList<>();
        for (Task t : history) {
            String id = t.getId().toString();
            hs.add(id);
        }
        String hist = String.join(",", hs);
        return hist;
    }

    protected static List<Integer> historyFromString(String value) {
        List<Integer> historyFromString = new ArrayList<>();
        String[] hst = value.split(",");
        for (String s : hst) {
            Integer el = Integer.parseInt(s);
            historyFromString.add(el);
        }
        return historyFromString;
    }
}
