package activity.post;

import util.AudioUtil;
import android.app.Fragment;
import android.media.MediaRecorder;
public class VoiceProcessFragment extends Fragment {
/**
 * step 4 final
 * 1.audio or 2. text
 */
	protected MediaRecorder mr;
	private String recordFileName;
	private AudioUtil au;
	public VoiceProcessFragment(){
		mr = new MediaRecorder();
	}
	
	public boolean recordVoice(){
		return false;
	}
	
	public void playRecord(){
		
	}
	public String getRecordStyle1(){
		return null;
	}
	public String getRecordStyle2(){
		return null;
	}
	public String getRecordStyle3(){
		return null;
	}
	
	
}
