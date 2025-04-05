package todo.service;

import db.Database;
import db.exception.InvalidEntityException;
import todo.entity.Task;

public class TaskService {

    public static void setAsCompleted(int taskId) throws InvalidEntityException {

        Task task = (Task) Database.get(taskId);

        task.status = Task.Status.Completed;

        Database.update(task);
    }
}
