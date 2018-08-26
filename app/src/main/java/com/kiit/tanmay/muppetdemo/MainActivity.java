package com.kiit.tanmay.muppetdemo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import kiit.tanmay.MultipurposeTextView.HandleDataTypeInterface;
import kiit.tanmay.MultipurposeTextView.MuppetUtils;
import kiit.tanmay.MultipurposeTextView.MuppetView;

public class MainActivity extends AppCompatActivity implements HandleDataTypeInterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MuppetUtils.getInstance().setHandleDataTypeInterface(this);
    }

    @Override
    public void onGetContactString(String number, MuppetView muppetView) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count, MuppetView muppetView) {
        Log.d(MainActivity.class.getSimpleName() , s.toString());
    }
}
