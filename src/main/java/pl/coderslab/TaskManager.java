package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class TaskManager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[][] tasks = readFile();
        handleUserInput(tasks, scanner);
    }

    public static void userOptions() {
        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Please select an option from the following:" + ConsoleColors.RESET);
        String[] options = {"add", "remove", "list", "exit"};
        for (String option : options) {
            System.out.println(ConsoleColors.WHITE_BRIGHT + option + ConsoleColors.RESET);
        }
        System.out.println(ConsoleColors.BLUE_BRIGHT + "Type in the selected option:" + ConsoleColors.RESET);
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

    public static void handleUserInput(String[][] tasks, Scanner scanner) {
        String input;
        do {
            userOptions();
            input = scanner.nextLine();

            switch (input) {
                case "add":
                    tasks = addTask(tasks, scanner);
                    break;
                case "remove":
                    tasks = removeTask(tasks, scanner);
                    break;
                case "list":
                    listTasks(tasks);
                    break;
                case "exit":
                    exitTasks(tasks, scanner);
                    break;
                default:
                    System.err.println("Please select a correct option!");
            }
        } while (!input.equalsIgnoreCase("exit"));
    }

    public static String[][] addTask(String[][] tasks, Scanner scanner) {

        System.out.println(ConsoleColors.GREEN_BOLD_BRIGHT + "Please enter the description of the task including importance: " + ConsoleColors.RESET);
        String task = scanner.nextLine();
        System.out.println(ConsoleColors.GREEN_BOLD_BRIGHT + "Please enter the deadline(YYYY-MM-DD): " + ConsoleColors.RESET);
        String deadline = scanner.nextLine();
        Boolean done = null;
        while (done == null) {
            System.out.println(ConsoleColors.GREEN_BOLD_BRIGHT + "Please enter true if the task is done or enter false if it is yet to be done: " + ConsoleColors.RESET);
            if (scanner.hasNextBoolean()) {
                done = scanner.nextBoolean();
                scanner.nextLine();
            } else {
                System.err.println("Invalid input. Please enter true or false.");
                scanner.next();
            }
        }

        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];

        tasks[tasks.length - 1][0] = task;
        tasks[tasks.length - 1][1] = deadline;
        tasks[tasks.length - 1][2] = String.valueOf(done);

        System.out.println(ConsoleColors.GREEN + "Task added successfully!" + ConsoleColors.RESET);

        return tasks;
    }

    public static void listTasks(String[][] tasks) {
        if (tasks.length == 0) {
            System.out.println(ConsoleColors.RED + "No tasks found!" + ConsoleColors.RESET);
        }
        for (int i = 0; i < tasks.length; i++) {
            System.out.println(ConsoleColors.BLUE_BOLD + "Task #" + (i + 1) + ": " + ConsoleColors.RESET
                    + tasks[i][0] + ConsoleColors.YELLOW + " Deadline: " + ConsoleColors.RESET
                    + tasks[i][1] + ConsoleColors.PURPLE + " Completion: " + ConsoleColors.RESET
                    + tasks[i][2] + "\n");
        }
    }

    public static String[][] removeTask(String[][] tasks, Scanner scanner) {
        listTasks(tasks);
        System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Insert the number of the task to remove: " + ConsoleColors.RESET);

        while (true) {
            if (scanner.hasNextInt()) {
                int taskIndex = scanner.nextInt();
                scanner.nextLine();
                if (taskIndex > 0 && taskIndex <= tasks.length) {
                    tasks = ArrayUtils.remove(tasks, taskIndex - 1);
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Task removed successfully!" + ConsoleColors.RESET);
                    break;
                } else {
                    System.err.println("Invalid index, try again.");
                }
            } else {
                System.err.println("Please enter a number!");
                scanner.next();
            }
        }

        return tasks;
    }

    public static void exitTasks(String[][] tasks, Scanner scanner) {
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
        scanner.close();
        System.exit(0);
    }
}
