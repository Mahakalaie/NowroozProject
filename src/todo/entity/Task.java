package todo.entity;

import db.Entity;
import db.Trackable;
import example.Document;

import java.util.Date;

public class Task extends Entity implements Trackable {

    public enum Status{
        NotStarted,
        InProgress,
        Completed;
    }

    public static final int TASK_ENTITY_CODE = 1;

    public String title;
    public String description;
    public Date dueDate;
    public Status status;
    private Date creationDate;
    private Date lastModificationDate;

    public Task(String title, String description, Date dueDate, Status status)
    {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
    }

    @Override
    public Task copy()
    {
        Task copyTask = new Task(title, description, dueDate, status);
        copyTask.id = id;

        if(creationDate != null)
            copyTask.creationDate = new Date(creationDate.getTime());

        if(lastModificationDate != null)
            copyTask.lastModificationDate = new Date(lastModificationDate.getTime());


        return copyTask;
    }

    @Override
    public int getEntityCode() { return TASK_ENTITY_CODE; }

    @Override
    public void setCreationDate(Date date)
    {
        creationDate = date;
    }

    @Override
    public Date getCreationDate()
    {
        return creationDate;
    }

    @Override
    public void setLastModificationDate(Date date)
    {
        lastModificationDate = date;
    }

    @Override
    public Date getLastModificationDate()
    {
        return lastModificationDate;
    }

}


