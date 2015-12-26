package com.ethicstech.ubox.Entertainment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.ethicstech.ubox.R;
import com.ethicstech.ubox.Network.Check_Network;
import com.ethicstech.ubox.Utill.SoundOnClick;
import com.ethicstech.ubox.Utill.SoundOnScroll;
import com.ethicstech.ubox.time_from_Internet.Get_CurrentTime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EntertainmentActivity extends Activity {
	/***********Network check********************************************/
	Runnable network_runnable;
	Handler networkhandler;
	boolean wifiavailable = false;
	public boolean isnetworkavailable = false;
	TextView time_text;
	/*******************************************************/
	AlertDialog levelDialog;
	TextView lefttext, centertext, righttext;
	ImageView wifistatus, log;
	// 1st ImageView
	ImageView youtube,showbox,netfix;
	// 2nd ImageView
	ImageView kodi,hulu,popcorntime;
	// 3rd ImageView
	ImageView pandora,soundcloud,spotify;
	// 4th ImageView
	ImageView iheartradio;
	
	// 1st LinearLayout
	LinearLayout firstLayout;
	// 2nd LinearLayout
	LinearLayout secondLayout;
	// 3rd LinearLayout
	LinearLayout thirdLayout;
	// 4th LinearLayout
	LinearLayout fourthLayout;
// Current focused ImageView 
	ImageView currentfoucusImage;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entertainment);
		/***************************************************************/
		firstLayout = (LinearLayout) findViewById(R.id.firstLayout);
		youtube = (ImageView) findViewById(R.id.youtube);
		showbox = (ImageView) findViewById(R.id.showbox);
		netfix = (ImageView) findViewById(R.id.netflix);
		/***************************************************************/
		secondLayout = (LinearLayout) findViewById(R.id.secondLayout);
		kodi = (ImageView) findViewById(R.id.kodi);
		hulu=(ImageView) findViewById(R.id.hulu);
		popcorntime=(ImageView) findViewById(R.id.popcorntime);
		/***************************************************************/
		thirdLayout = (LinearLayout) findViewById(R.id.thirdLayout);
		pandora = (ImageView) findViewById(R.id.pandora);
		soundcloud = (ImageView) findViewById(R.id.soundcloud);
		spotify = (ImageView) findViewById(R.id.spotify);
		/***************************************************************/
		fourthLayout = (LinearLayout) findViewById(R.id.fourthLayout);
		iheartradio = (ImageView) findViewById(R.id.iheart_radio);
		/***************************************************************/
		lefttext = (TextView) findViewById(R.id.left_1_textView);
		centertext = (TextView) findViewById(R.id.center_2_textView);
		righttext = (TextView) findViewById(R.id.right_3_textView);
		time_text=(TextView) findViewById(R.id.emy_time_text);
		
		/***************************************************************/
		///////////////////////////////////////////////
		youtube.setPadding(50, 50, 50, 50);
		showbox.setPadding(50, 50, 50, 50);
		netfix.setPadding(50, 50, 50, 50);
		////////////////////////////////////////
		kodi.setPadding(50, 50, 50, 50);
		hulu.setPadding(50, 50, 50, 50);
		popcorntime.setPadding(50, 50, 50, 50);
		////////////////////////////////////
		pandora.setPadding(50, 50, 50, 50);
		soundcloud.setPadding(50, 50, 50, 50);
		spotify.setPadding(50, 50, 50, 50);
		//////////////////////////////////////
		iheartradio.setPadding(50, 50, 50, 50);
        ///////////////////////////////////////////////
		secondLayout.setVisibility(View.GONE);
		thirdLayout.setVisibility(View.GONE);
		fourthLayout.setVisibility(View.GONE);
        ///////////////////////////////////////////////
		
		lefttext.setVisibility(View.INVISIBLE);
		centertext.setVisibility(View.INVISIBLE);
		righttext.setVisibility(View.INVISIBLE);

		/*************** First Image ***********************************************/
		firstLayout.setVisibility(View.VISIBLE);
		lefttext.setVisibility(View.VISIBLE);
//		String text = getResources().getString(R.string.youtube_str);
		
		String text=getResources().getString(R.string.youtube_str);
		writetext(text,300,1);
		
