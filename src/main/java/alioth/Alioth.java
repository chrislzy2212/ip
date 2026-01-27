package alioth;

import java.util.Scanner;

/**
 * A chatbot that greets the user, echoes the user's commands, and exits on "bye".
 */
public class Alioth {
    private static final String LINE = "____________________________________________________________";

    /**
     * Starts the chatbot.
     *
     * @param args Command line arguments (unused).
     */
    public static void main(String[] args) {
        printWelcome();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                printBye();
                break;
            } else {
                printEcho(input);
            }
        }
    }

    private static void printWelcome() {
        System.out.println(LINE);
        System.out.println("Hello! I'm Alioth");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    private static void printEcho(String input) {
        System.out.println(LINE);
        System.out.println(input);
        System.out.println(LINE);
    }

    private static void printBye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }
}
