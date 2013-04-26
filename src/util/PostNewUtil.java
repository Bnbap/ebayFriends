package util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bean.PurchasedItem;

public class PostNewUtil {

	private static List<PurchasedItem> itemList = new ArrayList<PurchasedItem>();

	public List<PurchasedItem> getItemList() {

		GetRequest gr = new GetRequest("URL");
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(gr.getContent());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jObject = (JSONObject) jsonArray.get(i);
				String name = null;
				String url = null;
				name = jObject.getString("name");
				url = jObject.getString("url");
				PurchasedItem pi = new PurchasedItem(name,url);
				itemList.add(pi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemList;
	}

}
