package com.redbadger.martianrobots.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * The bounded rectangular world: the upper-right corner (lower-left is always
 * 0,0) plus the scents left by lost robots. The scent set is the only mutable
 * state in the system, because scents are shared across sequential robots.
 */
public final class Grid {

    public static final int MAX_COORDINATE = 50;

    private final int maxXCoordinate;
    private final int maxYCoordinate;
    private final Set<Position> scents = new HashSet<>();

    public Grid(int maxXCoordinate, int maxYCoordinate) {
        requireInRange(maxXCoordinate, "Grid maximum x-coordinate");
        requireInRange(maxYCoordinate, "Grid maximum y-coordinate");
        this.maxXCoordinate = maxXCoordinate;
        this.maxYCoordinate = maxYCoordinate;
    }

    public boolean contains(Position position) {
        return position.xCoordinate() >= 0
                && position.xCoordinate() <= maxXCoordinate
                && position.yCoordinate() >= 0
                && position.yCoordinate() <= maxYCoordinate;
    }

    public boolean hasScent(Position position) {
        return scents.contains(position);
    }

    public void addScent(Position position) {
        scents.add(position);
    }

    private static void requireInRange(int coordinate, String description) {
        if (coordinate < 0 || coordinate > MAX_COORDINATE) {
            throw new IllegalArgumentException(
                    description + " must be between 0 and " + MAX_COORDINATE
                            + ", got " + coordinate + ".");
        }
    }
}
