package todo.service;

import db.Database;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;

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
            error = "Cannot find task with ID=" + taskRef + ".";
        }

        System.out.print("Title: ");
        String title = scanner.nextLine();
        if(title == null || "".equals(title))
            error = "Task title cannot be empty.";

        if(error == null) {
            Step step = new Step(title, Step.Status.NotStarted, taskRef);
            Database.add(step);
            System.out.println("Step saved successfully.\nID: " + step.id);
        }
        else{
            System.out.println("Cannot save step.\nError: " + error);
        }
    }
}
