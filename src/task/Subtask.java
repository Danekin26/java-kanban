package task;

public class Subtask extends Task {
    private int idToEpic;

    public Subtask(String title, String description, TasksStatus status) {
        super(title, description, status);
    }

    public Subtask(String title, String description, TasksStatus status, int id, int idToEpic) {
        super(title, description, status, id);
        this.idToEpic = idToEpic;
    }


    public int getIdToEpic() {
        return idToEpic;
    }

    public void setIdToEpic(int idToEpic) {
        this.idToEpic = idToEpic;
    }
}