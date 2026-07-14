package com.redbadger.martianrobots.command;

import com.redbadger.martianrobots.domain.Grid;
import com.redbadger.martianrobots.domain.RobotState;

/**
 * A single robot instruction: a pure function from the current state to the
 * next. New command types plug in via CommandRegistry.register without
 * changing any core class.
 */
@FunctionalInterface
public interface Command {

    RobotState execute(RobotState state, Grid grid);
}
