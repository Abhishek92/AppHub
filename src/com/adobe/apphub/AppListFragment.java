package com.adobe.apphub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class AppListFragment extends ListFragment implements OnItemClickListener,OnItemSelectedListener   {

	
	private List<AppListItem> mRowDataList;
	private ProgressDialog mDialog;
	private AppListAdapter mAdapter;
	private Spinner mSortOptions;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.app_list_layout, container,false);
		mSortOptions = (Spinner)v.findViewById(R.id.sortOptions);
		mSortOptions.setOnItemSelectedListener(this);
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mRowDataList = new ArrayList<AppListItem>();
		getListView().setTextFilterEnabled(true);
		mDialog = AppHubUtil.progressDialog(getActivity(), getActivity().getString(R.string.loading));
		JsonArrayRequest jsArrRequest = new JsonArrayRequest(AppHubUtil.API_URL, new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray arr) {
				if(mDialog.isShowing())
					mDialog.cancel();
				parseJsonArray(arr);
				mAdapter = new AppListAdapter(getActivity(), R.layout.app_list_item, mRowDataList);
				setListAdapter(mAdapter);
				
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if(mDialog.isShowing())
					mDialog.cancel();
				if(error != null)
					Crouton.makeText(getActivity(),R.string.oops_something_went_wrong_please_try_again, Style.ALERT).show();
			}
		});
		
		// Access the RequestQueue through your VolleySingleton class.
		VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsArrRequest);
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.search_menu, menu);
		SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);  
        
        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextChange(String newText)
            {
                // this is your adapter that will be filtered
                mAdapter.getFilter().filter(newText);
                System.out.println("on text chnge text: "+newText);
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                // this is your adapter that will be filtered
            	mAdapter.getFilter().filter(query);
                System.out.println("on query submit: "+query);
                return true;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);

	}



	
	private void parseJsonArray(JSONArray arr) {
		for(int i=0; i<arr.length(); i++)
		{
			try {
				JSONObject jObject = arr.getJSONObject(i);
				String name = jObject.getString("name");
				String type = jObject.getString("type");
				String productUrl = jObject.getString("url");
				String imageUrl = jObject.getString("image");
				String rating = jObject.getString("rating");
				String last_updated = jObject.getString("last updated");
				String inapp_purchase = jObject.getString("inapp-purchase");
				String description = jObject.getString("description");
				if(!rating.equals("null") && !inapp_purchase.equals("null")){
				 AppListItem rowData = new AppListItem(name, type, productUrl, imageUrl, description, last_updated, inapp_purchase, rating);
				 mRowDataList.add(rowData);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	Comparator<AppListItem> ratingComparator = new Comparator<AppListItem>() {
		@Override
		public int compare(AppListItem lhs, AppListItem rhs) {
			return Float.valueOf(rhs.getRating()).compareTo(Float.valueOf(lhs.getRating()));
		}
	};
	
	Comparator<AppListItem> inAppPurchaseComparator = new Comparator<AppListItem>() {
		@Override
		public int compare(AppListItem lhs, AppListItem rhs) {
			return rhs.getInAppPurchase().compareTo(lhs.getInAppPurchase());
		}
	};
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		AppListItem rowData = (AppListItem) l.getItemAtPosition(position);
		Intent intent = new Intent(getActivity(), ProductDetail.class);
		
		Bundle bundle = new Bundle();
		bundle.putString(AppHubUtil.NAME_KEY, rowData.getName());
		bundle.putString(AppHubUtil.TYPE_KEY, rowData.getType());
		bundle.putString(AppHubUtil.IMAGE_URL_KEY, rowData.getImageUrl());
		bundle.putString(AppHubUtil.PLAY_STORE_LINK_KEY, rowData.getProductUrl());
		bundle.putString(AppHubUtil.DESCRIPTION_KEY, rowData.getDescription());
		bundle.putString(AppHubUtil.LAST_UPDATED_KEY, rowData.getLastUpdated());
		bundle.putString(AppHubUtil.IN_APP_PURCHASE_KEY, rowData.getInAppPurchase());
		bundle.putString(AppHubUtil.RATING_KEY, rowData.getRating());
		
		intent.putExtra("detail_bundle", bundle);
		
		
		startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int pos, long id) {
		
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if(mSortOptions.getSelectedItemPosition() != 0){
			String sortOption = mSortOptions.getSelectedItem().toString();
			if(sortOption.equalsIgnoreCase("rating"))
				sortListByRating();
			else if(sortOption.equalsIgnoreCase("in-app purchase"))
				sortListByInAppPurchase();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	private void sortListByRating()
	{
		Collections.sort(mRowDataList, ratingComparator);
		mAdapter = new AppListAdapter(getActivity(), R.layout.app_list_item, mRowDataList);
		setListAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}
	
	private void sortListByInAppPurchase()
	{
		Collections.sort(mRowDataList, inAppPurchaseComparator);
		mAdapter = new AppListAdapter(getActivity(), R.layout.app_list_item, mRowDataList);
		setListAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}
}
