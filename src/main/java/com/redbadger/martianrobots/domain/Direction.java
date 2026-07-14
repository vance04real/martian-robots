package com.redbadger.martianrobots.domain;

/**
 * The four compass orientations. Input and output use the single letters
 * N, E, S, W from the brief. parse() and letter() translate at the edges.
 */
public enum Direction {

    NORTH, EAST, SOUTH, WEST;

    public Direction turnLeft() {
        return switch (this) {
            case NORTH -> WEST;
            case WEST -> SOUTH;
            case SOUTH -> EAST;
            case EAST -> NORTH;
        };
    }

    public Direction turnRight() {
        return switch (this) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }

    public String letter() {
        return switch (this) {
            case NORTH -> "N";
            case EAST -> "E";
            case SOUTH -> "S";
            case WEST -> "W";
        };
    }

    public static Direction parse(String token) {
        return switch (token) {
            case "N" -> NORTH;
            case "E" -> EAST;
            case "S" -> SOUTH;
            case "W" -> WEST;
            default -> throw new IllegalArgumentException(
                    "Invalid direction '" + token + "'. Expected one of N, E, S, W.");
        };
    }
}