//		youtubetext(getResources().getString(R.string.youtube_str),500);
		setzoom(youtube);
		currentfoucusImage = youtube;
		/******************************************************************************************/
		wifistatus = (ImageView) findViewById(R.id.wifi_status);

		/******************************************************************************************/
		youtube.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				youtubeclick();
			}
		});
		
		
	
		
		showbox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showboxclick();
			}
		});
		netfix.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				netfixclick();
			}
		});
		kodi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				kodiclick();
			}
		});
	
		
		hulu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				huluclick();
			}
		});
		popcorntime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popcorntimeclick();
			}
		});
		
		pandora.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pandoraclick();
			}
		});
		soundcloud.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				soundcloudclick();
			}
		});
		spotify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				spotifyclick();
			}
		});
		iheartradio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				iheartradioclick();
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
									wifistatus.invalidate();
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
		networkhandler.postDelayed(network_runnable, 1000);
		/************************************************************************************************/

		
		
		
		
		
		
	}


	@Override
	protected void onDestroy() {
		networkhandler.removeCallbacks(network_runnable);
		super.onDestroy();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (KeyEvent.KEYCODE_DPAD_CENTER == keyCode
				|| KeyEvent.KEYCODE_ENTER == keyCode) {

			if (currentfoucusImage == youtube) {
				youtubeclick();
				return true;
			}
			if (currentfoucusImage == showbox) {
				showboxclick();
				return true;
			}
			if (currentfoucusImage == netfix) {
				netfixclick();
				return true;
			}
			if (currentfoucusImage == kodi) {
				kodiclick();
				return true;
			}
			
			if (currentfoucusImage == hulu) {

				huluclick();
				return true;
			}
			if (currentfoucusImage == popcorntime) {
				popcorntimeclick();
				return true;
			}
			
			if (currentfoucusImage == pandora) {
				pandoraclick();
				return true;
			}
			if (currentfoucusImage == soundcloud) {
				soundcloudclick();
				return true;
			}
			if (currentfoucusImage == spotify) {
				spotifyclick();
				return true;
			}
			if (currentfoucusImage == iheartradio) {
				iheartradioclick();
				return true;
			}

			return false;
		} else if (KeyEvent.KEYCODE_DPAD_LEFT == keyCode) {
			new SoundOnScroll(getApplicationContext());
			// TODO*****************PREVIOUS IMAGE******************
			if (currentfoucusImage == youtube) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						resetzoom(youtube);
						layoutprevoiusanimation(fourthLayout);
						firstLayout.setVisibility(View.GONE);
						secondLayout.setVisibility(View.GONE);
						thirdLayout.setVisibility(View.GONE);
						fourthLayout.setVisibility(View.VISIBLE);
						setzoom(iheartradio);
						currentfoucusImage = iheartradio;
						String text=getResources().getString(R.string.iheartradio_str);
						writetext(text,800,1);
					}
				});

			} else if (currentfoucusImage == iheartradio) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						resetzoom(iheartradio);
						layoutprevoiusanimation(thirdLayout);
						firstLayout.setVisibility(View.GONE);
						secondLayout.setVisibility(View.GONE);
						thirdLayout.setVisibility(View.VISIBLE);
						fourthLayout.setVisibility(View.GONE);
						setzoom(spotify);
						currentfoucusImage = spotify;
						String text=getResources().getString(R.string.spotify_str);
						writetext(text,800,3);
					}
				});

			} else if (currentfoucusImage == spotify) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						resetzoom(spotify);
						firstLayout.setVisibility(View.GONE);
						secondLayout.setVisibility(View.GONE);
						thirdLayout.setVisibility(View.VISIBLE);
						fourthLayout.setVisibility(View.GONE);
						setzoom(soundcloud);
						currentfoucusImage = soundcloud;
						String text=getResources().getString(R.string.soundcloud_str);
						writetext(text,300,2);
					}
				});

			} else if (currentfoucusImage == soundcloud) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						resetzoom(soundcloud);
						firstLayout.setVisibility(View.GONE);
						secondLayout.setVisibility(View.GONE);
						thirdLayout.setVisibility(View.VISIBLE);
						fourthLayout.setVisibility(View.GONE);
						setzoom(pandora);
						currentfoucusImage = pandora;
						String text=getResources().getString(R.string.pandora_str);
						writetext(text,300,1);
					}
				});

			} else if (currentfoucusImage == pandora) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						resetzoom(pandora);
						layoutprevoiusanimation(secondLayout);
						firstLayout.setVisibility(View.GONE);
						secondLayout.setVisibility(View.VISIBLE);
						thirdLayout.setVisibility(View.GONE);
						fourthLayout.setVisibility(View.GONE);
						setzoom(popcorntime);
						currentfoucusImage = popcorntime;
						String text=getResources().getString(R.string.popcorntime_str);
						writetext(text,800,3);
					}
				});

			} else if (currentfoucusImage == popcorntime) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						resetzoom(popcorntime);
						firstLayout.setVisibility(View.GONE);
						secondLayout.setVisibility(View.VISIBLE);
						thirdLayout.setVisibility(View.GONE);
						fourthLayout.setVisibility(View.GONE);
						setzoom(hulu);
						currentfoucusImage = hulu;
						String text=getResources().getString(R.string.hulu_str);
						writetext(text,300,2);
					}
				});

			}

			else if (currentfoucusImage == hulu) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						resetzoom(hulu);
						firstLayout.setVisibility(View.GONE);
						secondLayout.setVisibility(View.VISIBLE);
						thirdLayout.setVisibility(View.GONE);
						fourthLayout.setVisibility(View.GONE);
						setzoom(kodi);
						currentfoucusImage = kodi;	
						String text=getResources().getString(R.string.kodi_str);
						writetext(text,300,1);
					}
				});

			} else if (currentfoucusImage == kodi) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						resetzoom(kodi);
						layoutprevoiusanimation(firstLayout);
						firstLayout.setVisibility(View.VISIBLE);
						secondLayout.setVisibility(View.GONE);
						thirdLayout.setVisibility(View.GONE);
						fourthLayout.setVisibility(View.GONE);
						setzoom(netfix);
						currentfoucusImage = netfix;
						String text=getResources().getString(R.string.netflix_str);
						writetext(text,800,3);
					}
				});

			}else if (currentfoucusImage == netfix) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						resetzoom(netfix);
						firstLayout.setVisibility(View.VISIBLE);
						secondLayout.setVisibility(View.GONE);
						thirdLayout.setVisibility(View.GONE);
						fourthLayout.setVisibility(View.GONE);
						setzoom(showbox);
						currentfoucusImage = showbox;
						String text=getResources().getString(R.string.showbox_str);
						writetext(text,300,2);
					}
				});

			}else if (currentfoucusImage == showbox) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						resetzoom(showbox);
						firstLayout.setVisibility(View.VISIBLE);
						secondLayout.setVisibility(View.GONE);
						thirdLayout.setVisibility(View.GONE);
						fourthLayout.setVisibility(View.GONE);
						setzoom(youtube);
						currentfoucusImage = youtube;
						String text=getResources().getString(R.string.youtube_str);
						writetext(text,300,1);
					}
				});

			}

			/**************************************************************/
			return true;
		} else if (KeyEvent.KEYCODE_DPAD_RIGHT == keyCode) {
			// TODO*****************NEXT IMAGE******************
			new SoundOnScroll(getApplicationContext());
			if (currentfoucusImage == youtube) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						resetzoom(youtube);
						firstLayout.setVisibility(View.VISIBLE);
						secondLayout.setVisibility(View.GONE);
						thirdLayout.setVisibility(View.GONE);
						fourthLayout.setVisibility(View.GONE);
						setzoom(showbox);
						currentfoucusImage = showbox;
						String text=getResources().getString(R.string.showbox_str);
						writetext(text,300,2);
					}
				});

			} else if (currentfoucusImage == showbox) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						firstLayout.setVisibility(View.VISIBLE);
						secondLayout.setVisibility(View.GONE);
						thirdLayout.setVisibility(View.GONE);
						fourthLayout.setVisibility(View.GONE);
						resetzoom(showbox);
						setzoom(netfix);
						currentfoucusImage = netfix;
						String text=getResources().getString(R.string.netflix_str);
						writetext(text,300,3);
					}
				});

			} else if (currentfoucusImage == netfix) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						resetzoom(netfix);
						layoutnextanimation(secondLayout);
						firstLayout.setVisibility(View.GONE);
						secondLayout.setVisibility(View.VISIBLE);
						thirdLayout.setVisibility(View.GONE);
						fourthLayout.setVisibility(View.GONE);
						setzoom(kodi);
						currentfoucusImage = kodi;
						String text=getResources().getString(R.string.kodi_str);
						writetext(text,800,1);
					}
				});

			} else if (currentfoucusImage == kodi) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						resetzoom(kodi);
						secondLayout.setVisibility(View.VISIBLE);
						thirdLayout.setVisibility(View.GONE);
						firstLayout.setVisibility(View.GONE);
						fourthLayout.setVisibility(View.GONE);
						setzoom(hulu);
						currentfoucusImage = hulu;
						String text=getResources().getString(R.string.hulu_str);
						writetext(text,300,2);
					}
				});

			} else if (currentfoucusImage == hulu) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						resetzoom(hulu);
						secondLayout.setVisibility(View.VISIBLE);
						thirdLayout.setVisibility(View.GONE);
						firstLayout.setVisibility(View.GONE);
						fourthLayout.setVisibility(View.GONE);
						setzoom(popcorntime);
						currentfoucusImage = popcorntime;
						String text=getResources().getString(R.string.popcorntime_str);
						writetext(text,300,3);
					}
				});

			} else if (currentfoucusImage == popcorntime) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						resetzoom(popcorntime);
						layoutnextanimation(thirdLayout);
						firstLayout.setVisibility(View.GONE);
						secondLayout.setVisibility(View.GONE);
						thirdLayout.setVisibility(View.VISIBLE);
						fourthLayout.setVisibility(View.GONE);
						setzoom(pandora);
						currentfoucusImage = pandora;
						String text=getResources().getString(R.string.pandora_str);
						writetext(text,800,1);
					}
				});

			} else if (currentfoucusImage == pandora) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						resetzoom(pandora);
						firstLayout.setVisibility(View.GONE);
						secondLayout.setVisibility(View.GONE);
						thirdLayout.setVisibility(View.VISIBLE);
						fourthLayout.setVisibility(View.GONE);
						setzoom(soundcloud);
						currentfoucusImage = soundcloud;
						String text=getResources().getString(R.string.soundcloud_str);
						writetext(text,300,2);
					}
				});

			} else if (currentfoucusImage == soundcloud) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						resetzoom(soundcloud);
						firstLayout.setVisibility(View.GONE);
						secondLayout.setVisibility(View.GONE);
						thirdLayout.setVisibility(View.VISIBLE);
						fourthLayout.setVisibility(View.GONE);
						setzoom(spotify);
						currentfoucusImage = spotify;
						String text=getResources().getString(R.string.spotify_str);
						writetext(text,300,3);
					}
				});

			} else if (currentfoucusImage == spotify) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						resetzoom(spotify);
						layoutnextanimation(fourthLayout);
						firstLayout.setVisibility(View.GONE);
						secondLayout.setVisibility(View.GONE);
						thirdLayout.setVisibility(View.GONE);
						fourthLayout.setVisibility(View.VISIBLE);
						setzoom(iheartradio);
						currentfoucusImage = iheartradio;
						String text=getResources().getString(R.string.iheartradio_str);
						writetext(text,800,1);
					}
				});

			}else if (currentfoucusImage == iheartradio) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						resetzoom(iheartradio);
						layoutnextanimation(firstLayout);
						firstLayout.setVisibility(View.VISIBLE);
						secondLayout.setVisibility(View.GONE);
						thirdLayout.setVisibility(View.GONE);
						fourthLayout.setVisibility(View.GONE);
						setzoom(youtube);
						currentfoucusImage = youtube;
						String text=getResources().getString(R.string.youtube_str);
						writetext(text,800,1);
					}
				});

			}
			
			
			
			
			
		
			return true;
		} else if (KeyEvent.KEYCODE_DPAD_UP == keyCode) {

			return true;
		} else if (KeyEvent.KEYCODE_DPAD_DOWN == keyCode) {

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


/*************************************************************************************************/
		
	private void writetext(final String text,final int time, final int position) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
		 switch (position) {
			case 1:
				lefttext.setVisibility(View.VISIBLE);
				centertext.setVisibility(View.INVISIBLE);
				righttext.setVisibility(View.INVISIBLE);
				lefttext.setText("");
				break;
			case 2:
				lefttext.setVisibility(View.INVISIBLE);
				centertext.setVisibility(View.VISIBLE);
				righttext.setVisibility(View.INVISIBLE);
				centertext.setText("");
				break;
			case 3:
				lefttext.setVisibility(View.INVISIBLE);
				centertext.setVisibility(View.INVISIBLE);
				righttext.setVisibility(View.VISIBLE);
				righttext.setText("");
				break;
		 }
			}
		});
		new Handler().postDelayed(new Runnable() {
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						 switch (position) {
							case 1:
								lefttext.setText(text);
								break;
							case 2:
								centertext.setText(text);
								break;
							case 3:
								righttext.setText(text);
								break;
						 }
					}
					});
			}
		}, time);
	}
	
	
	/*************************************************************************************************/
	private void iheartradioclick() {
		String packagename=getResources().getString(R.string.iheartradio_pacakage_name);
		String tittle = getResources().getString(R.string.iheartradio_str);
		launch(packagename,tittle);
	}

	private void spotifyclick() {
		String packagename=getResources().getString(R.string.spotify_pacakage_name);
		String tittle = getResources().getString(R.string.spotify_str);
		launch(packagename,tittle);
	}

	private void soundcloudclick() {
		String packagename=getResources().getString(R.string.soundcloud_pacakage_name);
		String tittle = getResources().getString(R.string.soundcloud_str);
		launch(packagename,tittle);
	}

	private void pandoraclick() {
		String packagename=getResources().getString(R.string.pandora_pacakage_name);
		String tittle = getResources().getString(R.string.pandora_str);
		launch(packagename,tittle);
	}

	private void netfixclick() {
		String packagename=getResources().getString(R.string.netfix_pacakage_name);
		String tittle = getResources().getString(R.string.netflix_str);
		launch(packagename,tittle);
	}

	private void kodiclick() {	
		String packagename=getResources().getString(R.string.kodi_pacakage_name);
		String tittle = getResources().getString(R.string.kodi_str);
		launch(packagename,tittle);
	}

	private void showboxclick() {
		String packagename=getResources().getString(R.string.showbox_pacakage_name);
		String tittle = getResources().getString(R.string.showbox_str);
		launch(packagename,tittle);
	}

	private void youtubeclick() {
		String packagename=getResources().getString(R.string.youtube_pacakage_name);
		String tittle = getResources().getString(R.string.youtube_str);
		launch(packagename,tittle);
	}

	protected void huluclick() {
		String packagename=getResources().getString(R.string.hulu_pacakage_name);
		String tittle = getResources().getString(R.string.hulu_str);
		launch(packagename,tittle);		 
	}

	protected void popcorntimeclick() {
		String packagename=getResources().getString(R.string.popcorntime_pacakage_name);
		String tittle = getResources().getString(R.string.popcorntime_str);
		launch(packagename,tittle);	 
	}
	
	
	private void launch(String packagename, String tittle) {
		new SoundOnClick(getApplicationContext());
		boolean isavaiable = isPackageExisted(packagename);
		if (isavaiable) {
			Intent LaunchIntent = getPackageManager()
					.getLaunchIntentForPackage(packagename);
			startActivity(LaunchIntent);
		} else {
			String message = "APPLICATION NOT FOUND.PLEASE INSTALL"+" "+tittle+"..!";
			alertforapplicationnotfound(tittle, message);
		}

		
	}
	/*********************************************************************************************/
	private void alertforapplicationnotfound(String tittle, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle(tittle);
		builder.setMessage(message);
		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				levelDialog.dismiss();
			}
		});
		levelDialog = builder.create();
		levelDialog.show();

	}

	/*********************************************************************************************/

	private void layoutprevoiusanimation(LinearLayout layout) {
		Animation animation = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.slide_in_left);
		layout.startAnimation(animation);
	}

	private void layoutnextanimation(LinearLayout layout) {
		Animation animation = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.slide_in_right);
		layout.startAnimation(animation);
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
						getApplicationContext(), R.anim.entertainment_pop_in);
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

	public boolean isPackageExisted(String targetPackage) {
		List<ApplicationInfo> packages;
		PackageManager pm;
		pm = getPackageManager();
		packages = pm.getInstalledApplications(0);
		for (ApplicationInfo packageInfo : packages) {
			if (packageInfo.packageName.equals(targetPackage))
				return true;
		}
		return false;
	}
}
