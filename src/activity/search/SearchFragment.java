package activity.search;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.GetRequest;
import activity.newsfeed.Constants;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.ebay.ebayfriend.R;

public class SearchFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.search,container,false);
		TextView windowTitleView = (TextView)getActivity().findViewById(R.id.window_title);
		windowTitleView.setText("Search");
		
		final SearchView searchView = (SearchView) view.findViewById(R.id.searchView);
		ListView lv = (ListView) view.findViewById(R.id.searchList);
		final SearchAdapter adapter = new SearchAdapter(getActivity(), lv);
		lv.setAdapter(adapter);
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(final String query) {
				new AsyncTask<Void, Void, List<SearchItem>>(){

					@Override
					protected List<SearchItem> doInBackground(Void... params) {
						List<SearchItem> searchList = new ArrayList<SearchItem>();
						// GET SEARCH RESULT FROM SERVER
						GetRequest request = new GetRequest(Constants.SEARCH_PREFIX + query);
						String jsonResult = request.getContent();
						Log.e("search", jsonResult);
						try{
							JSONArray itemArray = new JSONArray(jsonResult);
							for (int i = 0; i < itemArray.length(); i++) {
								JSONObject item = itemArray.getJSONObject(i);
								String name = item.getString("username");
								String portrait = item.getString("portrait");
								boolean isFollow = item.getBoolean("followed");
								SearchItem searchItem = new SearchItem(name, portrait, isFollow);
								searchList.add(searchItem);
							}
						}
						catch(JSONException e){
							Log.e("search", "Parse Json Error");
						}
						return searchList;
					}
					
					protected void onPostExecute(List<SearchItem> result) {
						adapter.setList(result);
						InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
					};
					
				}.execute();
				return true;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
		searchView.setFocusable(true);
	    searchView.setIconified(false);
	    searchView.requestFocusFromTouch();
		return view;
	}
}
