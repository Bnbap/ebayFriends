package activity.post;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;

import util.AudioUtil;
import util.MultipartEntityUtil;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;   
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import bean.PurchasedItem;

import com.ebay.ebayfriend.R;

@SuppressLint("HandlerLeak")
public class VoiceProcessFragment extends Fragment {
	/**
	 * step 4 final 1.audio or 2. text
	 */
	protected MediaRecorder mr;
	private AudioUtil au;
	PurchasedItem pi;
	Button recordButton, playButton,submitButton;
	private String recordName = null;
	private ImageView iv;
	public VoiceHandler vh;
	private String url;
	private String picturePath;
	private PostHandler ph;


	public VoiceProcessFragment() {
		mr = new MediaRecorder();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle b = getArguments();
		pi = (PurchasedItem) b.getSerializable("purchasedItem");
		url = pi.getUrl();
		picturePath = b.getString("picture");
		recordName = getActivity().getFilesDir().toString() + File.separator
				+ "MyRecord";
		
		vh = new VoiceHandler();
		ph = new PostHandler();

//		ct = new CustomToast(getActivity(),"Recording");
		au = new AudioUtil(vh);
		View view = inflater.inflate(R.layout.process_voice, container, false);
		TextView windowTitleView = (TextView) getActivity().findViewById(
				R.id.post_title);
		windowTitleView.setText("Record Voice");
		
		submitButton = (Button)view.findViewById(R.id.post_final_button);
		submitButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				PostThread pt = new PostThread();
				pt.start();
				
			}
			
		});
		recordButton = (Button) view.findViewById(R.id.process_record_button);
		recordButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Message msg = new Message();
					Bundle b = new Bundle();
					b.putString("status", "start");
					msg.setData(b);
					vh.sendMessage(msg);
					
					au.recordVoice(recordName);
//					ct.showToast(99999999);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					au.storeVoice();
					
					Message msg = new Message();
					Bundle b = new Bundle();
					b.putString("status", "finish");
					msg.setData(b);
					vh.sendMessage(msg);
				}
				return false;
			}

		});
		playButton = (Button) view.findViewById(R.id.process_play_button);
		playButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				au.playVoice();

			}

		});
		
		
		iv = (ImageView) view.findViewById(R.id.play_record);
		
		return view;

	}

	public void playRecord() {

	}

	public String getRecordStyle1() {
		return null;
	}

	public String getRecordStyle2() {
		return null;
	}

	public String getRecordStyle3() {
		return null;
	}
	
	public class VoiceHandler extends Handler{
		public VoiceHandler(){
			
		}
		public VoiceHandler(Looper l){
			super(l);
		}
		
		@Override
		public void handleMessage(Message msg){
			Bundle b = msg.getData();
			String status = b.getString("status");
			if(status.equals("finish")){

				AnimationDrawable anim = null;
				Object ob = iv.getBackground();
				anim = (AnimationDrawable) ob;
				anim.stop();
			}else{

				AnimationDrawable anim = null;
				Object ob = iv.getBackground();
				anim = (AnimationDrawable) ob;
				anim.stop();
				anim.start();
			}
		}
	}
	
	class PostThread extends Thread{
		@Override
		public void run(){
			boolean isSuccess = false;
			
			HashMap<String,File>fileMap = new HashMap<String,File>();
			fileMap.put("picture", new File(picturePath));
			fileMap.put("voice", new File(recordName));
			
			HashMap<String,String> stringMap = new HashMap<String,String>();
			stringMap.put("url", url);
			try {
				isSuccess = MultipartEntityUtil.post("http://192.168.47.19:8080/news/addNews", fileMap, stringMap);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Bundle b = new Bundle();
			b.putBoolean("isSuccess", isSuccess);
			Message msg = new Message();
			msg.setData(b);
			ph.sendMessage(msg);
			
		}
	}
	class PostHandler extends Handler{
		public PostHandler(){
			
		}
		public PostHandler(Looper l){
			
		}
		
		@Override
		public void handleMessage(Message msg){
			Bundle b = msg.getData();
			boolean isSuccess = b.getBoolean("isSuccess");
			if(isSuccess){
				Toast.makeText(getActivity(), "Post Success", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(getActivity(), "Post Failed", Toast.LENGTH_LONG).show();
			}
		}
	}
	public byte[] read(File file) throws IOException {


	    byte []buffer = new byte[(int) file.length()];
	    InputStream ios = null;
	    try {
	        ios = new FileInputStream(file);
	        if ( ios.read(buffer) == -1 ) {
	            throw new IOException("EOF reached while trying to read the whole file");
	        }        
	    } finally { 
	        try {
	             if ( ios != null ) 
	                  ios.close();
	        } catch ( IOException e) {
	        }
	    }

	    return buffer;
	}
	
}
