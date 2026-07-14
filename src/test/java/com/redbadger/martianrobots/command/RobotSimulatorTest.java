package com.redbadger.martianrobots.command;

import com.redbadger.martianrobots.domain.Direction;
import com.redbadger.martianrobots.domain.Grid;
import com.redbadger.martianrobots.domain.Position;
import com.redbadger.martianrobots.domain.RobotState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RobotSimulatorTest {

    private final RobotSimulator simulator = new RobotSimulator(CommandRegistry.standard());
    private final Grid grid = new Grid(5, 3);

    @Test
    void executesTheFirstSampleRobot() {
        RobotState result = simulator.execute(robotAt(1, 1, Direction.EAST), "RFRFRFRF", grid);
        assertEquals(robotAt(1, 1, Direction.EAST), result);
    }

    @ParameterizedTest
    @CsvSource({
            "5, 3, NORTH",
            "5, 3, EAST",
            "0, 0, SOUTH",
            "0, 0, WEST"
    })
    void fallsOffEachEdgeReportingTheLastValidState(
            int startXCoordinate, int startYCoordinate, Direction direction) {
        RobotState start = robotAt(startXCoordinate, startYCoordinate, direction);

        RobotState result = simulator.execute(start, "F", grid);

        assertTrue(result.lost());
        assertEquals(new Position(startXCoordinate, startYCoordinate), result.position());
        assertEquals(direction, result.direction());
    }

    @Test
    void scentSavesTheNextRobot() {
        RobotState first = simulator.execute(robotAt(5, 3, Direction.NORTH), "F", grid);
        assertTrue(first.lost());

        RobotState second = simulator.execute(robotAt(5, 3, Direction.NORTH), "F", grid);
        assertFalse(second.lost());
        assertEquals(new Position(5, 3), second.position());
    }

    @Test
    void lostRobotIgnoresTheRestOfItsInstructions() {
        RobotState result = simulator.execute(robotAt(5, 3, Direction.NORTH), "FRRF", grid);

        assertTrue(result.lost());
        assertEquals(Direction.NORTH, result.direction());
        assertEquals(new Position(5, 3), result.position());
    }

    @Test
    void emptyInstructionsLeaveTheRobotAtItsStart() {
        RobotState result = simulator.execute(robotAt(2, 2, Direction.EAST), "", grid);
        assertEquals(robotAt(2, 2, Direction.EAST), result);
    }

    @Test
    void unknownInstructionIsRejectedBeforeAnythingExecutes() {
        RobotState start = robotAt(5, 3, Direction.NORTH);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> simulator.execute(start, "FFX", grid));

        assertTrue(exception.getMessage().contains("'X'"));
        // The first F would have lost the robot and scented (5,3). An
        // untouched grid proves no command executed before the rejection.
        assertFalse(grid.hasScent(new Position(5, 3)));
    }

    @Test
    void newlyRegisteredCommandsRunWithoutCoreChanges() {
        CommandRegistry registry = CommandRegistry.standard();
        registry.register('B', (state, activeGrid) -> {
            Position backwards = state.position().next(state.direction().turnLeft().turnLeft());
            return activeGrid.contains(backwards) ? state.movedTo(backwards) : state;
        });
        RobotSimulator extendedSimulator = new RobotSimulator(registry);

        RobotState result = extendedSimulator.execute(robotAt(2, 2, Direction.NORTH), "BB", grid);

        assertEquals(new Position(2, 0), result.position());
        assertEquals(Direction.NORTH, result.direction());
    }

    private static RobotState robotAt(int xCoordinate, int yCoordinate, Direction direction) {
        return RobotState.at(new Position(xCoordinate, yCoordinate), direction);
    }
}
