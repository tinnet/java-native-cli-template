package com.example.cli;

import com.example.cli.commands.GreetCommand;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

@Command(
    name = "mycli",
    mixinStandardHelpOptions = true,
    versionProvider = VersionProvider.class,
    description = "A CLI application built with Picocli and GraalVM native-image.",
    subcommands = {GreetCommand.class})
public class App implements Callable<Integer> {

  @Spec private CommandSpec spec;

  @Override
  public Integer call() {
    spec.commandLine().usage(spec.commandLine().getOut());
    return 0;
  }

  public static void main(String[] args) {
    int exitCode = new CommandLine(new App()).execute(args);
    System.exit(exitCode);
  }
}
