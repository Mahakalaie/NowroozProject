package todo.service;

import db.Database;
import db.Entity;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class TaskService {

    private static Scanner scanner = new Scanner(System.in);

    public static void setAsCompleted(int taskId) throws InvalidEntityException {

        Task task = (Task) Database.get(taskId);

        task.status = Task.Status.Completed;

        Database.update(task);
    }

    public static void addTask() throws InvalidEntityException {
        String error = null;

        System.out.print("Title: ");
        String title = scanner.nextLine();
        if(title == null || "".equals(title))
            error = "Task title cannot be empty.";

        System.out.print("Description: ");
        String description = scanner.nextLine();

        DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        dateformat.setLenient(false);
        System.out.print("Due date: ");
        String dueDateString = scanner.nextLine();
        Date dueDate = null;

        try {
            dueDate = dateformat.parse(dueDateString);
        } catch (ParseException e) {
            error = "Date format should be yyyy-mm-dd";
        }

        Task task = new Task(title, description, dueDate, Task.Status.NotStarted);

        if(error == null) {

            Database.add(task);
            System.out.println("Task saved successfully.\nID: " + task.id);
        }
        else{
            System.out.println("Cannot save task.\nError: " + error);
        }
    }

    public static void deleteTask(){
        String error = null;

        System.out.print("ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        try {if(Database.get(id) instanceof Task)
            Database.delete(id);
        } catch (EntityNotFoundException e) {
            error = "There is no entity with ID=" + id ;
        }

        ArrayList<Entity> allSteps = Database.getAll(Step.STEP_ENTITY_CODE);

        for(Entity entity: allSteps)
        {
            int taskRef = ((Step) entity).taskRef;
            if(taskRef == id)
                Database.delete(taskRef);
        }

        if(error == null) {
            System.out.println("Entity with ID=" + id + " successfully deleted.");
        }
        else {
            System.out.println("Error: " + error);
        }
    }

    public static void updateTask() throws InvalidEntityException {
        String error = null;

        System.out.print("ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Field: ");
        String field = scanner.nextLine();

        System.out.print("New Value: ");
        String newValue = scanner.nextLine();

        try {
            if( ! (Database.get(id) instanceof Task))
                error = "there is no task with ID+" + id;
        } catch (EntityNotFoundException e) {
            error = "There is no entity with ID=" + id ;
        }

        Task task = null;
        String oldValue = null;

        if(error == null){

            task = (Task) Database.get(id);

            if("title".equalsIgnoreCase(field)){
                oldValue = ((Task) Database.get(id)).title;

                if(newValue == null || "".equals(newValue))
                    error = "Task title cannot be empty.";

                task.title = newValue;
            }

            else if("description".equalsIgnoreCase(field)){
                oldValue = ((Task) Database.get(id)).description;

                task.description = newValue;
            }

            else if("dueDate".equalsIgnoreCase(field)){
                Date oldDueDate = ((Task) Database.get(id)).dueDate;

                DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                dateformat.setLenient(false);

                oldValue = dateformat.format(oldDueDate);

                try {
                    task.dueDate = dateformat.parse(newValue);
                } catch (ParseException e) {
                    error = "Date format should be yyyy-mm-dd";
                }
            }

            else if("status".equalsIgnoreCase(field)){
                oldValue = ((Task) Database.get(id)).status.name();

                try{
                    task.status = Task.Status.valueOf(newValue);
                } catch (IllegalArgumentException e) {
                    error = "Invalid status, please check case and spelling.";
                }
            }

            else{
                error = "There is no such field.please check spelling.";
            }
        }

        if(error == null) {
            Database.update(task);
            System.out.println("Successfully updated the task.");
            System.out.println("Field: " + field);
            System.out.println("Old Value: " + oldValue);
            System.out.println("New Value: " + newValue);
            System.out.println("Modification Date: " + task.getLastModificationDate());
        }
        else {
            System.out.println("Cannot update task with ID=" + id + ".\nError: " + error);
        }

        if("status".equalsIgnoreCase(field) && error == null) {

            if(((Task) Database.get(id)).status == Task.Status.Completed){

                ArrayList<Entity> allSteps = Database.getAll(Step.STEP_ENTITY_CODE);

                for(Entity entity: allSteps)
                {
                    int taskRef = ((Step) entity).taskRef;
                    if(taskRef == id)
                        ((Step) entity).status = Step.Status.Completed;
                }

            }
        }
    }

    public static void getTaskByID() {
        String error = null;

        System.out.print("ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        try {
            if( ! (Database.get(id) instanceof Task))
                error = "cannot find task with ID=" + id;
        } catch (EntityNotFoundException e) {
            error = "can not find entity with ID=" + id ;
        }

        if(error == null){
            Task task = (Task) Database.get(id);

            DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

            System.out.println("ID: " + id);
            System.out.println("Title: " + task.title);
            System.out.println("Description: " + task.description);
            System.out.println("Due Date: " + dateformat.format(task.dueDate));
            System.out.println("Status: " + task.status);

            ArrayList<Entity> allSteps = Database.getAll(Step.STEP_ENTITY_CODE);

            boolean isTheFirstStep = true;

            for(Entity entity: allSteps)
            {
                Step step = (Step) entity;
                int taskRef = step.taskRef;
                if(taskRef != id)
                    continue;

                if(isTheFirstStep){
                    System.out.println("Steps:");
                    isTheFirstStep = false;
                }

                System.out.println("\t+ " + step.title + ":");
                System.out.println("\t\tID: " + step.id);
                System.out.println("\t\tStatus: " + step.status);
            }
        }
        else{
            System.out.println(error);
        }

    }
}
