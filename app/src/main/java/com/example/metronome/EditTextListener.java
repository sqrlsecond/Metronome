package com.example.metronome;

import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class EditTextListener implements View.OnKeyListener, View.OnFocusChangeListener{

    private EditText _editText;
    private int _value;
    private boolean _forceClearFocus = false;
    private ValueSetterCallback _valueCallback;
    private String _valueName;
    private int    _oldValue = 1;

    EditTextListener(){}

    EditTextListener(String valueName){
        _valueName = valueName;
    }
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
            /*if(_editText.getText().length() == 0){
                _value = _oldValue;
            }else{
                _value = Integer.parseInt(_editText.getText().toString());
            }*/
            //_valueCallback.callback(_valueName, _value);
            if(_forceClearFocus){
                new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        _editText.clearFocus();
                    }
                }, 10);
            }
        }
        return false;
    }

    public void setEditText(EditText editText)
    {
        _editText = editText;
    }

    public void setForceClearFocus(boolean state){
        _forceClearFocus = state;
    }

    public void setValueSetterCallback(ValueSetterCallback callback)
    {
        _valueCallback = callback;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            _oldValue = Integer.parseInt(_editText.getText().toString());
        }
        else {
            if(_editText.getText().length() == 0){
                _editText.setText(String.valueOf(_oldValue));
                return;
            }
            _value = Integer.parseInt(_editText.getText().toString());
            _valueCallback.callback(_valueName, _value);
        }
    }

}
