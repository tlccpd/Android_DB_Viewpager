package com.trwth.member;

import java.io.File;
import java.util.ArrayList;

import com.trwth.member.ListMain.DataAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity{
	
	String ident;
	String ag;
	String gen;
	
	Context context;
	DbAdapter dba;
	
	
	EditText name;
	EditText age;
	
	RadioButton gender_m;
	RadioButton gender_w;
	
	Button cancel;
	Button confirm;
	Button show;
	Button reset;
	Button img_file;
	
	DatabaseHelper dbhelp;
	Data datum;
	
	Bitmap bmp;
	String img_path;
	Uri uri;

	
	AlertDialog.Builder builder;
	
	private ViewPager viewPager;
	
	private Pager_Adapter page_adapter;
	
	public static final int REQ_PICK_IMG=1;
	public static final int REQ_SETTING=2;
	public static final int REQ_SHOW=3;
	
	AlertDialog.Builder alert;
	
	public OnClickListener mButtonClick=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			
			switch(v.getId()){
			
			case R.id.img:
			{
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
				intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, REQ_PICK_IMG);
			}	
				break;
				
			case R.id.cancel:
			{
				name.setText("");
				age.setText("");
				
				if(gender_m.isChecked())
					gender_m.setChecked(false);
				else if(gender_w.isChecked())
					gender_w.setChecked(false);
					
			}	
			break;
			case R.id.confirm:				
				try{
					if(gender_m.isChecked()){
						img_path = uri.toString();
						datum=new Data(name.getText().toString(),age.getText().toString(),null,img_path);
						
						gen = "MAN";
						datum.setGender(gen);
						dba.createMember(datum.getName(),datum.getAge(),datum.getGender(),datum.getPath());
						
					}
					else if(gender_w.isChecked()){
						img_path = uri.toString();
						datum=new Data(name.getText().toString(),age.getText().toString(),null,img_path);
						
						gen="WOMAN";
						datum.setGender(gen);
						dba.createMember(datum.getName(),datum.getAge(),datum.getGender(),datum.getPath());
										
					}
				}catch(Exception e){
					alert = builder.setPositiveButton("OK",new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "Move to gallery", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(Intent.ACTION_PICK);
							intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
							intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							startActivityForResult(intent, REQ_PICK_IMG);
						}						
					});
					alert.setTitle("MISSING IMAGE");
					alert.setMessage("Add Image,or not?");
					alert.create();
					alert.show();
				}
				
			break;
			
			case R.id.reset:
			{	
				dba.deleteAll();
				
			}
			break;
			
			case R.id.list_show:
			{				
				Intent intent_show = new Intent(MainActivity.this,ListMain.class);
				startActivityForResult(intent_show,REQ_SHOW);				
			}
			break;
			
			}
		}			
	};
		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_viewpager_layout);
		
		/*cancel.setOnClickListener(mButtonClick);
		confirm.setOnClickListener(mButtonClick);
		show.setOnClickListener(mButtonClick);
		reset.setOnClickListener(mButtonClick);
		img_file.setOnClickListener(mButtonClick);
		
		name=(EditText)findViewById(R.id.name);
		age=(EditText)findViewById(R.id.age);
		
		cancel = (Button)findViewById(R.id.cancel);
		confirm = (Button)findViewById(R.id.confirm);
		show = (Button)findViewById(R.id.list_show);
		reset = (Button)findViewById(R.id.reset);
		img_file = (Button)findViewById(R.id.img);
		
		gender_m = (RadioButton)findViewById(R.id.radioButton1);
		gender_w = (RadioButton)findViewById(R.id.radioButton2);	*/
	
		dba=new DbAdapter(this);
		dba.open();
		
		setViewPager();
		
		setActionBarTab();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == REQ_PICK_IMG){
			
			if (resultCode == Activity.RESULT_OK){				
				Cursor c = getContentResolver().query(data.getData(), null,null,null,null);
				c.moveToNext();
				String path = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
				uri = Uri.fromFile(new File(path));
				
				c.close();
			}
		}
		if(requestCode == REQ_SETTING){
			if(resultCode == Activity.RESULT_OK){
				setOption();
			}
		}
		
	}

	private void setOption() {
		// TODO Auto-generated method stub
		SharedPreferences prefs = getSharedPreferences("com.trwth.member", 0);
		String option= prefs.getString(this.getResources().getString(R.string.selected_sort_option), this.getResources().getString(R.string.sort_option_default));
		String[] option_txt=this.getResources().getStringArray(R.array.sort_options);
		
		Toast.makeText(getApplicationContext(), option_txt[Integer.parseInt(option)], Toast.LENGTH_SHORT).show();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() ==R.id.action_settings){
			Intent intent = new Intent(MainActivity.this,SettingActivity.class);
			startActivityForResult(intent, REQ_SETTING);
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void mOnClick(View v) {
		switch (v.getId()) {
		case R.id.tab_title1:
			viewPager.setCurrentItem(0);
			break;
		case R.id.tab_title2:
			viewPager.setCurrentItem(1);
			break;
		
		}
	}
	public void setActionBarTab() {
		ActionBar actionBar = getSupportActionBar();
		//액션바에 타이틀 및 홈 이미지 숨기기
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		
		// 액션바 네비게이션 모드를 탭으로 변경
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		actionBar.addTab(actionBar.newTab().setText("INFORMATION").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("PROFILE").setTabListener(tabListener));
	}
	
	ActionBar.TabListener tabListener = new ActionBar.TabListener() {
		
		@Override
		public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {}
		
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction arg1) {
			
			// 뷰페이저를 해당 위치로 이동시킴
			viewPager.setCurrentItem(tab.getPosition());
			
		}
		
		@Override
		public void onTabReselected(Tab arg0, FragmentTransaction arg1) {}


	};
	
	// 뷰페이저 셋팅
	public void setViewPager() {
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		page_adapter = new Pager_Adapter(this);
		viewPager.setAdapter(page_adapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				
				getSupportActionBar().setSelectedNavigationItem(position);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
	}
	private class Pager_Adapter extends PagerAdapter{

		private LayoutInflater mInflater;
		
		
		public Pager_Adapter(Context context) {
	        super();
	        this.mInflater = LayoutInflater.from(context);
	    	dba=new DbAdapter(context);
    		dba.open();
	    }

		@Override
		public Object instantiateItem(View pager, int position) {
			// TODO Auto-generated method stub
			 View v = null;
		        
		        switch(position){
		        case 0:
		        {
		            v = mInflater.inflate(R.layout.activity_main, null);
		            
		            name=(EditText)v.findViewById(R.id.name);
		    		age=(EditText)v.findViewById(R.id.age);
		    		
		    		cancel = (Button)v.findViewById(R.id.cancel);
		    		confirm = (Button)v.findViewById(R.id.confirm);
		    		show = (Button)v.findViewById(R.id.list_show);
		    		reset = (Button)v.findViewById(R.id.reset);
		    		img_file = (Button)v.findViewById(R.id.img);
		    		
		    		gender_m = (RadioButton)v.findViewById(R.id.radioButton1);
		    		gender_w = (RadioButton)v.findViewById(R.id.radioButton2);
		    		
		            v.findViewById(R.id.list_show).setOnClickListener(mButtonClick);
		            v.findViewById(R.id.confirm).setOnClickListener(mButtonClick);
		            v.findViewById(R.id.cancel).setOnClickListener(mButtonClick);
		            v.findViewById(R.id.img).setOnClickListener(mButtonClick);
		            v.findViewById(R.id.reset).setOnClickListener(mButtonClick);	           
		        }  
		            break;
		        case 1:{
		        	v = mInflater.inflate(R.layout.list_main, null);
		        		    		 
		    		ArrayList<Data> alist = dba.fetchAllMembers();
		    		
		    		DataAdapter adapter = new DataAdapter(getApplicationContext(),alist);
		        	ListView list = (ListView)v.findViewById(R.id.list);
		        	
		        	list.setAdapter(adapter);
		        }
		            
		            break;
		        
		        }
		        
		        ((ViewPager)pager).addView(v, 0);
		        
		        return v;

		}

		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public boolean isViewFromObject(View v, Object obj) {
			// TODO Auto-generated method stub
			return v == obj;
		}

		@Override
		public void destroyItem(View pager, int position, Object view) {
			// TODO Auto-generated method stub
			 ((ViewPager)pager).removeView((View)view);
		}

	}

}
