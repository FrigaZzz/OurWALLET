package com.mfr.model.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {
	public static final String IP = "http://192.168.43.211:8080";

	public static void setUsername(String username, Context c) {
		SharedPreferences settings = c.getSharedPreferences("taass", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("username",username);
		editor.commit();
	}

	public static void setPassword(String username, Context c) {
		SharedPreferences settings = c.getSharedPreferences("taass", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("password",username);
		editor.commit();
	}



	public static void setToken(String token, Context c) {
		SharedPreferences settings = c.getSharedPreferences("taass", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("token",token);
		editor.commit();
	}



	public static String getUsername(Context c) {
		SharedPreferences settings = c.getSharedPreferences("taass", Context.MODE_PRIVATE);
		return settings.getString("username", "");
	}

	public static String getPassword(Context c) {
		SharedPreferences settings = c.getSharedPreferences("taass", Context.MODE_PRIVATE);
		return settings.getString("password", "");
	}

	public static String getToken(Context c) {
		SharedPreferences settings = c.getSharedPreferences("taass", Context.MODE_PRIVATE);
		return settings.getString("token", "");
	}

	public static String getDate(long date) {
		Date d = new Date(date);

		SimpleDateFormat sdf = new SimpleDateFormat();
		String dataStr = sdf.format(d);

		sdf.applyPattern("dd/MM/yy HH.mm");
		return dataStr;
	}

	public static Date getFromDate() {
		Date date = Calendar.getInstance().getTime();

		int d=date.getDay();
		int m=date.getMonth();
		int y=date.getYear();

	    String day = (String) DateFormat.format("dd",   date);
		Log.e("ssssssss",day);
	    Integer days = Integer.parseInt(day);

		return new Date(y,m-1,days);
	}

	public static Date getToDate() {
		Date date = Calendar.getInstance().getTime();

		int d=date.getDay();
		int m=date.getMonth();
		int y=date.getYear();

		String day = (String) DateFormat.format("dd",   date);

		Integer days = Integer.parseInt(day);

		return new Date(y,m,days+1);
	}



	public static Long getDateFromDatePicker(DatePicker datePicker){
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth();
		int year =  datePicker.getYear();

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);

		return calendar.getTime().getTime();
	}
}