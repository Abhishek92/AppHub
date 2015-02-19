package com.adobe.apphub;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AppListAdapter extends ArrayAdapter<AppListItem> {

	private LayoutInflater mInflater;
	public AppListAdapter(Context context, int resource, List<AppListItem> objects) {
		super(context, resource, objects);
		mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final AppListItem rowData = getItem(position);
		if (convertView == null)
			convertView = mInflater.inflate(R.layout.app_list_item, null);
		
		ViewHolder holder = new ViewHolder();
		holder.appName = (TextView) convertView.findViewById(R.id.product);
		holder.inAppPurchase = (TextView) convertView.findViewById(R.id.inApp);
		convertView.setTag(holder);
		holder.appName.setText(rowData.getName());
		holder.inAppPurchase.setText(rowData.getInAppPurchase());
		return convertView;
	}
	
	static class ViewHolder {
		TextView appName;
		TextView inAppPurchase;
	}
}
