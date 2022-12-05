package Tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtaskList = new ArrayList<>();


    public Epic (String name, ArrayList<Subtask> subtaskList, int id) {
        super(name);
        this.subtaskList = subtaskList;
        this.taskid = id;
    }
    public Epic(String name, StatusOfTask statusOfTask){
        super(name);
        this.statusOfTask = statusOfTask;
    }

    public ArrayList<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(Subtask subtask) {
        subtaskList.add(subtask);
    }
    public void setEpicStatus() {
        ArrayList<Enum> subtaskStatuses = new ArrayList<>();
        for (int i = 0; i < subtaskList.size(); i++) {
            Enum status = subtaskList.get(i).getStatus();
            subtaskStatuses.add(status);
        }
            if (subtaskStatuses.contains(StatusOfTask.NEW) && subtaskStatuses.contains(StatusOfTask.DONE)) {
                statusOfTask = StatusOfTask.IN_PROGRESS;
            } else if (subtaskStatuses.contains(StatusOfTask.NEW) || subtaskList.isEmpty()) {
                statusOfTask = StatusOfTask.NEW;
            } else if (subtaskStatuses.contains(StatusOfTask.DONE)) {
                statusOfTask = StatusOfTask.DONE;
            } else {
                statusOfTask = null;
        }

    }
}