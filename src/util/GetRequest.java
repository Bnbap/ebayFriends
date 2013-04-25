package util;

import org.apache.http.client.methods.HttpGet;


public class GetRequest extends Request{

	public GetRequest(String url,String sessionid) {
		HTTP_URL = url;
		this.sessionid=sessionid;
		requestBase = new HttpGet(url);
	}
	
}
