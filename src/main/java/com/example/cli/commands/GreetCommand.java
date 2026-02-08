package com.example.cli.commands;

import java.io.PrintWriter;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@Command(name = "greet", mixinStandardHelpOptions = true, description = "Say hello to someone.")
public class GreetCommand implements Callable<Integer> {

    @Spec
    private CommandSpec spec;

    @Option(
            names = {"-n", "--name"},
            description = "Name to greet.",
            defaultValue = "World")
    private String name;

    @Option(
            names = {"-c", "--count"},
            description = "Number of times to greet.",
            defaultValue = "1")
    private int count;

    @Override
    public Integer call() {
        PrintWriter out = spec.commandLine().getOut();
        for (int i = 0; i < count; i++) {
            out.printf("Hello, %s!%n", name);
        }
        return 0;
    }
}
