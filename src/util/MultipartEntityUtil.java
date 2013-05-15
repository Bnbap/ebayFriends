package util;

//import io.IoStreamUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 */
public class MultipartEntityUtil {

	public static boolean post(String url, Map<String,File> fileList,
			Map<String,String> stringList) throws ClientProtocolException, IOException {
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Cookie","sessionid="+LoginUtil.getSession().get("s_sessionid"));
		MultipartEntity reqEntity = new MultipartEntity();
		if(fileList!=null){
			Set<String> keySet = fileList.keySet();
			
			for(String key:keySet){
				File file = fileList.get(key);
				if (file != null){
					reqEntity.addPart(key,new FileBody(file));
				}
			}
		}
		if(stringList!=null){
			Set<String> keySet = stringList.keySet();
			for(String key:keySet){
				String value = stringList.get(key);
				reqEntity.addPart(key,new StringBody(value));
			}
		}

		httppost.setEntity(reqEntity);
		// System.out.println("执行: " + httppost.getRequestLine());

		HttpResponse response = httpclient.execute(httppost);
		// System.out.println("statusCode is "
		// + response.getStatusLine().getStatusCode());
		int res = response.getStatusLine().getStatusCode();
		if (res == 200) {
			
			return true;

		}
		return false;
	}

}