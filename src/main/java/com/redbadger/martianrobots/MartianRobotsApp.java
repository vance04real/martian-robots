package com.redbadger.martianrobots;

import com.redbadger.martianrobots.command.CommandRegistry;
import com.redbadger.martianrobots.command.RobotSimulator;
import com.redbadger.martianrobots.domain.RobotState;
import com.redbadger.martianrobots.io.MissionParser;
import com.redbadger.martianrobots.io.MissionParser.ParsedInput;
import com.redbadger.martianrobots.io.ResultFormatter;
import com.redbadger.martianrobots.io.RobotMission;

import java.io.PrintStream;
import java.io.Reader;

/**
 * Parses everything first, then runs each robot in order (scents carry
 * over), printing one result line per robot.
 */
public final class MartianRobotsApp {

    private final MissionParser parser = new MissionParser();
    private final RobotSimulator simulator = new RobotSimulator(CommandRegistry.standard());
    private final ResultFormatter formatter = new ResultFormatter();

    public void run(Reader input, PrintStream output) {
        ParsedInput parsedInput = parser.parse(input);

        for (RobotMission mission : parsedInput.missions()) {
            RobotState finalState =
                    simulator.execute(mission.start(), mission.instructions(), parsedInput.grid());
            output.println(formatter.format(finalState));
        }
    }
}
