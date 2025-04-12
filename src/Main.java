import db.Database;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import example.Document;
import todo.entity.Step;
import todo.entity.Task;
import todo.service.StepService;
import todo.service.TaskService;
import todo.validator.StepValidator;
import todo.validator.TaskValidator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InvalidEntityException {

        TaskValidator taskValidator = new TaskValidator();
        StepValidator stepValidator = new StepValidator();

        Database.registerValidator(Task.TASK_ENTITY_CODE, taskValidator);
        Database.registerValidator(Step.STEP_ENTITY_CODE, stepValidator);
        
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();

        while ( ! command.equalsIgnoreCase("exit"))
        {
            if(command.equalsIgnoreCase("add task"))
                TaskService.addTask();

            else if(command.equalsIgnoreCase("add step"))
                StepService.addStep();

            else if(command.equalsIgnoreCase("delete")) {

                System.out.print("ID: ");
                int id = Integer.parseInt(scanner.nextLine());

                try {
                    if(Database.get(id) instanceof Task)
                        TaskService.deleteTask(id);
                    else if(Database.get(id) instanceof Step)
                        StepService.deleteStep(id);
                } catch (EntityNotFoundException e) {
                    System.out.println("Error: " + "There is no entity with ID=" + id);
                }
            }

            else if(command.equalsIgnoreCase("update task")) {
                TaskService.updateTask();
            }

            else if(command.equalsIgnoreCase("update step")) {
                StepService.updateStep();
            }

            else if(command.equalsIgnoreCase("get task-by-id")) {
                TaskService.getTaskByID();
            }

            else if(command.equalsIgnoreCase("get all-tasks")) {
                TaskService.getAllTasks();
            }

            else if(command.equalsIgnoreCase("get incomplete-tasks")) {
                TaskService.getIncompleteTasks();
            }

            else {
                System.out.println("Invalid command.");
            }

            command = scanner.nextLine();
        }
    }
}
