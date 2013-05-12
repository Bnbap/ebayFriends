package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bean.PurchasedItem;

public class PostNewUtil {

	public static ArrayList<HashMap<String,Object>> getItemList() {
		
		ArrayList<HashMap<String,Object>> itemList = new ArrayList<HashMap<String,Object>>();
		GetRequest gr = new GetRequest("http://192.168.47.19:8080/users/getGoodsList");
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
				String id = null;
				String date =null;
				name = jObject.getString("name");
				id = jObject.getString("id");
				date = jObject.getString("time").split(" ")[0];
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("name", name);
				map.put("url", id);
				map.put("date", date);
				
//				PurchasedItem pi = new PurchasedItem(name,url);
				itemList.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemList;
	}

}
