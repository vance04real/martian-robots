package com.redbadger.martianrobots.io;

import com.redbadger.martianrobots.domain.Direction;
import com.redbadger.martianrobots.domain.Position;
import com.redbadger.martianrobots.domain.RobotState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResultFormatterTest {

    private final ResultFormatter formatter = new ResultFormatter();

    @Test
    void formatsASurvivingRobot() {
        RobotState state = RobotState.at(new Position(1, 1), Direction.EAST);
        assertEquals("1 1 E", formatter.format(state));
    }

    @Test
    void formatsALostRobotWithTheSuffix() {
        RobotState state = RobotState.at(new Position(3, 3), Direction.NORTH).asLost();
        assertEquals("3 3 N LOST", formatter.format(state));
    }
}
