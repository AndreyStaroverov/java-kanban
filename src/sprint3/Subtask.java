package sprint3;

public class Subtask extends Task{
private int epicId;
private  int Id;

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public Integer getId() {
        return Id;
    }

    public void setSubtaskId(int subtaskId) {
        this.Id = subtaskId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public Subtask (String name, String nameof, StatusOfTask statusOfTask, int epicId) {
        super(name, nameof,statusOfTask);
        this.epicId = epicId;
    }
    public Subtask (String name, String nameof, StatusOfTask statusOfTask, int epicId, int id) {
        super(name, nameof,statusOfTask);
        this.epicId = epicId;
        this.taskid = id;
    }
}
