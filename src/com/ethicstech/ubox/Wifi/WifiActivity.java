package com.ethicstech.ubox.Wifi;

import java.util.ArrayList;
import java.util.List;

import com.ethicstech.ubox.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class WifiActivity extends Activity {
	// /////////////////////////////////////////////////////////////////////////
	// scan Devices
	boolean popupback = false;
	TextView exit_Textview;
	ListView wifi_Listview;
	String[] wifis;
	boolean[] isopen;
	List<Integer> networkList;
	Customlistadapter adapter;
	List<ScanResult> wifiList;
	List<ScanResult> wifiScanList;
	int wifiCount = 0;
	ArrayList<String> sidNames = new ArrayList<String>();
	Integer image_lock[] = { R.drawable.lock, R.drawable.unlock };

	Integer image_signal[] = { R.drawable.signal_no, R.drawable.signal_one,
			R.drawable.signal_two, R.drawable.signal_four, R.drawable.signal };
	/*
	 * up to Scan Devices
	 */
	PopupWindow popupWindow;// popupwindowforOpen;
	LinearLayout ordinarylayout, sucesslayout;
	Button button_select;
	TextView sucsesstext, Title;
	EditText edittext_password;
	LinearLayout main_linearLay;
	ProgressBar progress;

	public static final String PSK = "PSK";
	public static final String WEP = "WEP";
	public static final String EAP = "EAP";
	public static final String OPEN = "Open";

	/*
	 * additionally added
	 */
	FrameLayout framelayout_pass;
	ImageButton show_password;
	boolean pass_show;

	// ////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi);
		init();

		wifi_Listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String wifiname = sidNames.get(position);
				String securitymode = getmode(wifiname);
				if (securitymode.equalsIgnoreCase(OPEN)) {
					// TODO for open mode
					Progress_bar_for_Open();
					new WifiOperation_for_open(wifiname, OPEN).execute("");

				} else if ((securitymode.equalsIgnoreCase(PSK))
						|| (securitymode.equalsIgnoreCase(EAP))
						|| (securitymode.equalsIgnoreCase(WEP))) {
					// TODO for secure mode
					Show_Popup_Window_For_Secure_NETWORK(wifiname, securitymode);
				}

			}

		});

		exit_Textview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				exit_Textview_onclick();

			}

		});
	}

	private void init() {
		exit_Textview = (TextView) findViewById(R.id.text_exit);
		wifi_Listview = (ListView) findViewById(R.id.listlock);
	}

	@Override
	protected void onResume() {
		super.onResume();
		scanwifi();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void exit_Textview_onclick() {
		WifiActivity.this.finish();
	}

	/****************************************************************************/
	/*
	 * Scan Wifi
	 */
	/****************************************************************************/
	private void scanwifi() {
		// Scan Wifi
		final WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(true);
		wifiManager.startScan();
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context c, Intent intent) {
				wifiScanList = wifiManager.getScanResults();
				wifiManager.getConfiguredNetworks();
				wifis = new String[wifiScanList.size()];
				isopen = new boolean[wifiScanList.size()];
				networkList = new ArrayList<Integer>();
				for (int i = 0; i < wifiScanList.size(); i++) {
					wifiManager.saveConfiguration();
					wifis[i] = ((wifiScanList.get(i)).SSID);
					wifiList = wifiManager.getScanResults();
					wifiCount = wifiList.size();
					sidNames.add(wifiList.get(i).SSID);
					String securitymode = getmode(wifiList.get(i).SSID);
					if (securitymode.contains("Open")) {
						isopen[i] = true;
					} else {
						isopen[i] = false;
					}
					for (ScanResult result : wifiScanList) {
						int numberOfLevels = 5;
						int level = WifiManager.calculateSignalLevel(
								result.level, numberOfLevels);
						networkList.add(level);
					}
				}
				unregisterReceiver(this);
				adapter = new Customlistadapter(WifiActivity.this, wifis,
						isopen, image_lock, image_signal, networkList);
				wifi_Listview.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
		}, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

	}

	/****************************************************************************/
	protected String getmode(String wifiname) {
		String securityMode = "";
		for (ScanResult result : wifiScanList) {
			if (result.SSID.equals(wifiname)) {
				securityMode = getScanResultSecurity(result);
			}
		}
		return securityMode;
	}

	public static String getScanResultSecurity(ScanResult scanResult) {
		final String cap = scanResult.capabilities;
		final String[] securityModes = { WEP, PSK, EAP };
		for (int i = securityModes.length - 1; i >= 0; i--) {
			if (cap.contains(securityModes[i])) {
				return securityModes[i];
			}
		}
		return OPEN;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_DPAD_CENTER == keyCode
				|| KeyEvent.KEYCODE_ENTER == keyCode) {
			View focusedView = getWindow().getCurrentFocus();
			if (focusedView == exit_Textview) {
				exit_Textview_onclick();
			}

			return true;
		} else if (KeyEvent.KEYCODE_DPAD_LEFT == keyCode) {
			return true;
		} else if (KeyEvent.KEYCODE_DPAD_RIGHT == keyCode) {

			return true;
		} else if (KeyEvent.KEYCODE_DPAD_UP == keyCode) {
			View focusedView = getWindow().getCurrentFocus();
			if (focusedView == exit_Textview) {
				wifi_Listview.setFocusableInTouchMode(true);
				wifi_Listview.requestFocus();
				wifi_Listview.setSelector(getResources().getDrawable(
						R.drawable.listitem_selector));
				exit_Textview.setBackgroundResource(R.drawable.terms_focus_off);
			}

			return true;

		} else if (KeyEvent.KEYCODE_DPAD_DOWN == keyCode) {
			View focusedView = getWindow().getCurrentFocus();
			if (focusedView == wifi_Listview) {
				wifi_Listview.setSelector(getResources().getDrawable(
						R.drawable.focus_off));
				wifi_Listview.setItemsCanFocus(false);
				wifi_Listview.setFocusableInTouchMode(false);
				exit_Textview.setFocusableInTouchMode(true);
				exit_Textview.requestFocus();
				exit_Textview.setBackgroundResource(R.drawable.terms_focus_on);
				focusedView = getWindow().getCurrentFocus();
			}

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/****************************************************************************/
	/*
	 * Show_Popup_Window_For_Secure_NETWORK
	 */
	/****************************************************************************/

	private void Show_Popup_Window_For_Secure_NETWORK(final String wifiname,
			final String securitymode) {
		pass_show = false;
		popupback = true;
		LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View popupView = layoutInflater
				.inflate(R.layout.wifi_popup, null);
		popupWindow = new PopupWindow(popupView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT, true);
		init(popupView);
		// ordinarylayout visible
		ordinarylayout.setVisibility(View.VISIBLE);
		// progress bar Invisible
		progress.setVisibility(View.INVISIBLE);
		// sucesslayout Invisible
		sucesslayout.setVisibility(View.GONE);

		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

		show_password.setImageResource(R.drawable.show_pass_off);
		edittext_password.setTransformationMethod(new WifiAsteriskPassword());

		button_select.setBackground(getResources().getDrawable(
				R.drawable.wifi_select_button_with_out_focus));

		edittext_password.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (s.length() == 0) {
					main_linearLay.setBackground(getResources().getDrawable(
							R.drawable.rounded_corner));
				} else if (s.length() > 0) {
					edittext_password.setError(null);
					main_linearLay.setBackground(getResources().getDrawable(
							R.drawable.round_corner_white));

				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				for (int i = s.length(); i > 0; i--) {

					if (s.subSequence(i - 1, i).toString().equals("\n"))
						s.replace(i - 1, i, "");

				}
			}
		});

		
		edittext_password.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				
				Log.d("EDITTEXT ", "ONCLICK");
