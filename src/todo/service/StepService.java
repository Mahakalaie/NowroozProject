package todo.service;

import db.Database;
import db.Entity;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;

import java.util.ArrayList;
import java.util.Scanner;

public class StepService {

    private static Scanner scanner = new Scanner(System.in);

    public static void saveStep(int taskRef, String title) throws InvalidEntityException {

        Step step = new Step(title, Step.Status.NotStarted, taskRef);

        Database.add(step);

    }

    public static void addStep() throws InvalidEntityException {
        String error = null;

        System.out.print("TaskID: ");
        int taskRef = Integer.parseInt(scanner.nextLine());
        try{
            if( ! (Database.get(taskRef) instanceof Task))
                error = "Cannot find task with ID=" + taskRef + ".";
        } catch (EntityNotFoundException e){
            error = "Cannot find entity with ID=" + taskRef + ".";
        }

        System.out.print("Title: ");
        String title = scanner.nextLine();
        if(title == null || "".equals(title))
            error = "Step title cannot be empty.";

        if(error == null) {
            Step step = new Step(title, Step.Status.NotStarted, taskRef);
            Database.add(step);
            System.out.println("Step saved successfully.\nID: " + step.id);
        }
        else{
            System.out.println("Cannot save step.\nError: " + error);
        }
    }

    public static void deleteStep(int id) throws InvalidEntityException{
        Database.delete(id);

        System.out.println("Entity with ID=" + id + " successfully deleted.");
    }

    public static void updateStep() throws InvalidEntityException {
        String error = null;

        System.out.print("ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Field: ");
        String field = scanner.nextLine();

        System.out.print("New Value: ");
        String newValue = scanner.nextLine();

        try {
            if( ! (Database.get(id) instanceof Step))
                error = "there is no step with ID=" + id;
        } catch (EntityNotFoundException e) {
            error = "There is no entity with ID=" + id ;
        }

        Step step = null;
        String oldValue = null;

        if(error == null) {

            step = (Step) Database.get(id);

            if("title".equalsIgnoreCase(field)){
                oldValue = ((Step) Database.get(id)).title;

                if(newValue == null || "".equals(newValue))
                    error = "Step title cannot be empty.";

                step.title = newValue;
            }

            else if("status".equalsIgnoreCase(field)){
                oldValue = ((Step) Database.get(id)).status.name();

                try{
                    step.status = Step.Status.valueOf(newValue);
                } catch (IllegalArgumentException e) {
                    error = "Invalid status, please check case and spelling.";
                }
            }

            else if("taskRef".equalsIgnoreCase(field)){
                error = "task reference cannot be changed.";
            }

            else{
                error = "There is no such field.please check spelling.";
            }
        }

        if(error == null) {
            Database.update(step);
            System.out.println("Successfully updated the step.");
            System.out.println("Field: " + field);
            System.out.println("Old Value: " + oldValue);
            System.out.println("New Value: " + newValue);
        }
        else {
            System.out.println("Cannot update step with ID=" + id + ".\nError: " + error);
        }

        if("status".equalsIgnoreCase(field) && error == null)
        {
            Task theTask = (Task) Database.get(step.taskRef) ;

            //if all steps of the task are completed the task should be completed:
            if(step.status == Step.Status.Completed)
            {
                boolean allStepsCompleted = true;

                ArrayList<Entity> allSteps = Database.getAll(Step.STEP_ENTITY_CODE);

                for(Entity entity: allSteps)
                {
                    int taskRef = ((Step) entity).taskRef;

                    if(taskRef != step.taskRef)
                        continue;

                    if(((Step) entity).status != Step.Status.Completed){
                        allStepsCompleted = false;
                        break;
                    }
                }

                if(allStepsCompleted){
                    theTask.status = Task.Status.Completed;
                    Database.update(theTask);
                }

                //if even one step is completed the task should be inprogress:
                if(theTask.status == Task.Status.NotStarted){
                    theTask.status = Task.Status.InProgress;
                    Database.update(theTask);

                }
            }
        }
    }
}
