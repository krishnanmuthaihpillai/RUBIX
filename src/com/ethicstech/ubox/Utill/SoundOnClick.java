package com.ethicstech.ubox.Utill;


import com.ethicstech.ubox.R;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

public class SoundOnClick {
	public SoundOnClick(Context mContext) {
		Log.i("SoundOnClick", "Click");
		MediaPlayer mp;
		mp = MediaPlayer.create(mContext, R.raw.click);
		mp.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.reset();
				mp.release();
				mp = null;
			}

		});
		mp.start();
	}
}
