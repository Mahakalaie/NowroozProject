package todo.validator;

import db.Entity;
import db.Validator;
import db.exception.InvalidEntityException;
import example.Human;
import todo.entity.Task;

public class TaskValidator extends Validator {

    @Override
    public void validate(Entity entity) throws InvalidEntityException {

        if( !(entity instanceof Task) )
            throw new IllegalArgumentException("This Entity is not a Task");

        Task task = (Task) entity;

        if( task.title == null || task.title.isEmpty())
            throw new InvalidEntityException("Title can't be null or empty");
    }
}
