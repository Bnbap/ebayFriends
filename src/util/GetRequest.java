package util;

import org.apache.http.client.methods.HttpGet;


public class GetRequest extends Request{

	public GetRequest(String url) {
		HTTP_URL = url;
		requestBase = new HttpGet(url);
	}
	
}
