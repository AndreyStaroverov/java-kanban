import java.util.ArrayList;

public class Epic extends Task{
   public StatusOfTask statusOfTask1;
    private ArrayList<Subtask> subtaskList = new ArrayList<>();

    public Epic() {
    }

    public Epic (String name, ArrayList<Subtask> subtaskList, StatusOfTask statusOfTask) {
        this.name = name;
        this.subtaskList = subtaskList;
        this.statusOfTask1 = statusOfTask;
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
        ArrayList<String> status1 = new ArrayList<>();
        for (int i = 0; i < subtaskList.size(); i++) {
            String status = String.valueOf(subtaskList.get(i).getStatus());
            status1.add(status);
        }
        if (status1.contains("NEW") && status1.contains("DONE")) {
            statusOfTask1 = StatusOfTask.IN_PROGRESS;
        } else if (status1.contains("NEW") || subtaskList.isEmpty()) {
            statusOfTask1 = StatusOfTask.NEW;
        } else if (status1.contains("DONE")) {
            statusOfTask1 = StatusOfTask.DONE;
        } else {
            statusOfTask1 = null;
        }
    }
}