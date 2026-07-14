package com.redbadger.martianrobots;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MartianRobotsAppTest {

    @Test
    void sampleDataProducesTheExpectedOutput() throws Exception {
        String input = Files.readString(Path.of("sample-input.txt"));
        String expected = Files.readString(Path.of("expected-output.txt"));

        assertEquals(normalise(expected), normalise(run(input)));
    }

    @Test
    void scentCarriesAcrossRobotsThroughTheFullPipeline() {
        String input = "5 3\n3 3 N\nFF\n3 3 N\nFF\n";
        assertEquals("3 3 N LOST\n3 3 N\n", normalise(run(input)));
    }

    private String run(String input) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream output = new PrintStream(buffer, true, StandardCharsets.UTF_8);

        new MartianRobotsApp().run(new StringReader(input), output);

        return buffer.toString(StandardCharsets.UTF_8);
    }

    private static String normalise(String text) {
        return text.replace("\r\n", "\n");
    }
}
