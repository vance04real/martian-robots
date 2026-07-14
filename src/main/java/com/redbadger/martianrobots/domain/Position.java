package com.redbadger.martianrobots.domain;

/**
 * A grid coordinate. Deliberately unvalidated: a candidate next position may
 * lie outside the grid while the simulator decides whether a move is fatal.
 */
public record Position(int x, int y) {

    public Position next(Direction direction) {
        return switch (direction) {
            case NORTH -> new Position(x, y + 1);
            case EAST -> new Position(x + 1, y);
            case SOUTH -> new Position(x, y - 1);
            case WEST -> new Position(x - 1, y);
        };
    }
}
