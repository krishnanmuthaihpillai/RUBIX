package com.ethicstech.ubox.Network;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

public class Check_Network {
	Context mcontext;
	public Check_Network(Context cxt) {
		 this.mcontext = cxt.getApplicationContext();
	}
	public boolean isNetworkAvailable() {
		boolean value=false;
		ConnectivityManager connManager = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		  if (networkInfo.isConnected()) {
		    final WifiManager wifiManager = (WifiManager) mcontext.getSystemService(Context.WIFI_SERVICE);
		    final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
		    if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
		    	 String ssid = connectionInfo.getSSID();
		    	 Log.v("Check_Network", ""+ssid);
		    	 Log.v("Check_Network","Conected to a network");
		    	 value=true;
		    }else{
		    	 value=false;
		    	 Log.v("Check_Network","Not Conected to a network");
		    }
		  } 
		  return value;
	}
}
