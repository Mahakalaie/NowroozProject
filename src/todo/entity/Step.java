package todo.entity;

import db.Entity;

import java.util.Date;

public class Step extends Entity {

    public enum Status{
        NotStarted,
        Completed;
    }

    public static final int STEP_ENTITY_CODE = 2;

    public String title;
    public Status status;
    public int taskRef;

    public Step(String title, Status status, int taskRef)
    {
        this.title = title;
        this.status = status;
        this.taskRef = taskRef;
    }

    @Override
    public int getEntityCode() { return STEP_ENTITY_CODE; }

    @Override
    public Step copy()
    {
        Step copyStep = new Step(title, status, taskRef);
        copyStep.id = id;

        return copyStep;
    }
}
