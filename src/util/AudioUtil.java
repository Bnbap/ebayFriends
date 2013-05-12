package util;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AudioUtil {
	private String mFileName;
	private String LOG_TAG = "AudioUtil";
	private Handler h;

	public AudioUtil(Handler vh){
		h = vh;
		
	}
	MediaRecorder mRecorder;
	MediaPlayer mPlayer = new MediaPlayer();
	public void recordVoice(String fileName) {
		mFileName = fileName;
		mRecorder = new MediaRecorder();
		// 设置音源为Micphone
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		// 设置封装格式
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(mFileName);
		// 设置编码格式
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mRecorder.prepare();
			Log.e(LOG_TAG,"prepare record");
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}

		mRecorder.start();
		Log.e(LOG_TAG,"start Record");
	}
	public void storeVoice() {
		mRecorder.stop();  
        mRecorder.release();  
        mRecorder = null; 
        Log.e(LOG_TAG,"store success");
	}

	public void playVoice() {
		 mPlayer = new MediaPlayer();  
		     mPlayer.setOnCompletionListener(new OnCompletionListener(){

				@Override
				public void onCompletion(MediaPlayer mp) {
					Message msg = new Message();
					Bundle b = new Bundle();
					b.putString("status", "finish");
					msg.setData(b);
					h.sendMessage(msg);
				}
		    	 
		     });
	        try {  
	            mPlayer.setDataSource(mFileName);  
	            mPlayer.prepare();  
	            mPlayer.start();  
	            
	            Message msg = new Message();
				Bundle b = new Bundle();
				b.putString("status", "start");
				msg.setData(b);
				h.sendMessage(msg);
				
	            Log.e(LOG_TAG,"play success");
	        } catch (IOException e) {  
	            Log.e(LOG_TAG, "prepare() failed");  
	        }  
	}
}
