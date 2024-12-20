package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class TaskManager {
    public static void main(String[] args) {
        userOptions();
        String[][] tasks = readFile();
        removeTask(tasks);

    }

    public static void userOptions() {
        System.out.println(ConsoleColors.BLUE + "Please select an option:" + ConsoleColors.RESET);
        String[] options = {"add", "remove", "list", "exit"};
        for (String option : options) {
            System.out.println(option);
        }
    }

    public static String[][] readFile() {
        final var path = Paths.get("src/main/resources/tasks.csv");
        int lineCounter = 0;
        String[][] tasks = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            while (reader.readLine() != null) {
                lineCounter++;
            }

            reader.close();
            try (BufferedReader newReader = new BufferedReader(new FileReader(path.toFile()))) {
                String[] firstLine = newReader.readLine().split(",");
                tasks = new String[lineCounter][firstLine.length];
                tasks[0] = firstLine;

                for (int i = 1; i < lineCounter; i++) {
                    String line = newReader.readLine();
                    if (line != null) {
                        tasks[i] = line.split(",");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: File not found!");
            e.printStackTrace(System.err);
        } catch (IOException e) {
            System.err.println("ERROR: I/O error while reading file!");
            e.printStackTrace(System.err);
        }

        return tasks;
    }

//    public static void handleUserInput(){
//        final var scanner = new Scanner(System.in);
//        String input = scanner.nextLine();
//
//        switch (input) {
//            case "add":
//                addTask();
//                break;
//            case "remove":
//                removeTask();
//                break;
//            case "list":
//                listTasks();
//                break;
//            case "exit":
//                exitTasks();
//                break;
//            default:
//                System.out.println("Please select a correct option.");
//        }
//    }

    public static String[][] addTask(String[][] tasks) {

        final var scanner = new Scanner(System.in);
        System.out.println(ConsoleColors.GREEN + "Please enter the description of the task including importance: " + ConsoleColors.RESET);
        String task = scanner.nextLine();
        System.out.println(ConsoleColors.GREEN + "Please enter the deadline(YYYY-MM-DD): " + ConsoleColors.RESET);
        String deadline = scanner.nextLine();
        System.out.println(ConsoleColors.GREEN + "Please enter true if the task is done or enter false if it is yet to be done: " + ConsoleColors.RESET);
        Boolean done = scanner.nextBoolean();
        scanner.close();

        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];

        tasks[tasks.length - 1][0] = task;
        tasks[tasks.length - 1][1] = deadline;
        tasks[tasks.length - 1][2] = String.valueOf(done);

        return tasks;
    }

    public static void listTasks(String[][] tasks) {
        if (tasks.length == 0) {
            System.out.println(ConsoleColors.RED + "No tasks found!" + ConsoleColors.RESET);
        }
        for (int i = 0; i < tasks.length; i++) {
            System.out.println(ConsoleColors.BLUE_BOLD + "Task #" + (i + 1) + ": " + ConsoleColors.RESET
                    + tasks[i][0] + ConsoleColors.BLUE + " Deadline: " + ConsoleColors.RESET
                    + tasks[i][1] + ConsoleColors.BLUE + " Completion: " + ConsoleColors.RESET
                    + tasks[i][2] + "\n");
        }
    }

    public static String[][] removeTask(String[][] tasks) {
        listTasks(tasks);
        System.out.println(ConsoleColors.GREEN + "Insert the number of the task to remove: " + ConsoleColors.RESET);
        final var scanner = new Scanner(System.in);
        while (scanner.hasNextInt()) {
            int taskIndex = scanner.nextInt();
            if (taskIndex <= 0 || taskIndex > tasks.length) {
                System.err.println("ERROR: Invalid task index!");
                System.out.println("Insert valid task index:");
            } else {
                System.out.println("Array length before removal: " + tasks.length);
                tasks = ArrayUtils.remove(tasks, taskIndex - 1);
                System.out.println("Array length after removal: " + tasks.length);
                System.out.println(ConsoleColors.GREEN + "Task removed successfully!");
                break;
            }
        }

        return tasks;
    }

    public static void exitTasks(String[][] tasks) {
        final var path = Paths.get("src/main/resources/tasks.csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))) {
            for (int i = 0; i < tasks.length; i++) {
                writer.write(tasks[i][0] + "," + tasks[i][1] + "," + tasks[i][2]);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("ERROR: I/O error while writing file!");
            e.printStackTrace();
        }
        System.out.println(ConsoleColors.RED + "The program finished!" + ConsoleColors.RESET);
        System.exit(0);
    }
}