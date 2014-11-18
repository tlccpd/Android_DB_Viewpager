package com.trwth.member;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DatabaseHelper extends SQLiteOpenHelper{  
	private static String DATABASE_NAME = "trwth.db";
	private static int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = "create table member(_id integer primary key autoincrement,"+ "name text not null, age text not null, gender text not null, path text);";  
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);         
		// TODO Auto-generated constructor stub     
		}
	public void onCreate(SQLiteDatabase db){
		db.execSQL(DATABASE_CREATE);     
	}
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		Log.w("member", "Upgrading db from version" + oldVersion + " to" + newVersion + ", which will destroy all old data");         
		db.execSQL("DROP TABLE IF EXISTS member");         
		onCreate(db);     
		}       
}