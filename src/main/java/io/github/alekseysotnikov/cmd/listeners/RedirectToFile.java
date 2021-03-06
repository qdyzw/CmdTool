package io.github.alekseysotnikov.cmd.listeners;

import io.github.alekseysotnikov.cmd.core.ProcessListenerAdapter;
import io.github.alekseysotnikov.cmd.core.Listening;
import org.zeroturnaround.exec.ProcessExecutor;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Saves either output or error stream into a file within the working directory, even if the process stopped unexpectedly
 */
public final class RedirectToFile implements Listening.BeforeStart, Listening.AfterStop {
    private final File outputFile;
    private final boolean fromErrorStream;
    private OutputStream outputStream;

    public static RedirectToFile fromOutputStream(String path){
        return new RedirectToFile(path);
    }

    public static RedirectToFile fromErrorStream(String path){
        return new RedirectToFile(path, true);
    }

    public RedirectToFile(String path) {
        this(new File(path), false);
    }

    public RedirectToFile(String path, boolean fromErrorStream) {
        this(new File(path), fromErrorStream);
    }

    public RedirectToFile(File outputFile) {
        this(outputFile, false);
    }

    public RedirectToFile(File outputFile, boolean fromErrorStream) {
        this.outputFile = outputFile;
        this.fromErrorStream = fromErrorStream;
    }

    @Override
    public void run(ProcessExecutor processExecutor) {
        processExecutor.readOutput(true);
        outputStream = createFileOS(processExecutor.getDirectory());
        new RedirectTo(outputStream, fromErrorStream).run(processExecutor);
        processExecutor.addListener(new ProcessListenerAdapter((Listening.AfterStop) this));
    }

    @Override
    public void run(Process process) {
        try {
            outputStream.close();
        } catch (IOException e) {
            throw new UncheckedIOException("Can not close file output stream ", e);
        }
    }

    private OutputStream createFileOS(File workDir) {
        Path outputPath;
        if (outputFile.isAbsolute() || workDir == null) {
            outputPath = Paths.get(outputFile.toURI());
        } else {
            outputPath = Paths.get(workDir.getPath(), outputFile.getPath());
        }
        try {
            return Files.newOutputStream(outputPath, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new UncheckedIOException("Output file " + outputPath + " can not be created", e);
        }
    }
}
