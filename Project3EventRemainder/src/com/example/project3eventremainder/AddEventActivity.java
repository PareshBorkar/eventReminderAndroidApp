package com.example.project3eventremainder;



import java.util.Calendar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Reminders;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class AddEventActivity extends Activity
{
	TextView tvDateDisplayEventRemainder;
	EditText etEventDiscriptionEventRemainder,etEventTittleEventRemainder;
	Button btnSaveToDataBaseEventRemainder;
	TimePicker tpTimeOfAlertEventRemainder;

	Intent intentGet;
	SqliteClass db;
	
	Context actvity = AddEventActivity.this;
	
	int HourOfdayPass=9,MinuteOfDayPass=00;
	int dayOfMonth,month,year;
	
	int sYear,sMonth,sDay,eYear,eMonth,eDay;
	
	String strDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_event_layout);
		bridges();
		
		db = new SqliteClass(getApplicationContext());
		
		
		intentGet = getIntent();
		dayOfMonth = intentGet.getIntExtra("dayOfMonth", 0);
		month = intentGet.getIntExtra("month", 0);
	    year = intentGet.getIntExtra("year", 0);
	    
	    strDate = dayOfMonth+"-"+month+"-"+year;
		
		tvDateDisplayEventRemainder.setText(strDate);
		
		tpTimeOfAlertEventRemainder.setIs24HourView(true);
		
		tpTimeOfAlertEventRemainder.setOnTimeChangedListener(new OnTimeChangedListener() 
		{
			
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) 
			{
				HourOfdayPass = hourOfDay;
				MinuteOfDayPass = minute;
			}
		});
	
		
		
		
		
		
		btnSaveToDataBaseEventRemainder.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				String StrDescription = etEventDiscriptionEventRemainder.getText().toString();
				String StrTitle = etEventTittleEventRemainder.getText().toString();
				
				addReminder(strDate, strDate,HourOfdayPass,MinuteOfDayPass,StrDescription,StrTitle);
				
				
			}
		});
		
	}

	private void bridges() 
	{
		tvDateDisplayEventRemainder = (TextView) findViewById(R.id.tvDateDisplayEventRemainder);
		tpTimeOfAlertEventRemainder = (TimePicker) findViewById(R.id.tpTimeOfAlertEventRemainder);
		etEventDiscriptionEventRemainder = (EditText) findViewById(R.id.etEventDiscriptionEventRemainder);
		etEventTittleEventRemainder = (EditText) findViewById(R.id.etEventTittleEventRemainder);
		btnSaveToDataBaseEventRemainder = (Button) findViewById(R.id.btnSaveToDataBaseEventRemainder);
		
	}
	
	
	
	
	
	
	
	
	private void addReminder(String startDate, String endDate,int sTimeHr,int sTimeMin, String strEventDecription,String strEventTitle) 
	{
	    // TODO Auto-generated method stub
	    int shour=sTimeHr;
	    int smin =sTimeMin;
	    
	    int ehour=20;
	    int emin =00;
	    
	    ContentResolver cr= actvity.getContentResolver();

	    Calendar beginTime=Calendar.getInstance();
	    
	    
	    String[] sDat=startDate.split("-");
	    String[] eDat=endDate.split("-");
	    
	    sDay=Integer.parseInt(sDat[0]);
	    sMonth=Integer.parseInt(sDat[1]);
	    sYear=Integer.parseInt(sDat[2]);

	     eDay=Integer.parseInt(eDat[0]);
	     eMonth=Integer.parseInt(eDat[1]);
	     eYear=Integer.parseInt(eDat[2]);

	    Log.i("msg","StartDay="+sDay+"StartMonth="+sMonth+"startYear"+sYear+"and sale end date"+eDay+"Month"+eMonth+"Year="+eYear); 
	    
	  //month -1 because months are indexed from 0
	    beginTime.set(sYear, sMonth-1, sDay,shour,smin);//minute and hour values given by the user

	    long startTime=beginTime.getTimeInMillis();
	    
	    
	    Calendar endTime=Calendar.getInstance();

	    endTime.set(eYear,eMonth-1,eDay,ehour,emin);//default value given at 8pm
	    
	    long end1=endTime.getTimeInMillis();
	    
	    	ContentValues calEvent = new ContentValues();
	        calEvent.put(CalendarContract.Events.CALENDAR_ID, 1); // XXX pick)
	        calEvent.put(CalendarContract.Events.TITLE, strEventTitle);
	        calEvent.put(CalendarContract.Events.DESCRIPTION, strEventDecription);
	        calEvent.put(CalendarContract.Events.DTSTART, startTime);
	        calEvent.put(CalendarContract.Events.DTEND, end1);
	        calEvent.put(CalendarContract.Events.HAS_ALARM, 1);
	        calEvent.put(CalendarContract.Events.EVENT_TIMEZONE, CalendarContract.Calendars.CALENDAR_TIME_ZONE);
	        
	        Uri uri =cr.insert(CalendarContract.Events.CONTENT_URI, calEvent);

	        // The returned Uri contains the content-retriever URI for 
	        // the newly-inserted event, including its id
	        int id = Integer.parseInt(uri.getLastPathSegment());

	       // String reminderUriString = "content://com.android.calendar/reminders";

	        ContentValues reminders = new ContentValues();
	        reminders.put(Reminders.EVENT_ID,id);
	        reminders.put(Reminders.METHOD, Reminders.METHOD_ALERT);
	        reminders.put(Reminders.MINUTES, 60);

	        Uri uri2 = cr.insert(Reminders.CONTENT_URI, reminders);
	        Toast.makeText(actvity, "Reminder have been saved succesfully in the calendar!!!", Toast.LENGTH_SHORT).show();
	    }  
	
	
	
	
	
	
	
}
