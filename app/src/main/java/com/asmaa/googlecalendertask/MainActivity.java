package com.asmaa.googlecalendertask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.asmaa.googlecalendertask.view.CustomCalendarView;

public class MainActivity extends AppCompatActivity {

    CustomCalendarView customCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customCalendarView=findViewById(R.id.custom_calender_view);
    }
}
