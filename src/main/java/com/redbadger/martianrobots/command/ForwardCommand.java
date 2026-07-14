package com.redbadger.martianrobots.command;

import com.redbadger.martianrobots.domain.Grid;
import com.redbadger.martianrobots.domain.Position;
import com.redbadger.martianrobots.domain.RobotState;

/**
 * Moves the robot one grid point in its current direction, applying the
 * edge and scent rules.
 */
public final class ForwardCommand implements Command {

    @Override
    public RobotState execute(RobotState state, Grid grid) {
        Position destination = state.position().next(state.direction());

        if (grid.contains(destination)) {
            return state.movedTo(destination);
        }

        // A previous robot was lost from this grid point: the fatal move is
        // simply ignored and the robot stays put.
        if (grid.hasScent(state.position())) {
            return state;
        }

        // The robot is lost. The scent marks its last valid grid point.
        grid.addScent(state.position());
        return state.asLost();
    }
}
