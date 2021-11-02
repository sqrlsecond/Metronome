package com.example.metronome;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;



public class TapClickHandler implements Runnable {

    private SimpleExecutor _executor = new SimpleExecutor();
    private boolean _measureState = false;
    private int _counter = 0; //количество милисекунд * 10 между двумя вызовами handle
    private Handler _handler;

    public void handle(){
        if(!_measureState){
            _executor.execute(this);
            _measureState = true;
            return;
        }

        int bpm = 6000 / _counter;
        Bundle bundle = new Bundle();
        bundle.putInt("bpm", bpm);
        Message message = new Message();
        message.setData(bundle);
        _handler.sendMessage(message);
        _counter = 0;
    }

    @Override
    public void run() {
        while (_counter < 6000) {
            _counter++;
            try {
                Thread.sleep(10);
            }
            catch (InterruptedException ex){
                ex.printStackTrace();
            }

        }
        _measureState = false;
    }

    public void setHandler(Handler handler){
        _handler = handler;
    }
}
