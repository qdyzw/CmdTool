package io.github.alekseysotnikov.cmd.core;

public interface ICmd {
    ICmd configuring(Listening.BeforeStart... configuring);

    ICmd listening(Listening.BeforeStart... beforeStart);

    ICmd listening(Listening.AfterStart... afterStart);

    ICmd listening(Listening.AfterFinish... afterFinish);

    ICmd listening(Listening.AfterStop... afterStop);

    /**
     * Specify command interpreter
     */
    ICmd interpreter(String interpreter);

    /**
     * Create executable command
     *
     * @param command
     * @return executable command
     */
    Command command(String... command);

    /**
     * Create executable command
     *
     * @param command
     * @return executable command
     */
    Command command(Iterable<String> command);
}
