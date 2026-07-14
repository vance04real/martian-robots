package com.redbadger.martianrobots.domain;

/**
 * Immutable robot state. Commands derive the next state rather than mutating.
 */
public record RobotState(Position position, Direction direction, boolean lost) {

    public static RobotState at(Position position, Direction direction) {
        return new RobotState(position, direction, false);
    }

    public RobotState turnedLeft() {
        return new RobotState(position, direction.turnLeft(), lost);
    }

    public RobotState turnedRight() {
        return new RobotState(position, direction.turnRight(), lost);
    }

    public RobotState movedTo(Position destination) {
        return new RobotState(destination, direction, lost);
    }

    public RobotState asLost() {
        return new RobotState(position, direction, true);
    }
}
