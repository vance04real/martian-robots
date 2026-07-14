package com.redbadger.martianrobots.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RobotStateTest {

    private final RobotState start = RobotState.at(new Position(1, 1), Direction.NORTH);

    @Test
    void derivesTurnsWithoutMutating() {
        assertEquals(Direction.WEST, start.turnedLeft().direction());
        assertEquals(Direction.EAST, start.turnedRight().direction());
        assertEquals(Direction.NORTH, start.direction());
    }

    @Test
    void derivesMovesKeepingDirection() {
        RobotState moved = start.movedTo(new Position(1, 2));
        assertEquals(new Position(1, 2), moved.position());
        assertEquals(Direction.NORTH, moved.direction());
        assertEquals(new Position(1, 1), start.position());
    }

    @Test
    void marksLostKeepingLastValidPosition() {
        RobotState lost = start.asLost();
        assertTrue(lost.lost());
        assertEquals(new Position(1, 1), lost.position());
        assertFalse(start.lost());
    }
}
