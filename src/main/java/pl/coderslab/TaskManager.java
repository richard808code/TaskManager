package pl.coderslab;

import pl.coderslab.ConsoleColors;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

public class TaskManager {
    public static void main(String[] args) {
        userOptions();
        readFile();
    }

    public static void userOptions() {
        System.out.println(ConsoleColors.BLUE + "Please select an option:" +ConsoleColors.RESET);
        String[] options = {"add", "remove", "list", "exit"};
        for (String option : options) {
            System.out.println(option);
        }
    }

    public static void readFile(){
        final var path = Paths.get("src/main/resources/tasks.csv");
        int lineCounter = 0;

        try {
            final var reader = new BufferedReader(new FileReader(path.toFile()));
                while (reader.readLine() != null) {
                    lineCounter++;
                }
                reader.close();
                System.out.println("Number of lines in the file: " + lineCounter);

        } catch (FileNotFoundException e) {
            System.err.println("ERROR:File not found!");
            e.printStackTrace(System.err);
        } catch (IOException e) {
            System.err.println("ERROR:I/O error while reading file!");
            e.printStackTrace(System.err);
        }
    }
}
