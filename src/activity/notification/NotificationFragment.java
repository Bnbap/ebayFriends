package activity.notification;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.GetRequest;

import activity.chat.ChatActivity;
import activity.newsfeed.Constants;
import activity.newsfeed.NewsFeedItem;
import activity.newsfeed.PullAndLoadListView;
import android.app.ListFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ebay.ebayfriend.R;

public class NotificationFragment extends ListFragment {
	private List<Map<String, Object>> mData;
	private NotificationAdapter na;
	private PullAndLoadListView lv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.notification_view, container,
				false);
		TextView windowTitleView = (TextView) getActivity().findViewById(
				R.id.window_title);
		windowTitleView.setText("Friends");

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mData = getData();
		na = new NotificationAdapter(getActivity(), mData,
				R.layout.comments_list, new String[] { "title", "img" },
				new int[] { R.id.title, R.id.img });
		setListAdapter(na);
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		Toast.makeText(
				getActivity(),
				"You have selected "
						+ (String) mData.get(position).get("title"),
				Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(getActivity(), ChatActivity.class);
		intent.putExtra("otherName", (String) mData.get(position).get("title"));
		startActivity(intent);
	}

	// private List<Map<String, Object>> getData() {
	// Map<String, Object> map;
	// List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	//
	// /* URL���������� */
	// String uriAPI = "http://192.168.47.19:8080/users/showFriends";
	// /* ����HTTP Get���� */
	// HttpGet httpRequest = new HttpGet(uriAPI);
	// try {
	// /* ������������������ */
	// HttpResponse httpResponse = new DefaultHttpClient()
	// .execute(httpRequest);
	// /* ����������200 ok */
	// if (httpResponse.getStatusLine().getStatusCode() == 200) {
	// /*
	// * ����������200���������� ����������������json��������������
	// */
	// StringBuilder builder = new StringBuilder();
	// BufferedReader bufferedReader2 = new BufferedReader(
	// new InputStreamReader(httpResponse.getEntity()
	// .getContent()));
	// String str2 = "";
	// for (String s = bufferedReader2.readLine(); s != null; s =
	// bufferedReader2
	// .readLine()) {
	// builder.append(s);
	// }
	// /**
	// * ������������������������json����������
	// */
	// JSONObject jsonObject = new JSONObject(builder.toString())
	// .getJSONObject("comment");
	// JSONArray jsonArray = jsonObject.getJSONArray();
	// for (int i = 0; i < jsonArray.length(); i++) {
	// map = new HashMap<String, Object>();
	// JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
	// map.put("title", jsonObject2.getString("username"));
	// map.put("img",
	// getBitmapFromUrl(jsonObject2.getString("img")));
	// list.add(map);
	//
	// }
	//
	// } else {
	// System.out.println("Error Response: "
	// + httpResponse.getStatusLine().toString());
	// }
	// } catch (ClientProtocolException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// // map.put("title", "G1");
	// // map.put("info", "google 1");
	// // map.put("img", R.drawable.i1);
	// // list.add(map);
	// //
	// // map = new HashMap<String, Object>();
	// // map.put("title", "G2");
	// // map.put("info", "google 2");
	// // map.put("img", R.drawable.i2);
	// // list.add(map);
	// //
	// // map = new HashMap<String, Object>();
	// // map.put("title", "G3");
	// // map.put("info", "google 3");
	// // map.put("img", getBitmapFromUrl(""));
	// // list.add(map);
	//
	// return list;
	// }

	private List<Map<String, Object>> getData() {
		Map<String, Object> map;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String getURL = "http://192.168.47.19:8080/users/showFriends";
		GetRequest getRequest = new GetRequest(getURL);
		String jsonResult = getRequest.getContent();
		if (jsonResult == null) {
			Log.e("NewsFeedFragment", "Json Parse Error");
		} else {
			try {
				JSONArray itemArray = new JSONArray(jsonResult);
				for (int i = 0; i < itemArray.length(); i++) {
					map = new HashMap<String, Object>();
					JSONObject user = itemArray.getJSONObject(i);
					String portraitURL = user.getString("portrait");
					String authorName = user.getString("name");
					map.put("title", authorName);
					map.put("img", getBitmapFromUrl(portraitURL));
					list.add(map);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Log.e("NewsFeedFragment", "Parse Json Error");
			}
		}
		return list;
	}

	/**
	 * ��Url������������������������Bitmap����
	 * 
	 * @param imgUrl
	 * @return
	 */
	public static Bitmap getBitmapFromUrl(String imgUrl) {
		URL url;
		Bitmap bitmap = null;
		try {
			url = new URL(imgUrl);
			InputStream is = url.openConnection().getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is); // bitmap =
																	// BitmapFactory.decodeStream(bis);
																	// ����1
			byte[] b = getBytes(is);
			bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
			bis.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * ��InputStream����������Byte[]
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytes(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len = 0;
		while ((len = is.read(b, 0, 1024)) != -1) {
			baos.write(b, 0, len);
			baos.flush();
		}
		byte[] bytes = baos.toByteArray();
		return bytes;
	}
}
