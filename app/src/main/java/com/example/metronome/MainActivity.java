package com.example.metronome;

import android.app.Activity;

import android.content.Context;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;

import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class MainActivity extends Activity {

    //private SimpleExecutor _executor =  new SimpleExecutor();
    private final TimeTick ticker = new TimeTick();
    private MediaPlayer tickPlayer;
    private TapClickHandler tapClickHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditTextListener bpmTextListener = new EditTextListener("bpm");
        EditTextListener meterTextListener = new EditTextListener("meter");

        EditText editTextBpm = (EditText)findViewById(R.id.edit_bpm);
        editTextBpm.setOnKeyListener(bpmTextListener);
        editTextBpm.setText("60");
        bpmTextListener.setEditText(editTextBpm);
        bpmTextListener.setForceClearFocus(true);
        editTextBpm.setOnFocusChangeListener(bpmTextListener);
        bpmTextListener.setValueSetterCallback(ticker);

        EditText editTextMeter = (EditText) findViewById(R.id.edit_meter);
        editTextMeter.setOnKeyListener(meterTextListener);
        editTextMeter.setText("4");
        editTextMeter.setOnFocusChangeListener(meterTextListener);
        meterTextListener.setEditText(editTextMeter);
        meterTextListener.setForceClearFocus(true);
        meterTextListener.setValueSetterCallback(ticker);

        TextView textView = (TextView)findViewById(R.id.textView);

        tickPlayer = MediaPlayer.create(this, R.raw.tick);
        ticker.setMediaPlayer(tickPlayer);
        tapClickHandler = new TapClickHandler();


        ticker.setBpm(60);
        ticker.setMeter(4);
        Handler handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.getData().containsKey("bpm")){
                    int bpm = msg.getData().getInt("bpm");
                    editTextBpm.setText(String.valueOf(bpm));
                    ticker.setBpm(bpm);
                }else {
                    textView.setText(String.valueOf(msg.what));
                }

                //tickPlayer.start();
            }
        };
        ticker.setHandler(handler);
        tapClickHandler.setHandler(handler);


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    public void onClickStart(View v){
        ticker.startTick();
    }

    public void onClickStop(View v){
        tickPlayer.stop();
        ticker.stopTick();
    }

    public void onClickTap(View v){
        tapClickHandler.handle();
    }
}