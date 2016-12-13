package com.example.project3eventremainder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteClass
{
	
	static String DB_NAME = "dummy";
	static int DB_VER = 1;
	static String DB_PATH;

	DBhelper helper;
	SQLiteDatabase database;
	Context context;
	
	class DBhelper extends SQLiteOpenHelper 
	{

		public DBhelper(Context context)
		{
			super(context, DB_NAME, null, DB_VER);
		}

		@Override
		public void onCreate(SQLiteDatabase arg0) 
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
		{
			// TODO Auto-generated method stub

		}

		
		public void createDatabase() 
		{
			// TODO Auto-generated method stub
			boolean pathExist = checkDB();
			
			if (pathExist) 
			{// do nothing
			
				Log.i("TAG", "database Exists");
			}
			else
			{
				this.getReadableDatabase();

				try 
				{
					CopyDataBase();
					
				} catch (IOException e)
				{
					e.printStackTrace();
				}

			}

		}

		private void CopyDataBase() throws IOException
		{

			Log.i("TAG", "Copying Database.");
			
			InputStream myInput = context.getAssets().open(DB_NAME + ".sqlite");
			String outFileName = DB_PATH + "";
			
			Log.i("TAG", "output file path:" + DB_PATH);
			
			OutputStream myOutput = new FileOutputStream(outFileName);

			byte[] buffer = new byte[1024];
			int length;
			
			while ((length = myInput.read(buffer)) > 0) 
			{
				myOutput.write(buffer, 0, length);
			}

			// Close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();
			Log.i("TAG", "Finished copying.");
		}

		
		private boolean checkDB()
		{
			boolean dbBool = false;
			String path = context.getFilesDir().getAbsolutePath().replace("files", "databases")+ File.separator + DB_NAME;
			File Fpath = new File(path);
			dbBool = Fpath.exists();

			return dbBool;
		}

	}
	
	public SqliteClass(Context c) 
	{

		context = c;
		DB_PATH = context.getDatabasePath(DB_NAME).getAbsolutePath();

		DBhelper helper = new DBhelper(context);
		helper.createDatabase();
	}

	
	
	public void open() 
	{
		helper = new DBhelper(context);
		database = helper.getWritableDatabase();

	}
	
	

	public void close() 
	{

		helper.close();

	}
	
	public void insert(int dayOfMonth, int month,int year,int HourOfdayPass,int MinuteOfDayPass,String StrDescription)
	{
		String query = "insert into event_remainder(day, month, year, hour, minute ,description) values(" + dayOfMonth + ", " + month + "," + year + "," + HourOfdayPass + "," + MinuteOfDayPass + ",'"+ StrDescription + "')";
		database.execSQL(query);
		Log.i("databaseInsert", "data been inserted into database");
	}
	
	
	public String getdetails()
	{

		String description = null;
		String query = "SELECT day, month, year, hour, minute ,description FROM event_remainder"; 
		
		Cursor cursor = database.rawQuery(query, null);
		
		Log.d("dbInsert", "select query is fired from cursor");
		
		cursor.moveToLast();
		Log.d("dbInsert", "cursor moved to last");
		
		cursor.moveToFirst();
		
		 if (cursor.moveToFirst())
		 {
			  int i=0;
		        do 
		        {
		      	
			Log.d("dbInsert", "inside the cursors do while loop "+ i);
			
		    String day = cursor.getString(0);
			String month = cursor.getString(1);
			String year = cursor.getString(2);
			String hour = cursor.getString(3);
			String minute = cursor.getString(4);
			description = cursor.getString(5);
	
			Log.d("dbInsert", day +","+month+","+year+","+hour+","+minute+","+description);
			i++;

			
		        } while (cursor.moveToNext());
		    }
		 
		 
		 
		
		Log.d("dbInsert", "out of the cursors for loop");
		
		cursor.close();
		return description;

	}
	

}
