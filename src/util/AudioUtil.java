package util;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AudioUtil {
	private String mFileName;
	private String LOG_TAG = "AudioUtil";

	private float frequency = 1.0f;
	MediaRecorder mRecorder;
	MediaPlayer mPlayer = new MediaPlayer();
	SoundPool sp = new SoundPool(3	, AudioManager.STREAM_MUSIC, 0);


	public AudioUtil() {

	}

	public void recordVoice(String fileName) {
		mFileName = fileName;
		mRecorder = new MediaRecorder();
		// 设置音源为Micphone
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		// 设置封装格式
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(mFileName);
		// 设置编码格式
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

		try {
			mRecorder.prepare();
			Log.e(LOG_TAG, "prepare record");
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}

		mRecorder.start();
		Log.e(LOG_TAG, "start Record");
	}

	public void storeVoice() {
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
		Log.e(LOG_TAG, "store success");
	}

	public void playVoice() {
//		try {
//			int sessionId = mPlayer.getAudioSessionId();
//			Equalizer ae = new Equalizer(0,sessionId);
//			ae.setEnabled(true);
//			short bands = ae.getNumberOfBands();
//			for(short i=0;i<bands;i++){
//				short range[] = ae.getBandLevelRange();
//				
//				ae.setBandLevel(i, (short)i>=bands/2?range[0]:range[1]);
//			}
//			mPlayer.attachAuxEffect(ae.getId());
			
			
//			mPlayer.setDataSource(mFileName);
//			mPlayer.prepare();
//			mPlayer.start();
//			
			int soundId = sp.load(mFileName, 1);
			sp.play(soundId, 1, 1, 1, 0,  frequency);
			Log.e(LOG_TAG, "play success");
//		} catch (IOException e) {
//			Log.e(LOG_TAG, "prepare() failed");
//		}
	}
	public void changeFrequencyAndPlay(float frequency,Handler h){
		this.frequency=frequency;
		playVoice();
	}
}
