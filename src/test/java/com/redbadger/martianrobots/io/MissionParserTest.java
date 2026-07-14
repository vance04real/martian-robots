package com.redbadger.martianrobots.io;

import com.redbadger.martianrobots.domain.Direction;
import com.redbadger.martianrobots.domain.Position;
import com.redbadger.martianrobots.io.MissionParser.ParsedInput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MissionParserTest {

    private final MissionParser parser = new MissionParser();

    @Test
    void parsesTheGridAndOneMission() {
        ParsedInput input = parse("5 3\n1 1 E\nRFRFRFRF\n");

        assertTrue(input.grid().contains(new Position(5, 3)));
        assertEquals(1, input.missions().size());

        RobotMission mission = input.missions().get(0);
        assertEquals(new Position(1, 1), mission.start().position());
        assertEquals(Direction.EAST, mission.start().direction());
        assertEquals("RFRFRFRF", mission.instructions());
    }

    @Test
    void toleratesBlankLinesBetweenRobotBlocks() {
        ParsedInput input = parse("5 3\n\n1 1 E\nRF\n\n3 2 N\nFF\n");
        assertEquals(2, input.missions().size());
    }

    @Test
    void normalisesLowercaseInput() {
        RobotMission mission = parse("5 3\n1 1 e\nrflf\n").missions().get(0);
        assertEquals(Direction.EAST, mission.start().direction());
        assertEquals("RFLF", mission.instructions());
    }

    @Test
    void emptyInstructionLineIsAValidEmptyMission() {
        ParsedInput input = parse("5 3\n1 1 E\n\n");
        assertEquals("", input.missions().get(0).instructions());
    }

    @Test
    void missingInstructionLineIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> parse("5 3\n1 1 E\n"));
    }

    @Test
    void missingGridLineIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> parse(""));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "5",
            "5 3 7",
            "a 3",
            "51 3"
    })
    void malformedGridLineIsRejected(String gridLine) {
        assertThrows(IllegalArgumentException.class, () -> parse(gridLine + "\n"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1 1",
            "1 1 X",
            "-1 1 E",
            "51 1 N",
            "a 1 N"
    })
    void malformedRobotLineIsRejected(String robotLine) {
        assertThrows(IllegalArgumentException.class,
                () -> parse("5 3\n" + robotLine + "\nF\n"));
    }

    @Test
    void robotStartingOutsideTheGridIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> parse("5 3\n6 2 N\nF\n"));
    }

    @Test
    void instructionLengthBoundaryIsFewerThanOneHundred() {
        ParsedInput accepted = parse("5 3\n1 1 E\n" + "L".repeat(99) + "\n");
        assertEquals(99, accepted.missions().get(0).instructions().length());

        assertThrows(IllegalArgumentException.class,
                () -> parse("5 3\n1 1 E\n" + "L".repeat(100) + "\n"));
    }

    private ParsedInput parse(String text) {
        return parser.parse(new StringReader(text));
    }
}
