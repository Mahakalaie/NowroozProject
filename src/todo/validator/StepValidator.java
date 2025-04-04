package todo.validator;

import db.Database;
import db.Entity;
import db.Validator;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;

public class StepValidator implements Validator {

    @Override
    public void validate(Entity entity) throws InvalidEntityException {

        if( !(entity instanceof Step) )
            throw new IllegalArgumentException("This Entity is not a Step");

        Step step = (Step) entity;

        if( step.title == null || step.title.isEmpty())
            throw new InvalidEntityException("Title can't be null or empty");

        if(Database.get(step.taskRef) instanceof Task)
            return;
        throw new InvalidEntityException("this step doesn't belong to any task (the taskref is incorrect)");
    }
}
