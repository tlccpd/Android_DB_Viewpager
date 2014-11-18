package com.trwth.memberfragment;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class ProfileListFragment extends ListFragment{

	private ProfileListItemClick itemClick;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if(getActivity() instanceof ProfileListItemClick){
			itemClick = (ProfileListItemClick)getActivity();
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		if(itemClick !=null)
			itemClick.onListItemClick(position);
			
	}
	
	public interface ProfileListItemClick{
		public void onListItemClick(int position);
	}
}