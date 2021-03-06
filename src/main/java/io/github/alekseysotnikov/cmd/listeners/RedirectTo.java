package io.github.alekseysotnikov.cmd.listeners;

import io.github.alekseysotnikov.cmd.core.Listening;
import org.zeroturnaround.exec.ProcessExecutor;

import java.io.OutputStream;

/**
 * Redirect either output or error stream to another stream, even if the process stopped unexpectedly
 */
public final class RedirectTo implements Listening.BeforeStart {
    private final OutputStream outputStream;
    private final boolean fromErrorStream;

    public RedirectTo(OutputStream outputStream) {
        this(outputStream, false);
    }

    public RedirectTo(OutputStream outputStream, boolean fromErrorStream) {
        this.outputStream = outputStream;
        this.fromErrorStream = fromErrorStream;
    }

    @Override
    public void run(ProcessExecutor processExecutor) {
        if (fromErrorStream) {
            processExecutor.redirectErrorAlsoTo(outputStream);
        } else {
            processExecutor.redirectOutputAlsoTo(outputStream);
        }
    }

}
