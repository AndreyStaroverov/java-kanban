package Tasks;

public class Subtask extends Task{

protected int epicId;

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

    @Override
    public Integer getId() {
        return super.getId();
    }
}
