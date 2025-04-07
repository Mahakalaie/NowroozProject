package todo.service;

import db.Database;
import db.exception.InvalidEntityException;
import todo.entity.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
}
