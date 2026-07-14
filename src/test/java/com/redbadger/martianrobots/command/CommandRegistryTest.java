package com.redbadger.martianrobots.command;

import com.redbadger.martianrobots.domain.Direction;
import com.redbadger.martianrobots.domain.Grid;
import com.redbadger.martianrobots.domain.Position;
import com.redbadger.martianrobots.domain.RobotState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandRegistryTest {

    private final Grid grid = new Grid(5, 3);
    private final RobotState start = RobotState.at(new Position(1, 1), Direction.NORTH);

    @Test
    void standardRegistryKnowsExactlyLeftRightForward() {
        CommandRegistry registry = CommandRegistry.standard();
        assertTrue(registry.supports('L'));
        assertTrue(registry.supports('R'));
        assertTrue(registry.supports('F'));
        assertFalse(registry.supports('B'));
    }

    @Test
    void standardCommandsTurnAndMove() {
        CommandRegistry registry = CommandRegistry.standard();
        assertEquals(Direction.WEST, registry.commandFor('L').execute(start, grid).direction());
        assertEquals(Direction.EAST, registry.commandFor('R').execute(start, grid).direction());
        assertEquals(new Position(1, 2), registry.commandFor('F').execute(start, grid).position());
    }

    @Test
    void unknownCodeIsRejectedNamingTheSupportedOnes() {
        CommandRegistry registry = CommandRegistry.standard();
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> registry.commandFor('B'));
        assertTrue(exception.getMessage().contains("F, L, R"));
    }

    @Test
    void duplicateRegistrationIsRejected() {
        CommandRegistry registry = CommandRegistry.standard();
        assertThrows(IllegalArgumentException.class,
                () -> registry.register('F', (state, unusedGrid) -> state));
    }

    @Test
    void newCommandsCanBeRegistered() {
        CommandRegistry registry = CommandRegistry.standard();
        registry.register('B', (state, unusedGrid) ->
                state.movedTo(state.position().next(state.direction().turnLeft().turnLeft())));
        assertTrue(registry.supports('B'));
        assertEquals(new Position(1, 0), registry.commandFor('B').execute(start, grid).position());
    }
}
