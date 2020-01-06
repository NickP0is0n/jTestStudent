package me.NickP0is0n.jTestStudent.models;

import me.NickP0is0n.jTestStudent.service.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class TestingEnvironment {

    private static final int COMMAND_TIMEOUT = 1000;

    private File input;
    private File output;

    private String command;

    public TestingEnvironment(File input, File output, String command) {
        this.input = input;
        this.output = output;
        this.command = command;
    }

    public int test(String testInput, String testOutput) throws IOException, InterruptedException {
        input.createNewFile();
        output.createNewFile();
        PrintWriter inWriter = new PrintWriter(input);
        inWriter.print(testInput);
        inWriter.close();

        executeCommand();

        String outResult = FileUtils.readFileToString(output.getName());
        int good = 0;
        if (outResult.equals(testOutput)) {
            good++;
        }
        input.delete();
        output.delete();

        return good;
    }

    private void executeCommand() throws IOException, InterruptedException {
        Runtime.getRuntime().exec(command);
        Thread.sleep(TestingEnvironment.COMMAND_TIMEOUT);
    }
}
