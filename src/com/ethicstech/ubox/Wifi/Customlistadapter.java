package com.ethicstech.ubox.Wifi;
import java.util.List;

import com.ethicstech.ubox.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Customlistadapter extends ArrayAdapter<String> {
	private final Activity context;
	private final String[] web;
	private final Integer[] image_lock;
	private final Integer[] image_signal;
	private List<Integer> mNetworkList;
	private boolean []mIsOpen;
	
	public Customlistadapter(WifiActivity wifiConnectionActivity,
			String[] wifis,boolean[] isopen, Integer[] image_lock, Integer[] image_signal, List<Integer> networkList) {
		super(wifiConnectionActivity, R.layout.list_item, wifis);
		this.context = wifiConnectionActivity;
		this.web = wifis;
		this.mIsOpen=isopen;
		this.image_lock = image_lock;
		this.image_signal = image_signal;
		mNetworkList=networkList;
	}
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.list_item, null, true);
		
		if(position % 2 == 0 ){
		rowView.setBackgroundResource(R.drawable.list_item_design_first);
   } else {
	
	rowView.setBackgroundResource(R.drawable.list_item_design_second);
   }
		
		
		TextView txtTitle = (TextView) rowView.findViewById(R.id.wifiname);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.imglock);
		ImageView imageView1 = (ImageView) rowView.findViewById(R.id.imgsignal);		
		if(mIsOpen[position]){
			imageView.setImageResource(image_lock[1]);	
		}else{
			imageView.setImageResource(image_lock[0]);	
		}		
		imageView1.setImageResource(image_signal[mNetworkList.get(position)]);
		txtTitle.setText(web[position]);
		return rowView;
	}
}