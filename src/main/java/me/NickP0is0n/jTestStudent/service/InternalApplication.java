package me.NickP0is0n.jTestStudent.service;

import me.NickP0is0n.jTestStudent.controllers.TaskSolvingController;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InternalApplication {
    private static final int CODE_COMPILE_TIMEOUT = 3000;

    private String code;
    private String mainClass;

    private boolean isCompiled = false;

    public InternalApplication(String code, String mainClass) {
        this.code = code;
        this.mainClass = mainClass;
    }

    public void compile() throws IOException, InterruptedException {
        File codeFile = new File("Main.java");
        codeFile.createNewFile();
        PrintWriter fileWrite = new PrintWriter(codeFile);
        fileWrite.print(code);
        fileWrite.close();
        File output = new File("output.txt");
        output.createNewFile();
        Runtime.getRuntime().exec("javac Main.java");
        Thread.sleep(InternalApplication.CODE_COMPILE_TIMEOUT);
        isCompiled = true;
    }

    public void removeFiles() throws IOException {
        Files.delete(Paths.get("Main.java"));
        Files.delete(Paths.get("Main.class"));
    }


}
