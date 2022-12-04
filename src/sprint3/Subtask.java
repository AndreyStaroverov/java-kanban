package sprint3;

public class Subtask extends Task{
private int epicId;

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }


    public Integer getEpicId() {
        return epicId;
    }

    public Subtask (String name, String description, StatusOfTask statusOfTask, int epicId) {
        super(name, description,statusOfTask);
        this.epicId = epicId;
    }
    public Subtask (String name, String description, StatusOfTask statusOfTask, int epicId, int id) {
        super(name, description,statusOfTask);
        this.epicId = epicId;
        this.taskid = id;
    }
}
