package com.example.cli;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

class AppTest {

    private CommandLine cmd;
    private StringWriter out;
    private StringWriter err;

    @BeforeEach
    void setUp() {
        cmd = new CommandLine(new App());
        out = new StringWriter();
        err = new StringWriter();
        cmd.setOut(new PrintWriter(out));
        cmd.setErr(new PrintWriter(err));
    }

    @Test
    void helpShowsUsage() {
        int exitCode = cmd.execute("--help");

        assertThat(exitCode).isZero();
        assertThat(out.toString()).contains("mycli");
        assertThat(out.toString()).contains("Picocli and GraalVM native-image");
    }

    @Test
    void versionShowsVersionInfo() {
        int exitCode = cmd.execute("--version");

        assertThat(exitCode).isZero();
        assertThat(out.toString()).contains("mycli");
    }

    @Test
    void noArgsShowsHelp() {
        int exitCode = cmd.execute();

        assertThat(exitCode).isZero();
        assertThat(out.toString()).contains("Usage:");
    }
}
