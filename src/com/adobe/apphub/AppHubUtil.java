package com.adobe.apphub;

import android.app.ProgressDialog;
import android.content.Context;

public class AppHubUtil {

	public static final String API_URL = "http://adobe.0x10.info/api/products?type=json";
	
	public static final String NAME_KEY = "name";
	public static final String TYPE_KEY = "type";
	public static final String IMAGE_URL_KEY = "image_url";
	public static final String PLAY_STORE_LINK_KEY = "playStoreUrl";
	public static final String DESCRIPTION_KEY = "description";
	public static final String LAST_UPDATED_KEY = "lastupdated";
	public static final String IN_APP_PURCHASE_KEY = "inapp";
	public static final String RATING_KEY = "rating";
	
	public static ProgressDialog progressDialog(Context context,String message)
	{
		ProgressDialog mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setMessage(message);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.show();
		
		return mProgressDialog;
	}
}
