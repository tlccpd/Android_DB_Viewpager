package com.trwth.memberfragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trwth.member.R;


public class Text_Fragment extends Fragment {

	private TextView textView;
	
	String str;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.text_fragment_layout, container, false);
		textView = (TextView)v.findViewById(R.id.content);
		
		return v;
	}
	public void setTextViewText(String str){
		textView.setText(str);
	}
}
