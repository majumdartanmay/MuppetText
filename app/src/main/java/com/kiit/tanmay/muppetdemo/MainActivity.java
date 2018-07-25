package com.kiit.tanmay.muppetdemo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import kiit.tanmay.MultipurposeTextView.HandleDataTypeInterface;
import kiit.tanmay.MultipurposeTextView.MuppetView;

public class MainActivity extends AppCompatActivity implements HandleDataTypeInterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onGetContactString(String number, MuppetView muppetView) {

    }
}
