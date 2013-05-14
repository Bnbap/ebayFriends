package activity.newsfeed;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.GetRequest;
import util.PicUtil;
import util.PicUtil.AnimateFirstDisplayListener;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebay.ebayfriend.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ReplyAdapter extends BaseAdapter {
    private Context context;
    private List<ReplyItem> replyList;
    private NewsFeedItem newsFeedItem;
    private static int REPLY_ITEM = 0;
    private static int REPLY_HEADER = 1;
    private static int VIEW_COUNT = 2;
    
	public ReplyAdapter(Context context,
			 NewsFeedItem newsFeedItem) {
		this.context = context;
		this.newsFeedItem = newsFeedItem;
		replyList = new ArrayList<ReplyItem>();
		// Need to refresh list in separate thread
		updateReplyList();
	}
	
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
				holder.replyName = (TextView) rowView.findViewById(R.id.replyName);
			}
			rowView.setTag(holder);
		}
		else{
			holder = (ReplyHolder) convertView.getTag();
		}
		ImageLoader imageLoader = PicUtil.imageLoader;
		DisplayImageOptions iconOption = PicUtil.getIconOption();
		DisplayImageOptions imageOption = PicUtil.getMainImageOption();
		AnimateFirstDisplayListener animateListener = PicUtil.getAnimateListener();
		// Set content
		if (type ==  REPLY_HEADER){// set content in head
			// Display main image
			imageLoader.displayImage(newsFeedItem.getImage(), holder.mainImage, imageOption, animateListener);
			// Display Icon
			imageLoader.displayImage(newsFeedItem.getIcon(), holder.authorIcon, iconOption, animateListener);
			// Display Name
			holder.authorName.setText(newsFeedItem.getName());
		}
		else{// set content in reply item
			int index = position - 1;
			holder.replyName.setText(replyList.get(index).getReplyName() + " : ");
			// Display icon
			imageLoader.displayImage(replyList.get(index).getIcon(), holder.replyIcon, iconOption, animateListener);
			// Display reply text or button
			String replyText = replyList.get(index).getReplyContent();
			String audioUrl = replyList.get(index).getAudioReplyURL();
			if (replyText.length() != 0){
				holder.replyText.setVisibility(View.VISIBLE);
				holder.replyPlayButton.setVisibility(View.GONE);
				holder.replyText.setText(replyText);
			}
			if (audioUrl.length() != 0){
				holder.replyPlayButton.setVisibility(View.VISIBLE);
				holder.replyText.setVisibility(View.GONE);
				/**
				 * @TODO SET PLAYBUTTON DATASOURCE.
				 */
			}
		}
		Log.e("ReplyAdapter", "Display position" + position);
		return rowView;
	}
	
	public void setReplyList(List<ReplyItem> replyList){
		this.replyList = replyList;
		this.notifyDataSetChanged();
	}
	
	private class ReplyHolder{
		public ImageView mainImage;
		public ImageView authorIcon;
		public TextView authorName;
		public TextView replyName;
		public ImageView replyIcon;
		public TextView replyText;
		public ImageButton replyPlayButton;
	}
	
	@Override
	public int getItemViewType(int position) {
		if (position == 0) return ReplyAdapter.REPLY_HEADER;
		else return ReplyAdapter.REPLY_ITEM;
	}
	
	@Override
	public int getViewTypeCount() {
		return ReplyAdapter.VIEW_COUNT;
	}
	
	private class UpdateReplyListTask extends AsyncTask<Void, Void, List<ReplyItem>> {
		private String commentURL;
		private ReplyAdapter adapter;
		
		public UpdateReplyListTask(ReplyAdapter adapter, String commentURL) {
			this.adapter = adapter;
			this.commentURL = commentURL;
		}
		@Override
		protected List<ReplyItem> doInBackground(Void... params) {
			GetRequest getRequest = new GetRequest(commentURL);
			String jsonResult = getRequest.getContent();
			Log.e("ReplyAdapter", commentURL);
			Log.e("ReplyAdapter", jsonResult);
			List<ReplyItem> replyList = new ArrayList<ReplyItem>();
			if (jsonResult == null) {
				Log.e("ReplyAdapter", "Json Parse Error");
			} else {
				try {
					JSONArray itemArray = new JSONArray(jsonResult);
					for (int i = 0; i < itemArray.length(); i++) {
						JSONObject item = itemArray.getJSONObject(i);
						String username = item.getString("username");
						String content = item.getString("content");
						String voiceURL = item.getString("voice");
						String portrait = item.getString("portrait");
						ReplyItem replyItem = new ReplyItem(username, portrait, content, voiceURL);
						replyList.add(replyItem);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("ReplyAdapter", "Parse Json Error");
				}
			}
			return replyList;
		}

		@Override
		protected void onPostExecute(List<ReplyItem> result) {
			super.onPostExecute(result);
			adapter.setReplyList(result);
		}
	}
	
	public void updateReplyList(){
		new UpdateReplyListTask(this, newsFeedItem.getComments()).execute();
	}

	@Override
	public int getCount() {
		return replyList.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		if (position == 0) return newsFeedItem;
		else return replyList.get(position - 1);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
