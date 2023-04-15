package task;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> idToSubtask = new ArrayList<>();

    public ArrayList<Integer> getIdToSubtask() {
        return idToSubtask;
    }

    public void setIdToSubtask(int numIdSubtask) {
        this.idToSubtask.add(numIdSubtask);
    }

    public Epic(String title, String description) {
        super(title, description);
    }
}