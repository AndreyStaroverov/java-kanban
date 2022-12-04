package sprint3;

import java.util.ArrayList;

public class Epic extends Task{
   public StatusOfTask statusOfTask1;
    private ArrayList<Subtask> subtaskList = new ArrayList<>();

    public Epic() {
    }

    public Epic (String name, ArrayList<Subtask> subtaskList, int id) {
        this.name = name;
        this.subtaskList = subtaskList;
        this.taskid = id;
    }
    public Epic(String name, StatusOfTask statusOfTask){
        this.name = name;
        this.statusOfTask1 = statusOfTask;
    }

    public ArrayList<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(Subtask subtask) {
        subtaskList.add(subtask);
    }
    public void setEpicStatus() {
        ArrayList<Enum> status1 = new ArrayList<>();
        for (int i = 0; i < subtaskList.size(); i++) {
            Enum status = subtaskList.get(i).getStatus();
            status1.add(status);
        }
            if (status1.contains(StatusOfTask.NEW) && status1.contains(StatusOfTask.DONE)) {
                statusOfTask1 = StatusOfTask.IN_PROGRESS;
            } else if (status1.contains(StatusOfTask.NEW) || subtaskList.isEmpty()) {
                statusOfTask1 = StatusOfTask.NEW;
            } else if (status1.contains(StatusOfTask.DONE)) {
                statusOfTask1 = StatusOfTask.DONE;
            } else {
                statusOfTask1 = null;
        }

    }
}