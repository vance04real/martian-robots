package com.redbadger.martianrobots.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GridTest {

    private final Grid grid = new Grid(5, 3);

    @ParameterizedTest
    @CsvSource({"0, 0", "5, 3", "0, 3", "5, 0", "2, 1"})
    void containsPointsInsideTheBounds(int xCoordinate, int yCoordinate) {
        assertTrue(grid.contains(new Position(xCoordinate, yCoordinate)));
    }

    @ParameterizedTest
    @CsvSource({"-1, 0", "0, -1", "6, 0", "0, 4"})
    void excludesPointsOutsideTheBounds(int xCoordinate, int yCoordinate) {
        assertFalse(grid.contains(new Position(xCoordinate, yCoordinate)));
    }

    @Test
    void zeroSizedGridContainsOnlyTheOrigin() {
        Grid tiny = new Grid(0, 0);
        assertTrue(tiny.contains(new Position(0, 0)));
        assertFalse(tiny.contains(new Position(0, 1)));
    }

    @Test
    void maximumCoordinateIsAccepted() {
        assertTrue(new Grid(50, 50).contains(new Position(50, 50)));
    }

    @Test
    void boundAboveMaximumIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Grid(51, 50));
        assertThrows(IllegalArgumentException.class, () -> new Grid(50, 51));
    }

    @Test
    void negativeBoundIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Grid(-1, 3));
    }

    @Test
    void remembersScents() {
        Position edge = new Position(5, 3);
        assertFalse(grid.hasScent(edge));
        grid.addScent(edge);
        assertTrue(grid.hasScent(edge));
    }
}
