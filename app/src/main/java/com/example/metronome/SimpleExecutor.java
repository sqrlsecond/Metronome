package com.example.metronome;

import java.util.concurrent.Executor;

public class SimpleExecutor implements Executor {

    private Thread _thread;
    @Override
    public void execute(Runnable command) {
        _thread = new Thread(command);
        _thread.start();
    }
}
