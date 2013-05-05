package activity.newsfeed;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebay.ebayfriend.R;

public class ReplyAdapter extends ArrayAdapter<ReplyItem> {
    private Context context;
    private List<ReplyItem> replyList;
    private NewsFeedItem newsFeedItem;
    private static int REPLY_ITEM = 0;
    private static int REPLY_HEADER = 1;
    
	public ReplyAdapter(Context context, int rowResId,
			List<ReplyItem> replyList, NewsFeedItem newsFeedItem) {
		super(context, rowResId, replyList);
		this.context = context;
		this.replyList = replyList;
		this.newsFeedItem = newsFeedItem;
	}
	
	/**
	 * @TODO ========================================
	 * GET TYPE METHOD, ETC                         =
	 * ==============================================
	 */
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		int type = getItemViewType(position);
		ReplyHolder holder = new ReplyHolder();
		if (rowView == null){// Not appear before -> New sub view
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			if (type == REPLY_HEADER){
				rowView = inflater.inflate(R.layout.replyitemheader, parent, false);
				holder.mainImage = (ImageView) rowView.findViewById(R.id.image);
				holder.authorIcon = (ImageView) rowView.findViewById(R.id.icon);
				holder.authorName = (TextView) rowView.findViewById(R.id.name);
			}
			else if (type == REPLY_ITEM){
				rowView = inflater.inflate(R.layout.replyitem, parent, false);
				holder.replyIcon = (ImageView) rowView.findViewById(R.id.replyIcon);
				holder.replyPlayButton = (ImageButton) rowView.findViewById(R.id.play);
				holder.replyText = (TextView) rowView.findViewById(R.id.replyContent);
			}
			rowView.setTag(holder);
		}
		else{
			holder = (ReplyHolder) convertView.getTag();
		}
		// Set content
		if (type ==  REPLY_HEADER){// set content in head
			
		}
		else{// set content in reply item
			
		}
		return null;
	}
	
	private class ReplyHolder{
		public ImageView mainImage;
		public ImageView authorIcon;
		public TextView authorName;
		public ImageView replyIcon;
		public TextView replyText;
		public ImageButton replyPlayButton;
	}
}
