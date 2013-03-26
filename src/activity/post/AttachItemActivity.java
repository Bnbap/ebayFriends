package activity.post;

import activity.MainActivity;
import android.os.Bundle;
import android.widget.TextView;
import bean.PurchasedItem;

import com.ebay.ebayfriend.R;

public class AttachItemActivity extends MainActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		InitView();
		TextView windowTitleView = (TextView)findViewById(R.id.window_title);
		windowTitleView.setText("Post");
	}
	
	/**
	 * get the user's purchase records 
	 * 
	 * @param UserId the user who concerns
	 * @return PurchasedItems
	 */
	private PurchasedItem[] getBoughtRecords(long UserId){
		return null;
	}
}
