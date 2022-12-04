package sprint3;

public class Task {
    public String name;
    public String nameof;
    private StatusOfTask statusOfTask;
    protected Integer taskid;
    public Task(String name, String nameof, StatusOfTask statusOfTask) {
        this.name = name;
        this.nameof = nameof;
        this.statusOfTask = statusOfTask;
    }
    public Task(String name, String nameof, StatusOfTask statusOfTask, int id) {
        this.name = name;
        this.nameof = nameof;
        this.statusOfTask = statusOfTask;
        this.taskid = id;
    }
    public Task(){
    }
    public StatusOfTask getStatus() {

        return statusOfTask;
    }

    public void setStatus(StatusOfTask status) {
        this.statusOfTask = status;
    }


    public void setId(Integer taskid) {
        this.taskid = taskid;
    }


    public Integer getId() {
        return taskid;
    }
}