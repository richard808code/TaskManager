package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class TaskManager {

    // CLEAN-CODE: promenne tasks a scanner bych dal jako globalni promenne pristupne ze vsech metod
    //    private static String[][] tasks;
    //    private static Scanner scanner;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[][] tasks = readFile(); // CLEAN-CODE: prosim prejmenuj metodu readFile() na vice popisnou readTasks()
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
        /*
        CLEAN-CODE: jednodussi verze na mene radku
        try {
            final var lines = Files.readAllLines(TASK_FILE_PATH);

            tasks = new String[lines.size()][];
            for (int i = 0; i < lines.size(); i++) {
                tasks[i] = lines.get(i).split(", ");
            }
        } catch (IOException e) {
            System.err.println("Failed to load tasks.csv");
            e.printStackTrace(System.err);
            System.exit(-1);
        }
         */

        final var path = Paths.get("src/main/resources/tasks.csv");
        int lineCounter = 0;
        String[][] tasks = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            while (reader.readLine() != null) {
                lineCounter++;
            }

            reader.close(); //BUG: reader uz je uzavreny v ramci try-with-resources bloku
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
            //BUG: zde bych program zastavil dalsi chod programu uz nema smysl System.exit(-1);
        } catch (IOException e) {
            System.err.println("ERROR: I/O error while reading file!");
            //BUG: zde bych program zastavil dalsi chod programu uz nema smysl System.exit(-1);
            e.printStackTrace(System.err);
        }

        return tasks;
    }

    public static void handleUserInput(String[][] tasks, Scanner scanner) {
        // CLEAN-CODE: tuhle sekci bych celou predal do main metody aby bylo hned jasne co program dela
        String input;
        do {
            userOptions(); // CLEAN-CODE: userOptions() -> displayUserOptions() hned je z nazvu metody jasne co metoda dela
            input = scanner.nextLine(); // //IMPROVE: scanner.nextLine().toLowerCase(), umozni ti to zadat jak upper case tak lower case

            switch (input) {
                case "add":
                    tasks = addTask(tasks, scanner); //CLEAN-CODE: jak bude promenna tasks a scanner globalni tak zde se zjednodussi a bude jen metoda addTask()
                    break;
                case "remove":
                    tasks = removeTask(tasks, scanner); //CLEAN-CODE: jak bude promenna tasks a scanner globalni tak zde se zjednodussi a bude jen metoda removeTask()
                    break;
                case "list":
                    listTasks(tasks); //CLEAN-CODE: jak bude promenna tasks a scanner globalni tak zde se zjednodussi a bude jen metoda listTasks()
                    break;
                case "exit":
                    exitTasks(tasks, scanner); //CLEAN-CODE: jak bude promenna tasks a scanner globalni tak zde se zjednodussi a bude jen metoda exit(), asi lepsi pojmenovani nez exitTasks()
                    break;
                default:
                    System.err.println("Please select a correct option!");
            }
        } while (!input.equalsIgnoreCase("exit"));
    }

    public static String[][] addTask(String[][] tasks, Scanner scanner) {
        System.out.println(ConsoleColors.GREEN_BOLD_BRIGHT + "Please enter the description of the task including importance: " + ConsoleColors.RESET);
        String task = scanner.nextLine(); //CLEAN-CODE: promenne v metode defaultne nastavujeme na final, znamena to ze promenna po vytvoreni je nemenna coz je zde chteny stav
        // CLEAN-CODE: task -> description je to vice popisne a muzes pak pouzit jmeno task pro pole
        System.out.println(ConsoleColors.GREEN_BOLD_BRIGHT + "Please enter the deadline(YYYY-MM-DD): " + ConsoleColors.RESET);
        String deadline = scanner.nextLine(); //CLEAN-CODE: promenne v metode defaultne nastavujeme na final, znamena to ze promenna po vytvoreni je nemenna coz je zde chteny stav
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


        /*
        //CLEAN-CODE:
        final var task = new String[3];
        task[0] = task;
        task[1] = deadline;
        task[2] = String.valueOf(done);

        tasks[tasks.length - 1] = task;
        */


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

        //CLEAN-CODE: kod zapisujici seznam ukolu do souboru predat do metody saveTasksToFile()
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
