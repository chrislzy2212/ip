package alioth;

/**
 * A chatbot skeleton that greets the user and exits.
 */
public class Alioth {
    private static final String LINE = "____________________________________________________________";

    /**
     * Starts the chatbot.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        printWelcome();
        printBye();
    }

    private static void printWelcome() {
        System.out.println(LINE);
        System.out.println(" Hello! I'm Alioth");
        System.out.println(" What can I do for you?");
        System.out.println(LINE);
    }

    private static void printBye() {
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }
}
