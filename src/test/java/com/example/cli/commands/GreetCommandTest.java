package com.example.cli.commands;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.cli.App;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

class GreetCommandTest {

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
    void defaultGreeting() {
        int exitCode = cmd.execute("greet");

        assertThat(exitCode).isZero();
        assertThat(out.toString()).contains("Hello, World!");
    }

    @Test
    void customName() {
        int exitCode = cmd.execute("greet", "--name", "Ada");

        assertThat(exitCode).isZero();
        assertThat(out.toString()).contains("Hello, Ada!");
    }

    @Test
    void shortName() {
        int exitCode = cmd.execute("greet", "-n", "Grace");

        assertThat(exitCode).isZero();
        assertThat(out.toString()).contains("Hello, Grace!");
    }

    @Test
    void repeatCount() {
        int exitCode = cmd.execute("greet", "--name", "Ada", "--count", "3");

        assertThat(exitCode).isZero();
        String output = out.toString();
        assertThat(output.split("Hello, Ada!")).hasSize(4); // 3 occurrences = 4 parts after split
    }

    @Test
    void greetHelp() {
        int exitCode = cmd.execute("greet", "--help");

        assertThat(exitCode).isZero();
        assertThat(out.toString()).contains("Say hello");
        assertThat(out.toString()).contains("--name");
        assertThat(out.toString()).contains("--count");
    }
}