//				View current = getCurrentFocus();
//				if (current != null){
//					current.clearFocus();
//				}	
//				edittext_password.setFocusable(true);
//				findViewById(R.id.listlock).post(new Runnable() {
//					public void run() {
//						popupWindow.showAtLocation(findViewById(R.id.listlock),
//								Gravity.CENTER, 0, 0);
//					}
//				});
			}
		});
		
		
		edittext_password.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_DPAD_CENTER == keyCode
						|| KeyEvent.KEYCODE_ENTER == keyCode) {
					
					// edittext enter 
					if (event.getAction() == KeyEvent.ACTION_UP) {
						String pass = edittext_password.getText().toString();
						button_select_Onclick(wifiname, pass, securitymode);
						return true;
					}
					
					
					
					
					return true;
				} else if (KeyEvent.KEYCODE_DPAD_RIGHT == keyCode) {
					edittext_password.setFocusable(false);
					edittext_password.clearFocus();
					show_password.setFocusable(true);
					return true;
				} else if (KeyEvent.KEYCODE_DPAD_UP == keyCode) {
					return true;
				} else if (KeyEvent.KEYCODE_DPAD_DOWN == keyCode) {
					edittext_password.setFocusable(false);
					button_select.setFocusable(true);
					return true;
				}

				return false;
			}
		});

		show_password.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				show_passOnclick(pass_show);
			}

		});

		show_password.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_DPAD_CENTER == keyCode
						|| KeyEvent.KEYCODE_ENTER == keyCode) {
					if (event.getAction() == KeyEvent.ACTION_UP) {
						show_passOnclick(pass_show);
						return true;
					}
				} else if (KeyEvent.KEYCODE_DPAD_LEFT == keyCode) {
					show_password.setFocusable(false);
					show_password.clearFocus();
					edittext_password.setFocusable(true);
					return true;
				} else if (KeyEvent.KEYCODE_DPAD_DOWN == keyCode) {
					show_password.setFocusable(false);
					button_select.setFocusable(true);
					return true;
				}
				return false;
			}
		});

		button_select.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					button_select.setBackground(getResources().getDrawable(
							R.drawable.wifi_select_button_withfocus));
				} else {
					button_select.setBackground(getResources().getDrawable(
							R.drawable.wifi_select_button_with_out_focus));
				}
			}
		});

		button_select.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_DPAD_CENTER == keyCode
						|| KeyEvent.KEYCODE_ENTER == keyCode) {
					if (event.getAction() == KeyEvent.ACTION_UP) {
						String pass = edittext_password.getText().toString();
						button_select_Onclick(wifiname, pass, securitymode);
						return true;
					}
				} else if (KeyEvent.KEYCODE_DPAD_UP == keyCode) {
					edittext_password.setFocusable(true);
					button_select.setFocusable(false);
					return true;
				}

				return false;
			}
		});

		button_select.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				String pass = edittext_password.getText().toString();
				button_select_Onclick(wifiname, pass, securitymode);
			}

		});
		findViewById(R.id.listlock).post(new Runnable() {
			public void run() {
				popupWindow.showAtLocation(findViewById(R.id.listlock),
						Gravity.CENTER, 0, 0);
			}
		});
	}

	protected void button_select_Onclick(String wifiname, String pass,
			String securitymode) {
		if ((pass != "") && (pass.length() != 0)) {
			if (securitymode.equalsIgnoreCase(WEP)) {
				// TODO connect WIFI for WEP NETWORK
				new WifiOperation_for_WEP(wifiname, pass, WEP).execute("");
				Progress_bar();
			} else if (securitymode.equalsIgnoreCase(PSK)
					|| (securitymode.equalsIgnoreCase(EAP))) {
				// TODO Connect WIFI for WPA NETWORK
				new WifiOperation_for_WPA(wifiname, pass, "WPA").execute("");
				Progress_bar();

			}
		} else {
			// password empty
			closeview(popupWindow);
		}
	}

	protected void show_passOnclick(final boolean pass) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (pass) {
					edittext_password
							.setTransformationMethod(new WifiAsteriskPassword());
					show_password.setImageResource(R.drawable.show_pass_off);
					pass_show = false;
				} else {
					edittext_password.setTransformationMethod(null);
					show_password.setImageResource(R.drawable.show_pass_on);
					pass_show = true;
				}
			}
		});
	}

	private void init(View popupView) {
		main_linearLay = (LinearLayout) popupView
				.findViewById(R.id.popuplinear);
		ordinarylayout = (LinearLayout) popupView
				.findViewById(R.id.ordinarylayout);
		sucesslayout = (LinearLayout) popupView
				.findViewById(R.id.Successlinearpop_layout);
		Title = (TextView) popupView.findViewById(R.id.text_title);
		button_select = (Button) popupView.findViewById(R.id.btn_submit);
		progress = (ProgressBar) popupView.findViewById(R.id.myprogressbar);
		edittext_password = (EditText) popupView
				.findViewById(R.id.frompassword);
		sucsesstext = (TextView) popupView.findViewById(R.id.sucess_pop);
		framelayout_pass = (FrameLayout) popupView
				.findViewById(R.id.framelayout_password);
		show_password = (ImageButton) popupView
				.findViewById(R.id.show_password_button);
		pass_show = false;
	}

	/**** Show_Popup_Window_For_Secure_NETWORK ENDS ****************************************************************************************/

	/***************************************************************************************/
	/*
	 * Progress_bar for Secure network
	 */
	/***************************************************************************************/
	private void Progress_bar() {
		// setview
		setview();
		new Handler().postDelayed(new Runnable() {
			public void run() {
				// resetview
				resetview();
			}
		}, 8000);
	}

	private void setview() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Title.setVisibility(View.INVISIBLE);
				framelayout_pass.setVisibility(View.INVISIBLE);
				button_select.setVisibility(View.INVISIBLE);
				progress.setVisibility(View.VISIBLE);
				progress.bringToFront();
			}
		});
	}

	private void resetview() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Title.setVisibility(View.VISIBLE);
				framelayout_pass.setVisibility(View.VISIBLE);
				button_select.setVisibility(View.VISIBLE);
				progress.setVisibility(View.INVISIBLE);
			}
		});
	}

	/************ Progress_bar ends ************************************************************************************/

	private void Progress_bar_for_Open() {
		popupback = true;
		LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View popupView = layoutInflater
				.inflate(R.layout.wifi_popup, null);
		popupWindow = new PopupWindow(popupView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT, true);
		init(popupView);
		ordinarylayout.setVisibility(View.VISIBLE);
		sucesslayout.setVisibility(View.GONE);
		main_linearLay.setBackground(getResources().getDrawable(
				R.drawable.round_corner_white));
		Title.setVisibility(View.INVISIBLE);
		framelayout_pass.setVisibility(View.INVISIBLE);
		button_select.setVisibility(View.INVISIBLE);
		progress.setVisibility(View.VISIBLE);
		progress.bringToFront();
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.showAtLocation(wifi_Listview, Gravity.CENTER, 0, 0);

	}

	private void Popup_Window_For_Sucess(final String wifiname) {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				progress.setVisibility(View.INVISIBLE);
				ordinarylayout.setVisibility(View.GONE);
				sucesslayout.setVisibility(View.VISIBLE);
				sucsesstext.setText("CONNECTED TO " + wifiname);
			}
		});

		new Handler().postDelayed(new Runnable() {
			public void run() {
				closeview(popupWindow);
			}
		}, 5000);
	}

	/*****************************************************************************************/
	/*
	 * Connect_Wifi_for_Open
	 */
	/***************************************************************************************/
	private class WifiOperation_for_open extends
			AsyncTask<String, Void, String> {
		String open_wifiname;
		int open_netId;
		String open_mode;
		boolean changeHappen;

		public WifiOperation_for_open(String wifiname, String mode) {
			open_wifiname = wifiname;
			open_mode = mode;
		}

		@Override
		protected String doInBackground(String... params) {
			WifiConfiguration wifiConfig = new WifiConfiguration();
			wifiConfig.SSID = "\"" + open_wifiname + "\"";
			wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
			open_netId = wifiManager.addNetwork(wifiConfig);
			wifiManager.disconnect();
			wifiManager.enableNetwork(open_netId, true);
			wifiManager.reconnect();
			changeHappen = wifiManager.saveConfiguration();
			return open_wifiname;
		}

		@Override
		protected void onPostExecute(String wifiname) {
			check(wifiname, open_mode, changeHappen, open_netId);
		}
	}

	/******* WifiOperation_for_open ENDS *******************************************************************************/

	/**************************************************************************************/
	/*
	 * Connect_Wifi_for_WEP
	 */
	/*************************************************************************************/
	private class WifiOperation_for_WEP extends AsyncTask<String, Void, String> {
		String WEP_wifiname;
		int WEP_netId;
		String WEP_mode;
		String WEP_password;
		boolean WEP_changeHappen;

		public WifiOperation_for_WEP(String wifiname, String password,
				String mode) {
			WEP_wifiname = wifiname;
			WEP_password = password;
			WEP_mode = mode;
		}

		@Override
		protected String doInBackground(String... params) {
			WifiConfiguration wifiConfiguration = new WifiConfiguration();
			wifiConfiguration.SSID = "\"" + WEP_wifiname + "\"";
			wifiConfiguration.wepKeys[0] = "\"" + WEP_password + "\"";
			wifiConfiguration.wepTxKeyIndex = 0;
			wifiConfiguration.allowedKeyManagement
					.set(WifiConfiguration.KeyMgmt.NONE);
			wifiConfiguration.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.WEP40);
			WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
			WEP_netId = wifiManager.addNetwork(wifiConfiguration);
			wifiManager.disconnect();
			wifiManager.enableNetwork(WEP_netId, true);
			wifiManager.reconnect();
			WEP_changeHappen = wifiManager.saveConfiguration();
			return WEP_wifiname;
		}

		@Override
		protected void onPostExecute(String wifiname) {
			check(wifiname, WEP_mode, WEP_changeHappen, WEP_netId);
		}
	}

	/********* WifiOperation_for_WEP ENDS ****************************************************************************/

	/***************************************************************************************/
	/*
	 * Connect_Wifi_for_WPA
	 */
	/******************************************************************************************/

	private class WifiOperation_for_WPA extends AsyncTask<String, Void, String> {
		String WPA_wifiname;
		int WPA_netId;
		String WPA_mode;
		String WPA_password;
		boolean WPA_changeHappen;

		public WifiOperation_for_WPA(String wifiname, String password,
				String mode) {
			WPA_wifiname = wifiname;
			WPA_password = password;
			WPA_mode = mode;
		}

		@Override
		protected String doInBackground(String... params) {
			WifiConfiguration wifiConfig = new WifiConfiguration();
			wifiConfig.SSID = String.format("\"%s\"", WPA_wifiname);
			wifiConfig.preSharedKey = String.format("\"%s\"", WPA_password);
			wifiConfig.status = WifiConfiguration.Status.ENABLED;
			wifiConfig.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.TKIP);
			wifiConfig.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.CCMP);
			wifiConfig.allowedKeyManagement
					.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			wifiConfig.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			wifiConfig.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
			WPA_netId = wifiManager.addNetwork(wifiConfig);
			wifiManager.disconnect();
			wifiManager.enableNetwork(WPA_netId, true);
			wifiManager.reconnect();
			WPA_changeHappen = wifiManager.saveConfiguration();
			return WPA_wifiname;
		}

		@Override
		protected void onPostExecute(String wifiname) {
			check(wifiname, WPA_mode, WPA_changeHappen, WPA_netId);
		}
	}

	/********* WifiOperation_for_WPA ends **********************************************************************************/

	/**************************************************************************************************/
	/*
	 * check WIFI status
	 */
	/**************************************************************************************************/
	private void check(final String wifiname, final String mode,
			final boolean changeHappen, final int netId) {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				boolean result = checking_status(wifiname);
				if (result) {
					if (changeHappen && netId != -1) {
						Popup_Window_For_Sucess(wifiname);
					} else {
						WifiManager wifiManager = (WifiManager) getApplicationContext()
								.getSystemService(Context.WIFI_SERVICE);
						int networkId = wifiManager.getConnectionInfo()
								.getNetworkId();
						wifiManager.removeNetwork(networkId);
						wifiManager.saveConfiguration();
					}

				} else {
					negativefeedback(mode);
				}
			}
		}, 6500);
	}

	// checking_status
	private boolean checking_status(String wifiname) {
		boolean value = false;
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		final WifiManager wifiManager = (WifiManager) getApplicationContext()
				.getSystemService(Context.WIFI_SERVICE);
		if (mWifi.isConnected()) {
			String wssid = getCurrentSsid(wifiManager);
			if (wifiname.equalsIgnoreCase(wssid)) {
				value = true;
			}
		} else {
			value = false;
		}
		return value;
	}

	// getCurrentSsid
	private String getCurrentSsid(WifiManager manager) {
		String ssid = null;
		final WifiInfo connectionInfo = manager.getConnectionInfo();
		if (connectionInfo != null
				&& !TextUtils.isEmpty(connectionInfo.getSSID())) {
			ssid = connectionInfo.getSSID();
			ssid = ssid.replace("\"", "");
		}
		return ssid;
	}

	/******
	 * check WIFI status
	 * 
	 * @param mode
	 ************************************************************************************/

	protected void negativefeedback(String mode) {
		if (mode.equalsIgnoreCase(OPEN)) {
			closeview(popupWindow);
		} else {
			
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					View current = getCurrentFocus();
					if (current != null){
						current.clearFocus();
					}
						
					edittext_password.setFocusable(true);
					final SpannableStringBuilder sb = new SpannableStringBuilder(
							"INCORRECT PASSWORD. PLEASE TRY AGAIN");
					final ForegroundColorSpan fcs = new ForegroundColorSpan(
							Color.rgb(228, 114, 151));
					final StyleSpan bss = new StyleSpan(
							android.graphics.Typeface.BOLD);
					sb.setSpan(fcs, 0, 19, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
					sb.setSpan(bss, 0, 19, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
					Title.setText(sb);
				}
			});
		}
	}

	private void closeview(final PopupWindow popupWindow) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				popupback = false;
				popupWindow.dismiss();
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (popupback) {
			closeview(popupWindow);
		}
	}
}
