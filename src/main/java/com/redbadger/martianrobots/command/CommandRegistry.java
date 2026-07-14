package com.redbadger.martianrobots.command;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Single source of truth for which instruction codes exist. Future command
 * types are one register() call away and no other class changes.
 */
public final class CommandRegistry {

    private final Map<Character, Command> commands = new HashMap<>();

    public static CommandRegistry standard() {
        CommandRegistry registry = new CommandRegistry();
        registry.register('L', (state, _) -> state.turnedLeft());
        registry.register('R', (state, _) -> state.turnedRight());
        registry.register('F', new ForwardCommand());
        return registry;
    }

    public void register(char code, Command command) {
        if (command == null) {
            throw new IllegalArgumentException("Command for '" + code + "' cannot be null.");
        }
        if (commands.containsKey(code)) {
            throw new IllegalArgumentException("Command '" + code + "' is already registered.");
        }
        commands.put(code, command);
    }

    public boolean supports(char code) {
        return commands.containsKey(code);
    }

    public Command commandFor(char code) {
        Command command = commands.get(code);
        if (command == null) {
            throw new IllegalArgumentException(
                    "Unsupported instruction '" + code + "'. Expected one of "
                            + supportedCodes() + ".");
        }
        return command;
    }

    public String supportedCodes() {
        return commands.keySet().stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }
}
