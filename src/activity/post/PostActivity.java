package activity.post;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Window;

import com.ebay.ebayfriend.R;

public class PostActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.post);
		initView(savedInstanceState);
	}
	
	private void initView(Bundle savedInstanceState){
		Fragment fragment = new AttachItemFragment();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.post_content, fragment);
		transaction.commit();
	}
}
