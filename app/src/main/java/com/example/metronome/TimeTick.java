package com.example.metronome;


import android.media.MediaPlayer;
import android.os.Handler;

public class TimeTick implements Runnable, ValueSetterCallback {

    private int _bpm = 1;
    private int _sleepTime = 1;
    private int _meter = 1;
    private int _currentValue = 1;
    private Handler _handler;
    private boolean _running;
    private MediaPlayer _player;
    private final SimpleExecutor executor = new SimpleExecutor();


    @Override
    public void run() {
        _running = true;
        _currentValue = 1;
        _handler.sendEmptyMessage(_currentValue);
        //play sound
        while (_running) {
            try {
                Thread.sleep(_sleepTime);
                if(!_running){
                    return;
                }
                _currentValue++;
                if(_currentValue > _meter){
                    _currentValue = 1;
                }
                _player.start();
                _handler.sendEmptyMessage(_currentValue);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void calculateSleepTime()
    {
        _sleepTime = 60000 / _bpm;// в мс
    }

    public void setHandler(Handler handler)
    {
        _handler = handler;
    }

    public void setBpm(int bpm)
    {
        _bpm = bpm;
        calculateSleepTime();
    }

    public void setMeter(int meter)
    {
        _meter = meter;
    }

    public void stopTick(){
        _running = false;
        _player.stop();
        _player.prepareAsync();
    }

    public void startTick(){
        if(!_running){

            executor.execute(this);
        }else {
            resetCurrentValue();
        }

    }

    @Override
    public void callback(String valueName, int value) {
        if (valueName.equals("bpm")){
            setBpm(value);
        }else if (valueName.equals("meter")){
            setMeter(value);
        }
    }

    public void resetCurrentValue(){
        _currentValue = 1;
    }

    public void setMediaPlayer(MediaPlayer player){
        _player = player;
    }

}
