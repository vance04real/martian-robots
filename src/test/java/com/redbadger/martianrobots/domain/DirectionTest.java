package com.redbadger.martianrobots.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DirectionTest {

    @ParameterizedTest
    @CsvSource({"NORTH, WEST", "WEST, SOUTH", "SOUTH, EAST", "EAST, NORTH"})
    void turningLeftRotatesAnticlockwise(Direction from, Direction expected) {
        assertEquals(expected, from.turnLeft());
    }

    @ParameterizedTest
    @CsvSource({"NORTH, EAST", "EAST, SOUTH", "SOUTH, WEST", "WEST, NORTH"})
    void turningRightRotatesClockwise(Direction from, Direction expected) {
        assertEquals(expected, from.turnRight());
    }

    @ParameterizedTest
    @CsvSource({
            "NORTH, 3, 3, 3, 4",
            "EAST, 3, 3, 4, 3",
            "SOUTH, 3, 3, 3, 2",
            "WEST, 3, 3, 2, 3"
    })
    void movingForwardFollowsTheCompass(
            Direction direction,
            int startXCoordinate, int startYCoordinate,
            int expectedXCoordinate, int expectedYCoordinate) {
        Position start = new Position(startXCoordinate, startYCoordinate);
        assertEquals(new Position(expectedXCoordinate, expectedYCoordinate), start.next(direction));
    }

    @ParameterizedTest
    @CsvSource({"N, NORTH", "E, EAST", "S, SOUTH", "W, WEST"})
    void parsesTheSingleLetterFromInput(String letter, Direction expected) {
        assertEquals(expected, Direction.parse(letter));
    }

    @ParameterizedTest
    @CsvSource({"NORTH, N", "EAST, E", "SOUTH, S", "WEST, W"})
    void rendersAsTheSingleLetterForOutput(Direction direction, String expected) {
        assertEquals(expected, direction.letter());
    }

    @Test
    void unknownDirectionIsRejected() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> Direction.parse("X"));
        assertTrue(exception.getMessage().contains("N, E, S, W"));
    }
}
