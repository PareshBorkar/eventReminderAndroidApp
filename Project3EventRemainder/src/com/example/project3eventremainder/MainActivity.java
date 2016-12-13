package com.example.project3eventremainder;




import android.app.Activity;
import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;




public class MainActivity extends Activity
{
	CalendarView calendarViewEventRemainder;
	
	SqliteClass dbAlert;
	
	AlarmManager AlmMngr;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbAlert = new SqliteClass(getApplicationContext());
        
        calendarViewEventRemainder = (CalendarView) findViewById(R.id.calendarViewEventRemainder);
        
        
        
        calendarViewEventRemainder.setOnDateChangeListener(new OnDateChangeListener() 
        {
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month,int dayOfMonth) 
			{
				month = month+1;
				Toast.makeText(getApplicationContext(),  "Selected Date is\n\n" + dayOfMonth + " / " + month + " / " + year, Toast.LENGTH_LONG).show();
				
				Intent i = new Intent(MainActivity.this,AddEventActivity.class);
				
				i.putExtra("dayOfMonth", dayOfMonth);
				i.putExtra("month",month);
				i.putExtra("year",year);
				
				startActivity(i);
				
			}
		});

        
    }


   
}
