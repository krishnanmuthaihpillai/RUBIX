package com.ethicstech.ubox.Main;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.ethicstech.ubox.R;
import com.ethicstech.ubox.Entertainment.EntertainmentActivity;
import com.ethicstech.ubox.Network.Check_Network;
import com.ethicstech.ubox.Utill.SoundOnClick;
import com.ethicstech.ubox.Utill.SoundOnScroll;
import com.ethicstech.ubox.Wifi.WifiActivity;
import com.ethicstech.ubox.time_from_Internet.Get_CurrentTime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	/*******************************************************/
	Runnable network_runnable;
	Handler networkhandler;
	/*******************************************************/
	TextView wifi_text, entertainment_text, setting_text, time_text;
	ImageView wifi, entertainment, setting, wifistatus, log,currentfoucusImage;
//	public static int current_position = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		time_text = (TextView) findViewById(R.id.my_time_text);
		wifi = (ImageView) findViewById(R.id.wifi_imageView);
		entertainment = (ImageView) findViewById(R.id.entertainment_imageView);
		setting = (ImageView) findViewById(R.id.settings_imageView);
		wifi_text = (TextView) findViewById(R.id.wifi_textView);
		entertainment_text = (TextView) findViewById(R.id.Entertainment_textView);
		setting_text = (TextView) findViewById(R.id.setting_textView);
		wifistatus = (ImageView) findViewById(R.id.wifi_status);
		wifi_text.setVisibility(View.INVISIBLE);
		entertainment_text.setVisibility(View.INVISIBLE);
		setting_text.setVisibility(View.INVISIBLE);

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				wifi.setPadding(50, 50, 50, 50);
				entertainment.setPadding(50, 50, 50, 50);
				setting.setPadding(50, 50, 50, 50);
			}
		});

		wifi.requestFocus();
		setzoom(wifi);
		currentfoucusImage=wifi;
		showtext(1);
