package com.redbadger.martianrobots.domain;

/**
 * A grid coordinate. Deliberately unvalidated: a candidate next position may
 * lie outside the grid while the simulator decides whether a move is fatal.
 */
public record Position(int xCoordinate, int yCoordinate) {

    public Position next(Direction direction) {
        return switch (direction) {
            case NORTH -> new Position(xCoordinate, yCoordinate + 1);
            case EAST -> new Position(xCoordinate + 1, yCoordinate);
            case SOUTH -> new Position(xCoordinate, yCoordinate - 1);
            case WEST -> new Position(xCoordinate - 1, yCoordinate);
        };
    }
}
