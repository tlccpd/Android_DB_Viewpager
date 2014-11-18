package com.trwth.member;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.trwth.memberfragment.ProfileListFragment;
import com.trwth.memberfragment.ProfileListFragment.ProfileListItemClick;
import com.trwth.memberfragment.Text_Fragment;

public class ListMain extends Activity implements ProfileListItemClick{

	 DataAdapter adapter;
	 ArrayList<Data> alist; 
	DbAdapter dba;

	public Text_Fragment textfragment;
	public ProfileListFragment listfragment;
	public ListView listview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_main);
		
		/*FragmentManager fgManager = getSupportFragmentManager();
		textfragment = (Text_Fragment)fgManager.findFragmentById(R.id.text_fragment);
		listfragment = (ProfileListFragment)fgManager.findFragmentById(R.id.list_fragment);*/
		listview = (ListView)findViewById(R.id.list);		
		
		dba=new DbAdapter(this);
		dba.open();
		
		//item = intent.getStringArrayExtra("item");
		//listview = (ListView) findViewById(R.id.list);  
		alist = dba.fetchAllMembers();
		
		adapter = new DataAdapter(this,alist);
		
		//listfragment.setListAdapter(adapter);
		listview.setAdapter(adapter);
			
	}	
	
	public static class DataAdapter extends ArrayAdapter<Data>{

		private LayoutInflater mInflater;
		private Bitmap bmp;
		private Context context;
		private List<Data> list;
		 
		public DataAdapter(Context context,ArrayList<Data> list) {
			super(context,0,list);
			// TODO Auto-generated constructor stub
			 mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			 this.context=context;
			 this.list = list;
		}
		@Override
		public View getView(final int position, View v, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			if (v == null)	    
			   v = mInflater.inflate(R.layout.list,null);
		   
		   Data datum = this.getItem(position);
		   
		   if(datum!=null){
			   TextView name=(TextView)v.findViewById(R.id.tv1);
			   TextView age= (TextView)v.findViewById(R.id.tv2);
			   TextView gender= (TextView)v.findViewById(R.id.tv3);
			   			   
			   name.setText(datum.getName());
			   age.setText(datum.getAge());
			   gender.setText(datum.getGender());
			 if(datum.getPath()!=null){			   
			   ImageView image=(ImageView)v.findViewById(R.id.image);
			   bmp= BitmapFactory.decodeFile(datum.getPath().substring(7));
			   image.setImageBitmap(bmp);			   
			 }
		   }
		   		   
		   return v;	   
		}
	}

	@Override
	public void onListItemClick(int position) {
		// TODO Auto-generated method stub
		Data data = adapter.getItem(position);
		
		textfragment.setTextViewText(data.getName()+" : "+data.getGender()+"/"+data.getAge());
	}
}