package com.ethicstech.ubox.Terms;

import com.ethicstech.ubox.R;
import com.ethicstech.ubox.Main.MainActivity;
import com.ethicstech.ubox.Network.Check_Network;
import com.ethicstech.ubox.Scroll.ScrollViewExt;
import com.ethicstech.ubox.Scroll.ScrollViewListener;
import com.ethicstech.ubox.Utill.SoundOnClick;
import com.ethicstech.ubox.Utill.SoundOnScroll;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class TermsActivity extends Activity implements ScrollViewListener {
	/*******************************************************/
	Runnable network_runnable;
	Handler networkhandler;
	boolean wifiavailable = false;
	public boolean isnetworkavailable = false;
	/*******************************************************/
	ImageView log, wifi_status;
	ScrollViewExt scroll;
	TextView paragraph, agree, disagree, currently_focus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.terms);
		/**************************************************/
		 SharedPreferences prefs =
		 PreferenceManager.getDefaultSharedPreferences(this);
		 boolean value=prefs.getBoolean("TERMS",false);
		 if(value){
		 Intent i = new Intent(TermsActivity.this,MainActivity.class);
		 startActivity(i);
		 TermsActivity.this.finish();
		 }
		 /**************************************************/
		log = (ImageView) findViewById(R.id.log_imageView);
		wifi_status = (ImageView) findViewById(R.id.wifi_status);
		paragraph = (TextView) findViewById(R.id.paragraph);
		agree = (TextView) findViewById(R.id.agree_textView);
		disagree = (TextView) findViewById(R.id.disagree_textView);
		log.getLayoutParams().width = 200;

		scroll = (ScrollViewExt) findViewById(R.id.scrollView1);
		scroll.setScrollViewListener(this);

		disagree.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				disagree_onclick();
			}
		});
		agree.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				agree_onclick();
			}
		});

		/************************************************************************************************/
		network_runnable = new Runnable() {
			public void run() {
				Check_Network check = new Check_Network(getApplicationContext());
				if (check.isNetworkAvailable()) {
					isnetworkavailable = true;
				} else {
					isnetworkavailable = false;
				}

				if (isnetworkavailable) {
					if (!wifiavailable) {
						wifi_status.setVisibility(View.VISIBLE);
						wifiavailable = true;
					}
				} else {
					if (wifiavailable) {
						wifi_status.setVisibility(View.INVISIBLE);
						wifiavailable = false;
					}
				}

				networkhandler.postDelayed(network_runnable, 2000);
			}
		};
		networkhandler = new Handler();
		networkhandler.postDelayed(network_runnable, 1000);
		/************************************************************************************************/

	}

	@Override
	public void onScrollChanged(ScrollViewExt scrollView, int x, int y,
			int oldx, int oldy) {
		View view = (View) scrollView
				.getChildAt(scrollView.getChildCount() - 1);
		int diff = (view.getBottom() - (scrollView.getHeight() + scrollView
				.getScrollY()));
		currently_focus = paragraph;
		new SoundOnScroll(getApplicationContext());
		// if diff is zero, then the bottom has been reached
		if (diff == 0) {
			// do stuff
			new SoundOnScroll(getApplicationContext());
			currently_focus = agree;
			agree.requestFocus();
			agree.setBackgroundResource(R.drawable.terms_focus_on);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_DPAD_CENTER == keyCode
				|| KeyEvent.KEYCODE_ENTER == keyCode) {
			if (currently_focus == agree) {
				//Agree 
				agree_onclick();
			} else if (currently_focus == disagree) {
				//Disagree
				disagree_onclick();
			}
			return true;
		} else if (KeyEvent.KEYCODE_DPAD_LEFT == keyCode) {
			if (currently_focus == agree) {
				disagree.requestFocus();
				currently_focus = disagree;
				disagree.setBackgroundResource(R.drawable.terms_focus_on);
				agree.setBackgroundResource(R.drawable.terms_focus_off);
				new SoundOnScroll(getApplicationContext());
			} else if (currently_focus == disagree) {
				agree.requestFocus();
				currently_focus = agree;
				agree.setBackgroundResource(R.drawable.terms_focus_on);
				disagree.setBackgroundResource(R.drawable.terms_focus_off);
				new SoundOnScroll(getApplicationContext());
			}
			return true;
		} else if (KeyEvent.KEYCODE_DPAD_RIGHT == keyCode) {
			if (currently_focus == agree) {
				disagree.requestFocus();
				currently_focus = disagree;
				disagree.setBackgroundResource(R.drawable.terms_focus_on);
				agree.setBackgroundResource(R.drawable.terms_focus_off);
				new SoundOnScroll(getApplicationContext());
			} else if (currently_focus == disagree) {
				agree.requestFocus();
				currently_focus = agree;
				agree.setBackgroundResource(R.drawable.terms_focus_on);
				disagree.setBackgroundResource(R.drawable.terms_focus_off);
				new SoundOnScroll(getApplicationContext());
			}

			return true;
		} else if (KeyEvent.KEYCODE_DPAD_UP == keyCode) {
			if (currently_focus == agree) {
				scroll.requestFocus(ScrollView.FOCUS_UP);
				currently_focus = paragraph;
				agree.setBackgroundResource(R.drawable.terms_focus_off);
				new SoundOnScroll(getApplicationContext());
			} else if (currently_focus == disagree) {
				scroll.requestFocus(ScrollView.FOCUS_UP);
				currently_focus = paragraph;
				disagree.setBackgroundResource(R.drawable.terms_focus_off);
				new SoundOnScroll(getApplicationContext());
			}

			return true;

		} else if (KeyEvent.KEYCODE_DPAD_DOWN == keyCode) {

			if (currently_focus == agree) {
				disagree.requestFocus();
				currently_focus = disagree;
				disagree.setBackgroundResource(R.drawable.terms_focus_on);
				agree.setBackgroundResource(R.drawable.terms_focus_off);
				new SoundOnScroll(getApplicationContext());
			} else if (currently_focus == disagree) {
				agree.requestFocus();
				currently_focus = agree;
				agree.setBackgroundResource(R.drawable.terms_focus_on);
				disagree.setBackgroundResource(R.drawable.terms_focus_off);
				new SoundOnScroll(getApplicationContext());
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/*
	 * disagree_onclick
	 * *********************************************************
	 */
	private void disagree_onclick() {
		new SoundOnClick(getApplicationContext());
		CreateAlert();
	}

	private void CreateAlert() {
		AlertDialog alertDialog = new AlertDialog.Builder(TermsActivity.this)
				.create();
		alertDialog.setTitle("RUBIX");
		alertDialog.setMessage("You must agree to the terms to continue, are you sure you want to quit?");
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		alertDialog.show();
	}

	/*****************************************************************/
	private void agree_onclick() {
		new SoundOnClick(getApplicationContext());
		Nextpage();
	}

	private void Nextpage() {
		Intent positveActivity = new Intent(getApplicationContext(),
				MainActivity.class);
		startActivity(positveActivity);
		storepreference_value();
		TermsActivity.this.finish();
	}

	private void storepreference_value() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("TERMS", true);
		editor.commit();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	
@Override
protected void onDestroy() {
	networkhandler.removeCallbacks(network_runnable);
	super.onDestroy();
	
}
	@Override
	public void onBackPressed() {

	}
}
