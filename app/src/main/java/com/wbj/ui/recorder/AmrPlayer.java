package com.wbj.ui.recorder;

import android.media.MediaPlayer;

import java.io.IOException;

public class AmrPlayer {
	
	public static AmrPlayer mInstance = null;
	public synchronized static AmrPlayer getInstance() {
		if (mInstance == null) {
			mInstance = new AmrPlayer();
		}
		return mInstance;
	}
	
	private AmrPlayer() {
		
	}
	
	public void startPlayAndFile(String path) {
		MediaPlayer mp = new MediaPlayer();
		if (mp == null) return ;
		try {
			mp.setDataSource(path);
			mp.prepare();
			mp.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
