package util;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;

public class AudioUtil {
	private String mFileName;
	private String LOG_TAG = "AudioUtil";

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
	        try {  
	            mPlayer.setDataSource(mFileName);  
	            mPlayer.prepare();  
	            mPlayer.start();  
	            Log.e(LOG_TAG,"play success");
	        } catch (IOException e) {  
	            Log.e(LOG_TAG, "prepare() failed");  
	        }  
	}
}
