package control;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.*;

public class Manager {
    private Integer id = 0;

    private final HashMap<Integer, Task> taskMap = new HashMap<>(); // Integer = Id
    private final HashMap<Integer, Epic> epicMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subTaskMap = new HashMap<>();

    public void createTask(Task task) {
        int taskId = id;
        task.setId(id);
        taskMap.put(taskId, task);
        id++;
    }

    public void createEpic(Epic epic) {
        int epicId = id;
        epic.setId(id);
        epicMap.put(epicId, epic);
        id++;
    }

    public void createSubtask(Subtask subtask, Integer epicId) {
        int subTaskId = id;
        subtask.setId(id);
        subTaskMap.put(subTaskId, subtask);
        id++;
        epicMap.get(epicId).getSubtaskList().add(subtask);
        epicMap.get(epicId).setEpicStatus();
    }
    public List<Task> getTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        for (int k: taskMap.keySet()){
            allTasks.add(taskMap.get(k));
        }
       return allTasks;
    }
    public List<Subtask> getSubTasks() {
        ArrayList<Subtask> allSubTasks = new ArrayList<>();
        for (int k : subTaskMap.keySet()) {
            allSubTasks.add(subTaskMap.get(k));
        }
        return allSubTasks;
    }
    public List<Epic> getEpics() {
        ArrayList<Epic> allEpics = new ArrayList<>();
        for (int k : epicMap.keySet()) {
            allEpics.add(epicMap.get(k));
        }
        return allEpics;
    }
    public void updateTask(Task task) {
        taskMap.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epicMap.put(epic.getId(), epic);
    }

    public void updateSubtask(int epicId, Subtask subtask) {
        subTaskMap.put(subtask.getId(), subtask);
        for (int k = 0; k < epicMap.get(epicId).getSubtaskList().size(); k++) {
            boolean check;
            check = (epicMap.get(epicId).getSubtaskList().get(k).getId()) == (subtask.getId());
            if (check) {
                epicMap.get(epicId).getSubtaskList().remove(k);
            }
        }
        epicMap.get(epicId).getSubtaskList().add(subtask);
        epicMap.get(epicId).setEpicStatus();
    }

    public void deleteTask(int id) {
        taskMap.remove(id);
    }

    public void deleteAllTasks(){
        epicMap.clear();
        taskMap.clear();
        subTaskMap.clear();
        for (Integer k: epicMap.keySet()) {
            epicMap.get(k).getSubtaskList().clear();
        }
    }

    public void deleteSubtask(int id) {
        int eId = subTaskMap.get(id).getEpicId();
        for (int k = 0; k < epicMap.get(eId).getSubtaskList().size(); k++) {
            boolean check;
            check = (epicMap.get(eId).getSubtaskList().get(k).getId()) == id;
            if (check) {
                epicMap.get(eId).getSubtaskList().remove(k);
            }
        }
        subTaskMap.remove(id);
        epicMap.get(eId).setEpicStatus();
    }

    public void deleteEpic(int id) {
        ArrayList<Integer> delete = new ArrayList<>();
        for (int i: subTaskMap.keySet()) {
            int eId = subTaskMap.get(i).getEpicId();
            if (id == eId) {
                delete.add(i);
            }
        }
        for (Integer integer : delete) {
            subTaskMap.remove(integer);
        }

        epicMap.remove(id);
    }

    public Task getTaskById(int id) {
        return taskMap.get(id);
    }
    public Subtask getSubTaskById(int id) {
        return subTaskMap.get(id);
    }
    public Epic getEpicById(int id) {
        return epicMap.get(id);
    }
    public ArrayList<Subtask> getEpicSubtasks(int epicId){
        if (epicMap.containsKey(epicId)){
            ArrayList<Subtask> subTasksList = epicMap.get(epicId).getSubtaskList();
            return subTasksList;
        }  else {
            return null;
        }
    }

}
