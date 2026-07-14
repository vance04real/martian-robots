# Martian Robots

A small Java program that simulates robots exploring the rectangular surface
of Mars. Robots follow instruction strings made of `L`, `R` and `F`. A robot
that drives off an edge is lost forever, but it leaves a "scent" at its last
grid point that stops later robots from repeating the same fatal move.

## Requirements

- JDK 25 (no Maven install needed, the wrapper is included)

## Build and test

```bash
./mvnw test          # run the full test suite (79 tests)
./mvnw -q package    # build target/martian-robots.jar
```

## Run

```bash
java -jar target/martian-robots.jar sample-input.txt
# or via stdin:
java -jar target/martian-robots.jar < sample-input.txt
```

With the included sample data the output is:

```
1 1 E
3 3 N LOST
2 3 S
```

To verify the output mechanically:

```bash
java -jar target/martian-robots.jar < sample-input.txt | diff - expected-output.txt
```

Results are printed to stdout, one line per robot (`x y D`, plus ` LOST` if
the robot fell off the grid). Invalid input prints a single `Error: ...` line
to stderr and exits with code 1. Nothing is printed to stdout in that case,
because the input is fully parsed before any robot runs.

## Design notes

- **Immutable core.** A robot is a `RobotState` record and every command is a
  pure function from one state to the next. The only mutable object is the
  `Grid`, which owns the scent set. Scents are shared across sequentially
  processed robots, which is why they are the one piece of mutable state.
- **Commands are open for extension.** Instruction codes live in a
  `CommandRegistry` and the simulator validates and dispatches purely through
  it. A new command type is a single
  `registry.register('B', new BackwardCommand())` call, with no changes to
  the simulator, parser or existing commands. `RobotSimulatorTest` includes a
  test that registers a backward command and runs it, proving the extension
  point works.
- **One home per rule.** The parser owns all input validity (token counts,
  the 0-50 coordinate range, instruction length under 100, start position on
  the grid, case normalisation). The registry owns the set of known
  instructions. The grid owns its own bounds invariant.
- **Readability over cleverness.** Turning, moving and letter mapping are
  exhaustive switch expressions that the compiler checks for completeness.
  There is no ordinal arithmetic to decode.
- **Deliberate omissions (KISS).** No `World` interface (one geometry, and
  extracting an interface later is a two-minute refactor), no undo, no
  logging framework, no runtime dependencies.

## Assumptions

- A robot whose start position is outside the declared grid, or any
  malformed line, makes the whole input invalid. The program prints one
  error and no partial output.
- An empty instruction line is valid. The robot just reports its start
  position.
- Input is case-insensitive (`1 1 e` and `rflf` are accepted).
- Instruction strings may be 0 to 99 characters long ("less than 100").
- Multiple robots may occupy the same grid point. There are no collisions.
- A scent only suppresses moves that would leave the grid from that point.
  Safe moves from a scented point behave normally.

## Next steps

- Property-based tests, for example that no instruction sequence can ever
  leave a surviving robot outside the grid.
- Line numbers in parse error messages if inputs grow beyond a few robots.
- A streaming mode (execute robots as they are parsed) if input files ever
  become too large to buffer. Not worth it at the current scale.
