package com.adobe.apphub;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adobe.customviews.TextAwesome;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class ProductDetailFragment extends Fragment implements OnClickListener {
	
	private TextView mAppName;
	private TextView mAppRating;
	private TextView mAppType;
	private TextView mAppLastUpdated;
	private TextView mAppInAppPurchase;
	private TextView mAppDescription;
	private ImageView mAppIcon;
	private Button mAppStore;
	private Button mSendSms;
	private ProgressBar mProgressBar;
	
	private String mName;
	private String mType;
	private String mImageUrl ;
	private String mPlayStoreLink;
	private String mDescription;
	private String mLastUpdated ;
	private String mInApp;
	private String mRating;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Create global configuration and initialize ImageLoader with this config
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
            .build();
        ImageLoader.getInstance().init(config);
        setHasOptionsMenu(true);
        
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_share:
				openShare();
				return true;
			default:
				return false;
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.product_detail_fragment, container,false);
		mAppName = (TextAwesome)v.findViewById(R.id.productName);
		mAppRating = (TextAwesome)v.findViewById(R.id.ratingTv);
		mAppType = (TextAwesome)v.findViewById(R.id.appType);
		mAppLastUpdated = (TextAwesome)v.findViewById(R.id.lastUpdated);
		mAppInAppPurchase = (TextAwesome)v.findViewById(R.id.inAppPurchase);
		mAppDescription = (TextAwesome)v.findViewById(R.id.description);
		mAppIcon = (ImageView)v.findViewById(R.id.productIV);
		mAppStore = (Button)v.findViewById(R.id.appStore);
		mSendSms = (Button)v.findViewById(R.id.sms);
		mProgressBar = (ProgressBar)v.findViewById(R.id.progressBar);
		
		mAppStore.setOnClickListener(this);
		mSendSms.setOnClickListener(this);
		
		getProductDetailsFromBundle();
		return v;
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		setProductDetails();
		// Load image, decode it to Bitmap and return Bitmap to callback
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.loadImage(mImageUrl, new SimpleImageLoadingListener() {
		    @Override
		    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		        // Do whatever you want with Bitmap
		    	mProgressBar.setVisibility(View.GONE);
		    	mAppIcon.setImageBitmap(loadedImage);
		    }
		});
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.appStore)
			openPlayStore();
		else if(v.getId() == R.id.sms)
			sendPlayStoreLinkThroughSms();
	}

	private void getProductDetailsFromBundle()
	{
		mName = getArguments().getString(AppHubUtil.NAME_KEY);
		mType = getArguments().getString(AppHubUtil.TYPE_KEY);
		mImageUrl = getArguments().getString(AppHubUtil.IMAGE_URL_KEY);
		mPlayStoreLink = getArguments().getString(AppHubUtil.PLAY_STORE_LINK_KEY);
		mDescription = getArguments().getString(AppHubUtil.DESCRIPTION_KEY);
		mLastUpdated = getArguments().getString(AppHubUtil.LAST_UPDATED_KEY);
		mInApp = getArguments().getString(AppHubUtil.IN_APP_PURCHASE_KEY);
		mRating = getArguments().getString(AppHubUtil.RATING_KEY);
		
	}
	
	private void setProductDetails()
	{
		mAppName.setText(mName);
		mAppRating.setText(mRating);
		mAppDescription.setText(mDescription);
		mAppType.setText(mType);
		mAppInAppPurchase.setText(mInApp);
		mAppLastUpdated.setText(mLastUpdated);
	}
	
	private void openPlayStore()
	{
		try {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mPlayStoreLink)));
		} catch (android.content.ActivityNotFoundException exception) {
			Crouton.makeText(getActivity(), R.string.please_install_google_play_store_, Style.INFO).show();
		}
	}
	
	private void sendPlayStoreLinkThroughSms()
	{
		String sms_text = "Hey check out this cool Adobe app, here is the play store link: \n"+mPlayStoreLink;
		Uri uri = Uri.parse("smsto:" + "");
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		intent.putExtra("sms_body", sms_text); 
		startActivity(intent);
	}
	
	private void openShare()
	{
		String shareText = "App Name: "+mName+"\n"
				+"Description: "+mDescription+"\n"
				+"Type: "+mType+"\n"
				+"Rating: "+mRating+"\n"
				+"In-app purchase: "+mInApp+"\n"
				+"Last Updated: "+mLastUpdated+"\n"
				+"Play Store Link: "+mPlayStoreLink+"\n"
				;
		
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
		sendIntent.setType("text/plain");
		
		startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_product_detail_to)));
	}
	
}