//		current_position = 1;
//		wifi_text.setVisibility(View.VISIBLE);


		wifi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				wificlick(2);
			}
		});
		entertainment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				entertainmentclick(2);
			}
		});
		setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				settingclick(2);
			}
		});
		/************************************************************************************************/
		network_runnable = new Runnable() {
			public void run() {

				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						Check_Network check = new Check_Network(
								getApplicationContext());
						if (check.isNetworkAvailable()) {
							//Connected to a network
							Get_CurrentTime gt = new Get_CurrentTime();
							final String my_time = gt.mytime(getResources().getString(R.string.url_for_time));
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// connected
									if (((my_time !="") && (my_time != null))&&(my_time.length()!=0)) {
										time_text.setText(my_time);
										wifistatus.setImageResource(R.drawable.wifi_status_on);
										wifistatus.setImageAlpha(225);							
									}
								}
							});

						} else {
							// Not connected to a network
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									wifistatus.setImageResource(R.drawable.wifi_status_on);
									int alphaAmount = 90;
									wifistatus.setImageAlpha(alphaAmount);
//									Log.d("WIFISTATUS", "OFF");
									 String TIME_FORMAT = "hh:mm aa";
									SimpleDateFormat TimeFormat = new SimpleDateFormat(TIME_FORMAT);
									Calendar ATime = Calendar.getInstance();
									String Timein12hourFormat = TimeFormat.format(ATime.getTime());
									time_text.setText(Timein12hourFormat);
								}
							});

						}
					}
				});
				t.start();

				networkhandler.postDelayed(network_runnable, 1000);
			}
		};
		networkhandler = new Handler();
		networkhandler.postDelayed(network_runnable, 500);
		/************************************************************************************************/

		
	}

	

	protected void settingclick(int pos) {
		switch (pos) {
		case 1:
			launch_settings(getApplicationContext());
			break;
		case 2:
             if(currentfoucusImage==wifi){
            	    resetzoom(wifi);
            	    setzoom(setting);
    				currentfoucusImage=setting;
    				showtext(3);
 			
             }else if(currentfoucusImage==entertainment){
            	    resetzoom(entertainment);
            	    setzoom(setting);
    				currentfoucusImage=setting;
    				showtext(3);
             }	
         	launch_settings(getApplicationContext());
			break;
	 }	
	}

	private void launch_settings(Context acontext) {
		new SoundOnClick(acontext);
		Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(dialogIntent);
	}



	protected void entertainmentclick(int pos) {
		
		switch (pos) {
		case 1:
			launch_entertainment(getApplicationContext());
			break;
		case 2:
             if(currentfoucusImage==wifi){
            	    resetzoom(wifi);
            	    setzoom(entertainment);
    				currentfoucusImage=entertainment;
    				showtext(2);
    				
             }else if(currentfoucusImage==setting){
            	    resetzoom(setting);
            	    setzoom(entertainment);
    				currentfoucusImage=entertainment;
    				showtext(2);
             }	
             launch_entertainment(getApplicationContext());
			break;
	 }	
	}

	private void launch_entertainment(Context aContext) {
		new SoundOnClick(aContext);
		Intent i = new Intent(MainActivity.this, EntertainmentActivity.class);
		startActivity(i);
	}



	protected void wificlick(int pos) {
		
		switch (pos) {
		case 1:
			launch_wifi(getApplicationContext());
			break;
		case 2:
             if(currentfoucusImage==entertainment){
            	    resetzoom(entertainment);
            	    setzoom(wifi);
    				currentfoucusImage=wifi;
    				showtext(1);
    				
             }else if(currentfoucusImage==setting){
            	    resetzoom(setting);
            	    setzoom(wifi);
    				currentfoucusImage=wifi;
    				showtext(1);
             }	
             launch_wifi(getApplicationContext());
			break;
	 }	
	}

	private void launch_wifi(Context aContext) {
		new SoundOnClick(aContext);
		Intent i = new Intent(MainActivity.this, WifiActivity.class);
		startActivity(i);
	}



	@Override
	public void onBackPressed() {
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (KeyEvent.KEYCODE_DPAD_CENTER == keyCode
				|| KeyEvent.KEYCODE_ENTER == keyCode) {
			if(currentfoucusImage==wifi){
				wificlick(1);
			}else if(currentfoucusImage==entertainment){
				entertainmentclick(1);
			}else if(currentfoucusImage==setting){
				settingclick(1);
			}
			
			
			
			
			return true;
		} else if (KeyEvent.KEYCODE_DPAD_LEFT == keyCode) {
			new SoundOnScroll(getApplicationContext());
			if(currentfoucusImage==wifi){
				resetzoom(wifi);
				setzoom(setting);
				currentfoucusImage=setting;
				showtext(3);
				
			}else if(currentfoucusImage==setting){
				resetzoom(setting);
				setzoom(entertainment);
				currentfoucusImage=entertainment;
				showtext(2);
				
				
			}else if(currentfoucusImage==entertainment){
				resetzoom(entertainment);
				setzoom(wifi);
				currentfoucusImage=wifi;
				showtext(1);
				
			}
			

			return true;
		} else if (KeyEvent.KEYCODE_DPAD_RIGHT == keyCode) {
			new SoundOnScroll(getApplicationContext());		
			if(currentfoucusImage==wifi){
				resetzoom(wifi);
				setzoom(entertainment);
				currentfoucusImage=entertainment;
				showtext(2);
			
			}else if(currentfoucusImage==entertainment){
				resetzoom(entertainment);
				setzoom(setting);
				currentfoucusImage=setting;
				showtext(3);
				
			}else if(currentfoucusImage==setting){
				resetzoom(setting);
				setzoom(wifi);
				currentfoucusImage=wifi;
				showtext(1);
			}
			
			
			
			
			
			return true;
		} else if (KeyEvent.KEYCODE_DPAD_UP == keyCode) {

			return true;
		} else if (KeyEvent.KEYCODE_DPAD_DOWN == keyCode) {

			return true;

		}

		return super.onKeyDown(keyCode, event);
	}

	private void showtext(final int position) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
		 switch (position) {
			case 1:
				wifi_text.setVisibility(View.VISIBLE);
				entertainment_text.setVisibility(View.INVISIBLE);
				setting_text.setVisibility(View.INVISIBLE);
				break;
			case 2:
				wifi_text.setVisibility(View.INVISIBLE);
				entertainment_text.setVisibility(View.VISIBLE);
				setting_text.setVisibility(View.INVISIBLE);
				break;
			case 3:
				wifi_text.setVisibility(View.INVISIBLE);
				entertainment_text.setVisibility(View.INVISIBLE);
				setting_text.setVisibility(View.VISIBLE);
				break;
		 }
			}
		});
	}



	private void setzoom(final ImageView image) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				float x = image.getScaleX();
				float y = image.getScaleY();
				image.setScaleX((float) (x + 0.1));
				image.setScaleY((float) (y + 0.1));
				image.setPadding(50, 50, 50, 50);
				Animation animation = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.pop_in);
				image.startAnimation(animation);
			}
		});

	}

	private void resetzoom(final ImageView image) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				image.setPadding(50, 50, 50, 50);
				float x = image.getScaleX();
				float y = image.getScaleY();
				image.setScaleX((float) (x - 0.1));
				image.setScaleY((float) (y - 0.1));
			}
		});
	}

}