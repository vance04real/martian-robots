package com.redbadger.martianrobots.io;

import com.redbadger.martianrobots.domain.Direction;
import com.redbadger.martianrobots.domain.Grid;
import com.redbadger.martianrobots.domain.Position;
import com.redbadger.martianrobots.domain.RobotState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Turns the raw input text into a Grid and the list of robot missions.
 * Owns every input-validity rule: token counts, integer parsing, the 0-50
 * coordinate range for robot starts, the instruction-length limit,
 * start-position-on-grid, and case normalisation. Everything is parsed
 * before anything executes, so bad input produces one error and no output.
 */
public final class MissionParser {

    public static final int INSTRUCTION_LENGTH_LIMIT = 100; // spec: fewer than 100

    public record ParsedInput(Grid grid, List<RobotMission> missions) {
    }

    public ParsedInput parse(Reader input) {
        BufferedReader reader = new BufferedReader(input);
        try {
            Grid grid = readGrid(reader);

            List<RobotMission> missions = new ArrayList<>();
            RobotMission mission;
            while ((mission = readMission(reader, grid)) != null) {
                missions.add(mission);
            }
            return new ParsedInput(grid, missions);
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to read input.", exception);
        }
    }

    private Grid readGrid(BufferedReader reader) throws IOException {
        String line = readNextNonBlankLine(reader);
        if (line == null) {
            throw new IllegalArgumentException("Missing grid line: expected 'maxX maxY'.");
        }

        String[] tokens = tokens(line);
        if (tokens.length != 2) {
            throw new IllegalArgumentException(
                    "Grid line must contain exactly two coordinates, got '" + line + "'.");
        }

        // The 0-50 range on the grid line is the Grid's own constructor
        // invariant, so only integer parsing is checked here.
        return new Grid(
                parseInteger(tokens[0], "Grid maximum x-coordinate"),
                parseInteger(tokens[1], "Grid maximum y-coordinate"));
    }

    private RobotMission readMission(BufferedReader reader, Grid grid) throws IOException {
        String positionLine = readNextNonBlankLine(reader);
        if (positionLine == null) {
            return null;
        }
        RobotState start = parseStart(positionLine, grid);

        // Read raw (not skipping blanks): an empty instruction line is a
        // valid, empty instruction string.
        String instructionLine = reader.readLine();
        if (instructionLine == null) {
            throw new IllegalArgumentException(
                    "Missing instruction line for robot at '" + positionLine + "'.");
        }

        String instructions = instructionLine.trim().toUpperCase(Locale.ROOT);
        if (instructions.length() >= INSTRUCTION_LENGTH_LIMIT) {
            throw new IllegalArgumentException(
                    "Instruction strings must be fewer than " + INSTRUCTION_LENGTH_LIMIT
                            + " characters, got " + instructions.length() + ".");
        }
        return new RobotMission(start, instructions);
    }

    private RobotState parseStart(String line, Grid grid) {
        String[] tokens = tokens(line);
        if (tokens.length != 3) {
            throw new IllegalArgumentException(
                    "Robot position must be 'x y D', got '" + line + "'.");
        }

        Position position = new Position(
                parseCoordinate(tokens[0], "Robot x-coordinate"),
                parseCoordinate(tokens[1], "Robot y-coordinate"));
        Direction direction = Direction.parse(tokens[2].toUpperCase(Locale.ROOT));

        if (!grid.contains(position)) {
            throw new IllegalArgumentException(
                    "Robot starts outside the grid at "
                            + position.xCoordinate() + " " + position.yCoordinate() + ".");
        }
        return RobotState.at(position, direction);
    }

    private static String readNextNonBlankLine(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.isBlank()) {
                return line.trim();
            }
        }
        return null;
    }

    private static String[] tokens(String line) {
        return line.trim().split("\\s+");
    }

    private static int parseCoordinate(String token, String description) {
        int coordinate = parseInteger(token, description);
        if (coordinate < 0 || coordinate > Grid.MAX_COORDINATE) {
            throw new IllegalArgumentException(
                    description + " must be between 0 and " + Grid.MAX_COORDINATE
                            + ", got " + coordinate + ".");
        }
        return coordinate;
    }

    private static int parseInteger(String token, String description) {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    description + " must be an integer, got '" + token + "'.");
        }
    }
}
