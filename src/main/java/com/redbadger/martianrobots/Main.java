package com.redbadger.martianrobots;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * CLI entry point: reads from a file given as the first argument, or from
 * stdin. Results go to stdout and errors go to stderr with exit code 1.
 */
public final class Main {

    private Main() {
    }

    static void main(String[] arguments) {
        try (Reader input = openInput(arguments)) {
            new MartianRobotsApp().run(input, System.out);
        } catch (Exception exception) {
            System.err.println("Error: " + exception.getMessage());
            System.exit(1);
        }
    }

    private static Reader openInput(String[] arguments) throws IOException {
        return arguments.length > 0
                ? new FileReader(arguments[0], StandardCharsets.UTF_8)
                : new InputStreamReader(System.in, StandardCharsets.UTF_8);
    }
}
