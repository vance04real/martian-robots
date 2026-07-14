package com.redbadger.martianrobots.command;

import com.redbadger.martianrobots.domain.Grid;
import com.redbadger.martianrobots.domain.RobotState;

/**
 * Runs one robot's instruction string against the grid. The whole string is
 * checked against the registry first, so a bad instruction can never leave a
 * half-moved robot.
 */
public final class RobotSimulator {

    private final CommandRegistry registry;

    public RobotSimulator(CommandRegistry registry) {
        this.registry = registry;
    }

    public RobotState execute(RobotState start, String instructions, Grid grid) {
        requireSupported(instructions);

        RobotState state = start;
        for (int index = 0; index < instructions.length() && !state.lost(); index++) {
            Command command = registry.commandFor(instructions.charAt(index));
            state = command.execute(state, grid);
        }
        return state;
    }

    private void requireSupported(String instructions) {
        for (int index = 0; index < instructions.length(); index++) {
            char code = instructions.charAt(index);
            if (!registry.supports(code)) {
                throw new IllegalArgumentException(
                        "Invalid instruction '" + code + "' at index " + index
                                + ". Expected one of " + registry.supportedCodes() + ".");
            }
        }
    }
}
