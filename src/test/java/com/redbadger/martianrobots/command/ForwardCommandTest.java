package com.redbadger.martianrobots.command;

import com.redbadger.martianrobots.domain.Direction;
import com.redbadger.martianrobots.domain.Grid;
import com.redbadger.martianrobots.domain.Position;
import com.redbadger.martianrobots.domain.RobotState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ForwardCommandTest {

    private final ForwardCommand forward = new ForwardCommand();
    private final Grid grid = new Grid(5, 3);

    @Test
    void movesOneGridPointInTheCurrentDirection() {
        RobotState state = RobotState.at(new Position(2, 2), Direction.EAST);
        assertEquals(new Position(3, 2), forward.execute(state, grid).position());
    }

    @Test
    void fallingOffTheGridLosesTheRobotAndLeavesAScent() {
        RobotState state = RobotState.at(new Position(5, 3), Direction.NORTH);
        RobotState result = forward.execute(state, grid);
        assertTrue(result.lost());
        assertEquals(new Position(5, 3), result.position());
        assertTrue(grid.hasScent(new Position(5, 3)));
    }

    @Test
    void scentIgnoresTheFatalMoveFromThatPoint() {
        grid.addScent(new Position(5, 3));
        RobotState state = RobotState.at(new Position(5, 3), Direction.NORTH);
        RobotState result = forward.execute(state, grid);
        assertFalse(result.lost());
        assertEquals(new Position(5, 3), result.position());
    }

    @Test
    void scentIsPositionKeyedNotDirectionKeyed() {
        grid.addScent(new Position(5, 3));
        RobotState facingDifferently = RobotState.at(new Position(5, 3), Direction.EAST);
        assertFalse(forward.execute(facingDifferently, grid).lost());
    }

    @Test
    void scentDoesNotBlockSafeMoves() {
        grid.addScent(new Position(5, 3));
        RobotState state = RobotState.at(new Position(5, 3), Direction.SOUTH);
        RobotState result = forward.execute(state, grid);
        assertFalse(result.lost());
        assertEquals(new Position(5, 2), result.position());
    }
}
