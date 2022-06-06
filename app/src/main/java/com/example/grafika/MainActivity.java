package com.example.grafika;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button mRedBtn;
    private Button mGreenBtn;
    private Button mBlueBtn;
    private Button mBlackBtn;
    private Button mEreaseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRedBtn = findViewById(R.id.red_btn);
        mGreenBtn = findViewById(R.id.green_btn);
        mBlueBtn = findViewById(R.id.blue_btn);
        mBlackBtn = findViewById(R.id.black_btn);
        mEreaseBtn = findViewById(R.id.erease_btn);
    }
}