package org.example.io;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class InputHandler {
    private static final Scanner scanner = new Scanner(System.in);

    public static String get() {
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            OutputHandler.println("Завершаемся...");
            System.exit(0);
        }
        return null;
    }

    public static boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    public static void close() {
        scanner.close();
    }
}

