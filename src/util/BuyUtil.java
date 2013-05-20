package util;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BuyUtil {

	private static String HTTP_ADDRESS  ="http://192.168.47.19:8080/goods/getGoods?id=";
	
	public static HashMap<String,Object> getGoodsInfo(String goodsId){
		GetRequest getRequest = new GetRequest(HTTP_ADDRESS+goodsId);
		JSONObject jo = null;
		HashMap<String,Object> res = new HashMap<String,Object>();
		try {
			jo = new JSONObject(getRequest.getContent());
			String goodsName,goodsPrice,goodsDescription;
			ArrayList<String> goodsPics = new ArrayList<String>();
			
			goodsName = (String)jo.get("name");
//			goodsPrice = ""+((Double)jo.get("price"));
			
			goodsDescription = (String)jo.getString("description");
			String picture1 = (String)jo.getString("picture1");
			String picture2 = (String)jo.getString("picture2");
			String picture3 = (String)jo.getString("picture3");
			goodsPics.add(picture1);
			goodsPics.add(picture2);
			goodsPics.add(picture3);
			
			res.put("name", goodsName);
//			res.put("price", goodsPrice);
			res.put("description", goodsDescription);
			res.put("pictures", goodsPics);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static boolean buy(String goodsId){
		GetRequest gr = new GetRequest("http://192.168.47.19:8080/users/buy?id="+goodsId);
		gr.getContent();
		return true;
	}
}
