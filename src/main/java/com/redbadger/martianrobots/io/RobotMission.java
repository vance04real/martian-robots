package com.redbadger.martianrobots.io;

import com.redbadger.martianrobots.domain.RobotState;

/**
 * One robot's starting state and its instruction string, as read from input.
 */
public record RobotMission(RobotState start, String instructions) {
}
