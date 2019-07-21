package com.asmaa.googlecalendertask.view;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.asmaa.googlecalendertask.Adapter.MyGridAdapter;
import com.asmaa.googlecalendertask.R;
import com.asmaa.googlecalendertask.database.DBStructure;
import com.asmaa.googlecalendertask.database.DataBaseHelper;
import com.asmaa.googlecalendertask.model.Events;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CustomCalendarView extends LinearLayout {
    ImageButton NextButton , PerevoiusButton;
    TextView CurrentDate;
    GridView gridView ;
    private static final int MAX_CALANDER_DAY = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    DataBaseHelper dbOpenHelper ;
    Context context;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM yyyy",Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM",Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy",Locale.ENGLISH);
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    MyGridAdapter myGridAdapter;
    AlertDialog alertDialog;
    List<Date> dateList = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();



    public CustomCalendarView(Context context) {
       super(context);
   }

    public CustomCalendarView(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context ;
        initializeLayout();
        setUpCalender();

        PerevoiusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH,-1);
                setUpCalender();
            }
        });

        NextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH,1);
                setUpCalender();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                final View addview = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_newevent_layout,null);
                final EditText EventName = addview.findViewById(R.id.eventtype_edittext);
                final TextView EventTime = addview.findViewById(R.id.eventtime_textview);
                ImageButton setTime = addview.findViewById(R.id.eventTime_button);
                Button AddEvent = addview.findViewById(R.id.addevent_button);

                setTime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar calendar = Calendar.getInstance();
                        final int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minuts = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(addview.getContext(), R.style.Theme_AppCompat_Dialog,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                        Calendar c = Calendar.getInstance();
                                        c.set(calendar.HOUR_OF_DAY,hours);
                                        c.set(calendar.MINUTE,minute);
                                        c.setTimeZone(TimeZone.getDefault());
                                        SimpleDateFormat hformate = new SimpleDateFormat("K:mm a ",Locale.ENGLISH );
                                        String eventTime = hformate.format(c.getTime());
                                        EventTime.setText(eventTime);

                                    }
                                },hours,minuts,false);
                                  timePickerDialog.show();
                    }
                });

                final String date = eventDateFormat.format(dateList.get(position));
                final String month = monthFormat.format(dateList.get(position));
                final String year = yearFormat.format(dateList.get(position));

                AddEvent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       saveEvent(EventName.getText().toString(),EventTime.getText().toString(),date,month,year);
                       setUpCalender();
                       alertDialog.dismiss();
                    }
                });

                builder.setView(addview);
                alertDialog=builder.create();
                alertDialog.show();
            }
        });

    }



    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    private void saveEvent(String event , String time , String date , String month , String year){

        dbOpenHelper = new DataBaseHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvents(event,time,date,month,year,database);
        dbOpenHelper.close();
        Toast.makeText(context, "Event Saved ", Toast.LENGTH_SHORT).show();
    }



    private void initializeLayout(){

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_layout_calender,this);
        NextButton=view.findViewById(R.id.next_Button);
        PerevoiusButton=view.findViewById(R.id.previous_Button);
        CurrentDate =view.findViewById(R.id.currentDate_textview);
        gridView=view.findViewById(R.id.gridview);


    }

    private void setUpCalender() {

        String currentDate = simpleDateFormat.format(calendar.getTime());
        CurrentDate.setText(currentDate);
        dateList.clear();
        Calendar monthCalender = (Calendar) calendar.clone();
        monthCalender.set(Calendar.DAY_OF_MONTH,1);
        int FristDayOfMonth = monthCalender.get(calendar.DAY_OF_WEEK)-1;
        monthCalender.add(calendar.DAY_OF_MONTH,-FristDayOfMonth);
        collectEventsPerMonth(monthFormat.format(calendar.getTime()),yearFormat.format(calendar.getTime()));

        while (dateList.size() < MAX_CALANDER_DAY){
            dateList.add(monthCalender.getTime());
            monthCalender.add(Calendar.DAY_OF_MONTH,1);
        }

        myGridAdapter = new MyGridAdapter(context,dateList,calendar,eventsList);
        gridView.setAdapter(myGridAdapter);

    }

    private void collectEventsPerMonth(String month , String year){
        dbOpenHelper = new DataBaseHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventsMonth(month,year,database);

        while (cursor.moveToNext()){
            eventsList.clear();
            String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
            String date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
            String months = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
            String years = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));

            Events events = new Events(event,time,date,months,years);
            eventsList.add(events);

        }

        cursor.close();
        dbOpenHelper.close();
    }

}
