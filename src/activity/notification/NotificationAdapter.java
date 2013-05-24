package activity.notification;

import java.util.List;
import java.util.Map;

import util.LoginUtil;
import util.PicUtil;
import util.PicUtil.AnimateFirstDisplayListener;

import com.ebay.ebayfriend.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import activity.chat.ChatActivity;
import activity.chat.ChatMsgEntity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class NotificationAdapter extends BaseAdapter {

	private List<User> userData;
	private Context context;
	private LayoutInflater mInflater;

	public NotificationAdapter(Context context, List<User> data) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.userData = data;
		this.mInflater = LayoutInflater.from(context);
	}

	public final class ViewHolder {
		public TextView title;
		public ImageView img;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		final User entity = userData.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.comments_list, null);
			holder.img = (ImageView) convertView.findViewById(R.id.img);

			holder.title = (TextView) convertView.findViewById(R.id.title);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 进行数据设置
		try {
			holder.title.setText(entity.getTitle());
		} catch (NullPointerException e) {
			TextView tv = (TextView) convertView.findViewById(R.id.title);
			tv.setText(entity.getTitle());
		}
		try {
			AnimateFirstDisplayListener animateListener = PicUtil
					.getAnimateListener();
			ImageLoader imageLoader = PicUtil.imageLoader;
			DisplayImageOptions option = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.ic_stub)
					.showImageForEmptyUri(R.drawable.ic_empty)
					.showImageOnFail(R.drawable.ic_error).cacheInMemory()
					.cacheOnDisc().build();
			imageLoader.displayImage(entity.getImg(), holder.img, option,
					animateListener);
		} catch (NullPointerException e) {
			ImageView iv = (ImageView) convertView.findViewById(R.id.img);
			AnimateFirstDisplayListener animateListener = PicUtil
					.getAnimateListener();
			ImageLoader imageLoader = PicUtil.imageLoader;
			DisplayImageOptions option = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.ic_stub)
					.showImageForEmptyUri(R.drawable.ic_empty)
					.showImageOnFail(R.drawable.ic_error).cacheInMemory()
					.cacheOnDisc().build();
			imageLoader.displayImage(entity.getImg(), iv, option,
					animateListener);
		}
		// convertView.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) { // 加载详细新闻
		// Intent intent = new Intent(context, ChatActivity.class);
		// intent.putExtra("otherName", (String) entity.getTitle());
		// context.startActivity(intent);
		// }
		// });
		return convertView;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return userData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
