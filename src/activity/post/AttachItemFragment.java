package activity.post;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import bean.PurchasedItem;

import com.ebay.ebayfriend.R;

public class AttachItemFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	        Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.attachitem,container,false);
		TextView windowTitleView = (TextView)view.findViewById(R.id.window_title);
		windowTitleView.setText("Post");
		return view;
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
	
	class SelectItem implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
