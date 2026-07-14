package com.redbadger.martianrobots.io;

import com.redbadger.martianrobots.domain.RobotState;

/**
 * Renders a final robot state in the output format from the brief.
 */
public final class ResultFormatter {

    public String format(RobotState state) {
        return state.position().xCoordinate() + " " + state.position().yCoordinate() + " "
                + state.direction().letter() + (state.lost() ? " LOST" : "");
    }
}
