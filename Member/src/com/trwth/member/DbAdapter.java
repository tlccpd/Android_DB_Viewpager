package com.trwth.member;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DbAdapter extends DatabaseHelper{
	 
	 Context ctx;
	 public static String DATABASE_TABLE = "member";
	 public static final String KEY_NAME = "name";
	 public static final String KEY_AGE = "age";
	 public static final String KEY_GEN = "gender";
	 public static final String KEY_PATH = "path";
	 public static final String KEY_ROWID = "_id";
	
	 String name;
	 String age;
	 String gender;
	 String path;
	 
	 private DbAdapter mDbHelper;
	 private SQLiteDatabase mDb;
	 private Data data;
	
	 
	 public DbAdapter(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.ctx=context;
	}
	 
	public void open() throws SQLException{
		mDbHelper = new DbAdapter(ctx);
		try{
		mDb = mDbHelper.getWritableDatabase();
		}catch(Exception e){
			mDb = mDbHelper.getReadableDatabase();
		}
	}
	public void close(){
		mDbHelper.close();
	}
	public long createMember(String name, String age, String gender, String path){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_AGE, age);
		initialValues.put(KEY_GEN, gender);
		initialValues.put(KEY_PATH, path);
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}
	public boolean deleteMember(long rowID){
		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowID, null) > 0;
	}
	public void resetTable(){
		mDb.execSQL("DROP TABLE IF EXISTS member");
		mDb.execSQL("create table member(_id integer primary key autoincrement,"+ "name text not null, age text not null, gender text not null, path text);");
	}
	public void deleteAll(){
		mDb.delete(DATABASE_TABLE, null, null);
	}
		
	public ArrayList<Data> fetchAllMembers(){
		
		ArrayList<Data> list = new ArrayList<Data>();
		Cursor cur= mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_NAME, KEY_AGE, KEY_GEN, KEY_PATH}, null, null, null, null, KEY_NAME +" desc");
		 
		if(cur.moveToFirst()){			
			do{
				name = cur.getString(cur.getColumnIndex(KEY_NAME));
				age = cur.getString(cur.getColumnIndex(KEY_AGE));
				gender = cur.getString(cur.getColumnIndex(KEY_GEN));
				path = cur.getString(cur.getColumnIndex(KEY_PATH));
				data = new Data(name,age,gender,path);		
				list.add(data);				
			}while(cur.moveToNext());		
		}
		return list;
	}
	public Cursor fetchMember(long rowID) throws SQLException{
		Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[]{KEY_ROWID, KEY_NAME, KEY_AGE, KEY_GEN, KEY_PATH}, KEY_ROWID + "=" + rowID, null, null, null, null, null);         
		if(mCursor != null) 
			mCursor.moveToFirst();
		return mCursor;
		}
	public boolean updateMember(long rowID, String name, String age,String gender,String path){
		ContentValues args = new ContentValues();
		args.put(KEY_NAME, name);
		args.put(KEY_AGE, age);
		args.put(KEY_GEN, gender);
		args.put(KEY_PATH, path);
		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowID, null) > 0;		
		}   
}